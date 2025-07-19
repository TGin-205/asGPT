package view;

import controller.MathController;

public class MathSolverMenu extends Menu {
    private final MathController controller;

    public MathSolverMenu() {
        super("=== CHƯƠNG TRÌNH GIẢI TOÁN VỚI MISTRAL - PHIÊN BẢN NÂNG CAP ===",
                new String[]{
                    // NHÓM 1: CHỨC NĂNG CHÍNH - Giải toán
                    " Giải bài toán cơ bản",
                    " Giải thích chi tiết", 
                    " Thêm câu hỏi thủ công",
                    
                    // NHÓM 2: QUẢN LÝ LỊCH SỬ
                    " Xem lịch sử",
                    " Tìm kiếm lịch sử", 
                    " Sắp xếp lịch sử",
                    " Lọc theo độ khó",
                    " Xóa câu hỏi",
                    
                    // NHÓM 3: DỮ LIỆU & THỐNG KÊ  
                    " Xem thống kê",
                    " Lưu lịch sử",
                    " Tải lịch sử",
                    
                    // NHÓM 4: HỆ THỐNG & HỖ TRỢ
                    " Kiểm tra hệ thống",
                    " Xóa cache",
                    " Hướng dẫn",
                    " Thoát"
                });
        controller = new MathController();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            // CHỨC NĂNG CHÍNH - Giải toán
            case 1 -> controller.solveProblem();
            case 2 -> controller.explainProblem();
            case 3 -> controller.addCustomQuestion();
            
            // QUẢN LÝ LỊCH SỬ
            case 4 -> controller.showHistory();
            case 5 -> controller.searchHistory();
            case 6 -> controller.sortHistory();
            case 7 -> controller.filterByDifficulty();
            case 8 -> controller.deleteQuestion();
            
            // DỮ LIỆU & THỐNG KÊ
            case 9 -> controller.showStatistics();
            case 10 -> controller.saveHistory();
            case 11 -> controller.loadHistory();
            
            // HỆ THỐNG & HỖ TRỢ
            case 12 -> controller.checkSystem();
            case 13 -> controller.clearCache();
            case 14 -> controller.showHelp();
            case 15 -> {
                System.out.println("Tạm biệt!");
                System.exit(0);
            }
            
            default -> System.out.println("Lựa chọn không hợp lệ!");
        }
    }
}