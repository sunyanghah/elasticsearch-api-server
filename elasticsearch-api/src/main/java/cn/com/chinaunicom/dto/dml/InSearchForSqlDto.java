package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2018/7/30.
 * @author dell
 */
@Data
public class InSearchForSqlDto {

    @NotBlank
    private String sql;

}
