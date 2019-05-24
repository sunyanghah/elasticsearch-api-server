package cn.com.chinaunicom.dto.dml;

import lombok.Data;

/**
 * Created by dell on 2018/7/29.
 * @author dell
 */
@Data
public class BulkOperaDataDto {

    private String docId;

    private Object source;

    /**
     * 操作类型，index ,update , delete
     */
    private String operation;
}
