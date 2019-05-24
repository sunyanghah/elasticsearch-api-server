package cn.com.chinaunicom.appllication;

import cn.com.chinaunicom.dto.BaseResponseDto;
import cn.com.chinaunicom.dto.HttpResult;
import cn.com.chinaunicom.dto.dml.*;
import cn.com.chinaunicom.dto.dml.simple.InSearchDslDto;
import cn.com.chinaunicom.dto.dml.simple.OutSearchDslDto;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/7/28.
 */
public interface DmlApiApplication {

    /**
     * 获取单条document
     * @param inGetDocumentByIdDto
     * @return
     * @throws Exception
     */
    OutGetDocumentByIdDto getDocumentById(InGetDocumentByIdDto inGetDocumentByIdDto) throws Exception;

    /**
     * 新增单条document
     * @param inAddDocumentDto
     * @return
     * @throws Exception
     */
    OutAddDocumentDto insertDocument(InAddDocumentDto inAddDocumentDto) throws Exception;

    /**
     * 删除单条document
     * @param inDeleteDocumentDto
     * @return
     * @throws Exception
     */
    OutDeleteDocumentDto deleteDocument(InDeleteDocumentDto inDeleteDocumentDto) throws Exception;

    /**
     * 修改单条document，无法删除field
     * @param inUpdateDocumentDto
     * @return
     * @throws Exception
     */
    OutUpdateDocumentDto updateDocument(InUpdateDocumentDto inUpdateDocumentDto) throws Exception;

    /**
     * 批量操作
     * @param inBulkOperationDto
     * @return
     * @throws Exception
     */
    BaseResponseDto bulkOperation(InBulkOperationDto inBulkOperationDto) throws Exception;

    /**
     * _search 搜索
     * @param inSearchDslDto
     * @throws Exception
     * @return
     */
    OutSearchDslDto search(InSearchDslDto inSearchDslDto) throws Exception;

    /**
     * 根据sql查询数据
     * @param elasticSearchSql
     * @return
     * @throws Exception
     */
    OutSearchForSqlDto searchForSQL(String elasticSearchSql) throws Exception;

    /**
     * 标签解析为sql语句
     * @param inSearchForTagDto
     * @return
     * @throws Exception
     */
    OutSearchForSqlDto searchForTagBySql(InSearchForTagDto inSearchForTagDto) throws Exception;

    /**
     * 标签解析为ES请求体
     * @param inSearchForTagDto
     * @return
     * @throws Exception
     */
    OutSearchDto searchForTagByEsBody(InSearchForTagDto inSearchForTagDto) throws Exception;


    Long notAnnoy(Integer size, String tag, Map<String,Object> valueMap, List<String> notAnnoyList, String field) throws Exception;

    OutSearchByScrollDto queryDataOfScroll(InNotAnnoyDto inNotAnnoyDto) throws Exception;

    OutSearchByScrollDto queryDataByScrollId(String scrollId) throws Exception;
}
