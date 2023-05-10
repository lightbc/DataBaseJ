package com.lightbc.databasej.util;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;

import java.io.File;
import java.nio.file.Path;

/**
 * 插件工具类
 */
public class PluginUtil {
    // 插件ID
    private static final String PLUGIN_ID = "com.lightbc.databasej";

    /**
     * 获取插件的保存路径
     *
     * @return string
     */
    public static String getPluginSavePath() {
        IdeaPluginDescriptor[] plugins = PluginManager.getPlugins();
        for (IdeaPluginDescriptor plugin : plugins) {
            if (PLUGIN_ID.equals(plugin.getPluginId().getIdString())) {
                return adaptVersion(plugin);
            }
        }
        return null;
    }

    /**
     * 版本适应
     *
     * @param plugin idea插件描述
     * @return 缓存路径
     */
    private static String adaptVersion(IdeaPluginDescriptor plugin) {
        ReflectUtil util = new ReflectUtil();
        String path = null;
        try {
            Path filePath = (Path) util.getMethod(IdeaPluginDescriptor.class, "getPluginPath").invoke(plugin, new Object[]{});
            path = filePath.toFile().getAbsolutePath();
        } catch (Exception e) {
            try {
                File file = (File) util.getMethod(IdeaPluginDescriptor.class, "getPath").invoke(plugin, new Object[]{});
                path = (String) util.getMethod(File.class, "getAbsolutePath").invoke(file, new Object[]{});
            } catch (Exception e1) {
            }
        } finally {
            return path;
        }
    }

    /**
     * 缓存目录
     *
     * @return string 路径
     */
    public static String getCachePath() {
        return getPluginSavePath().concat(File.separator).concat("cache");
    }

}
