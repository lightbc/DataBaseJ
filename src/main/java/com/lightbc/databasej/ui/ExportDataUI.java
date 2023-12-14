package com.lightbc.databasej.ui;

import com.intellij.database.datagrid.*;
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
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据导出UI界面
 */
@Data
public class ExportDataUI {
    // 导出本地
    private static final String EXPORT_LOCAL = "local";
    // 导出钉钉
    private static final String EXPORT_DING_TALK = "dingTalk";
    // 导出微信
    private static final String EXPORT_WE_CHAT = "weChat";
    // 导出文件名命名组件
    private JTextField exportFileName;
    // 主面板
    private JPanel mainPanel;
    // 导出文件类型选择器
    private JComboBox exportType;
    private JLabel local;
    private JLabel dingTalk;
    private JLabel weChat;
    private JCheckBox allData;
    private String exportPath;
    private String tableName;
    private Map<Integer, List<Object>> dataMap;
    private DialogUtil.CustomDialog customDialog;
    private DataGrid dataGrid;

    public ExportDataUI(DataGrid dataGrid) {
        this.dataGrid = dataGrid;
        init();
    }

    private void init() {
        // 导出界面打开前，导出默认数据集校验，数据集为空不退出导出界面
        if (this.dataGrid == null) {
            DialogUtil.showTips(null, "导出数据对象为空！");
            return;
        }
        this.dataMap = currentPageData();
        if (this.dataMap == null || this.dataMap.size() == 0) {
            DialogUtil.showTips(mainPanel, "没有可供导出的数据结果！");
            return;
        }
        this.tableName = getTableName();
        showDefaultName();
        exportLocal();
        exportDingTalk();
        exportWeChat();
        allPageDataCheckBoxListener();
        DialogUtil dialogUtil = new DialogUtil();
        this.customDialog = dialogUtil.new CustomDialog((Frame) ProjectUtil.getWindow());
        this.customDialog.showDialog(null, this.mainPanel, "Export Data", 500, 240);
    }

    /**
     * 显示默认导出文件名称
     */
    private void showDefaultName() {
        exportFileName.setText(tableName);
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
     * 导出到微信
     */
    private void exportWeChat() {
        export(weChat, EXPORT_WE_CHAT);
    }

    /**
     * 确认导出数据
     *
     * @return boolean
     */
    public boolean ok() {
        if (this.allData.isSelected()) {
            this.dataMap = allPageData();
        }
        if (this.dataMap == null || this.dataMap.size() == 0) {
            DialogUtil.showTips(mainPanel, "没有可供导出的数据结果！");
            return false;
        }
        if (exportPath == null || "".equals(exportPath.trim())) {
            return false;
        }
        // 判断导出位置是否为空
        String savePath = exportPath.trim();
        if ("".equals(savePath)) {
            DialogUtil.showTips(mainPanel, "请选择导出路径！");
            return false;
        }
        boolean flag = false;
        try {
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
                exportExcel(savePath, dataMap, ExcelUtil.EXCEL03);
            }
            // 处理Excel文件（2007）
            if ("xlsx".equals(ext)) {
                exportExcel(savePath, dataMap, ExcelUtil.EXCEL07);
            }
            // 处理SQL文件
            if ("sql".equals(ext)) {
                exportSql(savePath, dataMap, tableName);
            }
            // 处理json文件
            if ("json".equals(ext)) {
                exportJson(savePath, dataMap);
            }
            customDialog.dispose();
            flag = true;
        } catch (Exception e) {
            DialogUtil.showTips(mainPanel, "导出错误：" + e.getMessage());
        } finally {
            return flag;
        }
    }

    /**
     * 导出Excel文件
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @param type     Excel版本
     */
    private void exportExcel(String savePath, Map<Integer, List<Object>> map, String type) throws Exception {
        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.writeExcel(savePath, map, type);
    }

    /**
     * 导出SQL文件
     *
     * @param savePath 保存路径
     * @param map      导出数据
     * @param name     数据表表名
     */
    private void exportSql(String savePath, Map<Integer, List<Object>> map, String name) {
        SqlUtil sqlUtil = new SqlUtil();
        sqlUtil.writeSql(savePath, map, name);
    }

    /**
     * 导出json文件
     *
     * @param savePath 保存位置
     * @param map      导出数据
     * @throws Exception
     */
    private void exportJson(String savePath, Map<Integer, List<Object>> map) throws Exception {
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.writeJson(savePath, dataMap);
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
                    case EXPORT_WE_CHAT:
                        weChat();
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
        try {
            // 获取当前操作的项目工程
            Project project = ProjectUtil.getProject();
            // 获取项目工程的虚拟文件对象
            String basePath = project.getBasePath();
            VirtualFile virtualFile = project.getProjectFile().findFileByRelativePath(basePath);
            // 路径选择器
            virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, virtualFile);
            if (virtualFile != null) {
                exportPath = virtualFile.getPath();
                boolean b = ok();
                if (b) {
                    DialogUtil.showTips(null, "导出本地成功！");
                }
            } else {
                DialogUtil.showTips(null, "未选择有效导出路径！");
            }
        } catch (Exception e) {
            DialogUtil.showTips(null, "导出本地报错：" + e.getMessage());
        }
    }

    /**
     * 钉钉
     */
    private void dingTalk() {
        try {
            DingTalkUtil dingTalkUtil = new DingTalkUtil();
            File file = getTempFile();
            if (file != null) {
                // 获取文件父级文件夹目录路径
                exportPath = file.getParent();
                boolean b = ok();
                if (b) {
                    new Thread(() -> dingTalkUtil.send(SupportProgramInterface.DING_TALK, file)).start();
                }
            } else {
                DialogUtil.showTips(null, "钉钉发送失败，临时文件未创建成功！");
            }
        } catch (IOException e) {
            DialogUtil.showTips(null, "钉钉发送失败，失败原因，临时文件创建异常：【" + e.getMessage() + "】");
        } catch (Exception e) {
            DialogUtil.showTips(null, "导出钉钉报错：" + e.getMessage());
        }
    }

    /**
     * 微信
     */
    private void weChat() {
        try {
            WeChatUtil weChatUtil = new WeChatUtil();
            File file = getTempFile();
            if (file != null) {
                // 获取文件父级文件夹目录路径
                exportPath = file.getParent();
                boolean b = ok();
                if (b) {
                    new Thread(() -> weChatUtil.send(SupportProgramInterface.WE_CHAT, file)).start();
                }
            } else {
                DialogUtil.showTips(null, "微信发送失败，临时文件未创建成功！");
            }
        } catch (IOException e) {
            DialogUtil.showTips(null, "微信发送失败，失败原因，临时文件创建异常：【" + e.getMessage() + "】");
        } catch (Exception e) {
            DialogUtil.showTips(null, "导出微信报错：" + e.getMessage());
        }
    }

    /**
     * 获取导出缓存文件
     *
     * @return file
     * @throws IOException
     */
    private File getTempFile() throws IOException {
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
        return PluginUtil.temp(fileName, suffix);
    }

    /**
     * 获取全部数据集
     *
     * @return map 全部数据集
     */
    private Map<Integer, List<Object>> allPageData() {
        return currentPageData();
    }


    /**
     * 获取当前页数据
     *
     * @return map 当前页数据集
     */
    private Map<Integer, List<Object>> currentPageData() {
        // 获取单元格数据
        Map<Integer, List<Object>> map = null;
        if (this.dataGrid != null) {
            map = new LinkedHashMap<>();
            GridDataHookUp dataHookUp = this.dataGrid.getDataHookup();
            GridModel model = dataHookUp.getModel();
            // 获取查询到的数据行
            int rows = model.getRows().size();
            // 获取查询到的数据列
            List<DataConsumer.Column> cols = model.getColumns();
            // 表头标题
            List<Object> header = new ArrayList<>();
            // 遍历行
            for (int i = 0; i < rows; i++) {
                List<Object> cellData = new ArrayList<>();
                // 遍历列
                for (int j = 0; j < cols.size(); j++) {
                    // 数据模型行下标
                    ModelIndex rowIndex = ModelIndex.forRow(model, i);
                    // 数据模型列下标
                    ModelIndex colIndex = ModelIndex.forColumn(model, j);
                    // 数据项
                    Object obj = model.getValueAt(rowIndex, colIndex);
                    cellData.add(obj);
                    // 根据第一行的列下标获取表头标题信息
                    if (i == 0) {
                        String colName = cols.get(j).getName();
                        header.add(colName);
                    }
                }
                // 填充数据，第二行开始填充数据
                map.put(i + 1, cellData);
            }
            if (rows > 0) {
                // 填充标题
                map.put(0, header);
            }
        }
        return map;
    }

    /**
     * 获取数据表名称
     *
     * @return string 数据表名
     */
    private String getTableName() {
        // 获取操作的数据表表名
        String tableName = UUID.randomUUID().toString().replaceAll("-", "");
        if (this.dataGrid != null) {
            try {
                tableName = DataGridUtil.getDatabaseTable(this.dataGrid).getName();
            } catch (Exception ignore) {
            }
        }
        return tableName;
    }

    /**
     * 是否导出全部查询结果集事件监听
     * 导出全部数据，重新加载结果集表，以便导出时获取全部数据集
     */
    private void allPageDataCheckBoxListener() {
        this.allData.addActionListener(e -> {
            if (this.allData.isSelected()) {
                GridPagingModel<DataConsumer.Row, DataConsumer.Column> pageModel = this.dataGrid.getDataHookup().getPageModel();
                pageModel.setPageSize(-1);
                GridLoader<DataConsumer.Row, DataConsumer.Column> loader = this.dataGrid.getDataHookup().getLoader();
                GridRequestSource<DataConsumer.Row, DataConsumer.Column> source = GridRequestSource.create(this.dataGrid, (Object) null);
                loader.load(source, 0);
            }
        });
    }
}
