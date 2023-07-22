package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lightbc.databasej.util.FileUtil;
import com.lightbc.databasej.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 插件使用帮助
 */
public class HelpAction extends AnAction {
    // 帮助文件路径
    private static final String HELP_FILE_PATH = "/html/help.html";
    // 重试次数
    private int test;

    HelpAction(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        cache();
        doAction(e);
    }

    /**
     * 动作执行
     *
     * @param e
     */
    private void doAction(@NotNull AnActionEvent e) {
        openHelpFileByBrowse("chrome.exe");
    }

    /**
     * 系统默认浏览器打开插件使用帮助文档
     *
     * @param browse 浏览器
     * @throws URISyntaxException
     * @throws IOException
     */
    private void openHelpFileByBrowse(String browse) {
        String path = PluginUtil.getCachePath().concat(HELP_FILE_PATH);
        try {
            String cmd = "cmd /c start " + browse + " " + path;
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            openTest();
        }
    }

    /**
     * 将打包的图片缓存到用户本地目录下
     */
    private void cache() {
        FileUtil fileUtil = new FileUtil();
        // 获取插件保存路径
        String cacheDir = PluginUtil.getCachePath();
        // 获取HTML文件缓存路径
        String htmlPath = cacheDir.concat(HELP_FILE_PATH);
        // 缓存HTML文件
        fileUtil.cacheFile(HELP_FILE_PATH, htmlPath);
        // 加载HTML文件内容
        String content = fileUtil.loadCacheContent(HELP_FILE_PATH);
        // 获取HTML文件中的图片路径
        List<String> images = getImagesPath(content);
        if (images != null) {
            for (String image : images) {
                int lastIndex = image.lastIndexOf("/");
                if (lastIndex != -1) {
                    // 缓存图片
                    image = processPath(image);
                    // 获取完整缓存路径
                    String imagePath = cacheDir.concat(image);
                    fileUtil.cacheFile(image, imagePath);
                }
            }
        }
    }

    /**
     * 获取HTML字符串中的图片src信息
     *
     * @param htmlContent HTML内容
     * @return list<string>
     */
    private static List<String> getImagesPath(String htmlContent) {
        List<String> src = new ArrayList<>();
        String img;
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlContent);
        while (m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                src.add(m.group(1));
            }
        }
        return src;
    }

    /**
     * 处理路中的上级目录../表示的问题
     *
     * @param path 路径
     * @return string 处理结果
     */
    private String processPath(String path) {
        String n = "../";
        int i = path.lastIndexOf(n);
        int begin = 0;
        if (i != -1) {
            begin = i + n.length() - 1;
        }
        return path.substring(begin);
    }

    /**
     * 打开尝试
     */
    private void openTest() {
        if (test > 2) {
            return;
        }
        test++;
        openHelpFileByBrowse("iexplore.exe");
    }
}
