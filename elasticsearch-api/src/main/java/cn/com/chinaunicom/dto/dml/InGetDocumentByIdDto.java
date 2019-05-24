package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
@Data
public class InGetDocumentByIdDto {
    @NotBlank
    private String index;

    private String type;

    @NotBlank
    private String docId;
}
