package PresentationLayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Home {

    static {
        try {
            scene = new Scene(FXMLLoader.load(Home.class.getResource("Home.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Scene scene;

    public void SwitchToCrawlerScene(ActionEvent actionEvent) throws IOException {
        SceneController.SwitchToCrawlerScene(actionEvent);
    }

    public void SwitchToAnnotationScene(ActionEvent actionEvent) throws IOException {
        SceneController.SwitchToAnnotationScene(actionEvent);
    }

    public void SwitchToTrainingScene(ActionEvent actionEvent) throws IOException {
        SceneController.SwitchToTrainingScene(actionEvent);
    }

    public static void setScene(Scene s) {
        Home.scene = s;
    }

    public static Scene getScene() {
        return scene;
    }

}
