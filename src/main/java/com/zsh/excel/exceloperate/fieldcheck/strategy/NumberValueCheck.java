package com.zsh.excel.exceloperate.fieldcheck.strategy;

import com.zsh.excel.exceloperate.fieldcheck.AbstractStrategy;
import org.springframework.stereotype.Component;

/**
 * @author 小白i
 * @date 2020/9/10
 */
@Component("number")
public class NumberValueCheck extends AbstractStrategy {

    @Override
    public String checkField(String fieldDesc, String isNullable, Object value, Integer valueLength) {
        return null;
    }
}
