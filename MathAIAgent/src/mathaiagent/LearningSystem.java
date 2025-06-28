package mathaiagent;

import java.util.*;

public class LearningSystem {
    private Map<String, Integer> patternFrequency;
    private Map<String, List<String>> userPreferences;
    private Map<String, Double> accuracyHistory;
    private List<InteractionRecord> interactionHistory;
    // RecommendationEngine should be defined or stubbed
    private RecommendationEngine recommendationEngine;
    
    public LearningSystem() {
        this.patternFrequency = new HashMap<>();
        this.userPreferences = new HashMap<>();
        this.accuracyHistory = new HashMap<>();
        this.interactionHistory = new ArrayList<>();
        this.recommendationEngine = new RecommendationEngine();
    }
    
    public void recordInteraction(String input, String output, boolean wasSuccessful, double responseTime) {
        InteractionRecord record = new InteractionRecord(input, output, wasSuccessful, responseTime, System.currentTimeMillis());
        interactionHistory.add(record);
        
        // Update pattern frequency
        String pattern = extractPattern(input);
        patternFrequency.put(pattern, patternFrequency.getOrDefault(pattern, 0) + 1);
        
        // Update accuracy
        updateAccuracy(pattern, wasSuccessful);
        
        // Update user preferences
        updateUserPreferences(input, output, wasSuccessful);
    }
    
    private String extractPattern(String input) {
        if (input.matches(".*[+\\-*/].*")) return "ARITHMETIC";
        if (input.contains("sin") || input.contains("cos") || input.contains("tan")) return "TRIGONOMETRY";
        if (input.contains("^") || input.contains("sqrt")) return "ALGEBRA";
        if (input.contains("đạo hàm") || input.contains("derivative")) return "CALCULUS";
        return "GENERAL";
    }
    
    private void updateAccuracy(String pattern, boolean wasSuccessful) {
        double currentAccuracy = accuracyHistory.getOrDefault(pattern, 0.5);
        double newAccuracy = wasSuccessful ? 
            (currentAccuracy * 0.9 + 1.0 * 0.1) : 
            (currentAccuracy * 0.9 + 0.0 * 0.1);
        accuracyHistory.put(pattern, newAccuracy);
    }
    
    private void updateUserPreferences(String input, String output, boolean wasSuccessful) {
        if (!wasSuccessful) return;
        
        String category = extractPattern(input);
        userPreferences.computeIfAbsent(category, k -> new ArrayList<>()).add(input);
        
        // Keep only recent preferences
        List<String> prefs = userPreferences.get(category);
        if (prefs.size() > 10) {
            prefs.remove(0);
        }
    }
    
    public List<String> getRecommendations() {
        return recommendationEngine.generateRecommendations(patternFrequency, userPreferences);
    }
    
    public double getPredictedAccuracy(String input) {
        String pattern = extractPattern(input);
        return accuracyHistory.getOrDefault(pattern, 0.5);
    }
    
    public void showLearningStats() {
        System.out.println("\n=== 📊 AI Learning Statistics ===");
        System.out.println("Tổng số tương tác: " + interactionHistory.size());
        
        System.out.println("\n📈 Độ chính xác theo lĩnh vực:");
        for (Map.Entry<String, Double> entry : accuracyHistory.entrySet()) {
            System.out.printf("• %s: %.1f%%\n", entry.getKey(), entry.getValue() * 100);
        }
        
        System.out.println("\n🔥 Chủ đề được sử dụng nhiều nhất:");
        patternFrequency.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> System.out.println("• " + entry.getKey() + ": " + entry.getValue() + " lần"));
    }
    
    // Getters for accessing data (optional)
    public Map<String, Integer> getPatternFrequency() {
        return new HashMap<>(patternFrequency);
    }
    
    public Map<String, List<String>> getUserPreferences() {
        return new HashMap<>(userPreferences);
    }
    
    public List<InteractionRecord> getInteractionHistory() {
        return new ArrayList<>(interactionHistory);
    }
}

// Stub cho RecommendationEngine (nếu chưa có)
class RecommendationEngine {
    public List<String> generateRecommendations(Map<String, Integer> patternFreq, Map<String, List<String>> userPrefs) {
        List<String> recs = new ArrayList<>();
        for(String k : patternFreq.keySet()) {
            recs.add("Recommendation for " + k);
        }
        return recs;
    }
}