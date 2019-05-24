package cn.com.chinaunicom.dto.ddl;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/7/28.
 */
@Data
public class InExistsIndexDto {

    @NotBlank
    private String index;

}
