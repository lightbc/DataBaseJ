package com.lightbc.databasej.util;

import com.intellij.database.console.JdbcConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.entity.SaveSql;
import com.lightbc.databasej.service.DataBaseJPersistentService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SqlOperateUtil {

    /**
     * 打开UI界面前验证
     */
    public static boolean validUse() {
        boolean flag = false;
        try {
            DataBaseJPersistentService service = DataBaseJPersistentService.getInstance();
            List<SaveSql> useList = service.getSaveSqlList();
            if (useList != null && useList.size() > 0) {
                flag = true;
            } else {
                DialogUtil.showTips(null, "无可用的保存项使用！");
            }

        } catch (Exception e) {
            DialogUtil.showTips(null, "打开UI界面报错：" + e.getMessage());
        } finally {
            return flag;
        }
    }


    /**
     * 获取当前的jdbc消息控制台对象
     *
     * @param e
     * @return JdbcConsole
     */
    public static JdbcConsole getConsole(@NotNull AnActionEvent e) {
        return JdbcConsole.findConsole(e);
    }

}
