package PresentationLayer;

import BuisnessLayer.Comment;
import BuisnessLayer.Comments;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.io.IOException;

import static DataAccessLayer.GetOrSaveData.saveToFile;
import static PresentationLayer.MyAlert.WarningAlert;
import static PresentationLayer.MyListView.getFromListView;
import static PresentationLayer.MyListView.showCommentInListView;

//Create classes in buisnessLayer

public class WebCrawlerController implements Initializable {

    private static Scene scene;

    static {
        try {
            scene = new Scene(FXMLLoader.load(WebCrawlerController.class.getResource("WebCrawlerScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private WebView webView;
    @FXML
    private TextField TXFURL, TXFElement, TxfCommentList,TXFPageEle,TXFPageEle1;
    @FXML
    private ListView CommentListView;
    @FXML
    private Label labelLoadingState, labelNumberOfComments;
    @FXML
    private ProgressBar WebProgressBar;
    @FXML
    private ComboBox<Comments> ComBoxCommentList;

    private WebEngine engine;
    private WebHistory history;
    private String homePage;
    private double webZoom;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CommentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        labelNumberOfComments.setText("No comments");
        engine = webView.getEngine();
        KeyPressed();
        WhenChangeHappen();
        homePage = "http://www.google.com";
        TXFURL.setText(homePage);
        webZoom = 1;
        loadPage(null);
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        WebCrawlerController.scene = scene;
    }

    public void loadPage(ActionEvent event) {
        engine.load(TXFURL.getText());
    }

    public void refreshPage() {
        engine.reload();
    }

    public void previousPage() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        try {
            history.go(-1);
        } catch (IndexOutOfBoundsException e) {
        }
        TXFURL.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    public void nextPage() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        try {
            history.go(1);
        } catch (IndexOutOfBoundsException e) {
        }
        TXFURL.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    public void SwitchToAnnotationScene(ActionEvent event) throws IOException {
        SceneController.SwitchToAnnotationScene(event);
    }

    public void SwitchToHome(ActionEvent actionEvent) throws IOException {
        SceneController.SwitchToHome(actionEvent);
    }

    public void CrawlingByElement(ActionEvent actionEvent) throws IOException {
        Comments commList = (Comments) ComBoxCommentList.getSelectionModel().getSelectedItem();
        if (ComBoxCommentList.getItems().isEmpty()) {
            WarningAlert("No Comment list exist!", "Add a comment List");
        } else {
            String url = TXFURL.getText();
            String element = TXFElement.getText();
            if (!(element == null || element.trim().isEmpty())) {
                Load_single(url,element,commList);
            } else {
                WarningAlert("Please write something!", "Element field empty");
            }
            showCommentInListView(commList.getStringAcoom(), CommentListView);
        }
    }

    public void CrawlingMultiple(ActionEvent actionEvent) throws Exception {
        Comments commList = (Comments) ComBoxCommentList.getSelectionModel().getSelectedItem();
        if (ComBoxCommentList.getItems().isEmpty()) {
            WarningAlert("No Comment list exist!", "Add a comment List");
        } else {
            String url = TXFURL.getText();
            String element = TXFElement.getText();
            String eleLinks = TXFPageEle.getText();
            int compteur = Integer.parseInt(TXFPageEle1.getText());
            if (!(element == null || element.trim().isEmpty() && !(eleLinks == null || eleLinks.trim().isEmpty()))
            && !(compteur==0)) {
                Load_multiple(url,eleLinks,element,commList,compteur);
            }
            else {
                WarningAlert("Please fill the both them!", "");
            }
            showCommentInListView(commList.getStringAcoom(), CommentListView);
        }
    }

    public void Load_single(String link, String element, Comments commList) throws IOException {
        String url = link;
        String ele = element;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(ele);
        if (elements.isEmpty()) {
            WarningAlert(null, "element empty or not found");
        } else {
            for (Element e : elements) {
                String comment = e.text();
                if (commList.getStringAcoom().contains(comment)) {
                } else {
                    commList.getStringAcoom().add(comment);
                }
            }
        }
    }

    public void Load_multiple(String url, String LinkElement,String element,Comments commList, int compteur) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Elements allLinks = doc.select(LinkElement);
        int cmp = 0;
        for (Element e : allLinks) {
            String link = e.absUrl("href");
            Load_single(link,element,commList);
            cmp++;
            if (cmp == compteur)
                break;
        }
    }

    public void RemoveFromListView(ActionEvent actionEvent) {
        CommentListView.getItems().removeAll(CommentListView.getSelectionModel().getSelectedItems());
    }

    public void CreateCommentList(ActionEvent actionEvent) {
        String CommentList = TxfCommentList.getText().replaceAll(" ", "");
        ArrayList<String> CommentArrayList = new ArrayList<String>();

        if (!(CommentList == null || CommentList.trim().isEmpty())) {
            Comments commList = new Comments(CommentArrayList, CommentList, false);
            ComBoxCommentList.getItems().add(commList);

            ComBoxCommentList.getSelectionModel().selectLast();
            System.out.println("The CommentList was successfully added!" + commList.getNom());
        } else {
            WarningAlert("Please write something!", "Comment List Text Field");
        }
        TxfCommentList.setText("");
    }

    public void SaveAllToFile(ActionEvent actionEvent) {
        ArrayList<String> comments = getFromListView(CommentListView);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Save your comments", "TXT", "src/main/resources/Comments");
        myChooser.SaveFile();
        File file = myChooser.getFile();
        if (!(file == null))
            saveToFile(comments, file);
    }

    public void KeyPressed() {
        TXFURL.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                    loadPage(null);
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
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            labelLoadingState.setText("Loading state: " + newValue.toString());
            WebProgressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                TXFURL.setText(engine.getLocation());
                labelLoadingState.setText("Finish!");
                WebProgressBar.progressProperty().unbind();
                WebProgressBar.setProgress(0);
            }
        });
        CommentListView.getItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                labelNumberOfComments.setText(String.valueOf(CommentListView.getItems().size()) + " comments");
            }
        });
        TXFPageEle1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    TXFPageEle1.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        ComBoxCommentList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (ComBoxCommentList.getItems().isEmpty()) {
                    CommentListView.getItems().clear();
                } else {
                    Comments commList = (Comments) ComBoxCommentList.getSelectionModel().getSelectedItem();
                    showCommentInListView(commList.getStringAcoom(), CommentListView);
                }
            }
        });
    }

}
