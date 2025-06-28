package mathaiagent;

import java.util.*;

public class ContextManager {
    private List<ConversationTurn> conversationHistory;
    private String currentTopic;
    private Map<String, Object> sessionVariables;
    
    public ContextManager() {
        this.conversationHistory = new ArrayList<>();
        this.sessionVariables = new HashMap<>();
        this.currentTopic = "GENERAL";
    }
    
    public void addTurn(String userInput, String agentResponse) {
        conversationHistory.add(new ConversationTurn(userInput, agentResponse, System.currentTimeMillis()));
        updateCurrentTopic(userInput);
        
        // Keep only last 10 turns for memory efficiency
        if (conversationHistory.size() > 10) {
            conversationHistory.remove(0);
        }
    }
    
    private void updateCurrentTopic(String input) {
        if (input.contains("sin") || input.contains("cos") || input.contains("tan")) {
            currentTopic = "TRIGONOMETRY";
        } else if (input.contains("đạo hàm") || input.contains("derivative")) {
            currentTopic = "CALCULUS";
        } else if (input.contains("sqrt") || input.contains("^")) {
            currentTopic = "ALGEBRA";
        } else if (input.matches(".*[+\\-*/].*")) {
            currentTopic = "ARITHMETIC";
        }
    }
    
    public String getCurrentTopic() { 
        return currentTopic; 
    }
    
    public List<String> getRecentQuestions(int count) {
        return conversationHistory.stream()
                .map(ConversationTurn::getUserInput)
                .skip(Math.max(0, conversationHistory.size() - count))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public void setVariable(String key, Object value) {
        sessionVariables.put(key, value);
    }
    
    public Object getVariable(String key) {
        return sessionVariables.get(key);
    }
}

class ConversationTurn {
    private String userInput;
    private String agentResponse;
    private long timestamp;
    
    public ConversationTurn(String userInput, String agentResponse, long timestamp) {
        this.userInput = userInput;
        this.agentResponse = agentResponse;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getUserInput() { return userInput; }
    public String getAgentResponse() { return agentResponse; }
    public long getTimestamp() { return timestamp; }
}