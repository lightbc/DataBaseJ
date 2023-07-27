package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.lightbc.databasej.ui.SaveSqlUI;
import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.ProjectUtil;
import com.lightbc.databasej.util.SqlOperateUtil;
import org.jetbrains.annotations.NotNull;

public class UseSqlAction extends AnAction {
    private Document document;

    UseSqlAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        doProcess(e);
    }

    private void doProcess(AnActionEvent e) {
        boolean b = SqlOperateUtil.validUse();
        if (b) {
            SaveSqlUI ui = new SaveSqlUI();
            int c = DialogUtil.showConfirmDialog(null, ui.getMainPanel(), "Use Sql");
            // 0:确认保存
            if (c == 0) {
                String content = ui.getSql();
                setContent(e, content);
            }
        }
    }

    /**
     * 设置文档内容
     *
     * @param e
     * @param content
     */
    private void setContent(@NotNull AnActionEvent e, String content) {
        this.document = SqlOperateUtil.getConsole(e).getDocument();
        doSetContent(this.document.getText(), content);
    }


    /**
     * jdbc控制台替换成指定保存的内容
     *
     * @param documentContent 当前文档内容
     * @param replaceContent  替换内容
     */
    private void doSetContent(String documentContent, String replaceContent) {
        WriteCommandAction.runWriteCommandAction(ProjectUtil.getProject(), () -> {
            if (this.document != null) {
                this.document.deleteString(0, documentContent.length());
            }
        });
        WriteCommandAction.runWriteCommandAction(ProjectUtil.getProject(), () -> {
            if (this.document != null) {
                this.document.insertString(0, replaceContent);
            }
        });
    }

}
