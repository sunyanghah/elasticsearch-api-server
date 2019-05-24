package cn.com.chinaunicom.appllication;

import cn.com.chinaunicom.dto.HttpResult;
import cn.com.chinaunicom.dto.InEsNativeDto;

/**
 * Created by dell on 2018/8/8.
 * @author dell
 */
public interface EsNativeApplication {

    /**
     * post
     * @param inEsNativeDto
     * @return
     * @throws Exception
     */
    HttpResult nativePost(InEsNativeDto inEsNativeDto) throws Exception;

    /**
     * put
     * @param inEsNativeDto
     * @return
     * @throws Exception
     */
    HttpResult nativePut(InEsNativeDto inEsNativeDto) throws Exception;

    /**
     * get
     * @param url
     * @return
     * @throws Exception
     */
    HttpResult nativeGet(String url) throws Exception;

    /**
     * delete
     * @param url
     * @return
     * @throws Exception
     */
    HttpResult nativeDelete(String url) throws Exception;

}
