package cn.com.chinaunicom.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
public class JestUtil {

    public static String getErrorReason(JestResult jestResult){
        JsonObject jsonObject = jestResult.getJsonObject();
        if (null != jsonObject){
            JsonElement jsonElement = jsonObject.get("error");
            if (null != jsonElement){
                jsonElement = jsonElement.getAsJsonObject().get("reason");
                if (null != jsonElement){
                    return jsonElement.getAsString();
                }
            }
        }
        return "";
    }
}
