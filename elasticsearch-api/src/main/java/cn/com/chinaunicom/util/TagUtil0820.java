package cn.com.chinaunicom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

/**
 * Created by dell on 2018/8/20.
 */
public class TagUtil0820 {

    //	[{"fieldName": "gender",
    //		"relation": "or",
    //		"value": [{"=": "男"}]
    //	}, {"fieldName":"age",
    //		"relation":"or",
    //		"value":[{">=":50,"<=":100}]
    //	}, {
    //		"fieldName": "website.webCode",
    //		"relation": "and",
    //		"value": [{"=": "W5214"}, {"=": "W348"}]
    //	}, {
    //		"fieldName": "app.appCode",
    //		"relation": "and",
    //		"value": [{"=": "C30646"}]
    //	}]
    public static void main(String[] args) throws Exception{
        String jsonStr = "[{\"fieldName\": \"gender\",\"relation\": \"or\",\"value\": [{\"=\": \"男\"}]},{\"fieldName\":\"age\",\"relation\":\"or\",\"value\":[{\">=\":50,\"<=\":100}]},{\"fieldName\": \"website.webCode\",\"relation\": \"and\",\"value\": [{\"=\": \"W5214\"}, {\"=\": \"W348\"}]}, {\"fieldName\": \"app.appCode\",\"relation\": \"and\",\"value\": [{\"=\": \"C30646\"}]}]";
        Map<String,Object> map = getRequestBodyByTag(jsonStr);
        String str = new Gson().toJson(map);
        System.out.println(str);
    }

    public static Map<String,Object> getNormalMap(String relation,String field,List<Map<String,Object>> valueList) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        if(valueList.size() > 1){
            Map<String,Object> boolMap = new HashMap<>();
            resultMap.put("bool", boolMap);
            String esRelation = "must";
            if("and".equals(relation)){
                esRelation = "must";
            }else if("or".equals(relation)){
                esRelation = "should";
            }
            List<Map<String,Object>> relationList = new ArrayList<>();
            boolMap.put(esRelation,relationList);

            for(Map<String,Object> valueMap : valueList){
                Map<String,Object> map = new HashMap<>();
                int keySize = valueMap.keySet().size();
                if( keySize > 1){
                    Map<String,Object> keyBoolMap = new HashMap<>();
                    map.put("bool", keyBoolMap);
                    List<Map<String,Object>> keyMustList = new ArrayList<>();
                    keyBoolMap.put("must",keyMustList);
                    for(Entry<String, Object> entry : valueMap.entrySet()){
                        keyMustList.add(getCompareMap(entry.getKey(),field,entry.getValue()));
                    }

                }else if(keySize == 1){
                    for(Entry<String, Object> entry : valueMap.entrySet()){
                        map = getCompareMap(entry.getKey(),field,entry.getValue());
                    }
                }
            }
        }else if(valueList.size() == 1){
            Map<String,Object> valueMap = valueList.get(0);
            int keySize = valueMap.keySet().size();
            if( keySize > 1){
                Map<String,Object> keyBoolMap = new HashMap<>();
                resultMap.put("bool", keyBoolMap);
                List<Map<String,Object>> keyMustList = new ArrayList<>();
                keyBoolMap.put("must",keyMustList);
                for(Entry<String, Object> entry : valueMap.entrySet()){
                    keyMustList.add(getCompareMap(entry.getKey(),field,entry.getValue()));
                }

            }else if(keySize == 1){
                for(Entry<String, Object> entry : valueMap.entrySet()){
                    resultMap = getCompareMap(entry.getKey(),field,entry.getValue());
                }
            }
        }

        return resultMap;
    }


    public static Map<String,Object> getNestedMap(String relation,
                                                  String field,List<Map<String,Object>> valueList) throws Exception{

        String[] fieldArr = field.split("[.]");
        String path = fieldArr[0];
        Map<String,Object> resultMap = new HashMap<>();
        if(valueList.size() > 1){
            Map<String,Object> boolMap = new HashMap<>();
            resultMap.put("bool", boolMap);
            List<Map<String,Object>> relationList = new ArrayList<>();
            boolMap.put(relation, relationList);
            for(Map<String,Object> valueMap : valueList){
                relationList.add(getSingleNestedMap(path,valueMap,field));
            }
        }else{
            resultMap = getSingleNestedMap(path, valueList.get(0), field);
        }


        return resultMap;

    }

    public static Map<String,Object> getSingleNestedMap(String path,Map<String,Object> valueMap,String field) throws Exception{
        Map<String,Object> pathMap = new HashMap<>();
        Map<String,Object> nestedMap = new HashMap<>();
        pathMap.put("nested", nestedMap);
        nestedMap.put("path",path);
        Map<String,Object> map = new HashMap<>();
        int keySize = valueMap.keySet().size();
        if( keySize > 1){
            Map<String,Object> keyBoolMap = new HashMap<>();
            map.put("bool", keyBoolMap);
            List<Map<String,Object>> keyMustList = new ArrayList<>();
            keyBoolMap.put("must",keyMustList);
            for(Entry<String, Object> entry : valueMap.entrySet()){
                keyMustList.add(getCompareMap(entry.getKey(),field,entry.getValue()));
            }
        }else if(keySize == 1){
            for(Entry<String, Object> entry : valueMap.entrySet()){
                map = getCompareMap(entry.getKey(),field,entry.getValue());
            }
        }
        nestedMap.put("query",map);
        return pathMap;
    }

    public static Map<String,Object> getCompareMap(String opera,String field,Object value) throws Exception{
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> keyValueMap = new HashMap<>();
        switch(opera){
            case "=": keyValueMap.put(field, value);
                map.put("term", keyValueMap);
                break;
            case "<": Map<String,Object> ltMap = new HashMap<>();
                ltMap.put("lt", value);
                keyValueMap.put(field, ltMap);
                map.put("range",keyValueMap);
                break;
            case "<=": Map<String,Object> lteMap = new HashMap<>();
                lteMap.put("lte", value);
                keyValueMap.put(field, lteMap);
                map.put("range",keyValueMap);
                break;
            case ">": Map<String,Object> gtMap = new HashMap<>();
                gtMap.put("gt", value);
                keyValueMap.put(field, gtMap);
                map.put("range",keyValueMap);
                break;
            case ">=": Map<String,Object> gteMap = new HashMap<>();
                gteMap.put("gte", value);
                keyValueMap.put(field, gteMap);
                map.put("range",keyValueMap);
                break;
            default:
        }
        return map;
    }

    public static Map<String,Object> getRequestBodyByTag(String tag) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        Gson gson = new Gson();
        List<Map<String,Object>> tagList = gson.fromJson(tag, List.class);
        Map<String,Object> queryMap = new HashMap<>();
        resultMap.put("query", queryMap);
        Map<String,Object> boolMap = new HashMap<>();
        queryMap.put("bool", boolMap);
        List<Map<String,Object>> mustList = new ArrayList<>();
        boolMap.put("must", mustList);
        Map<String,Object> singleMap;
        for(Map<String,Object> tagMap : tagList){
            singleMap = new HashMap<>();
            List<Map<String,Object>> valueList = (List<Map<String, Object>>) tagMap.get("value");
            String fieldName = tagMap.get("fieldName").toString();
            String relation = tagMap.get("relation").toString();
            if("and".equals(relation)){
                relation = "must";
            }else if("or".equals(relation)){
                relation = "should";
            }
            if(fieldName.contains(".")){
                singleMap = getNestedMap(relation, fieldName,valueList);
            }else{
                singleMap = getNormalMap(relation, fieldName, valueList);
            }
            mustList.add(singleMap);
        }
        return resultMap;

    }

}
