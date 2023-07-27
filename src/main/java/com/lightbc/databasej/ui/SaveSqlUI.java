package com.lightbc.databasej.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.lightbc.databasej.entity.SaveSql;
import com.lightbc.databasej.service.DataBaseJPersistentService;
import com.lightbc.databasej.util.DialogUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SaveSqlUI implements Configurable {
    @Getter
    private JPanel mainPanel;
    private JTextField nameInput;
    private JTextArea saveContent;
    private JTextField descriptionInfo;
    private JComboBox nameSelector;
    private JPanel nameSelectorPanel;
    private JPanel nameInputPanel;
    private DataBaseJPersistentService persistentService;
    private String selectName;
    private boolean delSql;

    /**
     * 创建使用/删除SQL UI 界面
     */
    public SaveSqlUI() {
        this.persistentService = DataBaseJPersistentService.getInstance();
        loadUseSaveSql();
    }

    /**
     * 创建保存SQL UI 界面
     *
     * @param saveContent 需要保存的内容
     */
    public SaveSqlUI(String saveContent) {
        this.persistentService = DataBaseJPersistentService.getInstance();
        this.saveContent.setText(saveContent);
        loadSaveSql();
    }

    /**
     * 加载保存界面
     */
    private void loadSaveSql() {
        showSavePanel();
    }

    /**
     * 加载使用界面
     */
    private void loadUseSaveSql() {
        showUsePanel();
        initNameSelector();
        loadSaveContent();
        loadDescriptionInfo();
    }

    /**
     * 显示保存SQL的面板
     */
    private void showSavePanel() {
        this.nameSelectorPanel.setVisible(false);
    }

    /**
     * 显示使用保存的SQL的面板
     */
    private void showUsePanel() {
        this.nameInputPanel.setVisible(false);
    }

    /**
     * 初始话名称选择器
     */
    private void initNameSelector() {
        DefaultComboBoxModel model = getNameSelectorModel();
        if (model != null) {
            this.nameSelector.setModel(model);
            Object selectObj = this.nameSelector.getSelectedItem();
            if (selectObj != null) {
                this.selectName = selectObj.toString();
            }
            nameSelectorListener();
        } else {
            DialogUtil.showTips(this.mainPanel, "名称选择器加载失败！");
        }
    }

    /**
     * 加载保存的内容
     */
    private void loadSaveContent() {
        // 保存内容不可编辑
        this.saveContent.setEditable(false);
        if (StringUtils.isNotBlank(this.selectName)) {
            String content = getContent(this.selectName);
            this.saveContent.setText(content);
        }
    }

    /**
     * 加载描述信息
     */
    private void loadDescriptionInfo() {
        // 描述信息不可编辑
        this.descriptionInfo.setEditable(false);
        if (StringUtils.isNotBlank(this.selectName)) {
            String descriptionInfo = getDescriptionInfo(this.selectName);
            this.descriptionInfo.setText(descriptionInfo);
        }
    }

    /**
     * 名称选择器事件监听
     */
    private void nameSelectorListener() {
        this.nameSelector.addActionListener(e -> {
            Object selectObj = this.nameSelector.getSelectedItem();
            if (selectObj != null) {
                this.selectName = selectObj.toString();
                loadSaveContent();
                loadDescriptionInfo();
            }
        });
    }

    /**
     * 获取名称选择的数据模型
     *
     * @return DefaultComboBoxModel
     */
    private DefaultComboBoxModel getNameSelectorModel() {
        DefaultComboBoxModel model = null;
        List<SaveSql> items = getSaveSqlList();
        if (items != null && items.size() > 0) {
            List<String> names = getNames(items);
            if (names != null && names.size() > 0) {
                model = new DefaultComboBoxModel();
                for (int i = 0; i < names.size(); i++) {
                    model.addElement(names.get(i));
                }
            }
        }
        return model;
    }

    /**
     * 获取所有名称项
     *
     * @param list 保存信息
     * @return list
     */
    private List<String> getNames(List<SaveSql> list) {
        List<String> resultList = null;
        if (list != null && list.size() > 0) {
            resultList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                SaveSql saveSql = list.get(i);
                resultList.add(saveSql.getName());
            }
        }
        return resultList;
    }

    /**
     * 获取保存内容
     *
     * @param name 名称
     * @return string
     */
    private String getContent(String name) {
        return get(name, 0);
    }

    /**
     * 获取保存信息描述内容
     *
     * @param name 名称
     * @return string
     */
    private String getDescriptionInfo(String name) {
        return get(name, 1);
    }

    /**
     * 获取指定内容
     *
     * @param name 名称
     * @param type 类型，0：保存内容，1：描述信息
     * @return
     */
    private String get(String name, int type) {
        if (StringUtils.isNotBlank(name)) {
            List<SaveSql> list = getSaveSqlList();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    SaveSql saveSql = list.get(i);
                    if (saveSql.getName().equals(name)) {
                        if (type == 0) {
                            return saveSql.getContent();
                        } else if (type == 1) {
                            return saveSql.getDescriptionInfo();
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取保存的SQL信息
     *
     * @return list
     */
    private List<SaveSql> getSaveSqlList() {
        return this.persistentService.getSaveSqlList();
    }

    /**
     * 判断输入的名称是否已存在
     *
     * @return Boolean true-存在，false-不存在
     */
    private boolean existSaveSqlName() {
        boolean flag = false;
        try {
            List<SaveSql> saveList = this.persistentService.getSaveSqlList();
            List<String> nameList = getNames(saveList);
            String inputName = this.nameInput.getText();
            for (int i = 0; i < nameList.size(); i++) {
                String existName = nameList.get(i);
                if (inputName.trim().equals(existName)) {
                    flag = true;
                    break;
                }
            }
        } catch (Exception e) {
        } finally {
            return flag;
        }
    }

    /**
     * 保存操作
     */
    public void doSave() {
        try {
            apply();
        } catch (ConfigurationException e) {
            DialogUtil.showTips(this.mainPanel, "信息保存失败！");
        }
    }

    /**
     * 获取保存的sql内容
     *
     * @return string 指定保存内容
     */
    public String getSql() {
        return this.saveContent.getText();
    }

    /**
     * 保存SQL
     */
    private void doSaveSql() {
        boolean b = doSaveSqlValid();
        if (b) {
            List<SaveSql> list = this.persistentService.getSaveSqlList();
            SaveSql saveSql = new SaveSql();
            String name = this.nameInput.getText().trim();
            saveSql.setName(name);
            String content = this.saveContent.getText().trim();
            saveSql.setContent(content);
            String descriptionInfo = this.descriptionInfo.getText().trim();
            saveSql.setDescriptionInfo(descriptionInfo);
            list.add(saveSql);
            DialogUtil.showTips(this.mainPanel, "保存成功！");
        }
    }

    /**
     * 保存字段校验
     *
     * @return boolean true-验证成功，false-验证失败
     */
    private boolean doSaveSqlValid() {
        if (StringUtils.isBlank(this.nameInput.getText())) {
            DialogUtil.showTips(this.mainPanel, "名称不能为空！");
            return false;
        }
        if (existSaveSqlName()) {
            DialogUtil.showTips(this.mainPanel, "该名称已存在！请重新输入新名称！");
            return false;
        }
        if (StringUtils.isBlank(this.saveContent.getText())) {
            DialogUtil.showTips(this.mainPanel, "保存内容不能为空！");
            return false;
        }
        return true;
    }

    /**
     * 删除信息
     */
    public void doDelSql() {
        this.delSql = true;
        try {
            apply();
            DialogUtil.showTips(this.mainPanel, "删除成功！");
        } catch (ConfigurationException e) {
            DialogUtil.showTips(this.mainPanel, "信息删除错误：" + e.getMessage());
        }
    }

    /**
     * 删除指定的保存信息
     */
    private void delSql() {
        List<SaveSql> sqlList = this.persistentService.getSaveSqlList();
        if (sqlList != null && sqlList.size() > 0) {
            for (int i = 0; i < sqlList.size(); i++) {
                SaveSql saveSql = sqlList.get(i);
                if (saveSql.getName().equals(this.selectName)) {
                    sqlList.remove(i);
                    return;
                }
            }
        }
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return this.mainPanel;
    }

    @Override
    public boolean isModified() {
        return existSaveSqlName();
    }

    @Override
    public void apply() throws ConfigurationException {
        if (this.delSql) {
            delSql();
        } else {
            doSaveSql();
        }
    }
}
