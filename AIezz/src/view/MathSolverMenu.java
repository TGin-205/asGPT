package view;

import controller.MathController;

public class MathSolverMenu extends Menu {

    private final MathController controller;

    public MathSolverMenu() {
        super("=== CHƯƠNG TRÌNH GIẢI TOÁN VỚI MISTRAL - PHIÊN BẢN NÂNG CAP ===",
                new String[]{
                    " Giải bài toán cơ bản",
                    " Giải thích chi tiết",
                    " Xem lịch sử",
                    " Tìm kiếm lịch sử",
                    " Xem thống kê",
                    " Lưu lịch sử",
                    " Tải lịch sử",
                    " Xóa cache",
                    " Kiểm tra hệ thống",
                    " Thêm câu hỏi thủ công",
                    " Xóa câu hỏi",
                    " Hướng dẫn",
                    " Thoát"
                });
        controller = new MathController();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1 ->
                controller.solveProblem();
            case 2 ->
                controller.explainProblem();
            case 3 ->
                controller.showHistory();
            case 4 ->
                controller.searchHistory();
            case 5 ->
                controller.showStatistics();
            case 6 ->
                controller.saveHistory();
            case 7 ->
                controller.loadHistory();
            case 8 ->
                controller.clearCache();
            case 9 ->
                controller.checkSystem();
            case 10 ->
                controller.addCustomQuestion();
            case 11 ->
                controller.deleteQuestion();
            case 12 ->
                controller.showHelp();
            case 13 -> {
                System.out.println("Tạm biệt!");
                System.exit(0);
            }
            default ->
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }
}
