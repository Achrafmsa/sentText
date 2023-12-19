package PresentationLayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class RunFromHere extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunFromHere.class.getResource("Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Home.setScene(scene);
        stage.setTitle("Sent Text");
        stage.setScene(scene);
        stage.getIcons().add(new Image(RunFromHere.class.getResource("artificial_intelligence_64px.png").toExternalForm()));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {

                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}