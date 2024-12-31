module com.github.darthcofferus.calculatorfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires junique;

    opens com.github.darthcofferus.calculatorfx.gui to javafx.fxml;
    exports com.github.darthcofferus.calculatorfx;
}