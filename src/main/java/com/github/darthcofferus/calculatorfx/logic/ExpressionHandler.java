package com.github.darthcofferus.calculatorfx.logic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionHandler {

    private static ArrayList<String> arrayList;

    static ArrayList<String> toArrayList(String expression) {
        expression = expression.replaceAll(",", ".").replaceAll("−", "-");
        arrayList = new ArrayList<>();
        Matcher matcher = Pattern.compile("[(]*-?\\d+(\\.\\d+)?[)]*").matcher(expression);
        while (matcher.find()) {
            String str = matcher.group();
            int previous = matcher.start() - 1;
            if (previous >= 0 && !expression.substring(previous, previous + 1).matches("\\+|-|×|÷")) {
                str = str.replace("-", "");
            }
            add(str);
            int next = matcher.end();
            if (next < expression.length()) {
                arrayList.add(expression.charAt(next) + "");
            }
        }
        return arrayList;
    }

    private static void add(String str) {
        if (str.contains("(")) {
            for (char ch : str.toCharArray()) {
                if (ch == '(') {
                    arrayList.add("(");
                } else {
                    break;
                }
            }
            arrayList.add(str.replaceAll("\\(", ""));
        } else if (str.contains(")")) {
            arrayList.add(str.replaceAll("\\)", ""));
            for (char ch : str.toCharArray()) {
                if (!Character.isDigit(ch) && ch != '.') {
                    arrayList.add(")");
                }
            }
        } else {
            arrayList.add(str);
        }
    }

}