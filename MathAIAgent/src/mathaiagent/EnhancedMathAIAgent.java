package mathaiagent;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class EnhancedMathAIAgent {

    private NaturalLanguageProcessor nlp;
    private EnhancedMathEvaluator evaluator;
    private LearningSystem learningSystem;
    private Scanner scanner;
    private boolean isRunning;

    public EnhancedMathAIAgent() {
        this.nlp = new NaturalLanguageProcessor();
        this.evaluator = new EnhancedMathEvaluator();
        this.learningSystem = new LearningSystem();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public void start() {
        displayWelcomeMessage();

        while (isRunning) {
            System.out.print("\n🤖 AI > ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            handleCommand(input);
        }

        displayGoodbyeMessage();
    }

    private void displayWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🤖 TRỢ LÝ TOÁN HỌC AI 2.0                 ║");
        System.out.println("║                Trợ lý toán học thông minh                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Tính năng:                                                   ║");
        System.out.println("║ • Hiểu ngôn ngữ tự nhiên tiếng Việt & English               ║");
        System.out.println("║ • Học từ tương tác của bạn                                   ║");
        System.out.println("║ • Gợi ý bài toán phù hợp                                     ║");
        System.out.println("║ • Giải thích chi tiết                                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Lệnh đặc biệt:                                               ║");
        System.out.println("║ 'help' - Hướng dẫn sử dụng                                  ║");
        System.out.println("║ 'stats' - Thống kê học tập                                  ║");
        System.out.println("║ 'recommend' - Gợi ý bài toán                                ║");
        System.out.println("║ 'exit' - Thoát chương trình                                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("\n💡 Thử nói: 'Tính sin(30 độ)' hoặc 'What is 5 squared?'");
    }

    private void handleCommand(String input) {
        String lowerInput = input.toLowerCase();

        switch (lowerInput) {
            case "exit":
            case "quit":
            case "thoat":
                isRunning = false;
                return;

            case "help":
            case "huong dan":
                displayHelp();
                return;

            case "stats":
            case "thong ke":
                learningSystem.showLearningStats();
                return;

            case "recommend":
            case "goi y":
                displayRecommendations();
                return;

            default:
                processNaturalLanguageInput(input);
        }
    }

    private void displayGoodbyeMessage() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     👋 TẠM BIỆT!                             ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Cảm ơn bạn đã sử dụng Trợ lý Toán Học AI!                  ║");
        System.out.println("║  Tôi đã học được nhiều từ cuộc trò chuyện này.              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        learningSystem.showLearningStats();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));

        EnhancedMathAIAgent agent = new EnhancedMathAIAgent();
        agent.start();
    }

    private void processNaturalLanguageInput(String input) {
        long startTime = System.currentTimeMillis();

        ProcessedInput processed = nlp.process(input);

        System.out.println("🧠 Phân tích: " + processed.getIntent()
                + " | Độ phức tạp: " + getComplexityLabel(processed.getComplexityLevel()));

        switch (processed.getIntent()) {
            case "CALCULATION":
                handleCalculation(processed);
                break;
            case "EXPLANATION":
                handleExplanation(processed);
                break;
            case "COMPARISON":
                handleComparison(processed);
                break;
            default:
                handleGeneral(processed);
        }

        long endTime = System.currentTimeMillis();
        double responseTime = endTime - startTime;

        learningSystem.recordInteraction(input, "Phản hồi được tạo", true, responseTime);
    }

    private void handleCalculation(ProcessedInput processed) {
        if (processed.getMathExpression().isEmpty()) {
            System.out.println("❌ Không tìm thấy biểu thức toán học trong câu hỏi của bạn.");
            System.out.println("💡 Thử: 'Tính 5 + 3' hoặc 'What is 2 * 7?'");
            return;
        }

        EvaluationResult result = evaluator.evaluate(processed.getMathExpression(), processed.getParameters());

        if (result.isSuccessful()) {
            System.out.println("✅ Kết quả: " + result.getFormattedResult());
            System.out.printf("⚡ Thời gian xử lý: %.2f ms\n", result.getResponseTime());

            if (processed.getComplexityLevel() >= 2) {
                provideAdditionalContext(processed.getMathExpression(), result.getNumericResult());
            }
        } else {
            System.out.println("❌ " + result.getMessage());
            System.out.println("💡 Kiểm tra lại cú pháp hoặc thử cách diễn đạt khác.");
        }
    }

    private void handleExplanation(ProcessedInput processed) {
        System.out.println("🎓 Giải thích:");

        if (!processed.getMathExpression().isEmpty()) {
            explainMathematicalConcept(processed.getMathExpression());
        } else {
            System.out.println("Hãy cung cấp một biểu thức toán học cụ thể để tôi có thể giải thích chi tiết.");
        }
    }

    private void handleComparison(ProcessedInput processed) {
        System.out.println("⚖️ So sánh:");
        System.out.println("Tính năng so sánh đang được phát triển...");
    }

    private void handleGeneral(ProcessedInput processed) {
        System.out.println("🤔 Tôi hiểu bạn muốn: " + processed.getOriginalInput());

        if (!processed.getMathExpression().isEmpty()) {
            handleCalculation(processed);
        } else {
            System.out.println("💭 Bạn có thể diễn đạt rõ hơn hoặc thử một phép tính cụ thể không?");
        }
    }

    private String getComplexityLabel(int level) {
        switch (level) {
            case 1:
                return "Cơ bản";
            case 2:
                return "Trung bình";
            case 3:
                return "Nâng cao";
            default:
                return "Không xác định";
        }
    }

    private void displayHelp() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    📖 HƯỚNG DẪN SỬ DỤNG                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Các lệnh cơ bản:                                            ║");
        System.out.println("║ • Phép tính: '5 + 3', 'tính 10 - 4'                         ║");
        System.out.println("║ • Lượng giác: 'sin(30)', 'cos(45 độ)'                       ║");
        System.out.println("║ • Mũ và căn: '2^3', 'sqrt(25)'                              ║");
        System.out.println("║ • Logarit: 'log(100)', 'ln(e)'                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Ví dụ cú pháp:                                              ║");
        System.out.println("║ • 'Tính 5 cộng 3'                                           ║");
        System.out.println("║ • 'What is 2 times 7?'                                      ║");
        System.out.println("║ • 'Calculate sin(30 degrees)'                               ║");
        System.out.println("║ • 'Giải thích sqrt(16)'                                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private void displayRecommendations() {
        System.out.println("\n🎯 GỢI Ý BÀI TOÁN PHÙ HỢP:");
        System.out.println("════════════════════════════════════════════════════════════");

        List<String> recommendations = learningSystem.getRecommendations();
        for (int i = 0; i < recommendations.size(); i++) {
            System.out.println((i + 1) + ". " + recommendations.get(i));
        }

        System.out.println("\n💡 Thử một trong những gợi ý trên hoặc tiếp tục với câu hỏi của bạn!");
    }

    private void explainMathematicalConcept(String mathExpression) {
        ExplanationGenerator explainer = new ExplanationGenerator();
        String explanation = explainer.generateStepByStepSolution(mathExpression, 0);
        System.out.println(explanation);
    }

    private void provideAdditionalContext(String mathExpression, double result) {
        System.out.println("\n📚 THÔNG TIN THÊM:");
        System.out.println("─────────────────────────────────────");

        if (mathExpression.contains("sin") || mathExpression.contains("cos") || mathExpression.contains("tan")) {
            System.out.println("🔄 Lưu ý: Kết quả lượng giác có thể thay đổi tuỳ theo đơn vị (độ/radian)");
            System.out.println("📐 Giá trị đặc biệt: sin(30°) = 0.5, cos(60°) = 0.5, tan(45°) = 1");
        }

        if (mathExpression.contains("sqrt")) {
            System.out.println("🌟 Căn bậc hai luôn có giá trị dương trong số thực");
            System.out.println("📝 Ví dụ: √16 = 4 (chỉ lấy giá trị dương)");
        }

        if (mathExpression.contains("^") || mathExpression.contains("pow")) {
            System.out.println("⚡ Lũy thừa: a^n = a × a × ... × a (n lần)");
            System.out.println("🔢 Lưu ý: Mọi số mũ 0 đều bằng 1 (trừ 0^0)");
        }
    }
}
