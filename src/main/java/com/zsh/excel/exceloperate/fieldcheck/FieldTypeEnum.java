package com.zsh.excel.exceloperate.fieldcheck;

/**
 * @author 小白i
 * @date 2020/9/10
 */
public enum FieldTypeEnum {
    /**
     * 字符型
     */
    VARCHER("string"),
    /**
     * 数值型
     */
    NUMBER("number"),
    /**
     * 时间类型
     */
    DATE("date");

    private final String code;

    FieldTypeEnum(String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
