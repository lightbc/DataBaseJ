package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.ui.KeyboardShortcutUI;
import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.ProjectUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * 快捷键配置
 */
public class KeyboardShortcutAction extends AnAction {
    private String text;

    KeyboardShortcutAction(String text){
        super(text);
        this.text=text;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        doAction(e);
    }

    private void doAction(@NotNull AnActionEvent e){
        // 显示快捷键配置UI界面
        DialogUtil.CustomDialog customDialog=new DialogUtil().new CustomDialog((Frame) ProjectUtil.getWindow());
        KeyboardShortcutUI ksUI=new KeyboardShortcutUI(text,customDialog);
        customDialog.showDialog(null,ksUI.getMainPanel(),text,500,300);
    }
}
