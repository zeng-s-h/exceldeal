package com.zsh.excel.exceloperate;

import com.zsh.excel.exceloperate.dto.ExcelCommonDTO;

import java.util.List;
import java.util.Map;

/**
 * @author 小白i
 * @date 2020/9/9
 */
public class TemplatesEnvironment {

    private final AbstractOperateTemplates strategy;

    public TemplatesEnvironment(AbstractOperateTemplates strategy){
        this.strategy = strategy;
    }
    public List<Map<String,Object>> executeTemplates(ExcelCommonDTO commonDTO){
        return this.strategy.executeTemplates(commonDTO);
    }

}
