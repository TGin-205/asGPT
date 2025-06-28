package mathaiagent;

import java.util.Map;

public class ProcessedInput {
    private String intent;
    private String mathExpression;
    private Map<String, Object> parameters;
    private int complexityLevel;
    private String originalInput;
    
    public ProcessedInput(String intent, String mathExpression, Map<String, Object> parameters, 
                         int complexityLevel, String originalInput) {
        this.intent = intent;
        this.mathExpression = mathExpression;
        this.parameters = parameters;
        this.complexityLevel = complexityLevel;
        this.originalInput = originalInput;
    }
    
    // Getters
    public String getIntent() { 
        return intent; 
    }
    
    public String getMathExpression() { 
        return mathExpression; 
    }
    
    public Map<String, Object> getParameters() { 
        return parameters; 
    }
    
    public int getComplexityLevel() { 
        return complexityLevel; 
    }
    
    public String getOriginalInput() { 
        return originalInput; 
    }
    
    // Setters (optional, for modification)
    public void setIntent(String intent) {
        this.intent = intent;
    }
    
    public void setMathExpression(String mathExpression) {
        this.mathExpression = mathExpression;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public void setComplexityLevel(int complexityLevel) {
        this.complexityLevel = complexityLevel;
    }
    
    @Override
    public String toString() {
        return String.format("ProcessedInput{intent='%s', mathExpression='%s', complexityLevel=%d, originalInput='%s'}", 
                           intent, mathExpression, complexityLevel, originalInput);
    }
}