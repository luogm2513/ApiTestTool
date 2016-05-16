package com.nazir;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonObject;
import com.utils.HttpClientUtils;
import com.utils.JsonUtils;
import com.utils.Md5Utils;
import com.utils.UrlUtil;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String LOGIN_URI = "/account/login.json";

    public Login() {
        setBackground(Color.WHITE);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);

            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;
            setSize(350, 300);
            setLocation(screenWidth * 3 / 8, screenHeight * 1 / 4);
            setTitle("ApiTest");

            JPanel jpanel = new JPanel();
            jpanel.setLayout(null);

            JLabel label1 = new JLabel("欢迎登录ApiTest", SwingConstants.CENTER);
            label1.setBounds(0, 0, 332, 30);
            jpanel.add(label1);
            JLabel label2 = new JLabel("版本类型：");
            label2.setBounds(50, 35, 75, 25);
            jpanel.add(label2);
            final JComboBox comboBox = new JComboBox();
            comboBox.setBounds(135, 35, 150, 25);
            jpanel.add(comboBox);
            comboBox.addItem("android");
            comboBox.addItem("ios");

            JLabel label3 = new JLabel("版本号：");
            label3.setBounds(50, 70, 75, 25);
            jpanel.add(label3);
            final JTextField versionField = new JTextField(UrlUtil.getInstance().getUrlVersion(), 20);
            versionField.setBounds(135, 70, 150, 25);
            jpanel.add(versionField);
            JLabel label4 = new JLabel("域名：");
            label4.setBounds(50, 105, 75, 25);
            jpanel.add(label4);
            final JTextField hostAndPort = new JTextField("localhost:8080", 20);
            hostAndPort.setBounds(135, 105, 150, 25);
            jpanel.add(hostAndPort);
            JLabel label5 = new JLabel("登录名：");
            label5.setBounds(50, 140, 75, 25);
            jpanel.add(label5);
            final JTextField account = new JTextField(20);
            account.setBounds(135, 140, 150, 25);
            jpanel.add(account);
            JLabel label6 = new JLabel("密码：");
            label6.setBounds(50, 175, 75, 25);
            jpanel.add(label6);
            final JPasswordField password = new JPasswordField(20);
            password.setBounds(135, 175, 150, 25);
            jpanel.add(password);

            ActionListener loginListener = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    String loginName = "";
                    String auth = "";
                    String version = versionField.getText();
                    String host = hostAndPort.getText();
                    if (StringUtils.isBlank(versionField.getText())) {
                        JOptionPane.showMessageDialog(versionField, "版本号为空！");
                        return;
                    }
                    if (StringUtils.isBlank(hostAndPort.getText())) {
                        JOptionPane.showMessageDialog(hostAndPort, "域名为空！");
                        return;
                    }
                    if (StringUtils.isBlank(account.getText())) {
                        JOptionPane.showMessageDialog(account, "登录名为空！");
                        return;
                    }
                    if (password.getPassword() == null || password.getPassword().length == 0) {
                        JOptionPane.showMessageDialog(password, "密码为空！");
                        return;
                    }
                    try {
                        String url = "http://" + host + LOGIN_URI;
                        JsonObject requestJson = new JsonObject();
                        requestJson.addProperty("loginId", account.getText());
                        requestJson.addProperty("password", Md5Utils.getMD5(String.valueOf(password.getPassword())));
                        requestJson.addProperty("deviceId", "apiTestTool");

                        String returnjson = HttpClientUtils.sendPost(url, requestJson.toString(),
                                comboBox.getSelectedItem().toString(), version, auth);
                        JsonObject responseJson = JsonUtils.fromJson(returnjson, JsonObject.class);
                        if (responseJson.get("flag") != null && responseJson.get("flag").toString().equals("0")) {
                            auth = responseJson.get("authentication").toString();
                            loginName = responseJson.get("userName").toString();
                        } else {
                            JOptionPane.showMessageDialog(account, responseJson.get("message").toString());
                            return;
                        }
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Login.this.setVisible(false);
                    ApiTest apiTest = new ApiTest(host, loginName, auth, version,
                            comboBox.getSelectedItem().toString());
                    apiTest.setDefaultCloseOperation(3);
                    apiTest.setVisible(true);
                }
            };
            getContentPane().add(jpanel);

            JButton loginBtn = new JButton("登录");
            loginBtn.setBounds(122, 210, 80, 30);
            jpanel.add(loginBtn);
            loginBtn.addActionListener(loginListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class WinCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
