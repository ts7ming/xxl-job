package com.xuxueli;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("功能序号:");
        System.out.println("1. 检查状态");
        System.out.println("2. 启动");
        System.out.println("3. 停止");

        // 读取用户输入的序号
        System.out.print("请输入序号: ");
        int choice = scanner.nextInt();

        // 根据用户输入的序号执行相应过程
        switch (choice) {
            case 1 -> checkJobStatus();
            case 2 -> start();
            case 3 -> stop();
            default -> System.out.println("无效的输入");
        }
    }

    // 选项一的处理过程
    private static void checkJobStatus() {
        System.out.println("你选择了选项一。");
        runCmd("");
        // 这里可以添加更多的处理逻辑
    }

    // 选项二的处理过程
    private static void start() {
        System.out.println("你选择了选项二。");
        // 这里可以添加更多的处理逻辑
    }

    // 选项三的处理过程
    private static void stop() {
        System.out.println("你选择了选项三。");
        // 这里可以添加更多的处理逻辑
    }

    private static void runCmd(String cmd) {
        try {
            new ProcessBuilder("cmd", "/c", cmd).inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}