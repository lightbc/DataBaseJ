package com.lightbc.databasej.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;

import java.awt.*;

/**
 * 项目工程工具类
 */
public class ProjectUtil {
    /**
     * 获取当前的工程项目对象
     *
     * @return com.intellij.openapi.project.Project
     */
    public static Project getProject() {
        // 项目管理器
        ProjectManager projectManager = ProjectManager.getInstance();
        // 默认项目
        Project project = projectManager.getDefaultProject();
        try {
            // 所有打开的项目
            Project[] projects = projectManager.getOpenProjects();
            // 窗体管理器
            WindowManager windowManager = WindowManager.getInstance();
            if (projects.length > 0) {
                for (Project p : projects) {
                    // 判断当前活动窗体的打开项目
                    Window window = windowManager.suggestParentWindow(p);
                    if (window != null && window.isActive()) {
                        project = p;
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            return project;
        }
    }

    /**
     * 获取当前活动窗体
     *
     * @return window 窗体
     */
    public static Window getWindow() {
        Window w = null;
        // 项目管理器
        ProjectManager projectManager = ProjectManager.getInstance();
        try {
            // 所有打开的项目
            Project[] projects = projectManager.getOpenProjects();
            // 窗体管理器
            WindowManager windowManager = WindowManager.getInstance();
            if (projects.length > 0) {
                for (Project p : projects) {
                    // 判断当前活动窗体的打开项目
                    Window window = windowManager.suggestParentWindow(p);
                    if (window != null && window.isActive()) {
                        w = window;
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            return w;
        }
    }
}
