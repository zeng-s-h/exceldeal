package com.zsh.excel.exceloperate.fieldcheck.strategy;

import com.zsh.excel.exceloperate.common.ErrorMessageEnum;
import com.zsh.excel.exceloperate.common.ErrorMsgUtils;
import com.zsh.excel.exceloperate.fieldcheck.AbstractStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Component("varchar")
public class StringValueCheck extends AbstractStrategy {

    private static final String NULL_ABLE = "no";

    @Override
    public String checkField(String fieldDesc, String isNullable, Object value, Integer valueLength) {

        String stringValue = value == null ? null : value.toString();
        //判断为空性
        if (StringUtils.equalsIgnoreCase(NULL_ABLE, isNullable) && StringUtils.isBlank(stringValue)) {
            return ErrorMsgUtils.createErrorMsg(ErrorMessageEnum.FIELD_NOTNULL_ERROR, fieldDesc);
        }
        //判断长度是否超出
        if (StringUtils.isNotBlank(stringValue) && stringValue.length() > valueLength) {
            return ErrorMsgUtils.createErrorMsg(ErrorMessageEnum.FIELD_OUTOFLENGTH_ERROR,
                    fieldDesc, String.valueOf(valueLength));
        }
        return null;
    }
}
