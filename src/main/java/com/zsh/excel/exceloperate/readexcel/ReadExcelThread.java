package com.zsh.excel.exceloperate.readexcel;

import com.zsh.excel.utils.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author 小白i
 * @date 2020/8/19
 */
public class ReadExcelThread implements Callable<List<Map<String, Object>>> {

    private final int startRow;

    private final int endRow;

    private final Sheet sheet;

    private final int lastCellNum;

    private final String[] headArray;

    public ReadExcelThread(int startRow, int endRow, Sheet sheet, int lastCellNum, String[] headArray) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.sheet = sheet;
        this.lastCellNum = lastCellNum;
        this.headArray = headArray;
    }

    @Override
    public List<Map<String, Object>> call() {
        try {
            return readExcel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> readExcel() {
        List<Map<String, Object>> resList = new LinkedList<>();
        //遍历行，从第二行开始拿到数据
        Map<String, Object> map;
        for (int i = startRow; i <= endRow; i++) {
            map = new HashMap<>(lastCellNum);
            //获取一行数据
            Row row = sheet.getRow(i);
            for (int j = 0; j < lastCellNum; j++) {
                // 得到列名key
                String key = headArray[j];
                //将列名与实体相转化
                key = translateKeyToEntryColumnMethod(key);
                Cell cell = row.getCell(j);
                //单元格为空，直接跳过
                if (StringUtils.isEmpty(cell)) {
                    continue;
                }
                getCellValue(map, key, cell);
            }
            //默认为不删除标识
            map.put("isDelete", "0");
            resList.add(map);
        }
        return resList;
    }


    private static void getCellValue(Map<String, Object> map, String key, Cell cell) {
        Object cellValue = null;

        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.NUMERIC) {
            cellValue = cell.getNumericCellValue();
        } else if (cellType == CellType.STRING) {
            cellValue = cell.getStringCellValue();
            //cellValue = "2020-12-31T12:12:12";
            if (DateUtils.isLocalDateTime(cell.getStringCellValue())) {
                cellValue = LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                //cellValue = null;
            } else if (isLocalDate(cell.getStringCellValue())) {
                cellValue = LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        } else if (cellType == CellType.BOOLEAN) {
            cellValue = cell.getBooleanCellValue();
        }
        map.put(key, cellValue);
    }

    private static boolean isLocalDate(String localDate) {
        try {
            LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String translateKeyToEntryColumnMethod(String key) {
        String newKey = key;
        String[] splitList = key.split("_");
        if (splitList.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            String firstPart = splitList[0];
            stringBuilder.append(firstPart);
            for (int i = 1; i < splitList.length; i++) {
                String otherPart = splitList[i];
                String splitOne = otherPart.substring(0, 1).toUpperCase() + otherPart.substring(1);
                stringBuilder.append(splitOne);
            }
            newKey = stringBuilder.toString();
        }
        return newKey;
    }
}
