package com.lightbc.databasej.actions;

import com.intellij.database.datagrid.*;
import com.intellij.database.model.DasColumn;
import com.intellij.database.run.ui.DataAccessType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.ui.ExportDataUI;
import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.ProjectUtil;
import com.lightbc.databasej.util.ReflectUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 查询结果导出
 */
public class ExportAction extends AnAction {

    ExportAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        doAction(e);
    }

    /**
     * 动作执行
     *
     * @param e
     */
    private void doAction(@NotNull AnActionEvent e) {
        DataGrid grid = DataGridUtil.getDataGrid(e.getDataContext());
        if (grid != null) {
            GridModel model = grid.getDataModel(DataAccessType.DATA_WITH_MUTATIONS);
            // 获取查询到的数据行
            List<DataConsumer.Row> rows = model.getRows();
            // 获取查询到的数据列
            List<DataConsumer.Column> cols = model.getColumns();
            // 获取单元格数据
            Map<Integer, List<Object>> map = new LinkedHashMap<>();
            // 表头标题
            List<Object> header = new ArrayList<>();
            // 遍历行
            for (int i = 0; i < rows.size(); i++) {
                List<Object> cellData = new ArrayList<>();
                // 遍历列
                for (int j = 0; j < cols.size(); j++) {
                    // 数据模型行下标
                    ModelIndex rowIndex = ModelIndex.forRow(model, i);
                    // 数据模型列下标
                    ModelIndex colIndex = ModelIndex.forColumn(model, j);
                    // 数据项
                    String value = model.getValueAt(rowIndex, colIndex).toString();
                    cellData.add(value);
                    // 根据第一行的列下标获取表头标题信息
                    if (i == 0) {
                        DasColumn column = adaptVersion(grid, cols.get(j), colIndex);
                        String colName = column.getName();
                        header.add(colName);
                    }
                }
                // 填充数据
                map.put(i, cellData);
            }
            // 填充标题
            map.put(0, header);
            // 获取操作的数据表表名
            String tableName = DataGridUtil.getDatabaseTable(grid).getName();
            DialogUtil dialogUtil = new DialogUtil();
            DialogUtil.CustomDialog customDialog = dialogUtil.new CustomDialog((Frame) ProjectUtil.getWindow());
            ExportDataUI exportDataUI = new ExportDataUI(tableName, map, customDialog);
            customDialog.showDialog(null, exportDataUI.getMainPanel(), "Export Data", 500, 300);
        }
    }

    /**
     * 版本适应
     *
     * @param grid  数据网格
     * @param col   列
     * @param index 数据模型下标
     * @return DasColumn
     */
    private static DasColumn adaptVersion(DataGrid grid, DataConsumer.Column col, ModelIndex index) {
        DasColumn column = null;
        ReflectUtil util = new ReflectUtil();
        try {
            column = (DasColumn) util.getMethod(DataGridUtil.class, "getDatabaseColumn", DataGrid.class, DataConsumer.Column.class).invoke(DataGridUtil.class, grid, col);
        } catch (Exception e) {
            try {
                column = (DasColumn) util.getMethod(DataGridUtil.class, "getDatabaseColumn", DataGrid.class, ModelIndex.class).invoke(DataGridUtil.class, grid, index);
            } catch (Exception e1) {
            }
        } finally {
            return column;
        }
    }
}
