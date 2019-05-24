package cn.com.chinaunicom.dto.dml;

import cn.com.chinaunicom.dto.BaseResponseDto;
import lombok.Data;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
@Data
public class OutGetDocumentByIdDto extends BaseResponseDto{
    /**
     * 索引
     */
    private String index;

    /**
     * 类型
     */
    private String type;

    /**
     * 文档id
     */
    private String id;

    /**
     * 数据的json字符串
     */
    private Object source;

}
