package mathaiagent;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.Map;

/**
 * Enhanced Math Evaluator - Đánh giá và tính toán biểu thức toán học
 */
public class EnhancedMathEvaluator {
    public EnhancedMathEvaluator() {
        // Không cần khởi tạo gì thêm
    }
    
    public EvaluationResult evaluate(String expression, Map<String, Object> parameters) {
        long startTime = System.nanoTime();
        
        try {
            // Preprocess expression
            expression = preprocessExpression(expression);

            Expression exp = new ExpressionBuilder(expression)
                    .variables("pi", "e")
                    .build()
                    .setVariable("pi", Math.PI)
                    .setVariable("e", Math.E);

            double numericResult = exp.evaluate();

            String formattedResult = formatResult(numericResult, parameters);

            long endTime = System.nanoTime();
            double responseTime = (endTime - startTime) / 1_000_000.0; // ms

            return new EvaluationResult(numericResult, formattedResult, true,
                    "Tính toán thành công", responseTime, expression);

        } catch (Exception e) {
            long endTime = System.nanoTime();
            double responseTime = (endTime - startTime) / 1_000_000.0;

            return new EvaluationResult(0, "", false,
                    "Lỗi: " + e.getMessage(), responseTime, expression);
        }
    }

    private String preprocessExpression(String expression) {
        // Thay ^ bằng pow, exp4j hỗ trợ ^ nên không cần
        // Thay sqrt(x) thành sqrt(x)
        return expression.replaceAll("sqrt", "sqrt");
    }

    private String formatResult(double result, Map<String, Object> parameters) {
        int decimalPlaces = parameters.containsKey("decimalPlaces") ?
                (Integer) parameters.get("decimalPlaces") : 2;

        if (result == Math.floor(result)) {
            return String.format("%.0f", result);
        } else {
            return String.format("%." + decimalPlaces + "f", result);
        }
    }
}