package com.lightbc.databasej.ui;

import com.lightbc.databasej.util.DialogUtil;
import com.lightbc.databasej.util.KeyboardShortcutUtil;
import lombok.Data;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 快捷键配置UI界面
 */
@Data
public class KeyboardShortcutUI {
    // 快捷键名称
    private JTextField ksName;
    // 快捷键
    private JTextField ks;
    // 添加的快捷键列表
    private JComboBox addKs;
    // 提示信息
    private JLabel tips;
    private JPanel mainPanel;
    // 取消按钮
    private JButton cancel;
    // 确定按钮
    private JButton ok;
    // 隐藏域
    private JTextField hidden;
    private DialogUtil.CustomDialog customDialog;
    // 三方应用名称
    private String appName;
    // 配置的快捷键
    private Map<Integer, String> keys;

    public KeyboardShortcutUI() {
        init();
    }

    public KeyboardShortcutUI(String appName, DialogUtil.CustomDialog customDialog) {
        this.appName = appName;
        this.customDialog = customDialog;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        initDisplay();
        operate();
    }

    /**
     * 初始化显示/隐藏
     */
    private void initDisplay() {
        hidden.setVisible(false);
    }

    /**
     * 操作
     */
    private void operate() {
        loadAddKeyboardShortcut();
        addKsKeyboardListener();
        ok();
        cancel();
    }

    /**
     * 加载新增的快捷键列表
     */
    private void loadAddKeyboardShortcut() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        List<String> elements = KeyboardShortcutUtil.getKeyNames(appName);
        if (elements != null) {
            for (String element : elements) {
                model.addElement(element);
            }
        }
        this.addKs.setModel(model);
        addKsListener();
    }

    /**
     * 添加快捷键组件监听
     */
    private void addKsListener() {
        initShow();
        this.addKs.addActionListener(e -> initShow());
    }

    /**
     * 添加键盘监听
     */
    private void addKsKeyboardListener() {
        keys = new LinkedHashMap<>();
        this.ks.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int code = e.getKeyCode();
                // 根据KeyCode获取键值字符串
                String kt = KeyEvent.getKeyText(code);
                keys.put(code, kt);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String sk = "";
                String k = "";
                if (keys != null) {
                    for (Integer key : keys.keySet()) {
                        // 拼接显示，格式:x + x + x
                        sk += keys.get(key) + " + ";
                        // 拼接，格式：x,x,x
                        k += key + ",";
                    }
                }
                if (!"".equals(sk.trim())) {
                    // 去除拼接后的多余后缀
                    sk = sk.substring(0, sk.length() - 3);
                    ks.setText("");
                    ks.setText(sk);
                }
                if (!"".equals(k.trim())) {
                    // 去除拼接后的多余后缀
                    k = k.substring(0, k.length() - 1);
                    hidden.setText("");
                    hidden.setText(k);
                }
                keys = new LinkedHashMap<>();
            }
        });
    }

    /**
     * 初始化显示
     */
    private void initShow() {
        String keyName = addKs.getSelectedItem().toString();
        if (!"".equals(keyName.trim())) {
            // 初始显示快捷键名称
            ksName.setText(keyName);
            String key = KeyboardShortcutUtil.getKey(appName, keyName);
            // 初始显示快捷键
            ks.setText(key);
            String defaultKey = KeyboardShortcutUtil.getDefaultKey(appName, keyName);
            // 初始化隐藏域的值
            hidden.setText(defaultKey);
        }
    }

    /**
     * 确认按钮
     */
    private void ok() {
        ok.addActionListener(e -> {
            String kn = ksName.getText();
            if ("".equals(kn.trim())) {
                DialogUtil.showTips(null, "快捷键名称不能为空！");
                return;
            }
            String value = hidden.getText().trim();
            // 保存配置的快捷键
            KeyboardShortcutUtil.save(appName, kn, value);
            // 关闭对话框
            customDialog.dispose();
        });
    }

    /**
     * 关闭对话框
     */
    private void cancel() {
        cancel.addActionListener(e -> customDialog.dispose());
    }
}
