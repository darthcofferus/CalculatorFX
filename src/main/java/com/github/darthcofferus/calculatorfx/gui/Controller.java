package com.github.darthcofferus.calculatorfx.gui;

import com.github.darthcofferus.calculatorfx.logic.Calculator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Controller {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private InputField inputField;

    @FXML
    private TextField bottomField;

    @FXML
    private void initialize() {
        setAutoCalculating();
        setComponentsResizing();
        setFontResizing();
        setActionsForButtons();
    }

    private void setAutoCalculating() {
        inputField.textProperty().addListener(((observableValue, newValue, oldValue) -> {
            bottomField.clear();
            printResult(false);
        }));
    }

    private void setComponentsResizing() {
        anchorPane.heightProperty().addListener(((observableValue, oldValue, newValue) -> {
            AnchorPane.setBottomAnchor(stackPane, newValue.doubleValue() * 0.725);
            AnchorPane.setTopAnchor(gridPane, newValue.doubleValue() * 0.275);
        }));
    }

    private void setFontResizing() {
        inputField.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            double size = Math.round(newValue.doubleValue() / 3.5);
            inputField.setStyle("-fx-font-size: " + size + ";");
            size = Math.round(newValue.doubleValue() / 7);
            bottomField.setStyle("-fx-font-size: " + size + ";");
        });
    }

    private void setActionsForButtons() {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setFocusTraversable(false);
                setHover(button);
                setAction(button);
            } else if (node instanceof GridPane bracketsPane) {
                for (Node node1 : bracketsPane.getChildren()) {
                    Button button = (Button) node1;
                    button.setFocusTraversable(false);
                    setHover(button);
                    button.setOnAction(event -> inputField.appendText(button.getText()));
                }
            }
        }
    }

    private void setAction(Button button) {
        switch (button.getText()) {
            case "C":
                button.setOnAction(event -> inputField.clear());
                break;
            case "⬅":
                button.setOnAction(event -> {
                    int length = inputField.getLength();
                    if (length == 0) return;
                    inputField.deleteText(length - 1, length);
                });
                break;
            case "=":
                button.setOnAction(event -> printResult(true));
                break;
            case "%":
                button.setOnAction(event -> printPercent());
                break;
            default:
                button.setOnAction(event -> inputField.appendText(button.getText()));
        }
    }

    private void setHover(Button button) {
        button.hoverProperty().addListener(((observableValue, isNotHover, isHover) -> {
            if (isHover && button.getOpacity() == 1) {
                button.setOpacity(0.8);
            } else if (button.getOpacity() == 0.8) {
                button.setOpacity(1);
            }
        }));
    }

    private void printPercent() {
        if (bottomField.getText().startsWith("Error:")) return;
        String expression = inputField.getText();
        if (isCorrect(expression)) {
            String result = Calculator.calculateResult(expression);
            inputField.setText(Calculator.calculatePercent(result.replace(',', '.')));
            result = expression.equals(result) ? "" : " (" + result + ")";
            bottomField.setText("1% of " + expression + result + ": " + inputField.getText());
        }
    }

    private void printResult(boolean isFinal) {
        if (bottomField.getText().startsWith("Error:")) return;
        String expression = inputField.getText();
        if (isCorrect(expression) && expression.matches(".+(\\+|−|×|÷).+")) {
            String begin = isFinal ? inputField.getText() + "=" : "";
            String result = Calculator.calculateResult(expression);
            if (isFinal) {
                if (result.startsWith("Error")) {
                    bottomField.setText(result);
                } else {
                    inputField.setText(result);
                }
            }
            bottomField.setText(begin + result);
        }
    }

    private boolean isCorrect(String expression) {
        return !expression.isEmpty() && inputField.hasOnlyCorrectBrackets() &&
                !expression.matches(".*(\\+|−|×|÷|,)$");
    }

}
