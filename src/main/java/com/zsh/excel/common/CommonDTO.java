package com.zsh.excel.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 小白i
 * @date 2020/9/7
 */
@Data
public class CommonDTO {
    /**
     * 导出的数据信息
     */
    private List<Map<String, Object>> excelData;
    /**
     * 表头字段信息
     */
    private String[] headKey;
    /**
     * 字段对应的描述信息
     */
    private Map<String, String> desc;
}
