package cn.com.chinaunicom.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/8/8.
 * @author dell
 */
@Data
public class InEsNativeDto {

    @NotBlank
    private String url;

    private String requestStr;
}
