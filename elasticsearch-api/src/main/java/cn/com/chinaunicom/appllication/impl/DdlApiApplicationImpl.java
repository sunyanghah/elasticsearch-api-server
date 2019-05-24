package cn.com.chinaunicom.appllication.impl;

import cn.com.chinaunicom.appllication.DdlApiApplication;
import cn.com.chinaunicom.constant.ResponseCode;
import cn.com.chinaunicom.dto.BaseResponseDto;
import cn.com.chinaunicom.dto.HttpResult;
import cn.com.chinaunicom.dto.ddl.InDeleteIndexDto;
import cn.com.chinaunicom.dto.ddl.InPutIndexDto;
import cn.com.chinaunicom.dto.ddl.InSetMappingDto;
import cn.com.chinaunicom.dto.ddl.OutIsIndexExistsDto;
import cn.com.chinaunicom.util.HttpUtil;
import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2018/7/27.
 * @author dell
 */
@Named
public class DdlApiApplicationImpl implements DdlApiApplication {

    @Inject
    private JestClient jestClient;

    @Inject
    private Gson gson;

    @Override
    public BaseResponseDto insertIndex(InPutIndexDto inPutIndexDto) throws Exception{
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        JestResult jr = jestClient.execute(new CreateIndex.Builder(inPutIndexDto.getIndex()).build());
        if (jr.isSucceeded()){
            baseResponseDto.setRtCode(ResponseCode.SUCCESS);
            baseResponseDto.setMessage("新建索引成功");
        }else{
            baseResponseDto.setRtCode(ResponseCode.OTHER_ERROR);
            baseResponseDto.setMessage(jr.getErrorMessage());
        }
        return baseResponseDto;
    }

    @Override
    public OutIsIndexExistsDto isIndexExists(String index) throws Exception{
        OutIsIndexExistsDto outIsIndexExistsDto = new OutIsIndexExistsDto();
        IndicesExists indicesExists = new IndicesExists.Builder(index).build();
        JestResult result = jestClient.execute(indicesExists);
        if (result.isSucceeded()){
            outIsIndexExistsDto.setExistsFlag(true);
        }else{
            outIsIndexExistsDto.setExistsFlag(false);
            outIsIndexExistsDto.setMessage(result.getErrorMessage());
        }
        return outIsIndexExistsDto;
    }

    @Override
    public BaseResponseDto deleteIndex(InDeleteIndexDto inDeleteIndexDto) throws Exception {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        DeleteIndex deleteIndex = new DeleteIndex.Builder(inDeleteIndexDto.getIndex()).build();
        JestResult result = jestClient.execute(deleteIndex);
        if(result.isSucceeded()){
            baseResponseDto.setRtCode(ResponseCode.SUCCESS);
            baseResponseDto.setMessage("删除索引成功");
        }else{
            baseResponseDto.setRtCode(ResponseCode.OTHER_ERROR);
            baseResponseDto.setMessage(result.getErrorMessage());
        }
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto mapping(InSetMappingDto inSetMappingDto) throws Exception{
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map1 = new HashMap<>();
        Map<String,Object> map2 = new HashMap<>();
        Map<String,Object> map3 = new HashMap<>();
        Map<String,Object> map4 = new HashMap<>();
        map.put("type",inSetMappingDto.getFieldType());
        map1.put(inSetMappingDto.getField(),map);
        map2.put("properties",map1);
        map3.put(inSetMappingDto.getType(),map2);
        map4.put("mappings",map3);

        HttpResult httpResult = HttpUtil.doPut(inSetMappingDto.getIndex(),gson.toJson(map4));
        if (httpResult.isSuccess()){
            baseResponseDto.setRtCode(ResponseCode.SUCCESS);
            baseResponseDto.setMessage("设置mapping成功");
        }else{
            baseResponseDto.setRtCode(ResponseCode.OTHER_ERROR);
            baseResponseDto.setMessage(httpResult.getResponseString());
        }
        return baseResponseDto;
    }


}
