package view;

import java.util.List;
import java.util.Scanner;
import model.Question;

public class MathView {

    private final Scanner scanner;

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

    public void showHistory(List<Question> history) {
        System.out.println("\n📚 Lịch sử bài toán:");
        if (history.isEmpty()) {
            System.out.println("Chưa có bài toán nào.");
            return;
        }

        for (Question q : history) {
            System.out.println("ID: " + q.getId());
            System.out.println("Câu hỏi: " + q.getQuestion());
            System.out.println("Đáp án: " + q.getAnswerS());
            System.out.println("Độ khó: " + q.getDifficulty());
            System.out.println("-------------------");
        }
    }

    public String inputCustomQuestion() {
        System.out.print("Nhập câu hỏi: ");
        return scanner.nextLine().trim();
    }

    public String inputCustomAnswer() {
        System.out.print("Nhập đáp án: ");
        return scanner.nextLine().trim();
    }

    public String inputCustomDifficulty() {
        while (true) {
            System.out.print("Nhập độ khó (Easy/Medium/Hard): ");
            String difficulty = scanner.nextLine().trim();
            if (difficulty.equalsIgnoreCase("Easy")
                    || difficulty.equalsIgnoreCase("Medium")
                    || difficulty.equalsIgnoreCase("Hard")) {
                return difficulty;
            }
            System.out.println("❌ Độ khó không hợp lệ! Vui lòng nhập: Easy, Medium hoặc Hard");
        }
    }

    public String[] inputSearch() {
        System.out.println("\nTìm kiếm theo:");
        System.out.println("1. Tất cả");
        System.out.println("2. Câu hỏi");
        System.out.println("3. Đáp án");
        System.out.println("4. Độ khó");
        System.out.print("Chọn loại tìm kiếm (1-4): ");

        String type = scanner.nextLine().trim();
        System.out.print("Nhập từ khóa tìm kiếm: ");
        String keyword = scanner.nextLine().trim();

        return new String[]{type, keyword};
    }

    public String inputQuestionId() {
        System.out.print("Nhập ID câu hỏi (vd: Q1): ");
        return scanner.nextLine().trim();
    }

    public boolean confirmDelete() {
        System.out.print("Bạn có chắc muốn xóa câu hỏi này? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }

    public void showQuestion(Question question) {
        if (question == null) {
            System.out.println("Không tìm thấy câu hỏi!");
            return;
        }
        System.out.println("\n=== Thông tin câu hỏi ===");
        System.out.println("ID: " + question.getId());
        System.out.println("Câu hỏi: " + question.getQuestion());
        System.out.println("Đáp án: " + question.getAnswerS());
        System.out.println("Độ khó: " + question.getDifficulty());
        System.out.println("========================");
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
