package com.zsh.excel.exceloperate.strategy;

import com.zsh.excel.exceloperate.AbstractOperateTemplates;
import com.zsh.excel.exceloperate.po.TableFieldPO;
import com.zsh.excel.exceloperate.readexcel.ImportFileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小白i
 * @date 2020/9/9
 */
@Component("device")
public class DefaultTemplates extends AbstractOperateTemplates {

    @Override
    public List<Map<String, Object>> readFile(MultipartFile file, String[] headArr) {
        return ImportFileUtils.upload(file, headArr);
    }

    @Override
    public boolean checkField(List<Map<String, Object>> dataList, List<TableFieldPO> fields) {
        dataList.forEach(data -> fields.forEach(po -> {
            String columnName = po.getColumnName();
            String dataType = po.getDataType();
            String isNullable = po.getIsNullable();
            //根据dataType判断是否成立
            try {
                String msg = factory.getStrategy(dataType).checkField(null, isNullable,
                        data.get(columnName), po.getCharacterMaximumLength());
                if (StringUtils.isNotBlank(msg)) {
                    data.put("errorMsg", msg);
                }
            } catch (Exception e) {
                throw new RuntimeException("no strategy defined", e);
            }
        }));
        return false;
    }

    @Override
    public Map<Integer, String> checkTableRelations(List<Map<String, Object>> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return new HashMap<>(0);
        }
        List<String> codes = new ArrayList<>(dataList.size());
        dataList.forEach(map -> codes.add((String) map.get("deviceTypeCode")));
        //List<String> list = deviceTypeDAO.queryExistDeviceTypeCode(null);
        return null;
    }

    @Override
    public void writeErrorBackToFile() {

    }

    @Override
    public <T> T extendFunction(T t) {
        return null;
    }

    @Override
    public void extendFunction() {

    }
}
