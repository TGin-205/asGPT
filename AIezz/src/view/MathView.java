package view;

import java.util.List;
import java.util.Scanner;

public class MathView {

    private Scanner scanner;

    public MathView() {
        scanner = new Scanner(System.in);
    }

    public String inputProblem() {
        System.out.print("Nhập bài toán: ");
        return scanner.nextLine().trim();
    }

    public String inputFilename(String message) {
        System.out.print(message + ": ");
        return scanner.nextLine().trim();
    }

    public void showResult(String result) {
        System.out.println("\n📝 Kết quả:");
        System.out.println(result);
        System.out.println();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String error) {
        System.err.println(error);
    }

    public void showHistory(List<String> history) {
        System.out.println("\n📚 Lịch sử bài toán:");
        if (history.isEmpty()) {
            System.out.println("Chưa có bài toán nào.");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
        System.out.println();
    }

    public void showHelp() {
        System.out.println("\n=== Hướng dẫn sử dụng ===");
        System.out.println("Các loại toán có thể giải:");
        System.out.println("• Tính toán: 2 + 3 * 4");
        System.out.println("• Phương trình: x^2 + 5x + 6 = 0");
        System.out.println("• Hình học: Tính diện tích hình tròn bán kính 5");
        System.out.println("• Đại số: Rút gọn (x+1)(x+2)");
        System.out.println("• Bài toán ứng dụng");
        System.out.println("\nVí dụ:");
        System.out.println("'Giải phương trình x^2 - 5x + 6 = 0'");
        System.out.println("'Tính chu vi hình vuông cạnh 4'");
        System.out.println("'15 + 25 * 2 = ?'");
        System.out.println();
    }
}
