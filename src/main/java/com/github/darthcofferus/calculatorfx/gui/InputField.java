package com.github.darthcofferus.calculatorfx.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class InputField extends TextField {

    public InputField() {
        setEditable(false);
        setContextMenu(new ContextMenu());
        setTextFormatter(new TextFormatter<>(change -> {
            String input = change.getControlNewText();
            if (inputIsIncorrect(input)) {
                change.setText("");
            } else if (input.matches(".*(\\+|−|×|÷|\\(|^)0\\d+")) {
                StringBuilder sB = new StringBuilder(input);
                int length = sB.length();
                sB.replace(length - 2, length - 1, "");
                setText(sB.toString());
                change.setText("");
            }
            return change;
        }));
    }

    private boolean inputIsIncorrect(String input) {
        return getCharsCount(input, '(') < getCharsCount(input, ')') ||
                input.matches(".*[(]\\d+(,|,\\d+)?[)]") ||
                input.matches(".*(\\+|−|×|÷|\\(|^)0{2}") ||
                input.matches(".*(\\d|[)])[(]") ||
                !input.matches("([(]*−?($|\\d+(,$|,\\d+)?)([)]*($|(\\+|−|×|÷)))?)*") ||
                input.matches(".*\\d{17}");
    }

    private int getCharsCount(String str, char x) {
        int result = 0;
        for (char ch : str.toCharArray()) {
            if (ch == x) result++;
        }
        return result;
    }

    boolean hasOnlyCorrectBrackets() {
        return getCharsCount(getText(), '(') == getCharsCount(getText(), ')');
    }

}
