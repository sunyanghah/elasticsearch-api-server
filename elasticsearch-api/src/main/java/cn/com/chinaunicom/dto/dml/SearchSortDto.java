package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/8/1.
 * @author dell
 */
@Data
public class SearchSortDto {

    @NotBlank
    private String column;

    private String sortMethod;
}
