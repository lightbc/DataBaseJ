package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

public class CleanCacheAction extends AnAction {

    CleanCacheAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        int c = DialogUtil.showConfirmDialog(null, "确认清理数据导出时缓存的数据？", "提示");
        if (c == 0) {
            boolean flag = new PluginUtil().delTemp();
            if (flag) {
                DialogUtil.showTips(null, "缓存清理成功！");
            } else {
                DialogUtil.showTips(null, "缓存清理失败/清理目录可能不存在！");
            }
        }
    }
}
