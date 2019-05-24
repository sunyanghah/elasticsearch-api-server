package cn.com.chinaunicom.appllication;

import cn.com.chinaunicom.dto.BaseResponseDto;
import cn.com.chinaunicom.dto.ddl.InDeleteIndexDto;
import cn.com.chinaunicom.dto.ddl.InPutIndexDto;
import cn.com.chinaunicom.dto.ddl.InSetMappingDto;
import cn.com.chinaunicom.dto.ddl.OutIsIndexExistsDto;

/**
 * Created by dell on 2018/7/27.
 * @author dell
 */
public interface DdlApiApplication {

    /**
     * 新建索引
     * @param inPutIndexDto
     * @throws Exception
     * @return
     */
    BaseResponseDto insertIndex(InPutIndexDto inPutIndexDto) throws Exception;


    /**
     * 判断索引是否存在
     * @return
     * @param index
     * @throws Exception
     */
    OutIsIndexExistsDto isIndexExists(String index) throws Exception;

    /**
     * 删除索引
     * @param inDeleteIndexDto
     * @return
     * @throws Exception
     */
    BaseResponseDto deleteIndex(InDeleteIndexDto inDeleteIndexDto) throws Exception;

    /**
     * 自定义mapping
     * @param inSetMappingDto
     * @throws Exception
     */
    BaseResponseDto mapping(InSetMappingDto inSetMappingDto) throws Exception;
}
