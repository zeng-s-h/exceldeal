package com.zsh.excel.exceloperate.po;

import com.zsh.excel.exceloperate.fieldcheck.FieldTypeEnum;
import lombok.Data;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Data
public class TableFieldPO {

    private String tableName;

    private String columnCode;

    private String columnName;

    private String isNullable;

    private Integer maximumLength;

    /**
     * 自定义类型
     */
    private FieldTypeEnum fieldType;
}
