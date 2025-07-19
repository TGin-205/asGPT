package model;

import java.io.*;
import java.net.*;
import java.util.*;

public class MathSolver {

    // ========== CONSTANTS & FIELDS ==========
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private List<Question> history = new ArrayList<>();
    private final Map<String, String> cache = new HashMap<>();

    // ========== CONSTRUCTOR ==========
    public MathSolver() {
        history = new ArrayList<>();
    }

    // ========== ENUMS ==========
    public enum SearchType {
        ALL,
        QUESTION,
        ANSWER,
        DIFFICULTY
    }

    public enum SortType {
        DIFFICULTY_ASC, // Dễ -> Khó
        DIFFICULTY_DESC, // Khó -> Dễ
        ID_ASC, // Theo ID tăng dần
        ID_DESC         // Theo ID giảm dần
    }

    // ========== CORE MATH SOLVING METHODS ==========
    public String solveMath(String problem) throws Exception {
        if (cache.containsKey(problem)) {
            return "📋 Từ bộ nhớ: " + cache.get(problem);
        }

        if (history.size() >= 100) {
            history.remove(0);
        }

        String result = sendToOllamaAndGetResponse(problem);

        // Create and store a new Question object
        String id = "Q" + (history.size() + 1);
        Question newQuestion = new Question(id, problem, result, assessDifficultyInternal(problem));
        history.add(newQuestion);

        cache.put(problem, result);
        return result;
    }

    public String explainSolution(String problem) throws Exception {
        String prompt = "Bạn là giáo viên toán học. Hãy giải thích chi tiết cách giải bài toán:\n"
                + problem + "\n"
                + "Yêu cầu:\n"
                + "- Giải thích từng bước một cách dễ hiểu\n"
                + "- Nêu lý do tại sao làm như vậy\n"
                + "- Đưa ra lời khuyên học tập";

        String jsonRequest = "{"
                + "\"model\": \"mistral\","
                + "\"prompt\": \"" + escapeJson(prompt) + "\","
                + "\"stream\": false,"
                + "\"options\": {\"temperature\": 0.3}"
                + "}";

        String response = sendToOllama(jsonRequest);
        return extractResponse(response);
    }

    public String generateSimilarProblem(String problem) throws Exception {
        String prompt = "Dựa vào bài toán: " + problem + "\n"
                + "Hãy tạo 3 bài toán tương tự với độ khó tương đương.\n"
                + "Format:\n"
                + "Bài 1: ...\n"
                + "Bài 2: ...\n"
                + "Bài 3: ...";

        String jsonRequest = "{"
                + "\"model\": \"mistral\","
                + "\"prompt\": \"" + escapeJson(prompt) + "\","
                + "\"stream\": false,"
                + "\"options\": {\"temperature\": 0.7}"
                + "}";

        String response = sendToOllama(jsonRequest);
        return extractResponse(response);
    }

    public String assessDifficulty(String problem) throws Exception {
        String prompt = "Hãy đánh giá độ khó của bài toán: " + problem + "\n"
                + "Trả lời theo format:\n"
                + "Độ khó: [Dễ/Trung bình/Khó]\n"
                + "Lý do: ...\n"
                + "Kiến thức cần: ...";

        String jsonRequest = "{"
                + "\"model\": \"mistral\","
                + "\"prompt\": \"" + escapeJson(prompt) + "\","
                + "\"stream\": false,"
                + "\"options\": {\"temperature\": 0.2}"
                + "}";

        String response = sendToOllama(jsonRequest);
        return extractResponse(response);
    }

    // ========== QUESTION MANAGEMENT ==========
    public void addCustomQuestion(String questionText, String answerText, String difficultyLevel) {
        String id = "Q" + (history.size() + 1);
        Question question = new Question(id, questionText, answerText, difficultyLevel);
        history.add(question);
    }

    public boolean deleteQuestion(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        int indexToRemove = -1;
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getId().equalsIgnoreCase(id)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            history.remove(indexToRemove);
            return true;
        }
        return false;
    }

    public Question findQuestionById(String id) {
        for (Question question : history) {
            if (question.getId().equalsIgnoreCase(id)) {
                return question;
            }
        }
        return null;
    }

    // ========== HISTORY & SEARCH METHODS ==========
    public List<Question> getHistory() {
        return new ArrayList<>(history);
    }

    public List<Question> searchHistory(String keyword, SearchType type) {
        List<Question> results = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (Question question : history) {
            boolean match = switch (type) {
                case QUESTION ->
                    question.getQuestion().toLowerCase().contains(keyword);
                case ANSWER ->
                    question.getAnswerS().toLowerCase().contains(keyword);
                case DIFFICULTY ->
                    question.getDifficulty().toLowerCase().contains(keyword);
                case ALL ->
                    question.getQuestion().toLowerCase().contains(keyword)
                    || question.getAnswerS().toLowerCase().contains(keyword)
                    || question.getDifficulty().toLowerCase().contains(keyword);
            };

            if (match) {
                results.add(question);
            }
        }
        return results;
    }

    public List<Question> getSortedHistory(SortType sortType) {
        List<Question> sortedHistory = new ArrayList<>(history);

        switch (sortType) {
            case DIFFICULTY_ASC ->
                sortedHistory.sort((q1, q2) -> {
                    int diff1 = getDifficultyValue(q1.getDifficulty());
                    int diff2 = getDifficultyValue(q2.getDifficulty());
                    return Integer.compare(diff1, diff2);
                });
            case DIFFICULTY_DESC ->
                sortedHistory.sort((q1, q2) -> {
                    int diff1 = getDifficultyValue(q1.getDifficulty());
                    int diff2 = getDifficultyValue(q2.getDifficulty());
                    return Integer.compare(diff2, diff1);
                });
            case ID_ASC ->
                sortedHistory.sort((q1, q2) -> {
                    int id1 = extractIdNumber(q1.getId());
                    int id2 = extractIdNumber(q2.getId());
                    return Integer.compare(id1, id2);
                });
            case ID_DESC ->
                sortedHistory.sort((q1, q2) -> {
                    int id1 = extractIdNumber(q1.getId());
                    int id2 = extractIdNumber(q2.getId());
                    return Integer.compare(id2, id1);
                });
        }

        return sortedHistory;
    }

    public List<Question> getQuestionsByDifficulty(String difficulty) {
        return history.stream()
                .filter(q -> q.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(java.util.stream.Collectors.toList());
    }

    // ========== STATISTICS & CACHE METHODS ==========
    public String getHistoryStats() {
        if (history.isEmpty()) {
            return "📊 Chưa có bài toán nào trong lịch sử";
        }

        int easy = 0;
        int medium = 0;
        int hard = 0;

        for (Question question : history) {
            switch (question.getDifficulty().toLowerCase()) {
                case "easy" ->
                    easy++;
                case "hard" ->
                    hard++;
                default ->
                    medium++;
            }
        }

        return "📊 Thống kê lịch sử:\n"
                + "- Tổng: " + history.size() + " bài\n"
                + "- Dễ: " + easy + " bài\n"
                + "- Trung bình: " + medium + " bài\n"
                + "- Khó: " + hard + " bài";
    }

    public void clearCache() {
        cache.clear();
    }

    public String getCacheStats() {
        return "📊 Cache: " + cache.size() + " bài toán đã lưu";
    }

    // ========== FILE I/O METHODS ==========
    public void saveHistory(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Question question : history) {
                // Write each field separated by a delimiter (using ||| to avoid conflicts)
                writer.write(question.getId() + "|||"
                        + question.getQuestion() + "|||"
                        + question.getAnswerS() + "|||"
                        + question.getDifficulty() + "\n");
            }
        }
    }

    public void loadHistory(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            history.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|\\|\\|");
                    if (parts.length == 4) {
                        Question question = new Question(
                                parts[0].trim(), // id
                                parts[1].trim(), // question
                                parts[2].trim(), // answer
                                parts[3].trim() // difficulty
                        );
                        history.add(question);
                    }
                }
            }
        }
    }

    // ========== OLLAMA CONNECTION METHODS ==========
    public boolean checkOllama() {
        try {
            URL url = new URL("http://localhost:11434/api/tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            int code = conn.getResponseCode();
            conn.disconnect();

            return code == 200;
        } catch (IOException e) {
            return false;
        }
    }

    // ========== PRIVATE HELPER METHODS ==========
    private String sendToOllamaAndGetResponse(String problem) throws Exception {
        String prompt = buildPrompt(problem);

        String jsonRequest = "{"
                + "\"model\": \"mistral\","
                + "\"prompt\": \"" + escapeJson(prompt) + "\","
                + "\"stream\": false,"
                + "\"options\": {\"temperature\": 0.1}"
                + "}";

        String response = sendToOllama(jsonRequest);
        return extractResponse(response);
    }

    private String buildPrompt(String problem) {
        StringBuilder prompt = new StringBuilder();
        if (problem.contains("=")) {
            prompt.append("Bạn là giáo viên toán học. Hãy giải phương trình sau:\n");
            prompt.append(problem).append("\n");
            prompt.append("Yêu cầu:\n");
            prompt.append("- Trình bày từng bước giải rõ ràng\n");
            prompt.append("- Kiểm tra nghiệm\n");
            prompt.append("- Trả lời ngắn gọn");
        } else if (problem.toLowerCase().contains("diện tích")
                || problem.toLowerCase().contains("chu vi")
                || problem.toLowerCase().contains("thể tích")) {
            prompt.append("Bạn là giáo viên hình học. Hãy giải bài toán sau:\n");
            prompt.append(problem).append("\n");
            prompt.append("Yêu cầu:\n");
            prompt.append("- Xác định công thức cần dùng\n");
            prompt.append("- Thế số vào công thức\n");
            prompt.append("- Tính toán và đưa ra kết quả");
        } else {
            prompt.append("Bạn là giáo viên toán học. Hãy tính toán:\n");
            prompt.append(problem).append("\n");
            prompt.append("Trả lời ngắn gọn với kết quả chính xác.");
        }

        return prompt.toString();
    }

    private String sendToOllama(String jsonData) throws Exception {
        URL url = new URL(OLLAMA_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(180000);

            try (OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8")) {
                writer.write(jsonData);
                writer.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Lỗi Ollama (" + responseCode + ")");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }

        } finally {
            connection.disconnect();
        }
    }

    private String extractResponse(String jsonResponse) {
        try {
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                return "❌ Không nhận được phản hồi từ AI.";
            }

            int responseStart = jsonResponse.indexOf("\"response\":\"");
            if (responseStart == -1) {
                return "❌ Định dạng phản hồi không hợp lệ.";
            }

            responseStart += 12; // Skip "\"response\":\""
            int responseEnd = jsonResponse.indexOf("\",\"", responseStart);
            if (responseEnd == -1) {
                responseEnd = jsonResponse.lastIndexOf("\"");
            }

            if (responseEnd <= responseStart) {
                return "❌ Không thể xử lý phản hồi.";
            }

            String response = jsonResponse.substring(responseStart, responseEnd);

            response = cleanResponse(response);

            if (response.length() > 2000) {
                response = response.substring(0, 2000) + "...\n[Phản hồi đã được rút gọn]";
            }

            return response.trim();

        } catch (Exception e) {
            return "❌ Lỗi xử lý: " + e.getMessage();
        }
    }

    private String cleanResponse(String response) {
        return response.replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\t", "\t")
                .replace("\\r", "\r")
                .replaceAll("^\\s+", "") // Xóa khoảng trắng đầu
                .replaceAll("\\s+$", ""); // Xóa khoảng trắng cuối
    }

    private String assessDifficultyInternal(String problem) {
        try {
            String assessment = assessDifficulty(problem);
            // Extract difficulty level from assessment
            if (assessment.toLowerCase().contains("dễ")) {
                return "Easy";
            }
            if (assessment.toLowerCase().contains("khó")) {
                return "Hard";
            }
            return "Medium";
        } catch (Exception e) {
            return "Medium"; // Default difficulty
        }
    }

    private int getDifficultyValue(String difficulty) {
        return switch (difficulty.toLowerCase()) {
            case "easy" ->
                1;
            case "medium" ->
                2;
            case "hard" ->
                3;
            default ->
                2; // medium làm mặc định
        };
    }

    private int extractIdNumber(String id) {
        try {
            return Integer.parseInt(id.substring(1)); // Bỏ chữ 'Q'
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}