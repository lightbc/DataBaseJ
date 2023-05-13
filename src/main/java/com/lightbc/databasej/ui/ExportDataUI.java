package com.lightbc.databasej.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.lightbc.databasej.interfaces.SupportProgramInterface;
import com.lightbc.databasej.util.*;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据导出UI界面
 */
@Data
public class ExportDataUI {
    private static final String EXPORT_LOCAL = "local";
    private static final String EXPORT_DING_TALK = "dingTalk";
    // 导出文件名命名组件
    private JTextField exportFileName;
    // 主面板
    private JPanel mainPanel;
    // 导出文件类型选择器
    private JComboBox exportType;
    private JLabel local;
    private JLabel dingTalk;
    private String exportPath;
    private String tableName;
    private Map<Integer, List<Object>> dataMap;
    private DialogUtil.CustomDialog customDialog;

    public ExportDataUI() {
    }

    public ExportDataUI(String tableName, Map<Integer, List<Object>> dataMap, DialogUtil.CustomDialog customDialog) {
        this.tableName = tableName;
        this.dataMap = dataMap;
        this.customDialog = customDialog;
        init();
    }

    private void init() {
        exportLocal();
        exportDingTalk();
    }


    /**
     * 导出到本地
     */
    private void exportLocal() {
        export(local, EXPORT_LOCAL);
    }

    /**
     * 导出到钉钉
     */
    private void exportDingTalk() {
        export(dingTalk, EXPORT_DING_TALK);
    }

    /**
     * 确认导出数据
     */
    public void ok() {
        if (exportPath == null || "".equals(exportPath.trim())) {
            return;
        }
        // 判断导出位置是否为空
        String savePath = exportPath.trim();
        if ("".equals(savePath)) {
            DialogUtil.showTips(mainPanel, "请选择导出路径！");
            return;
        }
        // 获取导出文件自定义命名名称
        String fileName = exportFileName.getText().trim();
        if ("".equals(fileName)) {
            fileName = tableName;
        }
        // 获取导出文件的文件类型
        String ext = Objects.requireNonNull(exportType.getSelectedItem()).toString();
        // 判断选择的路径是否是当前盘的根目录
        File file = new File(savePath);
        if (file.exists() && file.isDirectory()) {
            savePath = file.getAbsolutePath().concat(File.separator).concat(fileName).concat(".").concat(ext);
        }
        // 处理Excel文件（2003）
        if ("xls".equals(ext)) {
            excel(savePath, dataMap, ExcelUtil.EXCEL03);
        }
        // 处理Excel文件（2007）
        if ("xlsx".equals(ext)) {
            excel(savePath, dataMap, ExcelUtil.EXCEL07);
        }
        // 处理SQL文件
        if ("sql".equals(ext)) {
            sql(savePath, dataMap, tableName);
        }
        customDialog.dispose();
    }

    /**
     * 导出Excel文件
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @param type     Excel版本
     */
    private void excel(String savePath, Map<Integer, List<Object>> map, String type) {
        try {
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExcel(savePath, map, type);
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

    /**
     * 导出
     *
     * @param label 导出组件
     * @param type  导出类型
     */
    private void export(JLabel label, String type) {
        int thickness = 2;
        label.setBorder(BorderFactory.createLineBorder(Color.lightGray, thickness));
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (type) {
                    case EXPORT_LOCAL:
                        local();
                        break;
                    case EXPORT_DING_TALK:
                        dingTalk();
                        break;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBorder(BorderFactory.createLineBorder(new Color(0, 169, 255), thickness));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBorder(BorderFactory.createLineBorder(Color.lightGray, thickness));
            }
        });
    }

    /**
     * 本地
     */
    private void local() {
        // 获取当前操作的项目工程
        Project project = ProjectUtil.getProject();
        // 获取项目工程的虚拟文件对象
        String basePath = project.getBasePath();
        VirtualFile virtualFile = project.getProjectFile().findFileByRelativePath(basePath);
        // 路径选择器
        virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, virtualFile);
        if (virtualFile != null) {
            exportPath = virtualFile.getPath();
        }
        ok();
    }

    /**
     * 钉钉
     */
    private void dingTalk() {
        try {
            DingTalkUtil dingTalkUtil = new DingTalkUtil();
            FileUtil fileUtil = new FileUtil();
            // 选择的导出文件类型
            String ext = Objects.requireNonNull(exportType.getSelectedItem()).toString();
            String suffix = ".".concat(ext);
            // 自定义文件名
            String fileName = exportFileName.getText().trim();
            if ("".equals(fileName)) {
                // 文件名默认为数据表表名
                fileName = tableName;
            }
            //  创建临时文件
            File file = fileUtil.createTempFile(fileName, suffix, exportPath);
            if (file != null) {
                // 获取文件父级文件夹目录路径
                exportPath = file.getParent();
                // 不显示提示信息
                DialogUtil.SHOW_DIALOG = false;
                ok();
                // 显示提示信息
                DialogUtil.SHOW_DIALOG = true;
//                new Thread(() -> dingTalkUtil.send(SupportProgramInterface.DING_TALK, "我", file)).start();
                new Thread(() -> dingTalkUtil.send(SupportProgramInterface.DING_TALK, file)).start();
            } else {
                DialogUtil.showTips(null, "钉钉发送失败，临时文件未创建成功！");
            }
        } catch (IOException e) {
            DialogUtil.showTips(null, "钉钉发送失败，失败原因，临时文件创建异常：【" + e.getCause() + "】");
        }
    }
}
