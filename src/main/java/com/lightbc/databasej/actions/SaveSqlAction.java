package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.ui.SaveSqlUI;
import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.SqlOperateUtil;
import org.jetbrains.annotations.NotNull;

public class SaveSqlAction extends AnAction {

    SaveSqlAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        doProcess(e);
    }

    private void doProcess(AnActionEvent e) {
        String content = getContent(e);
        SaveSqlUI ui = new SaveSqlUI(content);
        int c = DialogUtil.showConfirmDialog(null, ui.getMainPanel(), "Save Sql");
        // 0：确认保存
        if (c == 0) {
            ui.doSave();
        }
    }

    /**
     * 获取报错内容
     *
     * @param e
     * @return string 保存内容
     */
    private String getContent(@NotNull AnActionEvent e) {
        return SqlOperateUtil.getConsole(e).getDocument().getText();
    }

}
