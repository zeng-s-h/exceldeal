package com.zsh.excel.exceloperate.common;

/**
 * @author 小白i
 * @date 2020/9/10
 */
public enum ErrorMessageEnum {

    /**
     * 字段不为空错误
     */
    FIELD_NOTNULL_ERROR("字段【{0}】为非空，请给定相应的值！"),
    /**
     * 字段长度超出错误
     */
    FIELD_OUTOFLENGTH_ERROR("字段【{0}】的数据长度为{1}，请重新填写正确的值！"),
    /**
     * 时间格式不匹配错误
     */
    FIELD_TATETYPEDISMACH_ERROR("字段【{0}】的数据类型为时间格式，请填写时间格式，以短横线隔开！");

    private final String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
