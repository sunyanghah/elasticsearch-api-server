package cn.com.chinaunicom.constant;

import lombok.Data;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
@Data
public class ResponseCode {

    /**
     * 成功
     */
    public final static String SUCCESS = "0";

    /**
     * 程序异常
     */
    public final static String ERROR = "1";

    /**
     * 非程序性失败
     */
    public final static String OTHER_ERROR = "2";

}
