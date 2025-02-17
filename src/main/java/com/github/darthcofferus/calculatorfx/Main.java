package com.github.darthcofferus.calculatorfx;

import com.github.darthcofferus.calculatorfx.gui.CalculatorScene;
import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.invoke.MethodHandles;

public class Main extends Application {

    public static void main(String[] args) {
        try {
            JUnique.acquireLock(MethodHandles.lookup().lookupClass().getPackageName());
        } catch (AlreadyLockedException e) {
            System.exit(0);
        }
        launch();
    }

    @Override
    public void start(Stage stage) {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/GUI.fxml"));
        try {
            root = loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("CalculatorFX");
        stage.getIcons().add(new Image(getClass().getResource("icon.png").toString()));
        stage.setScene(new CalculatorScene(root, Color.TRANSPARENT));
        stage.setMinWidth(328);
        stage.setMinHeight(600);
        stage.setWidth(stage.getMinWidth());
        stage.setHeight(stage.getMinHeight());
        stage.show();
    }

}