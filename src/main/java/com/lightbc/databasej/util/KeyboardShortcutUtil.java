package com.lightbc.databasej.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 快捷键处理工具类
 */
public class KeyboardShortcutUtil {
    // 快捷键配置文件存放位置
    private static final String CONFIG_JSON = "/config/keyboardshortcut.json";

    static {
        // 加载缓存jar包中的配置文件
        FileUtil fileUtil = new FileUtil();
        fileUtil.cacheFile(CONFIG_JSON, getCacheFilePath());
    }

    /**
     * 获取缓存文件缓存路径
     *
     * @return string
     */
    private static String getCacheFilePath() {
        // 获取插件保存路径
        String cacheDir = PluginUtil.getCachePath();
        return cacheDir.concat(CONFIG_JSON);
    }

    /**
     * 加载文件内容
     *
     * @return jsonarray
     */
    public static JSONArray loadContent() {
        FileUtil fileUtil = new FileUtil();
        String content = fileUtil.read(getCacheFilePath());
        return JSONArray.parseArray(content);
    }

    /**
     * 根据三方应用名称加载配置文件
     *
     * @param appName 三方应用名称
     * @return jsonarray
     */
    public static JSONArray getConfigByAppName(String appName) {
        JSONArray contents = loadContent();
        if (contents != null && contents.size() > 0) {
            for (Object content : contents) {
                JSONObject json = (JSONObject) content;
                if (json != null) {
                    for (String key : json.keySet()) {
                        boolean keyEq = key.toLowerCase().trim().equals(appName.trim().toLowerCase());
                        if (keyEq) {
                            return (JSONArray) json.get(key);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取快捷键
     *
     * @param appName 三方应用名称
     * @return map
     */
    public static Map<String, Object> getKeyboardShortcut(String appName) {
        Map<String, Object> map = null;
        JSONArray array = getConfigByAppName(appName);
        if (array != null && array.size() > 0) {
            map = new LinkedHashMap<>();
            for (Object obj : array) {
                JSONObject json = (JSONObject) obj;
                if (json != null) {
                    for (String key : json.keySet()) {
                        map.put(key, json.get(key));
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取全部的配置快捷键，按配置顺序有序读取
     *
     * @param appName 三方应用名称
     * @return map
     */
    public static Map<Integer, List<Integer>> getKeyCodesMap(String appName) {
        Map<Integer, List<Integer>> result = null;
        List<Integer> list;
        Map<String, Object> map = getKeyboardShortcut(appName);
        if (map != null && map.size() > 0) {
            result = new LinkedHashMap<>();
            int index = 0;
            for (String key : map.keySet()) {
                list = new ArrayList<>();
                String[] keys = map.get(key).toString().split(",");
                for (String k : keys) {
                    k = k.trim();
                    if (!"".equals(k)) {
                        int code = getKeyCode(k);
                        list.add(code);
                    }
                }
                result.put(index, list);
                index++;
            }
        }
        return result;
    }

    /**
     * 获取指定的快捷键信息
     *
     * @param appName 三方应用名称
     * @param index   快捷键配置顺序
     * @return list
     */
    public static List<Integer> getKeyCodes(String appName, int index) {
        Map<Integer, List<Integer>> map = getKeyCodesMap(appName);
        return map != null ? map.get(index) : null;
    }

    /**
     * 获取键值
     *
     * @param key 键盘键名称
     * @return int
     */
    public static int getKeyCode(String key) {
        int code = -1;
        try {
            code = Integer.parseInt(key);
        } catch (Exception e) {
            // 处理换行、制表符等字符问题
            char[] chars = key.toCharArray();
            if (chars != null) {
                for (char c : chars) {
                    code = c;
                    break;
                }
            }
        } finally {
            return code;
        }
    }

}
