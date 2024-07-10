package com.xuxueli;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import com.xxl.job.admin.XxlJobAdminApplication;
import com.xxl.job.executor.*;

public class TrayFrame extends JFrame implements WindowListener {

    private TrayIcon trayIcon;

    public TrayFrame() {
        // 设置窗口属性
        setTitle("托盘示例");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 阻止默认的关闭操作

        // 添加关闭窗口监听器
        addWindowListener(this);

        // 创建托盘图标
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("path_to_your_tray_icon.png"); // 指定托盘图标的路径
            trayIcon = new TrayIcon(image, "托盘示例");

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
        JButton startButton = new JButton("启动");
        JButton stopButton = new JButton("停止");

        // 为按钮添加事件监听器（这里只是打印消息）
        startButton.addActionListener(e -> System.out.println("启动按钮被点击"));
        stopButton.addActionListener(e -> System.out.println("停止按钮被点击"));

        panel.add(startButton);
        panel.add(stopButton);

        add(panel);
    }

    private void startAdmin(){
        try
        {
            Method method = XxlJobAdminApplication.class.getMethod("main", String[].class);
            method.invoke(null, (Object) new String[] { idStr });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (trayIcon != null) {
            this.setVisible(false);
        }
    }

    // 实现WindowListener的其他方法（可选）
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrayFrame frame = new TrayFrame();
            frame.setVisible(true);
        });
    }
}