package com.zsh.excel.exceloperate;

import com.zsh.excel.exceloperate.dto.ExcelCommonDTO;
import com.zsh.excel.exceloperate.fieldcheck.FieldCheckFactory;
import com.zsh.excel.exceloperate.po.TableFieldPO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 小白i
 * @date 2020/9/9
 */
public abstract class AbstractOperateTemplates {

    @Resource
    public FieldCheckFactory factory;

    public final List<Map<String, Object>> executeTemplates(ExcelCommonDTO commonDTO) {
        List<Map<String, Object>> dataList = readFile(commonDTO.getFile(), commonDTO.getHeadArr());
        //校验字段完整性
        checkField(dataList, commonDTO.getFields());
        //表联系
        //checkTableRelations(dataList);
        writeErrorBackToFile();
        //增加一个拓展接口，防止其他业务需要额外的逻辑，默认空实现 todo
        extendFunction();
        return null;
    }

    public final List<Map<String, Object>> executeTemplates(ExcelCommonDTO commonDTO, T t) {
        List<Map<String, Object>> dataList = readFile(commonDTO.getFile(), commonDTO.getHeadArr());
        //校验字段完整性
        checkField(dataList, commonDTO.getFields());
        //表联系
        //checkTableRelations(dataList);
        writeErrorBackToFile();
        //增加一个拓展接口，防止其他业务需要额外的逻辑，默认空实现 todo
        extendFunction(t);
        return null;
    }

    public abstract <T> T extendFunction(T t);

    public abstract void extendFunction();

    /**
     * 读取文件信息
     *
     * @param file    传入的文件
     * @param headArr 表头对应字段code
     * @return List<Map < String, Object>>
     */
    public abstract List<Map<String, Object>> readFile(MultipartFile file, String[] headArr);

    /**
     * 校验文件字段是否符合
     *
     * @return Map<Integer, String>行对应的错误信息
     */
    public abstract boolean checkField(List<Map<String, Object>> dataList, List<TableFieldPO> fields);

    /**
     * 校验表之间的联系
     * 需要数据信息，然后对应的表
     *
     * @return Map<Integer, String>
     */
    public abstract Map<Integer, String> checkTableRelations(List<Map<String, Object>> dataList);

    /**
     * 在文件写入错误信息
     */
    public abstract void writeErrorBackToFile();
}
