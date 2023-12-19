package PresentationLayer;

import BagOfWords.Language;
import BagOfWords.SimpleTokenizer;
import BagOfWords.bagOfWords;
import BuisnessLayer.Comments;
import DataAccessLayer.GetOrSaveData;
import DataAccessLayer.MongoDb;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.*;

import static BagOfWords.bagOfWords.getBagOfPrediction;
import static DataAccessLayer.MongoDb.getCollectionAsHashMap;
import static ML4DM.WekaAlgorithms.*;
import static PresentationLayer.MyAlert.WarningAlert;
import static PresentationLayer.MyListView.showCommentInListView;

public class TrainingDataController implements Initializable {

    static {
        try {
            scene = new Scene(FXMLLoader.load(TrainingDataController.class.getResource("TrainingScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, List<String>> hmColl;
    private String ProductName;
    private List<String> commentsToPredict;
    private Classifier cls;
    private Instances FToPInstances; //File To Predict instances
    private int lastIndexOfFP; //last index of prediction file
    private String strArffFP; //arff as string of prediction file
    private static Scene scene;
    private File ArffFile, FileToPredict, Annotedfile, modelFile, temp, ArffFileOut;
    private bagOfWords OriginalBag, bagOfPrediction;
    @FXML
    private Label lblFileName, lblArffFile, lblmodelFile, lblFileTest, lblProgArff;
    @FXML
    private TextArea TextResults, TextResultPrediction;
    @FXML
    private TextField txtRelatName;
    @FXML
    private ComboBox<String> ComBoxLang, ComBoxMongoProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WhenChangeHappen();
        ComBoxLang.getItems().add("English");
        ComBoxLang.getItems().add("Arabic");
    }

    public static Scene getScene() {
        return scene;
    }

    public void SwitchToAnnotationScene(ActionEvent event) throws IOException {
        SceneController.SwitchToAnnotationScene(event);
    }

    public void SwitchToHome(ActionEvent actionEvent) throws IOException {
        SceneController.SwitchToHome(actionEvent);
    }

    public void OpenFile(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Open Comments list", "TXT", "src/main/resources/AnnotedComments");
        myChooser.showMyFileChooser();
        Annotedfile = myChooser.getFile();
        lblFileName.setText(Annotedfile.getName());
    }

    public void ChangeToArffFile(ActionEvent actionEvent) throws InterruptedException {
        lblProgArff.setText("");
        if (OriginalBag == null) {
            WarningAlert("Create your bag of words first!", "");
        } else {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            MyFileChooser myChooser = new MyFileChooser(stage, "Save your ARFF file", "arff", "src/main/resources/arffFiles");
            myChooser.SaveFile();
            ArffFileOut = myChooser.getFile();
            if (!(ArffFileOut == null)) {
                String RelationName = txtRelatName.getText().replaceAll(" ", "");
                lblProgArff.setText("Please with!");
                OriginalBag.createFileArff(RelationName, ArffFileOut);
            }
        }
        lblProgArff.setText("Done!");
    }

    public void CreateBagOfWords(ActionEvent actionEvent) {
        String RelationName = txtRelatName.getText().replaceAll(" ", "");
        if (Annotedfile == null) {
            WarningAlert("Please Choose your annoted file!", "");
        } else {
            if (!(RelationName == null || RelationName.trim().isEmpty())) {
                if (ComBoxLang.getItems().isEmpty()) {
                    WarningAlert("Please Select a language", "");
                } else {
                    TextInputDialog dialog = new TextInputDialog(",");
                    dialog.setTitle("Separator");
                    dialog.setContentText("Please give the separator :");
                    Optional<String> result = dialog.showAndWait();
                    OriginalBag = new bagOfWords(Annotedfile.toPath(), new SimpleTokenizer(1), Language.language.valueOf(ComBoxLang.getValue()), result.get());
                }
            } else {
                WarningAlert("Please write something!", "Relation Name Text Field");
            }
        }
    }

    public void OpenArffFile(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Open Comments list", "arff", "src/main/resources/arffFiles");
        myChooser.showMyFileChooser();
        ArffFile = myChooser.getFile();
        try {
            lblArffFile.setText(ArffFile.getName());
        } catch (Exception e) {
        }
    }

    public void TrainingYourData(ActionEvent actionEvent) throws Exception {
        String FileName = ArffFile.getPath();
        if (ArffFile == null) {
            WarningAlert("Please Choose a file!", "");
        } else {
            Classifier smo = new SMO();

            Instances train;
            train = RemoveWithPercentage(FileName, 75, true);
            Instances test = RemoveWithPercentage(FileName, 25, false);
            test.setRelationName(train.relationName());

            int lastIndex = train.numAttributes() - 1;
            train.setClassIndex(lastIndex);
            test.setClassIndex(lastIndex);

            smo.buildClassifier(train);
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(smo, test);
            cls = smo;
            TextResults.setText(eval.toSummaryString() + "\n" + eval.toClassDetailsString() + "\n" + eval.toMatrixString());
        }
    }

    public void SaveModel(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Save your model", "model", "src/main/resources/models");
        myChooser.SaveFile();
        File Newfile = myChooser.getFile();
        if (!(Newfile == null)) {
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(new FileOutputStream(Newfile));
            oos.writeObject(cls);
            oos.flush();
            oos.close();
        }
    }

    public void SaveMongoComments(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Save your Comments", "txt", "src/main/resources/CommentsToTest");
        myChooser.SaveFile();
        File Newfile = myChooser.getFile();
        if (!(Newfile == null)) {
            GetOrSaveData.saveToFile((ArrayList<String>) commentsToPredict, Newfile);
        }
    }

    public void OpenFileTest(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Open a file to predict", "TXT", "src/main/resources/CommentsToTest");
        myChooser.showMyFileChooser();
        FileToPredict = myChooser.getFile();
        lblFileTest.setText("The test file is : " + FileToPredict.getName());
    }

    public void getMongoDbComments(ActionEvent actionEvent) throws IOException {
        String jsonsource = MongoDb.getCollection();
        hmColl = new HashMap<>();
        ComBoxMongoProduct.getItems().clear();
        hmColl = getCollectionAsHashMap(jsonsource);
        ComBoxMongoProduct.getItems().addAll(hmColl.keySet());
    }

    public void OpenModel(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        MyFileChooser myChooser = new MyFileChooser(stage, "Open Your model", "model", "src/main/resources/models");
        myChooser.showMyFileChooser();

        modelFile = myChooser.getFile();
        cls = (Classifier) weka.core.SerializationHelper.read(modelFile.getAbsolutePath());

        lblmodelFile.setText("The model file is : " + modelFile.getName());
    }

    public void Predict() throws Exception {
        if (FileToPredict == null) {
            WarningAlert("Please Choose a file!", "");
        } else {
            // create a temp file
            if (temp == null) {
                bagOfPrediction = getBagOfPrediction(FileToPredict, Language.language.valueOf(ComBoxLang.getValue()), OriginalBag);
                strArffFP = ArffToString(bagOfPrediction);
                temp = File.createTempFile("PredictArff", ".arff");
                String path = temp.getAbsolutePath();
                System.err.println("Temp file created: " + path);
                BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                bw.write(strArffFP);
                bw.close();
                FToPInstances = new Instances(new BufferedReader(new FileReader(temp)));
                lastIndexOfFP = FToPInstances.numAttributes() - 1;
                FToPInstances.setClassIndex(lastIndexOfFP);
            }
            if (cls == null) {
                WarningAlert("You can use a model or train a new one!", "");
            } else {
                // this does the trick
                List<String> cleanLines = bagOfPrediction.getcleanLines();
                System.out.println(cleanLines);
                int i = 0;
                String outputResutls = "";
                for (String line : cleanLines) {
                    double label = cls.classifyInstance(FToPInstances.instance(i));
                    FToPInstances.instance(i).setClassValue(label);
                    outputResutls += line + "     : " + FToPInstances.instance(i).stringValue(lastIndexOfFP) + "\n";
                    i++;
                }

                int[] array = FToPInstances.attributeStats(lastIndexOfFP).nominalCounts;
                double Total = FToPInstances.attributeStats(lastIndexOfFP).totalCount;
                String result = "";
                double percent;
                for (int j = 0; j < array.length; j++) {
                    percent = (array[j] * 100) / Total;
                    result += array[j] + " " + FToPInstances.attribute(lastIndexOfFP).value(j) + " : " + percent + "%\n";
                }
                TextResultPrediction.setText(outputResutls + "\n" + result);
            }
            temp.deleteOnExit();
        }
        temp = null;
    }

    public void WhenChangeHappen() {
        ComBoxMongoProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (ComBoxMongoProduct.getItems().isEmpty()) {
                    TextResultPrediction.setText("");
                } else {
                    ProductName = ComBoxMongoProduct.getSelectionModel().getSelectedItem();
                    String Comments = "";
                    List<String> Coll = hmColl.get(ProductName);
                    for (int i = 0; i < Coll.size(); i++)
                        Comments += Coll.get(i) + "\n";
                    commentsToPredict = Coll;
                    TextResultPrediction.setText(Comments);
                }
            }
        });
    }
}


