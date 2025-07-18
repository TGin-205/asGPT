package main;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.MathSolverMenu;
import view.MathSolverGUI;

public class MathSolverApp {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.out.println("🚀 Khởi động chương trình...");

        // Hỏi user muốn chạy chế độ nào
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chọn chế độ chạy:");
        System.out.println("1. Giao diện đồ họa (GUI)");
        System.out.println("2. Giao diện dòng lệnh (Console)");
        System.out.print("Nhập lựa chọn (1 hoặc 2): ");

        try {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> // Chạy GUI
                    SwingUtilities.invokeLater(() -> {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        new MathSolverGUI().setVisible(true);
                    });
                case 2 -> {
                    // Chạy Console menu
                    MathSolverMenu menu = new MathSolverMenu();
                    menu.run();
                }
                default -> {
                    System.out.println("Lựa chọn không hợp lệ! Chạy GUI mặc định...");
                    SwingUtilities.invokeLater(() -> {
                        new MathSolverGUI().setVisible(true);
                    });
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu! Chạy GUI mặc định...");
            SwingUtilities.invokeLater(() -> {
                new MathSolverGUI().setVisible(true);
            });
        }
    }
}
