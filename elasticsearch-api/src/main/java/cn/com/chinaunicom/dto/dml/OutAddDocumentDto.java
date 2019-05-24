package cn.com.chinaunicom.dto.dml;

import cn.com.chinaunicom.dto.BaseResponseDto;
import lombok.Data;

/**
 * Created by dell on 2018/7/28.
 * @author dell
 */
@Data
public class OutAddDocumentDto extends BaseResponseDto{

    private String docId;

    private String index;

    private String type;

}
