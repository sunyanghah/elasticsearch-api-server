package cn.com.chinaunicom.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 2018/8/1.
 * @author dell
 */
public class TagUtil {
    private static Pattern patternOperation = Pattern.compile("\\$\\{(\\S+?)\\}");
    private static Pattern patternValue = Pattern.compile("#\\{(\\S+?)\\}");
    private static Pattern patternOccupy = Pattern.compile("@\\{(\\S+?)\\}");
    private static Pattern patternCompare = Pattern.compile("&\\{(\\S+?)\\}");
    private static Pattern patternParentheses = Pattern.compile("\\(([^\\(]+?)\\)");

    private static Gson gson = new Gson();

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
                    for(Map.Entry<String, Object> entry : valueMap.entrySet()){
                        keyMustList.add(getCompareMap(entry.getKey(),field,entry.getValue()));
                    }

                }else if(keySize == 1){
                    for(Map.Entry<String, Object> entry : valueMap.entrySet()){
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
                for(Map.Entry<String, Object> entry : valueMap.entrySet()){
                    keyMustList.add(getCompareMap(entry.getKey(),field,entry.getValue()));
                }

            }else if(keySize == 1){
                for(Map.Entry<String, Object> entry : valueMap.entrySet()){
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
            for(Map.Entry<String, Object> entry : valueMap.entrySet()){
                keyMustList.add(getCompareMap(entry.getKey(),field,entry.getValue()));
            }
        }else if(keySize == 1){
            for(Map.Entry<String, Object> entry : valueMap.entrySet()){
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


    /**
     * ------------------------------------------------------------------------------------------------------------------
     */

    /**
     *
     * @param patternStr
     * @param valueMap
     * @return
     */
    public static String decodeTagToSql(String patternStr, Map<String,Object> valueMap){
        if(null != patternStr &&  null != valueMap){
            Matcher matcher = patternOperation.matcher(patternStr);
            while(matcher.find()){
                patternStr = patternStr.replace(matcher.group(0), " "+matcher.group(1)+" ");
            }
            matcher = patternCompare.matcher(patternStr);
            while (matcher.find()){
                patternStr = patternStr.replace(matcher.group(0), " "+matcher.group(1)+" ");
            }
            matcher = patternValue.matcher(patternStr);
            while(matcher.find()){
                Object valueObj = valueMap.get(matcher.group(1));
                String targetValue = valueObj.toString();
                if(valueObj instanceof String){
                    targetValue = "'"+valueObj.toString()+"'";
                }
                patternStr = patternStr.replace(matcher.group(0),targetValue);
            }
        }
        return patternStr;
    }



    public static String decodeTagToEsBody(String str, Map<String,Object> valueMap) throws Exception{
        Map<String,Object> queryMap = new HashMap<>();
        Map<String,Object> memoryMap = new HashMap<>();
        int i = 0;
        Matcher matcher = patternParentheses.matcher(str);
        while(matcher.find()){
            String sonStr = matcher.group(0);
            String sonTj = matcher.group(1);
            Map<String,Object> map = handleOperation(sonTj,valueMap,memoryMap);
            memoryMap.put(String.valueOf(i), map);
            Integer strIndex = str.indexOf(sonStr);
            str = str.substring(0, strIndex)+"@{"+i+"}"+str.substring(strIndex+sonStr.length(),str.length());
            i++;
            matcher = patternParentheses.matcher(str);
        }
        Map<String,Object> finalMap = handleOperation(str,valueMap,memoryMap);
        queryMap.put("query",finalMap);
        int[] pageInfo = getPageInfo(str);
        queryMap.put("from", pageInfo[0]);
        queryMap.put("size", pageInfo[1]);
        ObjectMapper mapper = new ObjectMapper();
        String finalString = mapper.writeValueAsString(queryMap);
        return finalString;
    }


    public static int[] getPageInfo(String str){
        String[] strArray = str.split(">>");
        if(null != strArray && strArray.length > 0){
            String[] pageArray = strArray[strArray.length-1].split(",");
            if(null != pageArray && pageArray.length == 2){
                int pageNo = Integer.parseInt(pageArray[0]);
                int pageSize = Integer.parseInt(pageArray[1]);
                return new int[]{(pageNo-1)*pageSize,pageSize};
            }
        }

        return new int[]{0,10};
    }

    public static Map<String,Object> handleOperation(String str,Map<String,Object> valueMap,Map<String,Object> memoryMap){
        Map<String,Object> map = new HashMap<>();

        List<Map<String,Object>> list = new ArrayList<>();
        int index = str.indexOf("${");
        int startIndex = 0;
        while(index != -1){
            list.add(handleCompare(str.substring(startIndex,index),valueMap,memoryMap));
            startIndex = str.indexOf("}", index)+1;
            index = str.indexOf("${",startIndex);
        }
        if(startIndex != str.length()){
            list.add(handleCompare(str.substring(startIndex,str.length()),valueMap,memoryMap));
        }

        Matcher matcher = patternOperation.matcher(str);
        Map<String,List> unionMap = new HashMap<>();
        if (matcher.find() && "or".equals(matcher.group(1))) {
            unionMap.put("should", list);
        }else{
            unionMap.put("must", list);
        }
        map.put("bool",unionMap);
        return map;
    }

    public static Map<String,Object> handleCompare(String str, Map<String,Object> valueMap,Map<String,Object> memoryMap){
        Matcher sonMatcher = patternOccupy.matcher(str);
        if(sonMatcher.find()){
            return (Map<String, Object>) memoryMap.get(sonMatcher.group(1));
        }
        Matcher searchMatcher = patternCompare.matcher(str);
        searchMatcher.find();
        Matcher valueMatcher = patternValue.matcher(str);
        valueMatcher.find();
        Map<String,Object> map = new HashMap<>();
        String searchStr = searchMatcher.group(0);
        String field = str.substring(0,str.indexOf(searchStr));
        Map<String,Object> keyValueMap = new HashMap<>();
        Object value = valueMap.get(valueMatcher.group(1));
        switch(searchMatcher.group(1)){
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
            case "like": keyValueMap.put(field, value);
                map.put("match_phrase", keyValueMap);
                break;
            case "in":  keyValueMap.put(field, value);
                map.put("terms",keyValueMap);
                break;
            default:

        }
        return map;
    }


    /**
     * 测试的数据来源
     * @return
     */
    public static Map<String,Object> getData(){
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> value = new HashMap<String,Object>();

        String str = "age=#{age}${and}gender=#{gender}${and}(address${like}#{address1}${or}address${like}#{address2})${and}#{a_n_begin}<=account_number${and}account_number<=#{a_n_end}";
        value.put("age",26);
        value.put("gender","M");
        value.put("address1","street");
        value.put("address2", "place");
        value.put("a_n_begin", 400);
        value.put("a_n_end", 500);

        map.put("pattern", str);
        map.put("value", value);
        return map;
    }

}
