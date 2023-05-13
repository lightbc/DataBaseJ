package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.interfaces.SupportProgramInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 快捷键配置组
 */
public class KeyboardShortcutGroup extends ActionGroup {

    KeyboardShortcutGroup(String text){
        super(text,true);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        KeyboardShortcutAction dingTalk=new KeyboardShortcutAction(SupportProgramInterface.DING_TALK);
        return new AnAction[]{dingTalk};
    }
}
