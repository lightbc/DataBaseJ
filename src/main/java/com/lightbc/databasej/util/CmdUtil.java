package com.lightbc.databasej.util;

import java.io.IOException;

public class CmdUtil {

    /**
     * 获取进程本地位置
     *
     * @param programName 进程名称
     * @return string 进程位置
     */
    public static String getProcessLocation(String programName) {
        String result = "";
        try {
            String cmd = "cmd /c wmic process get executablepath | findstr \"" + programName + "\"";
            Process dingTalk = Runtime.getRuntime().exec(cmd);
            FileUtil fileUtil = new FileUtil();
            String read = fileUtil.read(dingTalk.getInputStream());
            if (read != null && !"".equals(read.trim())) {
                String[] contents = read.split("\n");
                // 获取列表中第一个进程的所在位置
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
}
