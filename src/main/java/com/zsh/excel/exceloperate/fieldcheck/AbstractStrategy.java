package com.zsh.excel.exceloperate.fieldcheck;

/**
 * @author 小白i
 * @date 2020/9/9
 */
public abstract class AbstractStrategy implements FieldCheckStrategy {

    public String checkNullAble(String isNullable,Object value){
        return null;
    }

}
