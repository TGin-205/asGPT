package controller;

import model.MathSolver;
import view.MathView;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

public class MathController {

    private final MathSolver model;
    private final MathView view;

    public MathController() {
        model = new MathSolver();
        view = new MathView();
    }

    public MathSolver getModel() {
        return model;
    }

    public void solveProblem() {
        try {
            String problem = view.inputProblem();
            if (problem == null || problem.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập bài toán!");
                return;
            }
            if (problem.length() > 1000) {
                view.showError("Bài toán quá dài! (tối đa 1000 ký tự)");
                return;
            }

            view.showMessage("🤖 Đang giải...");
            String result = model.solveMath(problem);
            view.showResult(result);

        } catch (ConnectException e) {
            view.showError("Lỗi kết nối: Không thể kết nối Ollama!");
        } catch (SocketTimeoutException e) {
            view.showError("Lỗi timeout: Ollama mất quá lâu để phản hồi!");
        } catch (IOException e) {
            view.showError("Lỗi I/O: " + e.getMessage());
        } catch (Exception e) {
            view.showError("Lỗi không xác định: " + e.getMessage());
        }
    }

    public void explainProblem() {
        try {
            String problem = view.inputProblem();
            if (problem == null || problem.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập bài toán cần giải thích!");
                return;
            }

            view.showMessage("🧠 Đang giải thích...");
            String explanation = model.explainSolution(problem);
            view.showResult("📖 Giải thích chi tiết:\n" + explanation);

        } catch (Exception e) {
            view.showError("Lỗi khi giải thích: " + e.getMessage());
        }
    }

    public void generateSimilarProblems() {
        try {
            String problem = view.inputProblem();
            if (problem == null || problem.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập bài toán mẫu!");
                return;
            }

            view.showMessage("🎯 Đang tạo bài tương tự...");
            String similar = model.generateSimilarProblem(problem);
            view.showResult("📝 Bài toán tương tự:\n" + similar);

        } catch (Exception e) {
            view.showError("Lỗi khi tạo bài tương tự: " + e.getMessage());
        }
    }


    public void assessDifficulty() {
        try {
            String problem = view.inputProblem();
            if (problem == null || problem.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập bài toán cần đánh giá!");
                return;
            }

            view.showMessage("🔍 Đang đánh giá độ khó...");
            String assessment = model.assessDifficulty(problem);
            view.showResult("📊 Đánh giá độ khó:\n" + assessment);

        } catch (Exception e) {
            view.showError("Lỗi khi đánh giá: " + e.getMessage());
        }
    }

    // TÍNH NĂNG MỚI - Tìm kiếm lịch sử
    public void searchHistory() {
        try {
            String keyword = view.inputFilename("Nhập từ khóa tìm kiếm");
            if (keyword == null || keyword.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập từ khóa!");
                return;
            }

            List<String> results = model.searchHistory(keyword);
            if (results.isEmpty()) {
                view.showMessage("Không tìm thấy bài toán nào chứa: " + keyword);
            } else {
                view.showMessage("🔍 Kết quả tìm kiếm cho '" + keyword + "':");
                view.showHistory(results);
            }

        } catch (Exception e) {
            view.showError("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

    // TÍNH NĂNG MỚI - Xem thống kê
    public void showStatistics() {
        try {
            String historyStats = model.getHistoryStats();
            String cacheStats = model.getCacheStats();
            
            view.showMessage("📊 THỐNG KÊ HỆ THỐNG:");
            view.showMessage(historyStats);
            view.showMessage(cacheStats);

        } catch (Exception e) {
            view.showError("Lỗi khi xem thống kê: " + e.getMessage());
        }
    }

    // TÍNH NĂNG MỚI - Xóa cache
    public void clearCache() {
        try {
            model.clearCache();
            view.showMessage("✅ Đã xóa cache thành công!");

        } catch (Exception e) {
            view.showError("Lỗi khi xóa cache: " + e.getMessage());
        }
    }

    // CÁC TÍNH NĂNG CŨ (giữ nguyên)
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