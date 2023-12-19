package PresentationLayer;

import BagOfWords.Language;
import BagOfWords.SimpleTokenizer;
import BagOfWords.bagOfWords;
import BuisnessLayer.Annotation;
import BuisnessLayer.Comment;
import BuisnessLayer.Comments;
import com.alibaba.fastjson.JSON;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

import static DataAccessLayer.GetOrSaveData.getFromFile;
import static DataAccessLayer.GetOrSaveData.saveToFile;
import static PresentationLayer.MyAlert.WarningAlert;
import static PresentationLayer.MyListView.getFromListView;
import static PresentationLayer.MyListView.showCommentInListView;

public class AnnotationSceneController implements Initializable {
    static {
        try {
            scene = new Scene(FXMLLoader.load(AnnotationSceneController.class.getResource("AnnotationScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Scene scene;
    private Project proj;

    @FXML
    private Button btnCreateCommentList, btnRemoveCommentList, BtnCrawlerScene,
            btnCreateAnnotation, btnRemoveAnnotation, btnAddComments,
            btnAddToAnnotation, btnUndoFromAnnotation, btnSelectComList,
            btnDeselectComList, btnSelectAnnotation, btnDeselectAnnotation, btnSaveToFile;
    @FXML
    private TextField TxfCommentList, TxfAnnotation;
    @FXML
    private ComboBox<Comments> ComBoxCommentList;
    @FXML
    private ComboBox<Annotation> ComBoxAnnotation;
    @FXML
    private ListView CommentListView, AnnotationListView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CommentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        AnnotationListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        KeyPressed();
        WhenChangeHappen();
    }

    public static void setScene(Scene s) {
        AnnotationSceneController.scene = s;
    }

    public static Scene getScene() {
        return scene;
    }

    public void OpenProject(ActionEvent actionEvent) {
        MyFileChooser myChooser = new MyFileChooser("Open Project", "proj","src/main/resources/Project");
        myChooser.OpenProject(actionEvent);
        File file = myChooser.getFile();
        Project proj = Project.OpenProject(file);

        for (Comments comms : proj.getVc()) {
            ComBoxCommentList.getItems().add(comms);
        }
    }

    public void SaveProject(ActionEvent actionEvent) {
        Vector<Comments> vc = new Vector<>();
        for (Comments obj : ComBoxCommentList.getItems().stream().toList()) {
            vc.add(obj);
        }
        Project proj = new Project(vc, null);
        MyFileChooser myChooser = new MyFileChooser("Save Project", "proj","src/main/resources/Project");
        myChooser.SaveProject(actionEvent);
        File file = myChooser.getFile();
        if (!(file == null)) {
            proj.SaveProject(file, proj);
        }
    }

    public void CreateAnnotation(ActionEvent actionEvent) {
        String annotation_name = TxfAnnotation.getText().replaceAll(" ", "");
        if (!(annotation_name == null || annotation_name.trim().isEmpty())) {
            Annotation anno = new Annotation(annotation_name);
            ComBoxAnnotation.getItems().add(anno);

            ComBoxAnnotation.getSelectionModel().selectLast();
            System.out.println("The annotation was successfully added!" + anno.getNom());
        } else {
            WarningAlert("Please write something!", "Annotation Text Field");
        }
        TxfAnnotation.setText("");
    }

    public void RemoveAnnotation(ActionEvent actionEvent) {
        if (ComBoxAnnotation.getItems().isEmpty()) {
            WarningAlert("No Annotation exist!", "");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Field");
            alert.setContentText("Are you sure!");
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
            } else {
                ComBoxAnnotation.getItems().remove(ComBoxAnnotation.getSelectionModel().getSelectedItem());
                if (!ComBoxAnnotation.getItems().isEmpty())
                    ComBoxAnnotation.getSelectionModel().selectFirst();
            }
        }
    }

    public void CreateCommentList(ActionEvent actionEvent) {
        String CommentList = TxfCommentList.getText().replaceAll(" ", "");
        ArrayList<Comment> CommentArrayList = new ArrayList<Comment>();

        if (!(CommentList == null || CommentList.trim().isEmpty())) {
            Comments commList = new Comments(CommentArrayList, CommentList);
            ComBoxCommentList.getItems().add(commList);

            ComBoxCommentList.getSelectionModel().selectLast();
            System.out.println("The CommentList was successfully added!" + commList.getNom());
        } else {
            WarningAlert("Please write something!", "Comment List Text Field");
        }
        TxfCommentList.setText("");
    }

    public void RemoveCommentList(ActionEvent actionEvent) {
        if (ComBoxCommentList.getItems().isEmpty()) {
            WarningAlert("No Comment list exist!", "");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Field");
            alert.setContentText("Are you sure!");
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
            } else {
                ComBoxCommentList.getItems().remove(ComBoxCommentList.getSelectionModel().getSelectedItem());
                if (!ComBoxCommentList.getItems().isEmpty())
                    ComBoxCommentList.getSelectionModel().selectFirst();
            }
        }

    }

    public void AddComments(ActionEvent actionEvent) throws IOException {
        if (ComBoxCommentList.getItems().isEmpty()) {
            WarningAlert("No Comment list exist!", "Add a comment List");
        } else {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            MyFileChooser myChooser = new MyFileChooser(stage, "Open Comments list", "TXT","src/main/resources/Comments");
            myChooser.showMyFileChooser();
            File file = myChooser.getFile();
            //Create class in the future and add a if(list not exist)
            Comments commList = (Comments) ComBoxCommentList.getSelectionModel().getSelectedItem();
            ArrayList<Comment> ac = getFromFile(file);
            commList.setAcomm(ac);
            showCommentInListView(commList.getAcomm(), CommentListView);
        }
    }

    public void AddToAnnotation(ActionEvent actionEvent) {
        if (ComBoxAnnotation.getItems().isEmpty()) {
            WarningAlert("No Annotation exist!", "Add an Annotation");
        } else {
            if (CommentListView.getItems().isEmpty()) {
                WarningAlert("No Comment list exist!", "Add a comment List");
            } else {
                Annotation anno = (Annotation) ComBoxAnnotation.getSelectionModel().getSelectedItem();
                FromListViewToListView(CommentListView, AnnotationListView, anno, false);
            }
        }
    }

    public void FromListViewToListView(ListView src, ListView dest, Annotation anno, boolean AddToCommmentList) {
        ObservableList<Comment> comms = src.getSelectionModel().getSelectedItems();
        if (!comms.isEmpty()) {
            for (Comment comm : comms) {
                comm.setAnno(anno);
                Comments commList = (Comments) ComBoxCommentList.getSelectionModel().getSelectedItem();
                if (!AddToCommmentList)
                    commList.getAcomm().remove(comm);
                else
                    commList.getAcomm().add(comm);
                dest.getItems().add(comm);
            }
            src.getItems().removeAll(comms);
        } else
            WarningAlert("Please select a comment!", "Selection problem");
    }

    public void UndoFromAnnotation(ActionEvent actionEvent) {
        if (AnnotationListView.getItems().isEmpty()) {
            WarningAlert("", "No Comment in the list!");
        } else {
            FromListViewToListView(AnnotationListView, CommentListView, null, true);
        }
    }

    public void SelectAllComList(ActionEvent actionEvent) {
        CommentListView.getSelectionModel().selectAll();
    }

    public void DeselectAllComList(ActionEvent actionEvent) {
        CommentListView.getSelectionModel().select(-1);
    }

    public void SeselectAllAnnotation(ActionEvent actionEvent) {
        AnnotationListView.getSelectionModel().selectAll();
    }

    public void DeselectAllAnnotation(ActionEvent actionEvent) {
        AnnotationListView.getSelectionModel().select(-1);
    }

    public void SaveAllToFile(ActionEvent actionEvent) {
        ArrayList<Comment> ac = getFromListView(AnnotationListView);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Save your annotated comments", "TXT","src/main/resources/AnnotedComments");
        myChooser.SaveFile();
        File file = myChooser.getFile();
        if (!(file == null)){
            TextInputDialog dialog = new TextInputDialog(",");
            dialog.setTitle("Separator");
            dialog.setContentText("Please give the separator :");
            Optional<String> result = dialog.showAndWait();
            saveToFile(ac, file, result.get());
        }

    }


    public void SwitchToCrawlerScene(ActionEvent event) throws IOException {
        SceneController.SwitchToCrawlerScene(event);
    }

    public void SwitchToTrainingScene(ActionEvent event) throws IOException {
        SceneController.SwitchToTrainingScene(event);
    }

    public void SwitchToHome(ActionEvent actionEvent) throws IOException{
        SceneController.SwitchToHome(actionEvent);
    }

    public void KeyPressed() {
        TxfAnnotation.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    CreateAnnotation(null);
                }

            }
        });
        TxfCommentList.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                    CreateCommentList(null);
            }
        });
    }

    public void WhenChangeHappen() {
        ComBoxCommentList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (ComBoxCommentList.getItems().isEmpty()) {
                    CommentListView.getItems().clear();
                } else {
                    Comments commList = (Comments) ComBoxCommentList.getSelectionModel().getSelectedItem();
                    showCommentInListView(commList.getAcomm(), CommentListView);
                }
            }
        });
    }
}