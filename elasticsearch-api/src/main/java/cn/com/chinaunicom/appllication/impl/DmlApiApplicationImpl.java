package cn.com.chinaunicom.appllication.impl;

import cn.com.chinaunicom.appllication.DmlApiApplication;
import cn.com.chinaunicom.constant.ElasticDoc;
import cn.com.chinaunicom.constant.ElasticSearchField;
import cn.com.chinaunicom.constant.ResponseCode;
import cn.com.chinaunicom.dto.BaseResponseDto;
import cn.com.chinaunicom.dto.HttpResult;
import cn.com.chinaunicom.dto.dml.*;
import cn.com.chinaunicom.dto.dml.simple.InSearchDslDto;
import cn.com.chinaunicom.dto.dml.simple.OutSearchDslDto;
import cn.com.chinaunicom.util.HttpUtil;
import cn.com.chinaunicom.util.TagUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
@Named
@Slf4j
public class DmlApiApplicationImpl implements DmlApiApplication {

    @Inject
    private JestClient jestClient;

    @Inject
    private Gson gson;

//    public JestClient getJestClientBySSL() throws Exception{
//
////        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
////        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("amdin","admin"));
//
////        KeyStore keyStore  = KeyStore.getInstance("UBER");
////        FileInputStream instream = new FileInputStream(new File("D:/elasticsearch/elasticsearch-6.3.1/config/esnode-key.pem"));
////        try {
////            keyStore.load(instream, null);
////        } finally {
////            instream.close();
////        }
////        KeyStore certStore = KeyStore.getInstance("PKCS12");
////        FileInputStream certInstream = new FileInputStream(new File("D:\\elasticsearch\\elasticsearch-6.3.1\\config\\esnode.pem"));
////        try {
////            certStore.load(certInstream, null);
////        } finally {
////            certInstream.close();
////        }
//
////        SSLContext sslcontext = SSLContexts.custom()
////                .loadKeyMaterial(keyStore, null)
////                .loadTrustMaterial(certStore,new TrustSelfSignedStrategy())
////                .build();
//        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//            @Override
//            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
//                return true;
//            }
//        }).build();
//
//        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
//
//        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
////        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
////                sslcontext,
////                new String[] { "TLSv1" },
////                null,
////                hostnameVerifier);
//        SchemeIOSessionStrategy httpsIOSessionStrategy = new SSLIOSessionStrategy(sslContext, hostnameVerifier);
//
//        JestClientFactory factory = new JestClientFactory();
//        factory.setHttpClientConfig(new HttpClientConfig.Builder("https://localhost:9200")
//                .defaultSchemeForDiscoveredNodes("https") // required, otherwise uses http
//                .sslSocketFactory(sslSocketFactory) // this only affects sync calls
//                .httpsIOSessionStrategy(httpsIOSessionStrategy) // this only affects async calls
//                .defaultCredentials("admin","admin")
//                .build()
//        );
//        return factory.getObject();
//    }

    @Override
    public OutGetDocumentByIdDto getDocumentById(InGetDocumentByIdDto inGetDocumentByIdDto) throws Exception{
        OutGetDocumentByIdDto outGetDocumentByIdDto = new OutGetDocumentByIdDto();
        Get get;
        if (StringUtils.isBlank(inGetDocumentByIdDto.getType())){
            get = new Get.Builder(inGetDocumentByIdDto.getIndex(),inGetDocumentByIdDto.getDocId()).build();
        }else{
            get = new Get.Builder(inGetDocumentByIdDto.getIndex(),inGetDocumentByIdDto.getDocId())
                    .type(inGetDocumentByIdDto.getType()).build();
        }
        JestResult jestResult = jestClient.execute(get);
        if (jestResult.isSucceeded()){
            JsonElement source = jestResult.getJsonObject().get(ElasticSearchField.SOURCE);
            if (null != source){
                outGetDocumentByIdDto.setRtCode(ResponseCode.SUCCESS);
                outGetDocumentByIdDto.setSource(gson.fromJson(source.toString(),Map.class));
                outGetDocumentByIdDto.setIndex(inGetDocumentByIdDto.getIndex());
                outGetDocumentByIdDto.setType(inGetDocumentByIdDto.getType());
                outGetDocumentByIdDto.setId(inGetDocumentByIdDto.getDocId());
            }
        }else{
            outGetDocumentByIdDto.setRtCode(ResponseCode.OTHER_ERROR);
            outGetDocumentByIdDto.setMessage(jestResult.getErrorMessage());
        }
        return outGetDocumentByIdDto;
    }

    @Override
    public OutAddDocumentDto insertDocument(InAddDocumentDto inAddDocumentDto) throws Exception{
        OutAddDocumentDto outAddDocumentDto = new OutAddDocumentDto();
       Index.Builder builder = new Index.Builder(inAddDocumentDto.getSource()).index(inAddDocumentDto.getIndex());
        if (StringUtils.isNotBlank(inAddDocumentDto.getType())){
            builder.type(inAddDocumentDto.getType());
        }
        if (StringUtils.isNotBlank(inAddDocumentDto.getDocId())){
            builder.id(inAddDocumentDto.getDocId());
        }
        JestResult result = jestClient.execute(builder.build());
        if (result.isSucceeded()){
            outAddDocumentDto.setRtCode(ResponseCode.SUCCESS);
            outAddDocumentDto.setDocId(result.getJsonObject().get(ElasticSearchField.ID).getAsString());
            outAddDocumentDto.setMessage("新增数据成功");
            outAddDocumentDto.setType(result.getJsonObject().get(ElasticSearchField.TYPE).getAsString());
            outAddDocumentDto.setIndex(inAddDocumentDto.getIndex());
        }else{
            outAddDocumentDto.setRtCode(ResponseCode.OTHER_ERROR);
            outAddDocumentDto.setMessage(result.getErrorMessage());
        }
        return outAddDocumentDto;
    }

    @Override
    public OutDeleteDocumentDto deleteDocument(InDeleteDocumentDto inDeleteDocumentDto) throws Exception{
        OutDeleteDocumentDto outDeleteDocumentDto = new OutDeleteDocumentDto();
        Delete delete = new Delete.Builder(inDeleteDocumentDto.getDocId()).index(inDeleteDocumentDto.getIndex())
                .type(inDeleteDocumentDto.getType()).build();
        JestResult result = jestClient.execute(delete);
        if (result.isSucceeded()){
            outDeleteDocumentDto.setRtCode(ResponseCode.SUCCESS);
            outDeleteDocumentDto.setMessage("删除文档成功");
            outDeleteDocumentDto.setVersion(result.getJsonObject().get(ElasticSearchField.VERSION).getAsLong());
        }else{
            outDeleteDocumentDto.setRtCode(ResponseCode.OTHER_ERROR);
            outDeleteDocumentDto.setMessage(result.getErrorMessage());
        }
        return outDeleteDocumentDto;
    }

    @Override
    public OutUpdateDocumentDto updateDocument(InUpdateDocumentDto inUpdateDocumentDto) throws Exception {
        OutUpdateDocumentDto outUpdateDocumentDto = new OutUpdateDocumentDto();
        Object source = null;
        if (null != inUpdateDocumentDto.getSource()){
            source = new ElasticDoc(inUpdateDocumentDto.getSource());
        }
        Update update = new Update.Builder(source).index(inUpdateDocumentDto.getIndex())
                .type(inUpdateDocumentDto.getType()).id(inUpdateDocumentDto.getDocId()).build();
        JestResult result = jestClient.execute(update);
        if (result.isSucceeded()){
            outUpdateDocumentDto.setRtCode(ResponseCode.SUCCESS);
            outUpdateDocumentDto.setMessage("修改成功");
            outUpdateDocumentDto.setVersion(result.getJsonObject().get(ElasticSearchField.VERSION).getAsLong());
        }else{
            outUpdateDocumentDto.setRtCode(ResponseCode.OTHER_ERROR);
            outUpdateDocumentDto.setMessage(result.getErrorMessage());
        }
        return outUpdateDocumentDto;
    }

    @Override
    public BaseResponseDto bulkOperation(InBulkOperationDto inBulkOperationDto) throws Exception {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        List actionList = new ArrayList<>();
        List<BulkOperaDataDto> bulkOperaDataDtoList = inBulkOperationDto.getBulkOperaDataDtoList();
        if (null != bulkOperaDataDtoList && bulkOperaDataDtoList.size() > 0){
            for (BulkOperaDataDto bulkOperaDataDto : bulkOperaDataDtoList){
                Object source = bulkOperaDataDto.getSource();
                String docId = bulkOperaDataDto.getDocId();
                switch (bulkOperaDataDto.getOperation()){
                    case "index": actionList.add(new Index.Builder(source).id(docId).build());break;

                    case "update":source = new ElasticDoc(source);
                        actionList.add(new Update.Builder(source).id(docId).build());break;

                    case "delete": actionList.add(new Delete.Builder(docId).build());break;

                    default: log.info("无效的数据操作: {}",bulkOperaDataDto.getOperation());
                }
            }
            Bulk bulk = new Bulk.Builder().defaultIndex(inBulkOperationDto.getIndex()).
                    defaultType(inBulkOperationDto.getType()).addAction(actionList).build();
            JestResult jestResult = jestClient.execute(bulk);
            if (jestResult.isSucceeded()){
                baseResponseDto.setRtCode(ResponseCode.SUCCESS);
                baseResponseDto.setMessage("操作成功");
            }else{
                baseResponseDto.setRtCode(ResponseCode.OTHER_ERROR);
                baseResponseDto.setMessage(jestResult.getErrorMessage());
            }
        }
        return baseResponseDto;
    }

    @Override
    public OutSearchDslDto search(InSearchDslDto inSearchDslDto) throws Exception{
        OutSearchDslDto outSearchDto = new OutSearchDslDto();
        Search.Builder searchBuilder = new Search.Builder(inSearchDslDto.getQuery());
        if (null != inSearchDslDto.getIndex()){
            searchBuilder.addIndex(inSearchDslDto.getIndex());
        }
        if (null != inSearchDslDto.getType()){
            searchBuilder.addType(inSearchDslDto.getType());
        }
        JestResult jestResult = jestClient.execute(searchBuilder.build());
        outSearchDto.setResponse(jestResult.getJsonString());
        return outSearchDto;
    }

    private Long getSearchTotal(JestResult jestResult) throws Exception{
        if (null != jestResult){
           JsonElement hits = jestResult.getJsonObject().get(ElasticSearchField.HITS);
            if (null != hits){
                JsonElement total = hits.getAsJsonObject().get(ElasticSearchField.TOTAL);
                if (null != total){
                    return total.getAsLong();
                }
            }
        }
        return 0L;
    }

    private List<SearchHitsDto> getSearchHits(JestResult jestResult) throws Exception{
        Gson gson = new Gson();
        List<SearchHitsDto> rtList = new ArrayList<>();
        if (null != jestResult){
            JsonElement hits = jestResult.getJsonObject().get(ElasticSearchField.HITS);
            if (null != hits){
                hits = hits.getAsJsonObject().get(ElasticSearchField.HITS);
                JsonArray hitsArray = hits.getAsJsonArray();
                if (null != hitsArray && hitsArray.size() > 0){
                    SearchHitsDto searchHitsDto;
                    for (JsonElement jsonElement : hitsArray){
                        searchHitsDto = new SearchHitsDto();
                        JsonObject hit = jsonElement.getAsJsonObject();
                        searchHitsDto.setIndex(hit.get(ElasticSearchField.INDEX).getAsString());
                        searchHitsDto.setType(hit.get(ElasticSearchField.TYPE).getAsString());
                        searchHitsDto.setId(hit.get(ElasticSearchField.ID).getAsString());
                        Map<String,Object> source = gson.fromJson(hit.get(ElasticSearchField.SOURCE),
                                new TypeToken<Map<String, Object>>() {}.getType());
                        searchHitsDto.setSource(source);
                        rtList.add(searchHitsDto);
                    }
                }
            }
        }
        return rtList;
    }

    /**
     * http 无验证
     * @param elasticSearchSql
     * @return
     * @throws Exception
     */
    @Override
    public OutSearchForSqlDto searchForSQL(String elasticSearchSql) throws Exception{
        return searchForSQLByHttps(elasticSearchSql);
    }


    public OutSearchForSqlDto searchForSQLByHttps(String elasticSearchSql) throws Exception{
        OutSearchForSqlDto outSearchForSqlDto = new OutSearchForSqlDto();
        HttpResult httpResult = HttpUtil.doPost(ElasticSearchField.XPACK_FOR_SQL,
                "{\"query\":\""+elasticSearchSql+"\"}");
        if (httpResult.isSuccess()) {
            outSearchForSqlDto = gson.fromJson(httpResult.getResponseString(), OutSearchForSqlDto.class);
            if (null != outSearchForSqlDto) {
                outSearchForSqlDto.setRtCode(ResponseCode.SUCCESS);
                outSearchForSqlDto.setDataList(getDataByResult(outSearchForSqlDto));
            }
        }else{
            outSearchForSqlDto.setRtCode(ResponseCode.OTHER_ERROR);
            outSearchForSqlDto.setMessage(httpResult.getResponseString());
        }
        return outSearchForSqlDto;
    }

    @Override
    public OutSearchDto searchForTagByEsBody(InSearchForTagDto inSearchForTagDto) throws Exception{
        OutSearchDto outSearchDto = new OutSearchDto();
        Search.Builder searchBuilder = new Search.Builder(TagUtil.decodeTagToEsBody(inSearchForTagDto.getTag(),inSearchForTagDto.getValueMap()));
        if (StringUtils.isNotBlank(inSearchForTagDto.getIndex())){
            searchBuilder.addIndex(inSearchForTagDto.getIndex());
        }
        JestResult jestResult = jestClient.execute(searchBuilder.build());
        if (jestResult.isSucceeded()){
            outSearchDto.setRtCode(ResponseCode.SUCCESS);
            outSearchDto.setTotal(getSearchTotal(jestResult));
            outSearchDto.setHits(getSearchHits(jestResult));
        }else {
            outSearchDto.setRtCode(ResponseCode.OTHER_ERROR);
            outSearchDto.setMessage(jestResult.getErrorMessage());
        }
        return outSearchDto;
    }


    @Override
    public OutSearchForSqlDto searchForTagBySql(InSearchForTagDto inSearchForTagDto) throws Exception{
        StringBuilder stringBuilder = new StringBuilder("select ");
        if (null != inSearchForTagDto.getColumnList() && inSearchForTagDto.getColumnList().size() > 0){
            for (String column : inSearchForTagDto.getColumnList()){
                stringBuilder.append(column).append(",");
            }
            stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length()," ");
        }else{
            stringBuilder.append("* ");
        }
        stringBuilder.append(" from ").append(inSearchForTagDto.getIndex());
        if (StringUtils.isNotBlank(inSearchForTagDto.getTag())) {
            stringBuilder.append(" where ").append(TagUtil.decodeTagToSql(inSearchForTagDto.getTag(), inSearchForTagDto.getValueMap()));
        }
        if (null != inSearchForTagDto.getSortList() && inSearchForTagDto.getSortList().size() > 0){
            stringBuilder.append(" order by ");
            for (SearchSortDto searchSortDto : inSearchForTagDto.getSortList()){
                String sortMethod = StringUtils.isBlank(searchSortDto.getSortMethod())?"asc":searchSortDto.getSortMethod();
                stringBuilder.append(searchSortDto.getColumn()).append(" ").append(sortMethod).append(",");
            }
            stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length()," ");
        }
//        if (null != inSearchForTagDto.getPageNo() && null != inSearchForTagDto.getPageSize()){
//            stringBuilder.append(" limit ").append(10);
//        }
        return searchForSQLByHttps(stringBuilder.toString());
    }


    /**
     * 根据列信息和数据信息组装成我们习惯的对象集合形式
     * @param result
     * @return
     */
    private List<Map<String,Object>> getDataByResult(OutSearchForSqlDto result){
        List<Map<String,Object>> dataList = new ArrayList<>();
        List<SearchForSqlColumnDto> columnDtos = result.getColumns();
        List<ArrayList> rows = result.getRows();

        if (null != rows && rows.size() > 0 && null != columnDtos && columnDtos.size() > 0){
            for (ArrayList list : rows){
                Map<String,Object> map = new HashMap<>();
                for (int i = 0; i<columnDtos.size(); i++){
                    map.put(columnDtos.get(i).getName(),list.get(i));
                }
                dataList.add(map);
            }
        }
        return dataList;
    }

    @Override
    public Long notAnnoy(Integer size,String tag,Map<String,Object> valueMap,List<String> notAnnoyList,String field) throws Exception{
        Long customerNum = 0L;
        customerNum = scrollApiByFilter(tag,valueMap,size,null,notAnnoyList,field,customerNum);
        System.out.println(customerNum);
        return customerNum;
    }


    @Override
    public OutSearchByScrollDto queryDataOfScroll(InNotAnnoyDto inNotAnnoyDto) throws Exception{
        OutSearchByScrollDto outSearchByScrollDto = new OutSearchByScrollDto();
        String requestBody = TagUtil.decodeTagToEsBody(inNotAnnoyDto.getTag(),inNotAnnoyDto.getValueMap());
        Map<String,Object> requestMap = gson.fromJson(requestBody,Map.class);
        requestMap.put("size",inNotAnnoyDto.getSize());
        HttpResult httpResult = HttpUtil.doPost("customer/_search?scroll=3m",gson.toJson(requestMap));
        if (httpResult.isSuccess()){
            Map<String,Object> resultMap = gson.fromJson(httpResult.getResponseString(),Map.class);
            outSearchByScrollDto.setScrollId(resultMap.get("_scroll_id").toString());
            Map<String,Object> hits = (Map<String, Object>) resultMap.get("hits");
            outSearchByScrollDto.setTotal(new Double(hits.get("total").toString()).longValue());
            outSearchByScrollDto.setHits((List<Map<String, Object>>) hits.get("hits"));
        }
        return outSearchByScrollDto;
    }

    @Override
    public OutSearchByScrollDto queryDataByScrollId(String scrollId) throws Exception{
        OutSearchByScrollDto outSearchByScrollDto = new OutSearchByScrollDto();
        Map<String, Object> scrollMap = new HashMap<>();
        scrollMap.put("scroll", "3m");
        scrollMap.put("scroll_id", scrollId);
        HttpResult httpResult = HttpUtil.doPost("/_search/scroll", gson.toJson(scrollMap));
        if (httpResult.isSuccess()){
            Map<String,Object> resultMap = gson.fromJson(httpResult.getResponseString(),Map.class);
            outSearchByScrollDto.setScrollId(resultMap.get("_scroll_id").toString());
            Map<String,Object> hits = (Map<String, Object>) resultMap.get("hits");
            outSearchByScrollDto.setTotal(new Double(hits.get("total").toString()).longValue());
            outSearchByScrollDto.setHits((List<Map<String, Object>>) hits.get("hits"));
        }
        return outSearchByScrollDto;
    }



    private Long scrollApiByFilter(String tag,Map<String,Object> valueMap,Integer size,String scrollId,List<String> notAnnoyList,String field,Long customerNum) throws Exception{
        HttpResult httpResult;
        if (scrollId != null) {
            Map<String, Object> scrollMap = new HashMap<>();
            scrollMap.put("scroll", "3m");
            scrollMap.put("scroll_id", scrollId);
            httpResult = HttpUtil.doPost("/_search/scroll", gson.toJson(scrollMap));
        }else{
            String requestBody = TagUtil.decodeTagToEsBody(tag,valueMap);
            Map<String,Object> requestMap = gson.fromJson(requestBody,Map.class);
            requestMap.put("size",size);
            if (null != notAnnoyList && notAnnoyList.size() > 0){
                Map<String,Object> queryMap = (Map<String, Object>) requestMap.get("query");
                if(null != queryMap.get("bool")){
                    Map<String,Object> boolMap = (Map<String, Object>) queryMap.get("bool");
                    List<Map<String,Object>> mustNotList = new ArrayList<>();
                    Map<String,Object> mustNotMap = new HashMap<>();
                    Map<String,Object> termsMap = new HashMap<>();
                    termsMap.put(field,notAnnoyList);
                    mustNotMap.put("terms",termsMap);
                    mustNotList.add(mustNotMap);
                    boolMap.put("must_not",mustNotList);
                }
            }
            httpResult = HttpUtil.doPost("customer/_search?scroll=3m",gson.toJson(requestMap));
        }
        Map<String,Object> resultMap = gson.fromJson(httpResult.getResponseString(),Map.class);
        Map<String,Object> hitsMap = (Map<String, Object>) resultMap.get("hits");
        Long total = new Double(hitsMap.get("total").toString()).longValue();
        return total;
    }

    private Long scrollApiByLoop(String tag,Map<String,Object> valueMap,Integer size,String scrollId,List<String> notAnnoyList,String field,Long customerNum) throws Exception{
        HttpResult httpResult;
        if (scrollId != null) {
            Map<String, Object> scrollMap = new HashMap<>();
            scrollMap.put("scroll", "3m");
            scrollMap.put("scroll_id", scrollId);
            httpResult = HttpUtil.doPost("/_search/scroll", gson.toJson(scrollMap));
        }else{
            String requestBody = TagUtil.decodeTagToEsBody(tag,valueMap);
            Map<String,Object> requestMap = gson.fromJson(requestBody,Map.class);
            requestMap.put("size",size);
            httpResult = HttpUtil.doPost("customer/_search?scroll=3m",gson.toJson(requestMap));
        }
        Map<String,Object> resultMap = gson.fromJson(httpResult.getResponseString(),Map.class);
        Map<String,Object> hitsMap = (Map<String, Object>) resultMap.get("hits");
        Long total = new Double(hitsMap.get("total").toString()).longValue();
        if ( null != total && total > 0){
            List<Map<String,Object>> list = (List<Map<String, Object>>) hitsMap.get("hits");
            if (list != null && list.size() > 0) {
                for (Map<String, Object> hit : list) {
                    Map<String, Object> sourceMap = (Map<String, Object>) hit.get("_source");
                    boolean notAnnoyFlag = true;
                    if (null != notAnnoyList && notAnnoyList.size() > 0) {
                        for (String notAnnoy : notAnnoyList) {
                            if (notAnnoy.equals(sourceMap.get(field).toString())) {
                                notAnnoyFlag = false;
                            }
                        }
                    }
                    if (notAnnoyFlag) {
                        customerNum++;
                    }
                }
               return scrollApiByLoop(null,null,null,resultMap.get("_scroll_id").toString(),notAnnoyList,field,customerNum);
            }else{
                return customerNum;
            }
        }else{
            return customerNum;
        }
    }

    /**
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Long getAllNum(String tag,String field,String index,String type) throws Exception {
        Map<String,Object> map = TagUtil.getRequestBodyByTag(tag);
        Map<String,Object> aggsMap = new HashMap<>();
        map.put("aggs", aggsMap);
        Map<String,Object> userCountMap = new HashMap<>();
        aggsMap.put("user_count",userCountMap);
        Map<String,Object> cardinalityMap = new HashMap<>();
        userCountMap.put("cardinality", cardinalityMap);
        cardinalityMap.put("field", field);
        Search search = new Search.Builder(index).addIndex(index).addType(type).build();
        JestResult jestResult = jestClient.execute(search);
        if (jestResult.isSucceeded()){

        }
        return 0L;
    }

}
