package view;

import controller.MathController;

public class MathSolverMenu extends Menu {

    private MathController controller;

    public MathSolverMenu() {
        super("=== CHƯƠNG TRÌNH GIẢI TOÁN VỚI MISTRAL - PHIÊN BẢN NÂNG CAP ===",
                new String[]{
                    " Giải bài toán cơ bản",
                    " Giải thích chi tiết",
                    " Tạo bài tương tự", 
                    " Đánh giá độ khó",
                    " Xem lịch sử",
                    " Tìm kiếm lịch sử",
                    " Xem thống kê",
                    " Lưu lịch sử",
                    " Tải lịch sử",
                    " Xóa cache",
                    " Kiểm tra hệ thống",
                    " Hướng dẫn",
                    " Thoát"
                });
        controller = new MathController();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1 -> controller.solveProblem();
            case 2 -> controller.explainProblem();
            case 3 -> controller.generateSimilarProblems();
            case 4 -> controller.assessDifficulty();
            case 5 -> controller.showHistory();
            case 6 -> controller.searchHistory();
            case 7 -> controller.showStatistics();
            case 8 -> controller.saveHistory();
            case 9 -> controller.loadHistory();
            case 10 -> controller.clearCache();
            case 11 -> controller.checkSystem();
            case 12 -> controller.showHelp();
            case 13 -> {
                System.out.println("Tạm biệt!");
                System.exit(0);
            }
            default -> System.out.println("Lựa chọn không hợp lệ!");
        }
    }
}