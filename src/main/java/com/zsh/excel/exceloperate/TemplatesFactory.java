package com.zsh.excel.exceloperate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Service
public class TemplatesFactory {

    @Autowired
    Map<String, AbstractOperateTemplates> strategys = new ConcurrentHashMap<>(3);

    public AbstractOperateTemplates getStrategy(String component) throws Exception{
        AbstractOperateTemplates strategy = strategys.get(component);
        if(strategy == null) {
            throw new RuntimeException("no strategy defined");
        }
        return strategy;
    }

}
