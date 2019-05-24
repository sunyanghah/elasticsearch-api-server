package cn.com.chinaunicom.dto.ddl;

import cn.com.chinaunicom.dto.BaseResponseDto;
import lombok.Data;

/**
 * Created by dell on 2018/8/7.
 * @author dell
 */
@Data
public class OutIsIndexExistsDto extends BaseResponseDto{
    private Boolean existsFlag;
}
