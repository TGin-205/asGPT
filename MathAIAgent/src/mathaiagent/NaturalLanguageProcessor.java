package mathaiagent;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NaturalLanguageProcessor {
    private Map<String, String> mathVocabulary;
    private Map<String, String> contextPatterns;
    private List<String> questionWords;
    
    public NaturalLanguageProcessor() {
        initializeVocabulary();
        initializeContextPatterns();
        initializeQuestionWords();
    }
    
    private void initializeVocabulary() {
        mathVocabulary = new HashMap<>();
        // Arithmetic operations
        mathVocabulary.put("cộng|thêm|plus", "+");
        mathVocabulary.put("trừ|bớt|minus", "-");
        mathVocabulary.put("nhân|lấy .* nhân|times", "*");
        mathVocabulary.put("chia|chia cho|divided by", "/");
        mathVocabulary.put("mũ|lũy thừa|power|\\^", "^");
        mathVocabulary.put("căn bậc hai|sqrt|square root", "sqrt");
        mathVocabulary.put("sin|sine", "sin");
        mathVocabulary.put("cos|cosine", "cos");
        mathVocabulary.put("tan|tangent", "tan");
        mathVocabulary.put("log|logarithm", "log");
        mathVocabulary.put("ln|natural log", "ln");
        
        // Numbers in Vietnamese
        mathVocabulary.put("không|zero", "0");
        mathVocabulary.put("một|one", "1");
        mathVocabulary.put("hai|two", "2");
        mathVocabulary.put("ba|three", "3");
        mathVocabulary.put("bốn|four", "4");
        mathVocabulary.put("năm|five", "5");
        mathVocabulary.put("sáu|six", "6");
        mathVocabulary.put("bảy|seven", "7");
        mathVocabulary.put("tám|eight", "8");
        mathVocabulary.put("chín|nine", "9");
        mathVocabulary.put("mười|ten", "10");
    }
    
    private void initializeContextPatterns() {
        contextPatterns = new HashMap<>();
        contextPatterns.put("CALCULATION", "tính|calculate|compute|solve");
        contextPatterns.put("COMPARISON", "so sánh|compare|which is|lớn hơn|nhỏ hơn");
        contextPatterns.put("EXPLANATION", "giải thích|explain|why|tại sao|how|làm thế nào");
        contextPatterns.put("GRAPH", "vẽ đồ thị|graph|plot|biểu đồ");
        contextPatterns.put("DERIVATIVE", "đạo hàm|derivative|differentiate");
        contextPatterns.put("INTEGRAL", "tích phân|integral|integrate");
    }
    
    private void initializeQuestionWords() {
        questionWords = Arrays.asList("gì", "what", "tại sao", "why", "làm thế nào", "how", 
                                    "khi nào", "when", "ở đâu", "where", "ai", "who");
    }
    
    public ProcessedInput process(String input) {
        input = input.toLowerCase().trim();
        
        // Detect intent
        String intent = detectIntent(input);
        
        // Extract mathematical expressions
        String mathExpression = extractMathExpression(input);
        
        // Extract context and parameters
        Map<String, Object> parameters = extractParameters(input);
        
        // Determine complexity level
        int complexityLevel = determineComplexity(input, mathExpression);
        
        return new ProcessedInput(intent, mathExpression, parameters, complexityLevel, input);
    }
    
    private String detectIntent(String input) {
        for (Map.Entry<String, String> entry : contextPatterns.entrySet()) {
            if (Pattern.compile(entry.getValue()).matcher(input).find()) {
                return entry.getKey();
            }
        }
        
        // Default intent based on content analysis
        if (containsMathOperations(input)) {
            return "CALCULATION";
        } else if (containsQuestionWords(input)) {
            return "EXPLANATION";
        }
        
        return "GENERAL";
    }
    
    private String extractMathExpression(String input) {
        String processed = input;
        
        // Replace Vietnamese math terms with symbols
        for (Map.Entry<String, String> entry : mathVocabulary.entrySet()) {
            processed = processed.replaceAll(entry.getKey(), entry.getValue());
        }
        
        // Remove non-mathematical words
        processed = processed.replaceAll("tính giúp mình|tính|bằng|equals?|=", "");
        
        // Extract mathematical expression using regex
        Pattern mathPattern = Pattern.compile("[0-9+\\-*/.()^\\s]+|sqrt\\([^)]+\\)|sin\\([^)]+\\)|cos\\([^)]+\\)|tan\\([^)]+\\)|log\\([^)]+\\)|ln\\([^)]+\\)");
        Matcher matcher = mathPattern.matcher(processed);
        
        StringBuilder mathExpr = new StringBuilder();
        while (matcher.find()) {
            mathExpr.append(matcher.group()).append(" ");
        }
        
        return mathExpr.toString().trim();
    }
    
    private Map<String, Object> extractParameters(String input) {
        Map<String, Object> params = new HashMap<>();
        
        // Extract number of decimal places
        Pattern decimalPattern = Pattern.compile("(\\d+)\\s*(chữ số thập phân|decimal places?)");
        Matcher decimalMatcher = decimalPattern.matcher(input);
        if (decimalMatcher.find()) {
            params.put("decimalPlaces", Integer.parseInt(decimalMatcher.group(1)));
        }
        
        // Extract range for graphs
        Pattern rangePattern = Pattern.compile("từ\\s*([+-]?\\d+(?:\\.\\d+)?)\\s*đến\\s*([+-]?\\d+(?:\\.\\d+)?)");
        Matcher rangeMatcher = rangePattern.matcher(input);
        if (rangeMatcher.find()) {
            params.put("rangeStart", Double.parseDouble(rangeMatcher.group(1)));
            params.put("rangeEnd", Double.parseDouble(rangeMatcher.group(2)));
        }
        
        return params;
    }
    
    private int determineComplexity(String input, String mathExpr) {
        int complexity = 1; // Basic
        
        if (mathExpr.contains("^") || mathExpr.contains("sqrt") || 
            mathExpr.contains("sin") || mathExpr.contains("cos") || 
            mathExpr.contains("tan") || mathExpr.contains("log")) {
            complexity = 2; // Intermediate
        }
        
        if (input.contains("đạo hàm") || input.contains("tích phân") || 
            input.contains("derivative") || input.contains("integral") ||
            input.contains("limit") || input.contains("giới hạn")) {
            complexity = 3; // Advanced
        }
        
        return complexity;
    }
    
    private boolean containsMathOperations(String input) {
        return input.matches(".*[+\\-*/^=].*") || 
               Pattern.compile("cộng|trừ|nhân|chia|mũ").matcher(input).find();
    }
    
    private boolean containsQuestionWords(String input) {
        return questionWords.stream().anyMatch(input::contains);
    }
}