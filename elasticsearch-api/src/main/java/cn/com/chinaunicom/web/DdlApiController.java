package cn.com.chinaunicom.web;

import cn.com.chinaunicom.appllication.DdlApiApplication;
import cn.com.chinaunicom.constant.ResponseCode;
import cn.com.chinaunicom.dto.BaseResponseDto;
import cn.com.chinaunicom.dto.ddl.*;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by dell on 2018/7/27.
 * @author dell
 */
@RestController
@RequestMapping("/ddl")
public class DdlApiController {

    @Inject
    private DdlApiApplication ddlApiApplication;

    /**
     * 新建索引
     * @param inPutIndexDto
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.POST)
    public BaseResponseDto putIndex(@RequestBody @Valid InPutIndexDto inPutIndexDto){
        BaseResponseDto baseResponseDto;
        try {
            baseResponseDto = ddlApiApplication.insertIndex(inPutIndexDto);
        }catch (Exception e){
            e.printStackTrace();
            baseResponseDto = new BaseResponseDto();
            baseResponseDto.setRtCode(ResponseCode.ERROR);
        }
        return baseResponseDto;
    }

    /**
     * 判断索引是否存在
     * @param index
     * @return
     */
    @RequestMapping(value = "/indexexists/{index}",method = RequestMethod.GET)
    public OutIsIndexExistsDto isIndexExists(@PathVariable("index") String index){
        OutIsIndexExistsDto outIsIndexExistsDto = new OutIsIndexExistsDto();
        try {
            outIsIndexExistsDto = ddlApiApplication.isIndexExists(index);
            outIsIndexExistsDto.setRtCode(ResponseCode.SUCCESS);
        }catch(Exception e) {
            e.printStackTrace();
            outIsIndexExistsDto.setRtCode(ResponseCode.ERROR);
            outIsIndexExistsDto.setExistsFlag(null);
        }
        return outIsIndexExistsDto;
    }

    /**
     * 删除索引
     * @param inDeleteIndexDto
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.DELETE)
    public BaseResponseDto deleteIndex(@RequestBody @Valid InDeleteIndexDto inDeleteIndexDto){
        BaseResponseDto baseResponseDto;
        try {
            baseResponseDto = ddlApiApplication.deleteIndex(inDeleteIndexDto);
        }catch (Exception e){
            e.printStackTrace();
            baseResponseDto = new BaseResponseDto();
            baseResponseDto.setRtCode(ResponseCode.ERROR);
        }
        return baseResponseDto;
    }

    /**
     * 自定义设置mapping
     * @param inSetMappingDto
     * @return
     */
    @RequestMapping(value = "/setting/mapping",method = RequestMethod.POST)
    public BaseResponseDto putMapping(@RequestBody @Valid InSetMappingDto inSetMappingDto){
        BaseResponseDto baseResponseDto;
        try {
            baseResponseDto = ddlApiApplication.mapping(inSetMappingDto);
        }catch (Exception e){
            e.printStackTrace();
            baseResponseDto = new BaseResponseDto();
            baseResponseDto.setRtCode(ResponseCode.ERROR);
        }
        return baseResponseDto;
    }
}
