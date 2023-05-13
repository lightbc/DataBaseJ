package com.lightbc.databasej.util;

import com.lightbc.databasej.interfaces.DingTalkOperateInterface;

import java.io.IOException;

/**
 * 钉钉发送工具类
 */
public class DingTalkUtil {
    // 进程名称
    private static final String PROGRAM_NAME = "DingTalk.exe";

    /**
     * 获取进程本地位置
     *
     * @return string 进程位置
     */
    public String getProcessLocation() {
        String result = "";
        try {
            String cmd = "cmd /c wmic process get executablepath | findstr \"" + PROGRAM_NAME + "\"";
            Process dingTalk = Runtime.getRuntime().exec(cmd);
            FileUtil fileUtil = new FileUtil();
            String read = fileUtil.read(dingTalk.getInputStream());
            if (read != null && !"".equals(read.trim())) {
                String[] contents = read.split("\n");
                // 获取钉钉进程列表，获取列表中第一个进程的所在位置
                for (String content : contents) {
                    String c = content.trim();
                    if (!"".equals(c)) {
                        result = c;
                        break;
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            return result;
        }
    }

    /**
     * 自动发送
     *
     * @param appName 三方程序名称
     * @param contact 发送对象
     * @param object  发送内容
     */
    /*public void send(String appName, String contact, Object object) {
        try {
            String cmd = getProcessLocation();
            if("".equals(cmd.trim())){
                DialogUtil.showTips(null,"请确认钉钉程序当前正在正常运行中！");
                return;
            }
            Runtime.getRuntime().exec(cmd);
            RobotUtil robotUtil = new RobotUtil();
            Thread.sleep(500);
            robotUtil.search(KeyboardShortcutUtil.getKeyCodes(appName, DingTalkOperateInterface.SEARCH));
            Thread.sleep(500);
            send(appName,(Object) contact, robotUtil);
            robotUtil.searchContact();
            send(appName,object, robotUtil);
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }*/

    /**
     * 发送
     *
     * @param appName   三方程序名称
     * @param object    发送内容
     * @param robotUtil 自动发送工具
     * @throws InterruptedException
     */
    /*private void send(String appName, Object object, RobotUtil robotUtil) throws InterruptedException {
        robotUtil.copy(object);
        Thread.sleep(500);
        robotUtil.paste(KeyboardShortcutUtil.getKeyCodes(appName,DingTalkOperateInterface.PASTE));
        Thread.sleep(500);
        robotUtil.enter(KeyboardShortcutUtil.getKeyCodes(appName,DingTalkOperateInterface.ENTER));
    }*/

    /**
     * 自动发送
     *
     * @param appName 三方程序名称
     * @param object  发送内容
     */
    public void send(String appName, Object object) {
        try {
            String cmd = getProcessLocation();
            if ("".equals(cmd.trim())) {
                DialogUtil.showTips(null, "请确认钉钉程序当前正在正常运行中！");
                return;
            }
            Runtime.getRuntime().exec(cmd);
            RobotUtil robotUtil = new RobotUtil();
            send(appName, object, robotUtil);
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }

    /**
     * 发送
     *
     * @param appName   三方程序名称
     * @param object    发送内容
     * @param robotUtil 自动发送工具
     * @throws InterruptedException
     */
    private void send(String appName, Object object, RobotUtil robotUtil) throws InterruptedException {
        robotUtil.copy(object);
        Thread.sleep(500);
        robotUtil.paste(KeyboardShortcutUtil.getKeyCodes(appName, DingTalkOperateInterface.PASTE));
    }

}
