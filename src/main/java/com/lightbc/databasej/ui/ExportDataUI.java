package com.lightbc.databasej.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.lightbc.databasej.util.*;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据导出UI界面
 */
@Data
public class ExportDataUI {
    // 导出文件名命名组件
    private JTextField exportFileName;
    // 主面板
    private JPanel mainPanel;
    // 导出位置组件容器面板
    private JPanel exportPanel;
    // 导出文件类型选择器
    private JComboBox exportType;
    // 导出位置选择器
    private TextFieldWithBrowseButton exportPath;

    public ExportDataUI() {
        init();
    }

    private void init() {
        exportPath();
    }

    /**
     * 导出路径
     */
    private void exportPath() {
        this.exportPanel.setLayout(new BorderLayout());
        this.exportPath = new TextFieldWithBrowseButton();
        this.exportPanel.add(exportPath, BorderLayout.CENTER);
        exportPathListener();
    }

    /**
     * 导出路径选择器事件监听
     */
    private void exportPathListener() {
        this.exportPath.addActionListener(e -> {
            // 获取当前操作的项目工程
            Project project = ProjectUtil.getProject();
            // 获取项目工程的虚拟文件对象
            String basePath=project.getBasePath();
            VirtualFile virtualFile = project.getProjectFile().findFileByRelativePath(basePath);
            // 路径选择器
            virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, virtualFile);
            if (virtualFile != null) {
                exportPath.setText(virtualFile.getPath());
            }
        });
    }

    /**
     * 导出数据
     *
     * @param name 表名
     * @param map  数据
     */
    public void ok(String name, Map<Integer, List<Object>> map) {
        // 判断导出位置是否为空
        String savePath = exportPath.getText().trim();
        if ("".equals(savePath)) {
            DialogUtil.showTips(mainPanel, "请选择导出路径！");
            return;
        }
        // 获取导出文件自定义命名名称
        String fileName = exportFileName.getText().trim();
        if ("".equals(fileName)) {
            fileName = name;
        }
        // 获取导出文件的文件类型
        String ext = Objects.requireNonNull(exportType.getSelectedItem()).toString();
        // 判断选择的路径是否是当前盘的根目录
        int count = StringUtils.getCharCount(savePath, '/');
        if (count > 1) {
            savePath = savePath.concat(File.separator).concat(fileName).concat(".").concat(ext);
        } else {
            savePath = savePath.concat(fileName).concat(".").concat(ext);
        }
        // 处理Excel文件（2003）
        if ("xls".equals(ext)) {
            excel(savePath, map);
        }
        // 处理SQL文件
        if ("sql".equals(ext)) {
            sql(savePath, map, name);
        }
    }

    /**
     * 导出Excel文件（2003）
     *
     * @param savePath 保存路径
     * @param map      导出数据
     */
    private void excel(String savePath, Map<Integer, List<Object>> map) {
        try {
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExcel(savePath, map);
            DialogUtil.showTips(null, "Excel文件生成成功！");
        } catch (Exception ex) {
            DialogUtil.showTips(null, "Excel 写入错误原因:" + ex.getCause());
        }
    }

    /**
     * 导出SQL文件
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @param name     数据表表名
     */
    private void sql(String savePath, Map<Integer, List<Object>> map, String name) {
        try {
            SqlUtil sqlUtil = new SqlUtil();
            sqlUtil.writeSql(savePath, map, name);
            DialogUtil.showTips(null, "SQL文件生成成功！");
        } catch (Exception e) {
            DialogUtil.showTips(null, "SQL写入错误原因：" + e.getCause());
        }
    }
}
