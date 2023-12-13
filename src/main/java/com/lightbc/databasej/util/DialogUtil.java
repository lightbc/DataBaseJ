package com.lightbc.databasej.util;

import javax.swing.*;
import java.awt.*;

/**
 * 对话框工具类
 */
public class DialogUtil {
    public static boolean SHOW_DIALOG = true;

    /**
     * 显示提示信息对话框
     *
     * @param component 父级组件
     * @param message   展示的消息内容
     */
    public static void showTips(Component component, Object message) {
        if (SHOW_DIALOG) {
            JOptionPane.showMessageDialog(component, message);
        }
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

    /**
     * 自定义对话框
     */
    public class CustomDialog extends JDialog {

        /**
         * 使对话框在打开的窗体中顶层显示，不受失去鼠标焦点影响
         *
         * @param frame 父级窗体
         */
        public CustomDialog(Frame frame) {
            super(frame, false);
        }

        /**
         * 显示对话框
         *
         * @param parentComponent 父级组件
         * @param component       显示组件
         * @param title           标题
         * @param width           对话框宽度
         * @param height          对话框高度
         */
        public void showDialog(Component parentComponent, JComponent component, String title, int width, int height) {
            this.setTitle(title);
            this.setLayout(new BorderLayout());
            this.add(component, BorderLayout.CENTER);
            this.setSize(width, height);
            this.setLocationRelativeTo(parentComponent);
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.dispose();
            this.show();
        }
    }
}
