package com.zsh.excel.exceloperate.dto;

import com.zsh.excel.exceloperate.po.TableFieldPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelCommonDTO {
    /**
     * 导入的文件
     */
    private MultipartFile file;
    /**
     * 表头字段
     */
    private String[] headArr;
    /**
     * 表头字段对应的name
     */
    private Map<String, String> code2NameMap;
    /**
     * 导出的数据信息
     */
    private List<Map<String, Object>> dataList;

    private List<TableFieldPO> fields;

}
