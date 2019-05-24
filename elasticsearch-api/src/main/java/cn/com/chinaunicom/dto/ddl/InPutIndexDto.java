package cn.com.chinaunicom.dto.ddl;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/7/27.
 * @author dell
 */
@Data
public class InPutIndexDto {
    @NotBlank
    private String index;
}
