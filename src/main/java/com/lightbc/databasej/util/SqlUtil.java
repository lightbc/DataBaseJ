package com.lightbc.databasej.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL文件处理工具类
 */
public class SqlUtil {
    // 导出SQL文件内容模板
    private static final String SQL_TEMPLATE = "INSERT INTO `#{tableName}`(#{cols}) VALUES(#{values});";

    /**
     * 导出SQL文件
     *
     * @param savePath  导出位置
     * @param map       导出数据
     * @param tableName 导出的数据表表名
     */
    public void writeSql(String savePath, Map<Integer, List<Object>> map, String tableName) {
        FileUtil fileUtil = new FileUtil();
        boolean cb = fileUtil.create(savePath);
        if (cb) {
            String sql = getSql(map, tableName);
            fileUtil.write(savePath, sql);
        }
    }

    /**
     * 获取导出SQL文件的文件内容
     *
     * @param map       导出数据
     * @param tableName 导出的数据表表名
     * @return string 导出内容
     */
    private String getSql(Map<Integer, List<Object>> map, String tableName) {
        StringBuilder builder = null;
        List<Map<String, Object>> data = getData(map, tableName);
        if (data != null) {
            builder = new StringBuilder();
            for (Map<String, Object> m : data) {
                if (m != null) {
                    String sql = SQL_TEMPLATE;
                    // 模板内容替换成导出数据内容
                    for (String key : m.keySet()) {
                        String oldChar = "#{" + key + "}";
                        String newChar = String.valueOf(m.get(key));
                        sql = sql.replace(oldChar, newChar);
                    }
                    builder.append(sql).append("\n");
                }
            }
        }
        return builder.toString();
    }

    /**
     * 处理导出数据
     *
     * @param map       导出数据
     * @param tableName 导出数据表表名
     * @return list 处理结果
     */
    private List<Map<String, Object>> getData(Map<Integer, List<Object>> map, String tableName) {
        if (map == null) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        List<Object> header = map.get(0);
        String cols = "";
        // 获取列名
        if (header != null) {
            for (int i = 0; i < header.size(); i++) {
                cols += header.get(i) + ",";
            }
            if (!"".equals(cols.trim())) {
                cols = cols.substring(0, cols.length() - 1);
            }
        }
        // 处理导出数据
        for (Integer key : map.keySet()) {
            if (key > 0) {
                List<Object> data = map.get(key);
                String values = "";
                for (int j = 0; j < data.size(); j++) {
                    values += "'" + data.get(j) + "',";
                }
                if (!"".equals(values.trim())) {
                    values = values.substring(0, values.length() - 1);
                }
                Map<String, Object> params = new HashMap<>();
                params.put("tableName", tableName);
                params.put("cols", cols);
                params.put("values", values);
                list.add(params);
            }
        }
        return list;
    }
}
