package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
@Data
public class InAddDocumentDto {

    @NotBlank
    private String index;

    private String type;

    private String docId;

    @NotNull
    private Object source;
}
