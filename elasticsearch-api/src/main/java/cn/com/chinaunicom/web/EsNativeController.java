package cn.com.chinaunicom.web;

import cn.com.chinaunicom.appllication.EsNativeApplication;
import cn.com.chinaunicom.dto.HttpResult;
import cn.com.chinaunicom.dto.InEsNativeDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by dell on 2018/8/8.
 * @author dell
 */
@RestController
@RequestMapping("/native")
public class EsNativeController {

    @Inject
    private EsNativeApplication esNativeApplication;

    @RequestMapping(value = "/http",method = RequestMethod.POST)
    public HttpResult nativePost(@RequestBody @Valid InEsNativeDto inEsNativeDto){
        HttpResult httpResult;
        try {
            httpResult = esNativeApplication.nativePost(inEsNativeDto);
        }catch (Exception e){
            httpResult = new HttpResult();
            httpResult.setSuccess(false);
            httpResult.setResponseString("系统异常");
            e.printStackTrace();
        }
        return httpResult;
    }
    @RequestMapping(value = "/http",method = RequestMethod.GET)
    public HttpResult nativeGet(String url){
        HttpResult httpResult;
        try {
            httpResult = esNativeApplication.nativeGet(url);
        }catch (Exception e){
            httpResult = new HttpResult();
            httpResult.setSuccess(false);
            httpResult.setResponseString("系统异常");
            e.printStackTrace();
        }
        return httpResult;
    }
    @RequestMapping(value = "/http",method = RequestMethod.PUT)
    public HttpResult nativePut(@RequestBody @Valid InEsNativeDto inEsNativeDto){
        HttpResult httpResult;
        try {
            httpResult = esNativeApplication.nativePut(inEsNativeDto);
        }catch (Exception e){
            httpResult = new HttpResult();
            httpResult.setSuccess(false);
            httpResult.setResponseString("系统异常");
            e.printStackTrace();
        }
        return httpResult;
    }
    @RequestMapping(value = "/http",method = RequestMethod.DELETE)
    public HttpResult nativeDelete(String url){
        HttpResult httpResult;
        try {
            httpResult = esNativeApplication.nativeDelete(url);
        }catch (Exception e){
            httpResult = new HttpResult();
            httpResult.setSuccess(false);
            httpResult.setResponseString("系统异常");
            e.printStackTrace();
        }
        return httpResult;
    }
}
