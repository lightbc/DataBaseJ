package com.lightbc.databasej.util;

import com.lightbc.databasej.interfaces.AppOperateInterface;

import java.io.IOException;

/**
 * 钉钉发送工具类
 */
public class DingTalkUtil {
    // 进程名称
    private static final String PROGRAM_NAME = "DingTalk.exe";

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
            String cmd = CmdUtil.getProcessLocation(PROGRAM_NAME);
            if ("".equals(cmd.trim())) {
                DialogUtil.showTips(null, "请确认钉钉程序当前正在正常运行中！");
                return;
            }
            Runtime.getRuntime().exec(cmd);
            RobotUtil robotUtil = new RobotUtil();
            send(appName, object, robotUtil);
            DialogUtil.showTips(null, "导出钉钉成功！");
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
        robotUtil.paste(KeyboardShortcutUtil.getKeyCodes(appName, AppOperateInterface.DING_TALK_PASTE));
    }

}
