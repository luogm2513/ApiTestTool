package com.nazir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.apache.commons.lang.StringUtils;

import com.utils.HttpClientUtils;
import com.utils.JsonUtils;
import com.utils.UrlDTO;
import com.utils.UrlUtil;

public class ApiTest extends JFrame {
    private static final long serialVersionUID = 1L;
    static String version = "1.0";
    static String host;
    static String auth;

    public ApiTest(String host, String loginName, String auth, String version, String userAgent) {
        setBackground(Color.WHITE);
        ApiTest.version = version;
        ApiTest.host = host;
        ApiTest.auth = auth;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);

            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;

            setSize(920, 700); // 设定Frame大小
            setLocation(screenWidth * 1 / 8, screenHeight * 1 / 15);

            getContentPane().setLayout(null);

            JLabel label = new JLabel("版本类型");
            label.setBounds(320, 10, 60, 25);
            getContentPane().add(label);

            final JComboBox comboBox = new JComboBox();
            comboBox.setBounds(395, 10, 157, 25);
            comboBox.addItem("android");
            comboBox.addItem("ios");
            getContentPane().add(comboBox);
            if (StringUtils.isBlank(auth)) {
                setTitle("ApiTest (未登录)");
            } else {
                if (userAgent.equals("android")) {
                    setTitle("ApiTest (已登录安卓版:" + loginName + ")");
                    comboBox.setSelectedItem("android");
                } else if (userAgent.equals("ios")) {
                    setTitle("ApiTest (已登录IOS版)" + loginName + ")");
                    comboBox.setSelectedItem("ios");
                }
            }

            JLabel label_1 = new JLabel("版本号");
            label_1.setBounds(620, 10, 50, 25);
            getContentPane().add(label_1);

            final JTextField versionField = new JTextField(ApiTest.version);
            versionField.setColumns(10);
            versionField.setBounds(670, 10, 125, 25);
            getContentPane().add(versionField);

            final JComboBox restBox = new JComboBox();
            restBox.setBounds(320, 40, 60, 25);
            restBox.addItem("GET");
            restBox.addItem("POST");
            restBox.addItem("PUT");
            restBox.addItem("DELETE");
            getContentPane().add(restBox);

            final JTextField urlField = new JTextField("http://" + ApiTest.host);
            urlField.setBounds(395, 40, 400, 25);
            getContentPane().add(urlField);
            urlField.setColumns(10);

            JLabel lblRequestbody = new JLabel("requestParam");
            lblRequestbody.setBounds(320, 65, 90, 25);
            getContentPane().add(lblRequestbody);

            JPanel requestPanel = new JPanel();
            requestPanel.setBounds(320, 90, 575, 140);
            requestPanel.setLayout(new BorderLayout());
            getContentPane().add(requestPanel);
            final JTextArea requestArea = new JTextArea(3, 50);
            requestPanel.add(new JScrollPane(requestArea));

            JButton btnLogin = new JButton("login");
            btnLogin.setBounds(815, 5, 80, 30);
            getContentPane().add(btnLogin);

            JButton btnLogout = new JButton("logout");
            btnLogout.setBounds(815, 40, 80, 30);
            getContentPane().add(btnLogout);

            JButton btnSave = new JButton("save");
            btnSave.setBounds(520, 235, 80, 30);
            getContentPane().add(btnSave);

            JButton btnSend = new JButton("send");
            btnSend.setBounds(420, 235, 80, 30);
            getContentPane().add(btnSend);

            JPanel treePanel = new JPanel();
            treePanel.setBounds(10, 10, 300, 640);
            treePanel.setLayout(new BorderLayout());
            getContentPane().add(treePanel);

            JLabel lblResponse = new JLabel("responseBody");
            lblResponse.setBounds(320, 240, 90, 25);
            getContentPane().add(lblResponse);

            JPanel responsePanel = new JPanel();
            responsePanel.setBounds(320, 270, 575, 375);
            responsePanel.setLayout(new BorderLayout());
            getContentPane().add(responsePanel);
            final JTextArea responseArea = new JTextArea(13, 50);
            responseArea.setEditable(false);
            responsePanel.add(new JScrollPane(responseArea));

            JToolBar toolBar = new JToolBar();
            responsePanel.add(toolBar, BorderLayout.NORTH);
            JButton btnClear = new JButton("clear");
            toolBar.add(btnClear);

            ActionListener loginListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ApiTest.this.setVisible(false);
                    Login login = new Login();
                    login.setDefaultCloseOperation(3);
                    login.setVisible(true);
                }
            };
            btnLogin.addActionListener(loginListener);

            ActionListener logoutListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(requestArea, "登出成功");
                    ApiTest.this.setVisible(false);
                    ApiTest apiTest = new ApiTest(ApiTest.host, null, null, UrlUtil.getInstance().getUrlVersion(),
                            null);
                    apiTest.setDefaultCloseOperation(3);
                    apiTest.setVisible(true);
                }
            };
            btnLogout.addActionListener(logoutListener);

            ActionListener sendListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String returnjson = "";
                    if (restBox.getSelectedItem().toString().equals("GET")) {
                        returnjson = HttpClientUtils.sendGet(urlField.getText(), requestArea.getText(),
                                comboBox.getSelectedItem().toString(), versionField.getText(), ApiTest.auth);
                    } else if (restBox.getSelectedItem().toString().equals("POST")) {
                        returnjson = HttpClientUtils.sendPost(urlField.getText(), requestArea.getText(),
                                comboBox.getSelectedItem().toString(), versionField.getText(), ApiTest.auth);
                    } else if (restBox.getSelectedItem().toString().equals("PUT")) {
                        returnjson = HttpClientUtils.sendPut(urlField.getText(), requestArea.getText(),
                                comboBox.getSelectedItem().toString(), versionField.getText(), ApiTest.auth);
                    } else if (restBox.getSelectedItem().toString().equals("DELETE")) {
                        returnjson = HttpClientUtils.sendDelete(urlField.getText(), requestArea.getText(),
                                comboBox.getSelectedItem().toString(), versionField.getText(), ApiTest.auth);
                    }
                    responseArea.setText(JsonUtils.toJSONFormatter(JsonUtils.readValue(returnjson, Map.class), true));
                }
            };
            btnSend.addActionListener(sendListener);

            DefaultMutableTreeNode top = new DefaultMutableTreeNode(UrlUtil.getInstance().getUrlVersion() + "接口");
            List<String> urllist = UrlUtil.getInstance().listUrl();
            for (String url : urllist) {
                MutableTreeNode newChild = new DefaultMutableTreeNode(url);
                top.add(newChild);
            }
            final JTree tree = new JTree(top);
            treePanel.add(new JScrollPane(tree));
            TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent evt) {
                    MutableTreeNode node = (MutableTreeNode) evt.getPath().getLastPathComponent();
                    if (node.isLeaf()) {
                        UrlDTO dto = UrlUtil.getInstance().getUrlDTO(node.toString());
                        urlField.setText("http://" + ApiTest.host + dto.getUrl());
                        requestArea.setText(
                                JsonUtils.toJSONFormatter(JsonUtils.readValue(dto.getParam(), Map.class), true));
                        responseArea.setText("");
                        restBox.setSelectedItem(dto.getRestMethod());
                    }
                }
            };
            tree.addTreeSelectionListener(treeSelectionListener);

            ActionListener saveListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String url = urlField.getText().replace("http://" + ApiTest.host, "");
                    String params = requestArea.getText();
                    UrlUtil.getInstance().saveParams(url,
                            JsonUtils.toJSONFormatter(JsonUtils.readValue(params, Map.class), false));
                    JOptionPane.showMessageDialog(requestArea, "保存成功！");
                }
            };
            btnSave.addActionListener(saveListener);

            ActionListener clearListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    responseArea.setText("");
                }
            };
            btnClear.addActionListener(clearListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}