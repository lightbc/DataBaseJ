package com.lightbc.databasej.util;


import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
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

    public void esc(List<Integer> list) {
        robot.keyPress(list.get(0));
        robot.delay(commonDelay);
        robot.keyRelease(list.get(0));
    }

    /**
     * 快捷键打开钉钉搜索框
     */
    public void search(List<Integer> list) {
        robot.keyPress(list.get(0));
        robot.keyPress(list.get(1));
        robot.keyPress(list.get(2));
        robot.delay(commonDelay);
        robot.keyRelease(list.get(2));
        robot.keyRelease(list.get(1));
        robot.keyRelease(list.get(0));
        robot.delay(commonDelay);
    }

    /**
     * 搜索指定联系人
     */
    public void searchContact() {
        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(commonDelay);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(commonDelay);
    }

    /*public void copy(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(text);
        clipboard.setContents(transferable, null);
    }*/

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

    /*public void common(String appName){
        Map<Integer,List<Integer>> map=KeyboardShortcutUtil.getKeyCodes(appName);
        if(map!=null && map.size()>0){
            for(Integer key:map.keySet()){
                List<Integer> list=map.get(key);
                if(list!=null && list.size()>0){
                    keyPress(list);
                    robot.delay(commonDelay);
                    keyRelease(list);
                }
            }
        }
    }

    private void keyPress(List<Integer> list){
        Collections.reverse(list);
        for(Integer o:list){
            robot.keyPress(o);
        }
    }

    private void keyRelease(List<Integer> list){
        for(Integer o:list){
            robot.keyRelease(o);
        }
    }*/
}
