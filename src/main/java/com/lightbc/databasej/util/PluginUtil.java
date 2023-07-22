package com.lightbc.databasej.util;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 插件工具类
 */
public class PluginUtil {
    // 插件ID
    private static final String PLUGIN_ID = "com.lightbc.databasej";
    private static final String TEMP_DIR = "C:/databasej_temp/";

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


    /**
     * 创建临时文件
     *
     * @param name 文件名称
     * @param ext  文件后缀
     * @return file
     */
    public static File temp(String name, String ext) {
        String path = TEMP_DIR.concat(name).concat(ext);
        return new FileUtil().create(path) ? new File(path) : null;
    }

    /**
     * 删除临时文件
     *
     * @return Boolean true-删除成功，false-删除失败
     */
    public boolean delTemp() {
        boolean flag = false;
        try {
            File file = new File(TEMP_DIR);
            if (file.exists()) {
                del(file);
                flag = true;
            }
        } catch (Exception e) {
        } finally {
            return flag;
        }
    }

    /**
     * 删除指定目录及其子目录/文件
     *
     * @param file 文件对象
     */
    private void del(File file) {
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    // 删除文件
                    if (f.isFile()) {
                        f.delete();
                    }
                    // 文件夹下包含文件夹，递归调用删除全部文件
                    if (f.isDirectory()) {
                        del(f);
                    }
                }
            }
            // 最后删除文件夹
            file.delete();
        }
    }
}
