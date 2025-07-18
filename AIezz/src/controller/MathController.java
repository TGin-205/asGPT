package controller;

import model.MathSolver;
import view.MathView;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import model.Question;

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

    public void deleteQuestion() {
        try {
            String id = view.inputQuestionId();
            if (id == null || id.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập ID!");
                return;
            }

            Question question = model.findQuestionById(id);
            if (question == null) {
                view.showMessage("❌ Không tìm thấy câu hỏi với ID: " + id);
                return;
            }

            // Hiển thị thông tin câu hỏi trước khi xóa
            view.showQuestion(question);

            // Xác nhận xóa
            if (view.confirmDelete()) {
                if (model.deleteQuestion(id)) {
                    view.showMessage("✅ Đã xóa câu hỏi thành công!");
                } else {
                    view.showMessage("❌ Không thể xóa câu hỏi!");
                }
            } else {
                view.showMessage("Đã hủy xóa câu hỏi.");
            }

        } catch (Exception e) {
            view.showError("Lỗi khi xóa câu hỏi: " + e.getMessage());
        }
    }

    public void searchHistory() {
        try {
            String[] searchInput = view.inputSearch();
            String type = searchInput[0];
            String keyword = searchInput[1];

            if (keyword == null || keyword.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập từ khóa!");
                return;
            }

            MathSolver.SearchType searchType = switch (type) {
                case "2" ->
                    MathSolver.SearchType.QUESTION;
                case "3" ->
                    MathSolver.SearchType.ANSWER;
                case "4" ->
                    MathSolver.SearchType.DIFFICULTY;
                default ->
                    MathSolver.SearchType.ALL;
            };

            List<Question> results = model.searchHistory(keyword, searchType);
            if (results.isEmpty()) {
                view.showMessage("Không tìm thấy kết quả nào cho: " + keyword);
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

    public void showHistory() {
        List<Question> history = model.getHistory();
        view.showHistory(history);
    }

    public void saveHistory() {
        try {
            String filename = view.inputFilename("Nhập tên file để lưu");
            if (filename == null || filename.trim().isEmpty()) {
                filename = "math_history.dat"; // Đổi extension thành .dat vì là file binary
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
                filename = "math_history.dat";
            }

            model.loadHistory(filename);
            view.showMessage("✅ Đã tải lịch sử từ " + filename);
            showHistory(); // Hiển thị lịch sử sau khi load

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

    public void addCustomQuestion() {
        try {
            String question = view.inputCustomQuestion();
            if (question == null || question.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập câu hỏi!");
                return;
            }

            String answer = view.inputCustomAnswer();
            if (answer == null || answer.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập đáp án!");
                return;
            }

            String difficulty = view.inputCustomDifficulty();
            if (difficulty == null || difficulty.trim().isEmpty()) {
                view.showMessage("Vui lòng nhập độ khó!");
                return;
            }

            model.addCustomQuestion(question, answer, difficulty);
            view.showMessage("✅ Đã thêm câu hỏi thành công!");

        } catch (Exception e) {
            view.showError("Lỗi khi thêm câu hỏi: " + e.getMessage());
        }
    }

    public void showHelp() {
        view.showHelp();
    }
}
