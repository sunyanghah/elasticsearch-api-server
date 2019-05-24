package cn.com.chinaunicom.dto.dml;

import cn.com.chinaunicom.dto.BaseResponseDto;
import lombok.Data;

import java.util.List;

/**
 * Created by dell on 2018/7/31.
 * @author dell
 */
@Data
public class OutSearchDto extends BaseResponseDto{

    private Long total;

    private List<SearchHitsDto> hits;
}
