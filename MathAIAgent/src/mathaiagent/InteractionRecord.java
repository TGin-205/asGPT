package mathaiagent;

import java.util.Objects;

public class InteractionRecord {
    private String input;
    private String output;
    private boolean wasSuccessful;
    private double responseTime;
    private long timestamp;
    
    public InteractionRecord(String input, String output, boolean wasSuccessful, 
                           double responseTime, long timestamp) {
        this.input = input;
        this.output = output;
        this.wasSuccessful = wasSuccessful;
        this.responseTime = responseTime;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getInput() { 
        return input; 
    }
    
    public String getOutput() { 
        return output; 
    }
    
    public boolean wasSuccessful() { 
        return wasSuccessful; 
    }
    
    public double getResponseTime() { 
        return responseTime; 
    }
    
    public long getTimestamp() { 
        return timestamp; 
    }
    
    // Setters (if needed for modification)
    public void setInput(String input) {
        this.input = input;
    }
    
    public void setOutput(String output) {
        this.output = output;
    }
    
    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }
    
    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("InteractionRecord{input='%s', successful=%s, responseTime=%.2fms, timestamp=%d}", 
                           input, wasSuccessful, responseTime, timestamp);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        InteractionRecord that = (InteractionRecord) obj;
        return timestamp == that.timestamp && 
               Double.compare(that.responseTime, responseTime) == 0 &&
               wasSuccessful == that.wasSuccessful &&
               input.equals(that.input) &&
               output.equals(that.output);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(input, output, wasSuccessful, responseTime, timestamp);
    }
}