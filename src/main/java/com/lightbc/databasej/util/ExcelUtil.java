package com.lightbc.databasej.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel处理工具类
 */
public class ExcelUtil {

    /**
     * 导出Excel文件(2003)
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @throws Exception
     */
    public void writeExcel(String savePath, Map<Integer, List<Object>> map) throws Exception {
        if (map == null) {
            throw new Exception("无处理数据存在！");
        }
        // 获取导出数据的行数
        int rows = map.keySet().size();
        // 获取导出数据的列数
        int cols = map.get(0).size();
        // Excel（2003）
        if (rows >= 65536) {
            throw new Exception("超过最大写入数据行,当前有【" + rows + "】行需要处理！");
        }
        //创建工作簿
        Workbook workbook = new HSSFWorkbook();
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
                String value = String.valueOf(obj);
                // 填充单元格数据
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

}
