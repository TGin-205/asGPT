package mathaiagent;

/**
 * Evaluation Result - Lưu trữ kết quả đánh giá biểu thức toán học
 */
public class EvaluationResult {
    private double numericResult;
    private String formattedResult;
    private boolean successful;
    private String message;
    private double responseTime;
    private String originalExpression;
    
    public EvaluationResult(double numericResult, String formattedResult, boolean successful,
                           String message, double responseTime, String originalExpression) {
        this.numericResult = numericResult;
        this.formattedResult = formattedResult;
        this.successful = successful;
        this.message = message;
        this.responseTime = responseTime;
        this.originalExpression = originalExpression;
    }
    
    // Getters
    public double getNumericResult() { 
        return numericResult; 
    }
    
    public String getFormattedResult() { 
        return formattedResult; 
    }
    
    public boolean isSuccessful() { 
        return successful; 
    }
    
    public String getMessage() { 
        return message; 
    }
    
    public double getResponseTime() { 
        return responseTime; 
    }
    
    public String getOriginalExpression() { 
        return originalExpression; 
    }
    
    // Setters (nếu cần)
    public void setNumericResult(double numericResult) {
        this.numericResult = numericResult;
    }
    
    public void setFormattedResult(String formattedResult) {
        this.formattedResult = formattedResult;
    }
    
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }
    
    public void setOriginalExpression(String originalExpression) {
        this.originalExpression = originalExpression;
    }
    
    @Override
    public String toString() {
        return String.format("EvaluationResult{result=%.2f, formatted='%s', successful=%b, time=%.2fms}", 
                           numericResult, formattedResult, successful, responseTime);
    }
}