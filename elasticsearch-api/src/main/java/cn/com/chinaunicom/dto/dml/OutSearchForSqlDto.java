package cn.com.chinaunicom.dto.dml;

import cn.com.chinaunicom.dto.BaseResponseDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/7/30.
 * @author dell
 */
@Data
public class OutSearchForSqlDto extends BaseResponseDto{

    /**
     * 字段集合
     */
    private List<SearchForSqlColumnDto> columns;

    /**
     * 值集合
     */
    private List<ArrayList> rows;

    /**
     * 匹配好的字段和值
     */
    private List<Map<String,Object>> dataList;
}
