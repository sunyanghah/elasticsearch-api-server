package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by dell on 2018/7/29.
 * @author dell
 */
@Data
public class InBulkOperationDto {

    @NotBlank
    private String index;

    private String type;

    @NotEmpty
    private List<BulkOperaDataDto> bulkOperaDataDtoList;
}
