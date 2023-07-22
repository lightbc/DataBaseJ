package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 数据处理工作组
 */
public class DataBaseJGroup extends ActionGroup {
    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        ExportAction export = new ExportAction("Export Data");
        CleanCacheAction cleanCache = new CleanCacheAction("Clean Cache");
        HelpAction help = new HelpAction("Help");
        return new AnAction[]{export, cleanCache, help};
    }
}
