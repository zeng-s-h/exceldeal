package com.zsh.excel.exceloperate.strategy;

import com.zsh.excel.exceloperate.AbstractOperateTemplates;
import com.zsh.excel.exceloperate.fieldcheck.FieldTypeEnum;
import com.zsh.excel.exceloperate.po.TableFieldPO;
import com.zsh.excel.exceloperate.readexcel.ImportFileUtils;
import com.zsh.excel.exceloperate.readexcel.WriteBackErrorMsgThread;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

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
    public void checkField(List<Map<String, Object>> dataList, List<TableFieldPO> fields) {
        dataList.forEach(data -> fields.forEach(po -> {
            String columnName = po.getColumnName();
            FieldTypeEnum fieldType = po.getFieldType();
            String isNullable = po.getIsNullable();
            //根据dataType判断是否成立
            try {
                String msg = factory.getStrategy(fieldType.getCode()).checkField(columnName, isNullable,
                        data.get(po.getColumnCode()), po.getMaximumLength());
                if (StringUtils.isNotBlank(msg)) {
                    data.put("errorMsg", msg);
                }
            } catch (Exception e) {
                throw new RuntimeException("no strategy defined", e);
            }
        }));
    }

    @Override
    public void writeErrorBackToFile(MultipartFile file, List<Map<String, Object>> dataList) throws IOException {

        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        //如果后缀名为xls，使用HSSF
        Workbook workbook = null;
        assert filename != null;
        if (filename.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            //如果后缀名是xlsx，使用XSSF
            workbook = new XSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheetAt(0);

        ForkJoinPool pool = new ForkJoinPool();
        Integer a = 1;
        int taskNum = 500;
        CountDownLatch countDownLatch = new CountDownLatch(getCountDownNum(a, dataList.size(), taskNum));
        pool.submit(new WriteBackErrorMsgThread(0, dataList.size(), dataList, sheet,
                createErrorStyle(workbook), countDownLatch, taskNum));

        //将错误文件下载
        FileOutputStream fileOut = new FileOutputStream(filename);
        workbook.write(fileOut);
        fileOut.close();
    }

    private int getCountDownNum(Integer a, int size, int taskNum) {
        if (taskNum == 0) {
            throw new RuntimeException("每个线程最大任务数量不能为空！");
        }

        if (size > taskNum) {
            int m = size/2;
            a = getCountDownNum(a * 2, m, taskNum);
        } else {
            return a;
        }
        return a;
    }

    /**
     * 错误信息样式
     *
     * @param workbook excel对象
     */
    private CellStyle createErrorStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.RED.getIndex());

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    @Override
    public void extendFunction(T t) {
    }

    @Override
    public void extendFunction() {

    }
}
