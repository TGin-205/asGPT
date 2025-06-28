package mathaiagent;

// ===== MAIN AI AGENT CLASS =====

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
        System.out.println("║                    🤖 AI MATH AGENT 2.0                     ║");
        System.out.println("║                  Tro ly toan hoc thong minh                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Tinh nang:                                                   ║");
        System.out.println("║ • Hieu ngon ngu tu nhien tieng Viet & English               ║");
        System.out.println("║ • Hoc tu tuong tac cua ban                                   ║");
        System.out.println("║ • Goi y bai toan phu hop                                     ║");
        System.out.println("║ • Giai thich chi tiet                                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Lenh dac biet:                                               ║");
        System.out.println("║ 'help' - Huong dan su dung                                  ║");
        System.out.println("║ 'stats' - Thong ke hoc tap                                  ║");
        System.out.println("║ 'recommend' - Goi y bai toan                                ║");
        System.out.println("║ 'exit' - Thoat chuong trinh                                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("\n💡 Thu noi: 'Tinh sin(30 do)' hoac 'What is 5 squared?'");
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
        System.out.println("║                     👋 TAM BIET!                             ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Cam on ban da su dung AI Math Agent!                       ║");
        System.out.println("║  Toi da hoc duoc nhieu tu cuoc tro chuyen nay.               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        // Show final learning stats
        learningSystem.showLearningStats();
    }

    public static void main(String[] args) {
        EnhancedMathAIAgent agent = new EnhancedMathAIAgent();
        agent.start();
    }

    private void processNaturalLanguageInput(String input) {
        long startTime = System.currentTimeMillis();

        // Process input through NLP
        ProcessedInput processed = nlp.process(input);

        System.out.println("🧠 Phan tich: " + processed.getIntent()
                + " | Do phuc tap: " + getComplexityLabel(processed.getComplexityLevel()));

        // Handle based on intent
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

        // Record interaction for learning
        learningSystem.recordInteraction(input, "Response generated", true, responseTime);
    }

    private void handleCalculation(ProcessedInput processed) {
        if (processed.getMathExpression().isEmpty()) {
            System.out.println("❌ Khong tim thay bieu thuc toan hoc trong cau hoi cua ban.");
            System.out.println("💡 Thu: 'Tinh 5 + 3' hoac 'What is 2 * 7?'");
            return;
        }

        EvaluationResult result = evaluator.evaluate(processed.getMathExpression(), processed.getParameters());

        if (result.isSuccessful()) {
            System.out.println("✅ Ket qua: " + result.getFormattedResult());
            System.out.printf("⚡ Thoi gian xu ly: %.2f ms\n", result.getResponseTime());

            // Provide additional context based on complexity
            if (processed.getComplexityLevel() >= 2) {
                provideAdditionalContext(processed.getMathExpression(), result.getNumericResult());
            }
        } else {
            System.out.println("❌ " + result.getMessage());
            System.out.println("💡 Kiem tra lai cu phap hoac thu cach dien dat khac.");
        }
    }

    private void handleExplanation(ProcessedInput processed) {
        System.out.println("🎓 Giai thich:");

        if (!processed.getMathExpression().isEmpty()) {
            // Provide step-by-step explanation
            explainMathematicalConcept(processed.getMathExpression());
        } else {
            System.out.println("Hay cung cap mot bieu thuc toan hoc cu the de toi co the giai thich chi tiet.");
        }
    }

    private void handleComparison(ProcessedInput processed) {
        System.out.println("⚖️ So sanh:");
        System.out.println("Tinh nang so sanh dang duoc phat trien...");
    }

    private void handleGeneral(ProcessedInput processed) {
        System.out.println("🤔 Toi hieu ban muon: " + processed.getOriginalInput());

        if (!processed.getMathExpression().isEmpty()) {
            handleCalculation(processed);
        } else {
            System.out.println("💭 Ban co the dien dat ro hon hoac thu mot phep tinh cu the khong?");
        }
    }

    private String getComplexityLabel(int level) {
        switch (level) {
            case 1:
                return "Co ban";
            case 2:
                return "Trung binh";
            case 3:
                return "Nang cao";
            default:
                return "Khong xac dinh";
        }
    }

    private void displayHelp() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    📖 HUONG DAN SU DUNG                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Cac lenh co ban:                                            ║");
        System.out.println("║ • Phep tinh: '5 + 3', 'tinh 10 - 4'                         ║");
        System.out.println("║ • Luong giac: 'sin(30)', 'cos(45 do)'                       ║");
        System.out.println("║ • Mu va can: '2^3', 'sqrt(25)'                              ║");
        System.out.println("║ • Logarithm: 'log(100)', 'ln(e)'                            ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Vi du cu phap:                                              ║");
        System.out.println("║ • 'Tinh 5 cong 3'                                           ║");
        System.out.println("║ • 'What is 2 times 7?'                                      ║");
        System.out.println("║ • 'Calculate sin(30 degrees)'                               ║");
        System.out.println("║ • 'Giai thich sqrt(16)'                                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private void displayRecommendations() {
        System.out.println("\n🎯 GOI Y BAI TOAN PHU HOP:");
        System.out.println("════════════════════════════════════════════════════════════");
        
        List<String> recommendations = learningSystem.getRecommendations();
        for (int i = 0; i < recommendations.size(); i++) {
            System.out.println((i + 1) + ". " + recommendations.get(i));
        }
        
        System.out.println("\n💡 Thu mot trong nhung goi y tren hoac tiep tuc voi cau hoi cua ban!");
    }

    private void explainMathematicalConcept(String mathExpression) {
        ExplanationGenerator explainer = new ExplanationGenerator();
        String explanation = explainer.generateStepByStepSolution(mathExpression, 0);
        System.out.println(explanation);
    }

    private void provideAdditionalContext(String mathExpression, double result) {
        System.out.println("\n📚 THONG TIN THEM:");
        System.out.println("─────────────────────────────────────");
        
        if (mathExpression.contains("sin") || mathExpression.contains("cos") || mathExpression.contains("tan")) {
            System.out.println("🔄 Luu y: Ket qua luong giac co the thay doi tuy theo don vi (do/radian)");
            System.out.println("📐 Gia tri dac biet: sin(30°) = 0.5, cos(60°) = 0.5, tan(45°) = 1");
        }
        
        if (mathExpression.contains("sqrt")) {
            System.out.println("🌟 Can bac hai luon co gia tri duong trong so thuc");
            System.out.println("📝 Vi du: √16 = 4 (chi lay gia tri duong)");
        }
        
        if (mathExpression.contains("^") || mathExpression.contains("pow")) {
            System.out.println("⚡ Luy thua: a^n = a × a × ... × a (n lan)");
            System.out.println("🔢 Luu y: Moi so mu 0 deu bang 1 (tru 0^0)");
        }
    }
}