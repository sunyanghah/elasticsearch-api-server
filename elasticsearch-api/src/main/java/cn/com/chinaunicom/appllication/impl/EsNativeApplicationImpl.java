package cn.com.chinaunicom.appllication.impl;

import cn.com.chinaunicom.appllication.EsNativeApplication;
import cn.com.chinaunicom.dto.HttpResult;
import cn.com.chinaunicom.dto.InEsNativeDto;
import cn.com.chinaunicom.util.HttpUtil;

import javax.inject.Named;

/**
 * Created by dell on 2018/8/8.
 * @author dell
 */
@Named
public class EsNativeApplicationImpl implements EsNativeApplication{

    @Override
    public HttpResult nativePost(InEsNativeDto inEsNativeDto) throws Exception {
        return HttpUtil.doPost(inEsNativeDto.getUrl(),inEsNativeDto.getRequestStr());
    }

    @Override
    public HttpResult nativePut(InEsNativeDto inEsNativeDto) throws Exception {
        return HttpUtil.doPut(inEsNativeDto.getUrl(),inEsNativeDto.getRequestStr());
    }

    @Override
    public HttpResult nativeGet(String url) throws Exception {
        return HttpUtil.doGet(url);
    }

    @Override
    public HttpResult nativeDelete(String url) throws Exception {
        return HttpUtil.doDelete(url);
    }
}
