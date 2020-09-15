package com.zsh.excel.exceloperate;

import com.zsh.excel.exceloperate.dto.ExcelCommonDTO;
import com.zsh.excel.exceloperate.fieldcheck.FieldCheckFactory;
import com.zsh.excel.exceloperate.po.TableFieldPO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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
        //获取文件数据信息
        List<Map<String, Object>> dataList = readFile(commonDTO.getFile(), commonDTO.getHeadArr());
        //校验字段完整性,将每一条数据的错误信息放在每条数据里面
        checkField(dataList, commonDTO.getFields());
        //回写错误信息到源文件
        try {
            writeErrorBackToFile(commonDTO.getFile(), dataList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("错误信息回写excel异常", e);
        }
        //增加一个拓展接口，防止其他业务需要额外的逻辑，默认空实现 todo
        extendFunction();
        return dataList;
    }

    public final List<Map<String, Object>> executeTemplates(ExcelCommonDTO commonDTO, T t) {
        List<Map<String, Object>> dataList = readFile(commonDTO.getFile(), commonDTO.getHeadArr());
        //校验字段完整性
        checkField(dataList, commonDTO.getFields());
        try {
            writeErrorBackToFile(commonDTO.getFile(), dataList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("错误信息回写excel异常", e);
        }
        //增加一个拓展接口，防止其他业务需要额外的逻辑，默认空实现 todo
        extendFunction(t);
        return dataList;
    }

    /**
     * 拓展接口，自定义实现
     *
     * @param t 参数
     */
    public abstract void extendFunction(T t);

    /**
     * 扩展接口，用于自定义逻辑
     */
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
     */
    public abstract void checkField(List<Map<String, Object>> dataList, List<TableFieldPO> fields);

    /**
     * 在文件写入错误信息
     *
     * @param file     文件
     * @param dataList 数据信息
     */
    public abstract void writeErrorBackToFile(MultipartFile file, List<Map<String, Object>> dataList) throws IOException;
}
