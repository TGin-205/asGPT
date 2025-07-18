package model;

import java.io.*;
import java.net.*;
import java.util.*;

public class MathSolver {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private List<String> history = new ArrayList<>();
    
    private final Map<String, String> cache = new HashMap<>();
    
    public MathSolver() {
        history = new ArrayList<>();
    }

    public String solveMath(String problem) throws Exception {
        if (cache.containsKey(problem)) {
            return "📋 Từ bộ nhớ: " + cache.get(problem);
        }
        
        if (history.size() >= 100) {
            history.remove(0);
        }
        history.add(problem);

        String prompt = buildPrompt(problem);
        
        String jsonRequest = "{"
                + "\"model\": \"mistral\","
                + "\"prompt\": \"" + escapeJson(prompt) + "\","
                + "\"stream\": false,"
                + "\"options\": {\"temperature\": 0.1}"
                + "}";

        String response = sendToOllama(jsonRequest);
        String result = extractResponse(response);
        
        cache.put(problem, result);
        
        return result;
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
        } else if (problem.toLowerCase().contains("diện tích") || 
                  problem.toLowerCase().contains("chu vi") ||
                  problem.toLowerCase().contains("thể tích")) {
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

    public String explainSolution(String problem) throws Exception {
        String prompt = "Bạn là giáo viên toán học. Hãy giải thích chi tiết cách giải bài toán:\n" +
                       problem + "\n" +
                       "Yêu cầu:\n" +
                       "- Giải thích từng bước một cách dễ hiểu\n" +
                       "- Nêu lý do tại sao làm như vậy\n" +
                       "- Đưa ra lời khuyên học tập";
        
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
        String prompt = "Dựa vào bài toán: " + problem + "\n" +
                       "Hãy tạo 3 bài toán tương tự với độ khó tương đương.\n" +
                       "Format:\n" +
                       "Bài 1: ...\n" +
                       "Bài 2: ...\n" +
                       "Bài 3: ...";
        
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
        String prompt = "Hãy đánh giá độ khó của bài toán: " + problem + "\n" +
                       "Trả lời theo format:\n" +
                       "Độ khó: [Dễ/Trung bình/Khó]\n" +
                       "Lý do: ...\n" +
                       "Kiến thức cần: ...";
        
        String jsonRequest = "{"
                + "\"model\": \"mistral\","
                + "\"prompt\": \"" + escapeJson(prompt) + "\","
                + "\"stream\": false,"
                + "\"options\": {\"temperature\": 0.2}"
                + "}";

        String response = sendToOllama(jsonRequest);
        return extractResponse(response);
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
                      .replaceAll("^\\s+", "")  // Xóa khoảng trắng đầu
                      .replaceAll("\\s+$", ""); // Xóa khoảng trắng cuối
    }

    public void clearCache() {
        cache.clear();
    }

    public String getCacheStats() {
        return "📊 Cache: " + cache.size() + " bài toán đã lưu";
    }

    public List<String> searchHistory(String keyword) {
        List<String> results = new ArrayList<>();
        for (String problem : history) {
            if (problem.toLowerCase().contains(keyword.toLowerCase())) {
                results.add(problem);
            }
        }
        return results;
    }
    
    // TÍNH NĂNG THÊM - Thống kê lịch sử
    public String getHistoryStats() {
        if (history.isEmpty()) {
            return "📊 Chưa có bài toán nào trong lịch sử";
        }
        
        int equations = 0;
        int calculations = 0;
        int geometry = 0;
        
        for (String problem : history) {
            if (problem.contains("=")) {
                equations++;
            } else if (problem.toLowerCase().contains("diện tích") || 
                      problem.toLowerCase().contains("chu vi") ||
                      problem.toLowerCase().contains("thể tích")) {
                geometry++;
            } else {
                calculations++;
            }
        }
        
        return "📊 Thống kê lịch sử:\n" +
               "- Tổng: " + history.size() + " bài\n" +
               "- Phương trình: " + equations + " bài\n" +
               "- Hình học: " + geometry + " bài\n" +
               "- Tính toán: " + calculations + " bài";
    }

    // Gửi request đến Ollama (giữ nguyên)
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

    // Escape JSON string (giữ nguyên)
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public void saveHistory(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String problem : history) {
                writer.write(problem + "\n");
            }
        }
    }

    public void loadHistory(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            history.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    history.add(line.trim());
                }
            }
        }
    }

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
}