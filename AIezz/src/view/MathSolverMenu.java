package view;

import controller.MathController;

public class MathSolverMenu extends Menu {

    private MathController controller;

    public MathSolverMenu() {
        super("=== CHƯƠNG TRÌNH GIẢI TOÁN VỚI MISTRAL ===",
                new String[]{
                    "Giải bài toán",
                    "Xem lịch sử",
                    "Lưu lịch sử",
                    "Tải lịch sử",
                    "Kiểm tra hệ thống",
                    "Hướng dẫn",
                    "Thoát"
                });
        controller = new MathController();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1:
                controller.solveProblem();
                break;
            case 2:
                controller.showHistory();
                break;
            case 3:
                controller.saveHistory();
                break;
            case 4:
                controller.loadHistory();
                break;
            case 5:
                controller.checkSystem();
                break;
            case 6:
                controller.showHelp();
                break;
            case 7:
                System.out.println("Tạm biệt!");
                System.exit(0);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }


  }
