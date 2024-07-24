package com.xxl.job.admin;

import com.xxl.job.admin.taskExecutor.ExecutorApplication;
import com.xxl.job.admin.dbConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@SpringBootApplication
public class XxlJobAdminApplication {
    private static TrayIcon trayIcon;

    private static JSONObject ReadConfig() {
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

    public static void myJobClient() {
        createConfig();
        JFrame frame = new JFrame("MyJob");
        // 设置窗口属性
        frame.setTitle("MyJob");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (trayIcon != null) {
                    frame.setVisible(false);
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
        });

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage("path_to_your_tray_icon.png");
            trayIcon = new TrayIcon(image, "MyJob");

            // 添加托盘图标的动作监听器
            trayIcon.addActionListener(e -> {
                if (frame.isVisible()) {
                    frame.setVisible(false);
                } else {
                    frame.setVisible(true);
                    frame.toFront();
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
                  首次启动将在当前目录创建 "config.json" 和 "xxl_job.sql"<br>
                  修改 "config.json" 里数据库连接信息<br>
                  点击[初始化数据库]按钮,将在"config.json"定义的目标MySQL创建xxl-job数据库<br><br>
                  点击[启动]按钮将启动xxl-job管理后台并启动一个执行器<br>
                  点击[停止]按钮将关闭管理后台和执行器并退出<br>
                  点击[打开Web]按钮将打开xxl-job管理后台<br>
                  点击[x]将隐藏到系统托盘区 (双击恢复)<br><br>
                		
                  (启动后大约30秒内执行器注册到调度中心)<br><br>
                </html>
                """;
        JLabel note = new JLabel(noteText);
        panel.add(note);

        JPanel panel2 = new JPanel();
        JButton startButton = new JButton("启动");
        JButton stopButton = new JButton("停止");
        JButton startWeb = new JButton("打开Web");
        JButton initButton = new JButton("初始化数据库");
        JButton issueButton = new JButton("关于");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog(frame, "确定启动?")) {
                    startJob();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog(frame, "确定关闭?")) {
                    System.exit(0);
                }
            }
        });

        initButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog(frame, "确定执行?")) {
                    initDB();
                    showConfirmDialog(frame, "已完成");
                }
            }
        });
        startWeb.addActionListener(e -> openWeb(ReadConfig().getString("executorAddresses")));
        panel2.add(startButton);
        panel2.add(stopButton);
        panel2.add(startWeb);


        JPanel panel3 = new JPanel();
        issueButton.addActionListener(e -> openWeb("https://github.com/ts7ming/xxl-job-win"));

        panel3.add(initButton);
        panel3.add(issueButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);
        frame.add(panel3, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void startJob() {
        JSONObject runConfig = ReadConfig();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(() -> {
            try {
                ExecutorApplication.main(runConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.setProperty("server.port", runConfig.getString("serverPort"));
        System.setProperty("spring.datasource.url", runConfig.getString("mysqlUrl"));
        System.setProperty("spring.datasource.username", runConfig.getString("mysqlUserName"));
        System.setProperty("spring.datasource.password", runConfig.getString("mysqlPassword"));
        String[] args = new String[]{};
        SpringApplication.run(XxlJobAdminApplication.class, args);
    }


    private static void initDB() {
        JSONObject runConfig = ReadConfig();
        String url = runConfig.getString("mysqlUrl");
        String user = runConfig.getString("mysqlUserName");
        String password = runConfig.getString("mysqlPassword");

        String initUrl = url.replace("xxl_job?", "mysql?");

        // SQL文件路径
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + File.separator + "xxl_job.sql";
        // 加载MySQL JDBC驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 创建数据库
            try (Connection conn = DriverManager.getConnection(initUrl, user, password);
                 Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE database if NOT EXISTS `xxl_job` default character set utf8mb4 collate utf8mb4_unicode_ci;");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {
                try {
                    byte[] encoded = Files.readAllBytes(Paths.get(filePath));
                    String sqlText = new String(encoded, StandardCharsets.UTF_8); // 假设文件是UTF-8编码
                    String[] statements = sqlText.split(";");
                    for (String s : statements) {
                        if (!s.trim().equals("")) {
                            System.out.println(s);
                            System.out.println("===================================================");
                            stmt.execute(s);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createConfig() {
        File currentDir = new File(System.getProperty("user.dir"));
        File configFile = new File(currentDir, "config.json");
        File sqlFile = new File(currentDir, "xxl_job.sql");
        if (!configFile.exists()) {
            try {
                if (configFile.createNewFile()) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                        writer.write(dbConfig.getConfigJson());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!sqlFile.exists()) {
            try {
                if (sqlFile.createNewFile()) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFile))) {
                        writer.write(dbConfig.getDbSQL());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void openWeb(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    URL urlFixd = new URL(url);
                    desktop.browse(urlFixd.toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean showConfirmDialog(JFrame frame, String info) {
        int result = JOptionPane.showConfirmDialog(frame, // 父组件
                info,
                "确认", // 对话框标题
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        myJobClient();
    }


}