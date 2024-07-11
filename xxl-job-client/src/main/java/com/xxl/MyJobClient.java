package com.xxl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

public class MyJobClient extends JFrame implements WindowListener {
    private TrayIcon trayIcon;

    private JSONObject ReadConfig() {
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + File.separator + "config.json";
        org.json.JSONObject cfg = null;
        try (FileReader reader = new FileReader(filePath)) {
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            String jsonString = sb.toString();
            cfg = new JSONObject(jsonString);
        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
        }
        return cfg;
    }

    public MyJobClient() {
        // 设置窗口属性
        setTitle("MyJob");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(this);

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("path_to_your_tray_icon.png");
            trayIcon = new TrayIcon(image, "MyJob");

            // 添加托盘图标的动作监听器
            trayIcon.addActionListener(e -> {
                if (this.isVisible()) {
                    this.setVisible(false);
                } else {
                    this.setVisible(true);
                    this.toFront();
                }
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("托盘图标无法添加");
            }
        } else {
            System.out.println("系统托盘不支持");
        }

        // 创建面板和按钮
        JPanel panel = new JPanel();
        String noteText = """
                <html>
                点击[启动]按钮将启动xxl-job管理后台并启动一个执行器<br>
                点击[停止]按钮将关闭管理后台和执行器并退出<br>
                点击[x]将隐藏到系统托盘区 (双击恢复)<br><br>
                </html>
                """;
        JLabel note = new JLabel(noteText);
        panel.add(note);


        JPanel panel2 = new JPanel();
        JButton startButton = new JButton("启动");
        JButton stopButton = new JButton("停止");
        startButton.addActionListener(e -> startMyJob());
        stopButton.addActionListener(e -> System.exit(0));
        panel2.add(startButton);
        panel2.add(stopButton);

        add(panel, BorderLayout.NORTH);
        add(panel2, BorderLayout.SOUTH);
    }

    private void startMyJob() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        JSONObject runConfig = ReadConfig();
        executor.submit(() -> {
            String mainClassName = "com.xxl.job.admin.XxlJobAdminApplication";
            try {
                String[] amdCfg = new String[]{};
                System.setProperty("server.port", runConfig.getString("port"));
                System.setProperty("spring.datasource.url", runConfig.getString("url"));
                System.setProperty("spring.datasource.username", runConfig.getString("username"));
                System.setProperty("spring.datasource.password", runConfig.getString("password"));
                Class<?> clazz = Class.forName(mainClassName);
                Method mainMethod = clazz.getDeclaredMethod("main", String[].class);
                mainMethod.invoke(null, (Object) amdCfg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            String mainClassName = "com.xxl.job.executor.ExecutorApplication";
            try {
                Class<?> clazz = Class.forName(mainClassName);
                Method mainMethod = clazz.getDeclaredMethod("main", JSONObject.class);
                mainMethod.invoke(null, (Object) runConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (trayIcon != null) {
            this.setVisible(false);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyJobClient frame = new MyJobClient();
            frame.setVisible(true);
        });
    }
}