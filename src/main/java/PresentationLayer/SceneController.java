package PresentationLayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SceneController {

    public static void SceneSwitcher(@NotNull ActionEvent event, String sceneFxml, Scene DestinationScene) throws IOException {
        Parent root = FXMLLoader.load(SceneController.class.getResource(sceneFxml));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        if (!(DestinationScene== null)) {
            scene = DestinationScene;
        }
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void SwitchToCrawlerScene(ActionEvent actionEvent) throws IOException {
        SceneController.SceneSwitcher(actionEvent, "WebCrawlerScene.fxml", WebCrawlerController.getScene());
    }

    public static void SwitchToAnnotationScene(ActionEvent actionEvent) throws IOException {
        SceneController.SceneSwitcher(actionEvent, "AnnotationScene.fxml", AnnotationSceneController.getScene());
    }

    public static void SwitchToTrainingScene(ActionEvent actionEvent) throws IOException {
        SceneController.SceneSwitcher(actionEvent, "TrainingScene.fxml", TrainingDataController.getScene());
    }

    public static void SwitchToHome(ActionEvent actionEvent) throws IOException{
        SceneController.SceneSwitcher(actionEvent, "Home.fxml", Home.getScene());
    }
}
