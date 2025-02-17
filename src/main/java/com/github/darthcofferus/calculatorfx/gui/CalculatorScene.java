package com.github.darthcofferus.calculatorfx.gui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

public class CalculatorScene extends Scene {

    private GridPane gridPane;

    private boolean ctrlIsPressed;

    public CalculatorScene(Parent parent, Paint paint) {
        super(parent, paint);
        for (Node node : getRoot().getChildrenUnmodifiable()) {
            if (node instanceof GridPane gridPane) {
                this.gridPane = gridPane;
            }
        }
        setActionForKeyTyped();
        setActionForKeyPressed();
        setActionForKeyReleased();
    }

    private void setActionForKeyTyped() {
        addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            String keyText = replaceChars(keyEvent.getCharacter());
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button button) {
                    String btnText = button.getText();
                    if (keyText.equals(btnText)) {
                        pressButton(button);
                        break;
                    }
                } else if (node instanceof GridPane bracketsPane) {
                    for (Node node1 : bracketsPane.getChildren()) {
                        Button button = (Button) node1;
                        String btnText = button.getText();
                        if (keyText.equals(btnText)) {
                            pressButton(button);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void setActionForKeyPressed() {
        addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.CONTROL) ctrlIsPressed = true;
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button button) {
                    String btnText = button.getText();
                    if ((code == KeyCode.BACK_SPACE && btnText.equals("⬅")) ||
                            (!ctrlIsPressed && code == KeyCode.C || code == KeyCode.DELETE && btnText.equals("C")) ||
                            (code == KeyCode.ENTER && btnText.equals("="))) {
                        pressButton(button);
                        break;
                    }
                }
            }
        });
    }

    private void setActionForKeyReleased() {
        addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.CONTROL) ctrlIsPressed = false;
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button button) {
                    resetOpacity(button);
                } else if (node instanceof GridPane bracketsPane) {
                    for (Node node1 : bracketsPane.getChildren()) {
                        Button button = (Button) node1;
                        resetOpacity(button);
                    }
                }
            }
        });
    }

    private String replaceChars(String str) {
        return switch (str) {
            case "-" -> "−";
            case "*" -> "×";
            case "/" -> "÷";
            case "." -> ",";
            default -> str;
        };
    }

    private void pressButton(Button button) {
        button.setOpacity(0.5);
        button.fire();
    }

    private void resetOpacity(Button button) {
        if (button.isHover()) {
            button.setOpacity(0.8);
        } else {
            button.setOpacity(1);
        }
    }

}
