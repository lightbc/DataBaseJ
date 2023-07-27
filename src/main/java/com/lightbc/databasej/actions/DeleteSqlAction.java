package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.ui.SaveSqlUI;
import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.SqlOperateUtil;
import org.jetbrains.annotations.NotNull;

public class DeleteSqlAction extends AnAction {
    DeleteSqlAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        doProcess();
    }

    private void doProcess() {
        boolean b = SqlOperateUtil.validUse();
        if (b) {
            SaveSqlUI ui = new SaveSqlUI();
            int c = DialogUtil.showConfirmDialog(null, ui.getMainPanel(), "Del Sql");
            // 0:确认保存
            if (c == 0) {
                ui.doDelSql();
            }
        }
    }
}
