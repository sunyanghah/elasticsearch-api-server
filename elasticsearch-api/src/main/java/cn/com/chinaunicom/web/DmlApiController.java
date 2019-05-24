package cn.com.chinaunicom.web;

import cn.com.chinaunicom.appllication.DmlApiApplication;
import cn.com.chinaunicom.constant.ResponseCode;
import cn.com.chinaunicom.dto.BaseResponseDto;
import cn.com.chinaunicom.dto.dml.*;
import cn.com.chinaunicom.dto.dml.simple.InSearchDslDto;
import cn.com.chinaunicom.dto.dml.simple.OutSearchDslDto;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import javax.inject.Inject;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2018/7/27.
 * @author dell
 */
@RestController
@RequestMapping("/dml")
public class DmlApiController {

    @Inject
    private DmlApiApplication dmlApiApplication;

    /**
     * 获取一条document数据
     * @param inGetDocumentByIdDto
     * @return
     */
    @RequestMapping(value = "/get/document",method = RequestMethod.POST)
    public OutGetDocumentByIdDto getDocumentById(@RequestBody @Valid InGetDocumentByIdDto inGetDocumentByIdDto){
        OutGetDocumentByIdDto outGetDocumentByIdDto;
        if (null == inGetDocumentByIdDto){
            return null;
        }
        try {
            outGetDocumentByIdDto = dmlApiApplication.getDocumentById(inGetDocumentByIdDto);
        }catch (Exception e){
            e.printStackTrace();
            outGetDocumentByIdDto = new OutGetDocumentByIdDto();
            outGetDocumentByIdDto.setRtCode(ResponseCode.ERROR);
        }
        return outGetDocumentByIdDto;
    }

    /**
     * 新增一条document数据
     * @param inAddDocumentDto
     * @return
     */
    @RequestMapping(value = "/document",method = RequestMethod.POST)
    public OutAddDocumentDto insertDocument(@RequestBody @Valid InAddDocumentDto inAddDocumentDto){
        OutAddDocumentDto outAddDocumentDto;
        try {
            outAddDocumentDto = dmlApiApplication.insertDocument(inAddDocumentDto);
        }catch (Exception e){
            e.printStackTrace();
            outAddDocumentDto = new OutAddDocumentDto();
            outAddDocumentDto.setRtCode(ResponseCode.ERROR);
        }
        return outAddDocumentDto;
    }

    /**
     * 删除一条document数据
     * @param inDeleteDocumentDto
     * @return
     */
    @RequestMapping(value = "/document",method = RequestMethod.DELETE)
    public OutDeleteDocumentDto deleteDocument(@RequestBody @Valid InDeleteDocumentDto inDeleteDocumentDto){
        OutDeleteDocumentDto outDeleteDocumentDto;
        try {
            outDeleteDocumentDto = dmlApiApplication.deleteDocument(inDeleteDocumentDto);
        }catch (Exception e){
            outDeleteDocumentDto = new OutDeleteDocumentDto();
            e.printStackTrace();
            outDeleteDocumentDto.setRtCode(ResponseCode.ERROR);
        }
        return outDeleteDocumentDto;
    }

    /**
     * 修改一条document数据
     * @param inUpdateDocumentDto
     * @return
     */
    @RequestMapping(value = "/document",method = RequestMethod.PUT)
    public OutUpdateDocumentDto upateDocument(@RequestBody @Valid InUpdateDocumentDto inUpdateDocumentDto){
        OutUpdateDocumentDto outUpdateDocumentDto;
        try {
            outUpdateDocumentDto = dmlApiApplication.updateDocument(inUpdateDocumentDto);
        }catch (Exception e){
            outUpdateDocumentDto = new OutUpdateDocumentDto();
            e.printStackTrace();
            outUpdateDocumentDto.setRtCode(ResponseCode.ERROR);
        }
        return outUpdateDocumentDto;
    }

    /**
     * 批量操作增，改，删
     * @param inBulkOperationDto
     * @return
     */
    @RequestMapping(value = "/bulk/operation",method = RequestMethod.POST)
    public BaseResponseDto bulkOperation(@RequestBody @Valid InBulkOperationDto inBulkOperationDto){
        BaseResponseDto baseResponseDto;
        try {
            baseResponseDto = dmlApiApplication.bulkOperation(inBulkOperationDto);
        }catch (Exception e){
            baseResponseDto = new BaseResponseDto();
            baseResponseDto.setRtCode(ResponseCode.ERROR);
            e.printStackTrace();
        }
        return baseResponseDto;
    }

    /**
     * 自定义ES查询 需要自己传入整个的ES查询体
     * @param inSearchDto
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public OutSearchDslDto search(@RequestBody @Valid InSearchDslDto inSearchDto){
        OutSearchDslDto outSearchDslDto ;
        try {
            outSearchDslDto = dmlApiApplication.search(inSearchDto);
        }catch (Exception e){
            e.printStackTrace();
            outSearchDslDto = new OutSearchDslDto();
            outSearchDslDto.setRtCode(ResponseCode.ERROR);
        }
        return outSearchDslDto;
    }

    /**
     * 传入sql查询
     * @param inSearchForSqlDto
     * @return
     */
    @RequestMapping(value = "/sql/search",method = RequestMethod.POST)
    public OutSearchForSqlDto sqlSearch(@RequestBody @Valid InSearchForSqlDto inSearchForSqlDto){
        OutSearchForSqlDto outSearchForSqlDto;
        try {
            outSearchForSqlDto = dmlApiApplication.searchForSQL(inSearchForSqlDto.getSql());
            outSearchForSqlDto.setRtCode(ResponseCode.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            outSearchForSqlDto = new OutSearchForSqlDto();
            outSearchForSqlDto.setRtCode(ResponseCode.ERROR);
        }
        return outSearchForSqlDto;
    }

    /**
     * 传入标签，转化为sql
     * @param inSearchForTagDto
     * @return
     */
    @RequestMapping(value = "/get/searchForTagBySql",method = RequestMethod.POST)
    public OutSearchForSqlDto tagSearch(@RequestBody @Valid InSearchForTagDto inSearchForTagDto){
        OutSearchForSqlDto outSearchForSqlDto;
        try {
            outSearchForSqlDto = dmlApiApplication.searchForTagBySql(inSearchForTagDto);
            outSearchForSqlDto.setRtCode(ResponseCode.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            outSearchForSqlDto = new OutSearchForSqlDto();
            outSearchForSqlDto.setRtCode(ResponseCode.ERROR);

        }
        return outSearchForSqlDto;
    }

    /**
     * 传入标签，解析为ES请求体
     * @param inSearchForTagDto
     * @return
     */
    @RequestMapping(value = "/get/searchForTagByEsBody",method = RequestMethod.POST)
    public OutSearchDto tagSearchForEsBody(@RequestBody @Valid InSearchForTagDto inSearchForTagDto){
        OutSearchDto outSearchDto ;
        try {
            outSearchDto = dmlApiApplication.searchForTagByEsBody(inSearchForTagDto);
        }catch (Exception e){
            e.printStackTrace();
            outSearchDto = new OutSearchDto();
            outSearchDto.setRtCode(ResponseCode.ERROR);
        }
        return outSearchDto;
    }

    @RequestMapping(value = "/notAnnoy/test",method = RequestMethod.POST)
    public Long notAnnoyTest(@RequestBody InNotAnnoyDto inNotAnnoyDto) throws Exception{
        return dmlApiApplication.notAnnoy(inNotAnnoyDto.getSize(),inNotAnnoyDto.getTag(),
                inNotAnnoyDto.getValueMap(),inNotAnnoyDto.getNotAnnoyList(),inNotAnnoyDto.getField());
    }

    @RequestMapping(value = "/get/scroll" ,method = RequestMethod.POST)
    public OutSearchByScrollDto queryDataOfScroll(@RequestBody InNotAnnoyDto inNotAnnoyDto) throws Exception{
        return dmlApiApplication.queryDataOfScroll(inNotAnnoyDto);
    }

    @RequestMapping(value = "/get/scrollId",method = RequestMethod.GET)
    public OutSearchByScrollDto queryDataByScrollId(String scrollId) throws Exception{
        return dmlApiApplication.queryDataByScrollId(scrollId);
    }


}
