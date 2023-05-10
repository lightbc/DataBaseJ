package com.lightbc.databasej.util;

import javax.swing.*;
import java.awt.*;

/**
 * 对话框工具类
 */
public class DialogUtil {

    /**
     * 显示提示信息对话框
     *
     * @param component 父级组件
     * @param message   展示的消息内容
     */
    public static void showTips(Component component, Object message) {
        JOptionPane.showMessageDialog(component, message);
    }

    /**
     * 显示确认消息对话框
     *
     * @param component 父级组件
     * @param message   展示的消息内容
     * @param title     标题
     * @return int 0:确认
     */
    public static int showConfirmDialog(Component component, Object message, String title) {
        return JOptionPane.showConfirmDialog(component, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}
