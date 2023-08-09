package com.lightbc.databasej.actions;

import com.intellij.database.console.JdbcConsole;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.util.SqlOperateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContextGroup extends ActionGroup {

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent event) {
        JdbcConsole console = SqlOperateUtil.getConsole(event);
        // 判断右键对象是否是JdbcConsole编辑器
        if (console != null) {
            SaveSqlAction saveSqlAction = new SaveSqlAction("Save Sql");
            UseSqlAction useSqlAction = new UseSqlAction("Use Sql");
            DeleteSqlAction deleteSqlAction = new DeleteSqlAction("Del Sql");
            return new AnAction[]{saveSqlAction, useSqlAction, deleteSqlAction};
        }
        return AnAction.EMPTY_ARRAY;
    }

}
