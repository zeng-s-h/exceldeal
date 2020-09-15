package com.zsh.excel.exceloperate.readexcel;

import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RecursiveAction;

/**
 * @author 小白i
 * @date 2020/9/15
 */
public class WriteBackErrorMsgThread extends RecursiveAction {

    private int count = 500;

    private int start = 0;

    private int end = 0;

    private List<Map<String,Object>> dataList;

    private Sheet sheet;

    private CellStyle cellStyle;

    private CountDownLatch countDownLatch;

    public WriteBackErrorMsgThread(int start, int end, List<Map<String,Object>> dataList,
                                   Sheet sheet,CellStyle cellStyle,CountDownLatch countDownLatch,
                                   int taskNum) {
        this.start = start;
        this.end = end;
        this.dataList = dataList;
        this.sheet = sheet;
        this.cellStyle = cellStyle;
        this.countDownLatch = countDownLatch;
        if (taskNum != 0) {
            this.count = taskNum;
        }
    }

    @Override
    protected void compute() {
        if (dataList.size() <= count) {
            end = dataList.size();
            execute(start, end);
            countDownLatch.countDown();
        } else {
            int mid = dataList.size()/2;
            WriteBackErrorMsgThread left = new WriteBackErrorMsgThread(0, mid, dataList, sheet,
                    cellStyle,countDownLatch,count);
            WriteBackErrorMsgThread right = new WriteBackErrorMsgThread(mid + 1, dataList.size(),
                    dataList, sheet, cellStyle,countDownLatch,count);
            left.fork();
            right.fork();

            left.join();
            right.join();
        }
    }

    private void execute(int start, int end) {
        if (start >= end) {
            return;
        }
        for (int i = start; i <= end; i++) {
            Map<String, Object> map = dataList.get(i);
            Row row = sheet.getRow(i + 2);
            if (row == null) {
                throw new RuntimeException("操作excel异常");
            }
            short lastCellNum = row.getLastCellNum();
            Cell cell = row.createCell(lastCellNum + 1);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(String.valueOf(map.get("errorMsg")));
            cell.setCellStyle(cellStyle);
        }
    }
}
