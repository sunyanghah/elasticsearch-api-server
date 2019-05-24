package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/8/1.
 * @author dell
 */
@Data
public class InSearchForTagDto {

    @NotBlank
    private String index;

    private List<String> columnList;

    private String tag;

    private Map<String,Object> valueMap;

    private List<SearchSortDto> sortList;

}
