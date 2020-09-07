package com.zsh.excel.download;


import com.zsh.excel.utils.DateUtils;
import lombok.Data;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author fs
 */
@Data
public class ExportUtils {
    /**
     * 表头
     */
    private String title;
    /**
     * 各个列的表头
     */
    private String[] headList;
    /**
     * 各个列的元素key值
     */
    private String[] headKey;
    /**
     * 需要填充的数据信息
     */
    private List<Map<String, Object>> data;
    /**
     * 字体大小
     */
    private int fontSize = 14;
    /**
     * 行高
     */
    private int rowHeight = 30;
    /**
     * 列宽
     */
    private int columnWidth = 200;
    /**
     * 工作表
     */
    private String sheetName = "sheet1";


    /**
     * 开始导出数据信息
     */
    public void exportExport(HttpServletResponse response) throws IOException {
        //检查参数配置信息
        checkConfig();
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建工作表
        HSSFSheet wbSheet = wb.createSheet(this.sheetName);
        //设置默认行宽
        wbSheet.setDefaultColumnWidth(20);
        //获取title样式
        HSSFCellStyle titleCellStyle = getTitleCellStyle(wb);
        //创建第一行并设置样式
        createFirstRow(wbSheet, titleCellStyle);

        //表头行样式
        HSSFCellStyle style = getColumnHeadCellStyle(wb);
        //创建表头行
        createColumnRow(wbSheet, style);

        //导出数据
        try {
            //填充数据
            fillData(wbSheet, getDataCellStyle(wb));

            String encodedFileName = (URLEncoder.encode(sheetName,"utf-8") + ".xls").replaceAll("\\+", "%20");
            //设置Http响应头告诉浏览器下载这个附件
            response.setContentType("application/vnd.ms-excel;charset=iso8859-1");
            response.setHeader("Content-Disposition", "attachment;Filename="+encodedFileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream outputStream = response.getOutputStream();

            wb.write(outputStream);
            outputStream.close();
        } catch (Exception ex) {
            throw new IOException("导出Excel出现严重异常，异常信息：" + ex.getMessage());
        }finally {
            wb.close();
        }

    }

    /**
     * excel填充数据
     *
     * @param wbSheet sheet页
     * @param style   样式
     */
    private void fillData(HSSFSheet wbSheet, HSSFCellStyle style) {

        /*CyclicBarrier barrier = new CyclicBarrier(1);
        //开始写入实体数据信息，从第三行开始写入
        ForkJoinPool pool = new ForkJoinPool();
        ExportExcelTask task = new ExportExcelTask(wbSheet,0,data.size(),data,headKey,style,barrier);
        pool.submit(task);
        barrier.await();*/
        int a = 2;
        for (Map<String, Object> map : data) {
            //创建行
            HSSFRow row = wbSheet.createRow(a);
            HSSFCell cell;
            for (int j = 0, len = headKey.length; j < len; j++) {
                //创建单元格
                cell = row.createCell(j);
                //设置样式
                cell.setCellStyle(style);
                Object valueObject = map.get(headKey[j]);
                //String value;
                if (StringUtils.isEmpty(valueObject)) {
                    continue;
                }
                if (valueObject instanceof Integer) {
                    //取出的数据是Integer
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue((Integer) (valueObject));
                } else if (valueObject instanceof BigDecimal) {
                    //取出的数据是BigDecimal
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(Double.parseDouble(String.valueOf(valueObject)));
                } else if (valueObject instanceof LocalDateTime) {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(DateUtils.getStringDateymdhms((LocalDateTime) valueObject));
                } else {
                    //取出的数据是字符串直接赋值
                    cell.setCellType(CellType.STRING);
                    if (DateUtils.isLocalDateTimeType(valueObject.toString())) {
                        cell.setCellValue(DateUtils.getStringymdhmsDateByStringLocalDate(valueObject.toString()));
                    } else {
                        cell.setCellValue(valueObject.toString());
                    }
                }
            }
            a++;
        }
    }

    /**
     * 创建表头行
     *
     * @param wbSheet sheet页
     * @param style   样式
     */
    private void createColumnRow(HSSFSheet wbSheet, HSSFCellStyle style) {
        //在第1行创建rows
        HSSFRow row = wbSheet.createRow(1);
        //设置列头元素
        HSSFCell cellHead;
        int cellIndex = 0;
        for (String s : headList) {
            String name = s;
            /*if (StringUtils.isEmpty(name)) {
                continue;
            }*/
            cellHead = row.createCell(cellIndex);
            cellHead.setCellValue(name);
            cellHead.setCellStyle(style);
            cellIndex++;
        }
    }

    /**
     * 创建title
     *
     * @param wbSheet        sheet页
     * @param titleCellStyle 样式
     */
    private void createFirstRow(HSSFSheet wbSheet, HSSFCellStyle titleCellStyle) {
        //在第0行创建rows  (表标题)
        HSSFRow title = wbSheet.createRow(0);
        //行高
        title.setHeightInPoints(30);
        HSSFCell cellValue = title.createCell(0);
        cellValue.setCellValue(this.title);
        cellValue.setCellStyle(titleCellStyle);
        wbSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (this.headList.length - 1)));
    }

    /**
     * 表头样式
     * @param wb 工作表
     * @return HSSFCellStyle
     */
    private HSSFCellStyle getColumnHeadCellStyle(HSSFWorkbook wb) {
        //设置表头样式，表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //设置单元格样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private HSSFCellStyle getDataCellStyle(HSSFWorkbook wb) {
        //设置表头样式，表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //设置单元格样式
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        style.setFont(font);
        return style;
    }

    /**
     * 创建title样式
     * @param wb 工作表
     * @return HSSFCellStyle
     */
    private HSSFCellStyle getTitleCellStyle(HSSFWorkbook wb) {
        // 标题样式（加粗，垂直居中）
        HSSFCellStyle titleCellStyle = wb.createCellStyle();
        //垂直居中
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFFont fontStyle = wb.createFont();
        //加粗
        fontStyle.setBold(true);
        //设置标题字体大小
        fontStyle.setFontHeightInPoints((short) 16);
        titleCellStyle.setFont(fontStyle);
        return titleCellStyle;
    }

    /**
     * 检查数据配置问题
     */
    private void checkConfig() {
        if (headKey == null || headList.length == 0) {
            throw new RuntimeException("列名数组不能为空或者为NULL");
        }

        if (fontSize < 0 || rowHeight < 0 || columnWidth < 0) {
            throw new RuntimeException("字体、宽度或者高度不能为负值");
        }

        if (StringUtils.isEmpty(sheetName)) {
            throw new RuntimeException("工作表表名不能为NULL");
        }
    }
}