package com.zsh.excel.exceloperate;

import com.zsh.excel.exceloperate.po.TableFieldPO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author 小白i
 * @date 2020/9/9
 */
public interface TemplatesInterface {

    /**
     * 读取文件信息
     *
     * @param file    传入的文件
     * @param headArr 表头对应字段code
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> readFile(MultipartFile file, String[] headArr);

    /**
     * 校验文件字段是否符合
     *
     * @return Map<Integer, String>行对应的错误信息
     */
    boolean checkField(List<Map<String, Object>> dataList, List<TableFieldPO> fields);

    /**
     * 校验表之间的联系
     * 需要数据信息，然后对应的表
     *
     * @return Map<Integer, String>
     */
    Map<Integer, String> checkTableRelations(List<Map<String, Object>> dataList);

    /**
     * 在文件写入错误信息
     */
    void writeErrorBackToFile();

}
