package com.zsh.excel.exceloperate.po;

import com.zsh.excel.exceloperate.fieldcheck.FieldTypeEnum;
import lombok.Data;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Data
public class TableFieldPO {

    private String tableCatalog;

    private String tableSchema;

    private String tableName;

    private String columnName;

    private String ordinalPosition;

    private String columnDefault;

    private String isNullable;

    private String dataType;

    private Integer characterMaximumLength;

    private String udtName;

    /**
     * 自定义类型
     */
    private FieldTypeEnum fieldType;
}
