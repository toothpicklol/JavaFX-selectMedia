module tw.toothpick.selectmediav2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens tw.toothpick.selectmediav2 to javafx.fxml;
    exports tw.toothpick.selectmediav2;
}