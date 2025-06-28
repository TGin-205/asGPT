package mathaiagent;

import java.util.*;

public class RecommendationEngine {
    
    public List<String> generateRecommendations(Map<String, Integer> patternFrequency, 
                                               Map<String, List<String>> userPreferences) {
        List<String> recommendations = new ArrayList<>();
        
        // Find most used pattern
        String mostUsedPattern = patternFrequency.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("GENERAL");
        
        // Generate recommendations based on usage pattern
        switch (mostUsedPattern) {
            case "ARITHMETIC":
                recommendations.addAll(Arrays.asList(
                    "Thử học về phần trăm: 'Tính 20% của 150'",
                    "Khám phá phép tính phức tạp: '(5+3)*(7-2)'",
                    "Thử tính toán với số thập phân: '3.14 * 2.5'"
                ));
                break;
            case "TRIGONOMETRY":
                recommendations.addAll(Arrays.asList(
                    "Học về đơn vị radian: 'sin(π/2)'",
                    "Thử các hàm ngược: 'arcsin(0.5)'",
                    "Khám phá đồng nhất thức: 'sin²(x) + cos²(x)'"
                ));
                break;
            case "ALGEBRA":
                recommendations.addAll(Arrays.asList(
                    "Thử giải phương trình: 'x² - 5x + 6 = 0'",
                    "Học về logarithm: 'log(100)'",
                    "Khám phá số mũ: '2^10'"
                ));
                break;
            case "CALCULUS":
                recommendations.addAll(Arrays.asList(
                    "Thử đạo hàm cơ bản: 'đạo hàm của x²'",
                    "Học tích phân: 'tích phân của 2x'",
                    "Khám phá giới hạn: 'lim x→0 (sin x)/x'"
                ));
                break;
            default:
                recommendations.addAll(Arrays.asList(
                    "Bắt đầu với phép tính cơ bản: '5 + 3'",
                    "Thử hàm toán học: 'sqrt(25)'",
                    "Khám phá lượng giác: 'sin(30)'"
                ));
        }
        
        return recommendations.size() > 3 ? recommendations.subList(0, 3) : recommendations;
    }
    
    public List<String> getPersonalizedRecommendations(Map<String, List<String>> userPreferences, 
                                                      String currentTopic) {
        List<String> personalizedRecs = new ArrayList<>();
        
        // Get user's preferred patterns in current topic
        List<String> topicPreferences = userPreferences.getOrDefault(currentTopic, new ArrayList<>());
        
        if (!topicPreferences.isEmpty()) {
            // Analyze user's complexity preference
            boolean prefersAdvanced = topicPreferences.stream()
                .anyMatch(input -> input.contains("^") || input.contains("sqrt") || 
                         input.contains("sin") || input.contains("log"));
            
            if (prefersAdvanced) {
                personalizedRecs.addAll(getAdvancedRecommendations(currentTopic));
            } else {
                personalizedRecs.addAll(getBasicRecommendations(currentTopic));
            }
        }
        
        return personalizedRecs;
    }
    
    private List<String> getAdvancedRecommendations(String topic) {
        Map<String, List<String>> advancedRecs = new HashMap<>();
        
        advancedRecs.put("ARITHMETIC", Arrays.asList(
            "Tính toán với số phức: '(3+4i) * (1-2i)'",
            "Phép tính mod: '17 mod 5'",
            "Tính giai thừa: '7!'"
        ));
        
        advancedRecs.put("TRIGONOMETRY", Arrays.asList(
            "Hàm hyperbolic: 'sinh(2)'",
            "Biến đổi lượng giác: 'sin(3x) = 3sin(x) - 4sin³(x)'",
            "Phương trình lượng giác: 'sin(x) = 0.5'"
        ));
        
        advancedRecs.put("ALGEBRA", Arrays.asList(
            "Ma trận: 'det([[1,2],[3,4]])'",
            "Phương trình bậc 3: 'x³ - 6x² + 11x - 6 = 0'",
            "Chuỗi Taylor: 'Taylor series của e^x'"
        ));
        
        return advancedRecs.getOrDefault(topic, Arrays.asList("Khám phá chủ đề nâng cao hơn"));
    }
    
    private List<String> getBasicRecommendations(String topic) {
        Map<String, List<String>> basicRecs = new HashMap<>();
        
        basicRecs.put("ARITHMETIC", Arrays.asList(
            "Phép chia có dư: '23 chia 7'",
            "Tính phần trăm: '15% của 80'",
            "Tính trung bình: '(5+7+9)/3'"
        ));
        
        basicRecs.put("TRIGONOMETRY", Arrays.asList(
            "Góc đặc biệt: 'sin(45°)'",
            "Chuyển đổi độ-radian: '60° sang radian'",
            "Định lý Pythagoras: 'a² + b² = c²'"
        ));
        
        basicRecs.put("ALGEBRA", Arrays.asList(
            "Giải phương trình bậc 1: '2x + 5 = 11'",
            "Bình phương hoàn hảo: '(x+3)²'",
            "Căn bậc hai: 'sqrt(144)'"
        ));
        
        return basicRecs.getOrDefault(topic, Arrays.asList("Ôn tập kiến thức cơ bản"));
    }
    
    public List<String> getTrendingTopics(Map<String, Integer> patternFrequency) {
        return patternFrequency.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(3)
            .map(entry -> entry.getKey() + " (" + entry.getValue() + " lần)")
            .collect(java.util.stream.Collectors.toList());
    }
}