package mathaiagent;

// ===== PERFORMANCE MONITOR =====
import java.util.*;

// Performance Monitor - Theo dõi hiệu suất
class PerformanceMonitor {
    private List<Double> responseTimes;
    private int totalRequests;
    private int successfulRequests;
    private long startTime;
    
    public PerformanceMonitor() {
        this.responseTimes = new ArrayList<>();
        this.totalRequests = 0;
        this.successfulRequests = 0;
        this.startTime = System.currentTimeMillis();
    }
    
    public void recordRequest(double responseTime, boolean successful) {
        responseTimes.add(responseTime);
        totalRequests++;
        if (successful) successfulRequests++;
    }
    
    public void showPerformanceStats() {
        System.out.println("\n⚡ THỐNG KÊ HIỆU SUẤT");
        System.out.println("════════════════════════════════════");
        System.out.println("📊 Tổng số yêu cầu: " + totalRequests);
        System.out.println("✅ Yêu cầu thành công: " + successfulRequests);
        System.out.printf("📈 Tỷ lệ thành công: %.1f%%\n", 
            (double) successfulRequests / totalRequests * 100);
        
        if (!responseTimes.isEmpty()) {
            double avgTime = responseTimes.stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);
            double maxTime = responseTimes.stream()
                .mapToDouble(Double::doubleValue)
                .max().orElse(0);
            double minTime = responseTimes.stream()
                .mapToDouble(Double::doubleValue)
                .min().orElse(0);
            
            System.out.printf("⏱️ Thời gian phản hồi trung bình: %.2f ms\n", avgTime);
            System.out.printf("🚀 Nhanh nhất: %.2f ms\n", minTime);
            System.out.printf("🐌 Chậm nhất: %.2f ms\n", maxTime);
        }
        
        long uptime = System.currentTimeMillis() - startTime;
        System.out.printf("⏰ Thời gian hoạt động: %.1f phút\n", uptime / 60000.0);
    }
    
    // Getters
    public List<Double> getResponseTimes() { return new ArrayList<>(responseTimes); }
    public int getTotalRequests() { return totalRequests; }
    public int getSuccessfulRequests() { return successfulRequests; }
    public double getSuccessRate() { 
        return totalRequests > 0 ? (double) successfulRequests / totalRequests * 100 : 0; 
    }
}