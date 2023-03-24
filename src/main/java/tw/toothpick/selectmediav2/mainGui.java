package tw.toothpick.selectmediav2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class mainGui extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainGui.class.getResource("gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("今晚看什麼");

        Image icon=new Image(mainGui.class.getResourceAsStream("/image/think.png"));
        stage.getIcons().add(icon);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
