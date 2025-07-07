package model;

import java.io.*;
import java.net.*;
import java.util.*;

public class MathSolver {
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private List<String> history;
    
    public MathSolver() {
        history = new ArrayList<>();
    }
    
    // Giải toán thông qua Ollama
    public String solveMath(String problem) throws Exception {
        // Lưu vào lịch sử
        history.add(problem);
        
        // Tạo prompt
        String prompt = "Bạn là trợ lý toán học. Giải bài toán sau chi tiết:\n\n" +
                       "Bài toán: " + problem + "\n\n" +
                       "Hãy giải từng bước và đưa ra kết quả cuối cùng bằng tiếng Việt:";
        
        // Tạo JSON request
        String jsonRequest = "{" +
                "\"model\": \"mistral\"," +
                "\"prompt\": \"" + escapeJson(prompt) + "\"," +
                "\"stream\": false" +
                "}";
        
        // Gửi request
        String response = sendToOllama(jsonRequest);
        return extractResponse(response);
    }
    
    // Gửi request đến Ollama
    private String sendToOllama(String jsonData) throws Exception {
        URL url = new URL(OLLAMA_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(120000);
            
            // Ghi dữ liệu
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();
            writer.close();
            
            // Đọc response
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Lỗi kết nối Ollama. Mã lỗi: " + responseCode);
            }
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            return response.toString();
            
        } finally {
            connection.disconnect();
        }
    }
    
    // Trích xuất response từ JSON
    private String extractResponse(String jsonResponse) {
        try {
            int start = jsonResponse.indexOf("\"response\":\"");
            if (start == -1) return "Không tìm thấy kết quả.";
            
            start += 12;
            int end = jsonResponse.lastIndexOf("\"");
            
            if (end <= start) return "Lỗi khi đọc kết quả.";
            
            String response = jsonResponse.substring(start, end);
            response = response.replace("\\n", "\n")
                              .replace("\\\"", "\"")
                              .replace("\\\\", "\\");
            
            return response.trim();
            
        } catch (Exception e) {
            return "Lỗi khi xử lý kết quả: " + e.getMessage();
        }
    }
    
    // Escape JSON string
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    // Lấy lịch sử
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
    
    // Lưu lịch sử vào file
    public void saveHistory(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (String problem : history) {
            writer.write(problem + "\n");
        }
        writer.close();
    }
    
    // Đọc lịch sử từ file
    public void loadHistory(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        history.clear();
        
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                history.add(line.trim());
            }
        }
        reader.close();
    }
    
    // Kiểm tra Ollama
    public boolean checkOllama() {
        try {
            URL url = new URL("http://localhost:11434/api/tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            
            int code = conn.getResponseCode();
            conn.disconnect();
            
            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }
}