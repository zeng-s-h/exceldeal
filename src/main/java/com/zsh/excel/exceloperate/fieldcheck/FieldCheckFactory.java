package com.zsh.excel.exceloperate.fieldcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Service
public class FieldCheckFactory {

    @Autowired
    Map<String, FieldCheckStrategy> strategys = new ConcurrentHashMap<>(3);

    public FieldCheckStrategy getStrategy(String component) throws Exception{
        FieldCheckStrategy strategy = strategys.get(component);
        if(strategy == null) {
            throw new RuntimeException("no strategy defined");
        }
        return strategy;
    }

}
