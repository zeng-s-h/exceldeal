package com.zsh.excel.exceloperate.readexcel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 小白i
 * @date 2020/9/9
 */
public class ImportFileUtils {

    private static Map<String,String> nameMap;


    /**
     * 入口方法
     *
     * @param file  上传文件
     * @param heads 表头数组
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> upload(MultipartFile file, String[] heads) {
        try {
            return chooseExecuteMethod(file.getOriginalFilename(), file.getInputStream(), heads);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, Object>> chooseExecuteMethod(String fileName, InputStream inputStream, String[] headArray) throws Exception {
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("文件名称不能为空");
        }
        // (?i)忽略大小写
        if (fileName.matches("^.+\\.(?i)(xls)$")) {
            return readExcel(headArray, new HSSFWorkbook(inputStream));
        } else if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return readExcel(headArray, new XSSFWorkbook(inputStream));
        } else if (fileName.matches("^.+\\.(?i)(csv)$")) {
            return readCsv(inputStream, headArray);
        } else {
            throw new RuntimeException("格式不对");
        }
    }

/*    public static List<Map<String, Object>> doExport(MultipartFile file, String[] headArray) throws Exception {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("文件名称不能为空");
        }
        // (?i)忽略大小写
        if (fileName.matches("^.+\\.(?i)(xls)$") || fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return readExcel(file, headArray);
        } else if (fileName.matches("^.+\\.(?i)(csv)$")) {
            return readCsv(file, headArray);
        } else {
            return null;
        }
    }*/

    /**
     * 读取csv文件按
     *
     * @param inputStream 文件输入流
     * @param headArray   表头
     */
    private static List<Map<String, Object>> readCsv(InputStream inputStream, String[] headArray) throws IOException {
        //实体的集合，把csv中的列装在list里。
        List<Map<String, Object>> list = new ArrayList<>();
        //设置编码格式
        InputStreamReader is = new InputStreamReader(inputStream, Charset.defaultCharset());
        BufferedReader reader = new BufferedReader(is);
        //第一行信息，为标题信息，不用,如果需要，注释掉,第三行字段信息
        reader.readLine();
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            //实体类
            Map<String, Object> entity = new HashMap<>();
            for (int i = 0; i < headArray.length; i++) {
                //名称,//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                String[] item = line.split(",");
                if (getValue(item, i) != null) {
                    //就是文件中去掉标题行的第一列的数据
                    entity.put(headArray[i], getValue(item, i));
                }
                list.add(entity);
            }

        }
        return list;
    }

    public static String getValue(String[] item, int index) {

        if (item.length > index) {
            return item[index];
        }
        return null;
    }

    public static List<Map<String, Object>> readExcel(String[] headArray, Workbook workbook) throws Exception {

        ExecutorService executorService = Executors.newCachedThreadPool();

        if (ArrayUtils.isEmpty(headArray)) {
            return new ArrayList<>(0);
        }
        //获取sheet页，默认第一页
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, Object>> resultList = new LinkedList<>();
        // 得到标题行
        Row titleRow = sheet.getRow(1);
        //获取最后一列
        int lastCellNum = titleRow.getLastCellNum();
        //获取最后的一行
        int lastRowNum = sheet.getLastRowNum();

        //每1000条一个线程，a为线程总数
        int a = lastRowNum / 1000;
        a = lastRowNum % 1000 == 0 ? a : ++a;
        //每条线程开始行和结束行
        int start;
        int end = 1;
        //返回统一存起来，执行完再调用阻塞get方法
        List<Future<List<Map<String, Object>>>> futures = new LinkedList<>();
        for (int i = 0; i < a; i++) {
            start = end + 1;
            end = 1000 * (i + 1);
            if (end > lastRowNum) {
                end = lastRowNum;
            }
            Future<List<Map<String, Object>>> submit = executorService.submit(new ReadExcelThread(start, end, sheet
                    , lastCellNum, headArray));
            futures.add(submit);
        }
        //获取返回值
        futures.forEach(future -> {
            try {
                resultList.addAll(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        workbook.close();
        return resultList;
    }

}
