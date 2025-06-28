package mathaiagent;

// ===== SMART ERROR HANDLER =====

import java.util.*;

// Error Handler - Xử lý lỗi thông minh
class SmartErrorHandler {

    private Map<String, String> commonErrors;
    private Map<String, List<String>> suggestions;

    public SmartErrorHandler() {
        initializeErrorMappings();
    }

    private void initializeErrorMappings() {
        commonErrors = new HashMap<>();
        suggestions = new HashMap<>();

        commonErrors.put("division by zero", "Không thể chia cho 0");
        commonErrors.put("invalid syntax", "Cú pháp không hợp lệ");
        commonErrors.put("undefined function", "Hàm không tồn tại");
        commonErrors.put("invalid number", "Số không hợp lệ");
        commonErrors.put("overflow", "Số quá lớn");

        suggestions.put("division by zero", Arrays.asList(
                "Kiểm tra xem mẫu số có bằng 0 không",
                "Thử sử dụng giới hạn: lim(x→0) f(x)/g(x)",
                "Xem xét trường hợp đặc biệt khi mẫu tiến về 0"
        ));

        suggestions.put("invalid syntax", Arrays.asList(
                "Kiểm tra dấu ngoặc đóng mở",
                "Đảm bảo các toán tử được viết đúng (+, -, *, /)",
                "Thử diễn đạt lại bằng tiếng Việt: 'Tính 5 cộng 3'"
        ));

        suggestions.put("undefined function", Arrays.asList(
                "Kiểm tra tên hàm có đúng không (sin, cos, tan, sqrt, log)",
                "Đảm bảo cú pháp hàm đúng: sin(x), không phải sinx",
                "Xem danh sách hàm được hỗ trợ trong help"
        ));

        suggestions.put("invalid number", Arrays.asList(
                "Kiểm tra định dạng số (sử dụng dấu chấm cho thập phân: 3.14)",
                "Tránh sử dụng ký tự đặc biệt trong số",
                "Đảm bảo số nằm trong phạm vi cho phép"
        ));

        suggestions.put("overflow", Arrays.asList(
                "Số quá lớn để xử lý, thử số nhỏ hơn",
                "Kiểm tra lại phép tính, có thể có lỗi logic",
                "Sử dụng logarithm để tính toán số lớn"
        ));
    }

    public ErrorResponse handleError(String errorMessage) {
        String errorType = categorizeError(errorMessage);
        String friendlyMessage = commonErrors.getOrDefault(errorType,
                "Đã xảy ra lỗi không xác định");
        List<String> suggestions = this.suggestions.getOrDefault(errorType,
                Arrays.asList("Thử lại với cú pháp khác", "Kiểm tra đầu vào"));

        return new ErrorResponse(friendlyMessage, suggestions, errorType);
    }

    private String categorizeError(String errorMessage) {
        String lowerMessage = errorMessage.toLowerCase();

        if (lowerMessage.contains("/ by zero") || lowerMessage.contains("division by zero")) {
            return "division by zero";
        }
        if (lowerMessage.contains("syntax") || lowerMessage.contains("token")) {
            return "invalid syntax";
        }
        if (lowerMessage.contains("undefined") || lowerMessage.contains("unknown function")) {
            return "undefined function";
        }
        if (lowerMessage.contains("number format") || lowerMessage.contains("invalid number")) {
            return "invalid number";
        }
        if (lowerMessage.contains("overflow") || lowerMessage.contains("infinity")) {
            return "overflow";
        }

        return "unknown";
    }
}

class ErrorResponse {

    private String message;
    private List<String> suggestions;
    private String errorType;

    public ErrorResponse(String message, List<String> suggestions, String errorType) {
        this.message = message;
        this.suggestions = suggestions;
        this.errorType = errorType;
    }

    public void display() {
        System.out.println("❌ " + message);
        System.out.println("\n💡 Gợi ý:");
        for (String suggestion : suggestions) {
            System.out.println("  • " + suggestion);
        }
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public String getErrorType() {
        return errorType;
    }
}
