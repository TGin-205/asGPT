package mathaiagent;

import java.util.*;

// Explanation Generator - Tạo giải thích chi tiết
public class ExplanationGenerator {
    private Map<String, String> conceptExplanations;
    
    public ExplanationGenerator() {
        initializeExplanations();
    }
    
    private void initializeExplanations() {
        conceptExplanations = new HashMap<>();
        
        conceptExplanations.put("addition", 
            "Phép cộng là phép toán cơ bản nhất, biểu thị việc kết hợp hai hay nhiều số lại với nhau.");
        
        conceptExplanations.put("multiplication", 
            "Phép nhân là phép cộng lặp lại. Ví dụ: 3 × 4 = 3 + 3 + 3 + 3 = 12.");
        
        conceptExplanations.put("sine", 
            "Hàm sin trong tam giác vuông là tỷ số giữa cạnh đối và cạnh huyền. " +
            "Trong đường tròn đơn vị, sin(θ) là tọa độ y của điểm trên đường tròn.");
        
        conceptExplanations.put("logarithm", 
            "Logarithm là phép toán ngược của lũy thừa. log₁₀(100) = 2 vì 10² = 100.");
        
        conceptExplanations.put("square_root", 
            "Căn bậc hai của một số là số mà khi nhân với chính nó sẽ được số ban đầu. √25 = 5 vì 5 × 5 = 25.");
    }
    
    public String explainConcept(String concept) {
        return conceptExplanations.getOrDefault(concept.toLowerCase(), 
            "Khái niệm này chưa có trong cơ sở dữ liệu giải thích.");
    }
    
    public String generateStepByStepSolution(String expression, double result) {
        StringBuilder explanation = new StringBuilder();
        explanation.append("🔍 GIẢI CHI TIẾT:\n");
        explanation.append("════════════════════════════════════\n");
        
        if (expression.contains("(")) {
            explanation.append("Bước 1: Giải các biểu thức trong ngoặc đơn trước\n");
        }
        
        if (expression.contains("^") || expression.contains("pow")) {
            explanation.append("Bước 2: Tính lũy thừa (ưu tiên cao nhất)\n");
        }
        
        if (expression.contains("*") || expression.contains("/")) {
            explanation.append("Bước 3: Thực hiện phép nhân và chia (từ trái sang phải)\n");
        }
        
        if (expression.contains("+") || expression.contains("-")) {
            explanation.append("Bước 4: Thực hiện phép cộng và trừ (từ trái sang phải)\n");
        }
        
        explanation.append("Kết quả cuối cùng: ").append(result).append("\n");
        
        return explanation.toString();
    }
}