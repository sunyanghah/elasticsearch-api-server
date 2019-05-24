package cn.com.chinaunicom.dto.dml;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/8/9.
 * @author dell
 */
@Data
public class InNotAnnoyDto {
    private Integer size;

    private String tag;

    private Map<String,Object> valueMap;

    private List<String> notAnnoyList;

    private String field;
}
