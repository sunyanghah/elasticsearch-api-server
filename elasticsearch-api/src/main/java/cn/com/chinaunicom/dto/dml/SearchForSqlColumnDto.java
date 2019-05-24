package cn.com.chinaunicom.dto.dml;

import lombok.Data;

/**
 * Created by dell on 2018/7/30.
 * @author dell
 */
@Data
public class SearchForSqlColumnDto {
    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;
}
