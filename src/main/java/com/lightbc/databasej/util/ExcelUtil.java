package com.lightbc.databasej.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel处理工具类
 */
public class ExcelUtil {
    // excel 2003
    public static final String EXCEL03 = "Excel_2003";
    // excel 2007
    public static final String EXCEL07 = "Excel_2007";

    /**
     * 写入Excel
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @param type     Excel版本
     * @throws Exception
     */
    public void writeExcel(String savePath, Map<Integer, List<Object>> map, String type) throws Exception {
        write(savePath, map, type);
    }

    /**
     * 写入
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @param type     类型
     * @throws Exception
     */
    private void write(String savePath, Map<Integer, List<Object>> map, String type) throws Exception {
        if (map == null) {
            throw new Exception("无处理数据存在！");
        }
        // 获取导出数据的行数
        int rows = map.keySet().size();
        // 获取导出数据的列数
        int cols = map.get(0).size();
        Workbook workbook = null;
        // Excel2003
        if (type.trim().equals(EXCEL03)) {
            workbook = excel2003();
        }
        // Excel2007
        if (type.trim().equals(EXCEL07)) {
            workbook = excel2007();
        }
        // Excel（2003）
        if (type.trim().equals(EXCEL03) && rows >= 65536) {
            throw new Exception("超过最大写入数据行,当前有【" + rows + "】行需要处理！");
        }
        // 创建工作表
        Sheet sheet = workbook.createSheet();
        // 创建单元格样式
        CellStyle style = workbook.createCellStyle();
        // 水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 遍历行数
        for (int i = 0; i < rows; i++) {
            Row row = sheet.createRow(i);
            List<Object> list = map.get(i);
            // 遍历列数
            for (int j = 0; j < cols; j++) {
                Object obj = list.get(j);
                Cell cell = row.createCell(j);
                String value = "";
                if (obj != null) {
                    value = String.valueOf(obj);
                }
                // 填充单元格数据
                if (value.length() > 32767) {
                    throw new Exception("单元格内容过长：【" + i + "行;" + (j + 1) + "列】");
                }
                cell.setCellValue(value);
                cell.setCellStyle(style);
                // 根据数据行最后一行，每列单元格中的数据长度设置单元格的长度值，前后留白
                if (i == (rows - 1)) {
                    sheet.setColumnWidth(j, (value.length() + 4) * 256);
                }
            }
            // 设置行高
            row.setHeightInPoints(20);
        }
        // 文件导出
        File file = new File(savePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    /**
     * 获取Excel2003工作簿
     *
     * @return workbook
     */
    private Workbook excel2003() {
        return new HSSFWorkbook();
    }

    /**
     * 获取Excel2007工作薄
     *
     * @return workbook
     */
    private Workbook excel2007() {
        return new SXSSFWorkbook();
    }

}
