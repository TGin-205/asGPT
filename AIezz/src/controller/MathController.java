package controller;

import model.MathSolver;
import view.MathView;
import java.io.*;
import java.util.List;

public class MathController {

    private MathSolver model;
    private MathView view;

    public MathController() {
        model = new MathSolver();
        view = new MathView();
    }

    public void solveProblem() {
        try {
            String problem = view.inputProblem();
            if (problem == null || problem.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập bài toán!");
                return;
            }

            view.showMessage("🤖 Đang giải...");
            String result = model.solveMath(problem);
            view.showResult(result);

        } catch (Exception e) {
            view.showError("Lỗi: " + e.getMessage());
        }
    }

    public void showHistory() {
        List<String> history = model.getHistory();
        view.showHistory(history);
    }

    public void saveHistory() {
        try {
            String filename = view.inputFilename("Nhập tên file để lưu");
            if (filename == null || filename.trim().isEmpty()) {
                filename = "math_history.txt";
            }

            model.saveHistory(filename);
            view.showMessage("✅ Đã lưu lịch sử vào " + filename);

        } catch (IOException e) {
            view.showError("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void loadHistory() {
        try {
            String filename = view.inputFilename("Nhập tên file để đọc");
            if (filename == null || filename.trim().isEmpty()) {
                filename = "math_history.txt";
            }

            model.loadHistory(filename);
            view.showMessage("✅ Đã tải lịch sử từ " + filename);

        } catch (FileNotFoundException e) {
            view.showError("File không tồn tại: " + e.getMessage());
        } catch (IOException e) {
            view.showError("Lỗi khi đọc file: " + e.getMessage());
        }
    }

    public void checkSystem() {
        view.showMessage("🔍 Kiểm tra hệ thống...");

        if (model.checkOllama()) {
            view.showMessage("✅ Ollama đã sẵn sàng!");
        } else {
            view.showError("❌ Không thể kết nối Ollama!");
            view.showMessage("Hãy chắc chắn:");
            view.showMessage("1. Đã cài Ollama");
            view.showMessage("2. Chạy: ollama serve");
            view.showMessage("3. Tải model: ollama pull mistral");
        }
    }

    public void showHelp() {
        view.showHelp();
    }
}


