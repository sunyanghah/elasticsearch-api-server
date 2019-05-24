package cn.com.chinaunicom.dto.dml;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/8/13.
 * @author dell
 */
@Data
public class OutSearchByScrollDto {

    private Long total;

    private String scrollId;

    private List<Map<String,Object>> hits;
}
