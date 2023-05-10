package com.lightbc.databasej.util;

/**
 * 字符串处理工具类
 */
public class StringUtils {
    /**
     * 获取指定字符在源字符串中出现的次数
     *
     * @param s 源字符串
     * @param c 指定字符
     * @return int 出现次数，-1：源字符串为空，0：不存在
     */
    public static int getCharCount(String s, char c) {
        if (s != null) {
            int count = 0;
            char[] chars = s.toCharArray();
            for (char ch : chars) {
                if (ch == c) {
                    count++;
                }
            }
            return count;
        }
        return -1;
    }
}
