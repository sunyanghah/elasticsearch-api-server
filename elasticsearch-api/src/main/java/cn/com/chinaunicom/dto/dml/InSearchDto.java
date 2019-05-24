package cn.com.chinaunicom.dto.dml;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created by dell on 2018/7/31.
 * @author dell
 */
@Data
public class InSearchDto {

    /**
     * 要从哪些索引中查询，如果为null或空集合，则从全部索引中查询
     * 支持*操作符，如 test*  则从test开头的索引中查询
     */
    private List<String> index;

    /**
     *要从哪些类型中查询，如果为null或空集合，则从全部类型中查询
     */
    private List<String> type;

    @NotBlank
    private String query;
}
