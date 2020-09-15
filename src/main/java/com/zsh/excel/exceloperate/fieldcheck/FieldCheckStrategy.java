package com.zsh.excel.exceloperate.fieldcheck;

/**
 * @author 小白i
 * @date 2020/9/9
 */
public interface FieldCheckStrategy {

    /**
     * 校验是否
     *
     * @param isNullable  是否为空
     * @param value       校验的值
     * @param valueLength 目标字段长度
     * @return 错误信息
     */
    String checkField(String fieldDesc,String isNullable, Object value, Integer valueLength);


}
