package cn.com.chinaunicom.dto.dml;

import lombok.Data;

import java.util.Map;

/**
 * Created by dell on 2018/7/31.
 * @author dell
 */
@Data
public class SearchHitsDto {
    private String index;
    private String type;
    private String id;
    private Map<String,Object> source;
}
