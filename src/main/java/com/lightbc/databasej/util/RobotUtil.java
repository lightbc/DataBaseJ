package com.lightbc.databasej.util;


import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动发送工具类
 */
public class RobotUtil {
    private Robot robot;
    // 正常延迟500毫秒
    private int commonDelay = 500;
    // 长时延迟10秒
    private int longDelay = 1000;

    public RobotUtil() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制内容到剪贴板
     *
     * @param object 复制到剪贴板的内容
     */
    public void copy(Object object) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = null;
        // 复制字符串
        if (object instanceof String) {
            transferable = new StringSelection(String.valueOf(object));
        }
        // 复制文件
        if (object instanceof File) {
            List<File> files = new ArrayList<>();
            files.add((File) object);
            transferable = new ClipBoardUtil(files);
        }
        if (transferable != null) {
            clipboard.setContents(transferable, null);
        }
    }

    /**
     * 粘贴剪贴板内容
     */
    public void paste(List<Integer> list) {
        robot.keyPress(list.get(0));
        robot.keyPress(list.get(1));
        robot.keyRelease(list.get(1));
        robot.keyRelease(list.get(0));
        robot.delay(longDelay);
    }

    /**
     * 确认发送粘贴内容
     */
    public void enter(List<Integer> list) {
        robot.keyPress(list.get(0));
        robot.delay(commonDelay);
        robot.keyRelease(list.get(0));
        robot.delay(longDelay);
    }

}
