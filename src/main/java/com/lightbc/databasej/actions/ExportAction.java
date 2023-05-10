package com.lightbc.databasej.actions;

import com.intellij.database.datagrid.*;
import com.intellij.database.model.DasColumn;
import com.intellij.database.run.ui.DataAccessType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.ui.ExportDataUI;
import com.lightbc.databasej.util.DialogUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
                        DasColumn column = DataGridUtil.getDatabaseColumn(grid, cols.get(j));
                        String colName = column.getName();
                        header.add(colName);
                    }
                }
                // 填充数据
                map.put(i, cellData);
            }
            // 填充标题
            map.put(0, header);
            // 导出对话框
            ExportDataUI exportDataUI = new ExportDataUI();
            int r = DialogUtil.showConfirmDialog(null, exportDataUI.getMainPanel(), "Export Data");
            // 确认操作
            if (r == 0) {
                // 获取操作的数据表表名
                String tableName = DataGridUtil.getDatabaseTable(grid).getName();
                exportDataUI.ok(tableName, map);
            }
        }
    }
}
