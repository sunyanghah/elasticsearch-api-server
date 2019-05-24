package cn.com.chinaunicom.dto;

import lombok.Data;

/**
 * Created by dell on 2018/8/7.
 */
@Data
public class HttpResult{

    private boolean isSuccess;

    private String responseString;
}