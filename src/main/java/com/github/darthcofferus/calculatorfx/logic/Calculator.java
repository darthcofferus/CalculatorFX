package com.github.darthcofferus.calculatorfx.logic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private static ArrayList<String> listWithExpressions;

    public static String calculatePercent(String decimal) {
        return new BigDecimal(decimal)
                .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64).toString()
                .replace("-", "−")
                .replace(".", ",");
    }

    public static String calculateResult(String expression) {
        return calculate(expression).replace("-", "−").replace(".", ",");
    }

    private static String calculate(String expression) {
        listWithExpressions = ExpressionHandler.toArrayList(expression);
        try {
            calculatePriorityExpression();
            calculateExpression(listWithExpressions);
        } catch (ArithmeticException e) {
            return "Error: " + e.getMessage().toLowerCase();
        }
        return listWithExpressions.getFirst();
    }

    private static void calculatePriorityExpression() {
        while (listWithExpressions.contains("(")) {
            int index = listWithExpressions.indexOf("(");
            listWithExpressions.remove(index);
            ArrayList<String> expression = new ArrayList<>();
            while (!listWithExpressions.get(index).equals(")")) {
                String str = listWithExpressions.get(index);
                if (str.equals("(")) {
                    calculatePriorityExpression();
                } else {
                    expression.add(str);
                    listWithExpressions.remove(index);
                }
            }
            listWithExpressions.remove(index);
            calculateExpression(expression);
            listWithExpressions.addAll(index, expression);
        }
    }

    private static void calculateExpression(ArrayList<String> expression) {
        while (expression.contains("×") || expression.contains("÷")) {
            calculateActions(expression, "×|÷");
        }
        while (expression.contains("+") || expression.contains("-")) {
            calculateActions(expression, "\\+|-");
        }
    }

    private static void calculateActions(ArrayList<String> expression, String actions) {
        for (int i = 0; i < expression.size(); i++) {
            String str = expression.get(i);
            if (str.matches(actions)) {
                var d1 = new BigDecimal(expression.get(i - 1));
                var d2 = new BigDecimal(expression.get(i + 1));
                expression.set(i, getResult(d1, d2, str));
                expression.remove(i - 1);
                expression.remove(i);
                break;
            }
        }
    }

    private static String getResult(BigDecimal d1, BigDecimal d2, String operator) {
        BigDecimal decimal = switch (operator) {
            case "×" -> d1.multiply(d2, MathContext.DECIMAL64);
            case "÷" -> d1.divide(d2, MathContext.DECIMAL64);
            case "+" -> d1.add(d2, MathContext.DECIMAL64);
            case "-" -> d1.subtract(d2, MathContext.DECIMAL64);
            default -> throw new RuntimeException();
        };
        String result = decimal.stripTrailingZeros().toPlainString();
        if (result.matches(".*\\d{17}.*")) {
            throw new ArithmeticException("Result is too long");
        }
        return result;
    }

}
