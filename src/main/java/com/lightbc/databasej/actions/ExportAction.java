package com.lightbc.databasej.actions;

import com.intellij.database.datagrid.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.ui.ExportDataUI;
import org.jetbrains.annotations.NotNull;

/**
 * 查询结果导出
 */
public class ExportAction extends AnAction {
    public ExportAction() {
    }

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
        if (grid != null && grid.isReady()) {
            new ExportDataUI(grid);
        }
    }

}
