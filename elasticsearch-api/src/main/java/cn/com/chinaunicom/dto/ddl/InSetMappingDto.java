package cn.com.chinaunicom.dto.ddl;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/7/31.
 * @author dell
 */
@Data
public class InSetMappingDto {

    @NotBlank
    private String index;

    @NotBlank
    private String type;

    @NotBlank
    private String field;

    @NotBlank
    private String fieldType;
}
