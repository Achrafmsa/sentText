package PresentationLayer;

import javafx.event.ActionEvent;

import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyFileChooser{

    private Stage stage;
    private String str, filter;
    private FileChooser fileChooser;
    private File file,defaultDirectory;
    private static Desktop desktop = Desktop.getDesktop();

    public MyFileChooser(String str, String filter,String pathName) {
        this.str = str;
        this.filter = filter;
        this.defaultDirectory = new File(pathName);
        fileChooser = new FileChooser();
    }

    public MyFileChooser(Stage stage, String str, String filter,String pathName) {
        this.stage = stage;
        this.str = str;
        this.filter = filter;
        this.defaultDirectory = new File(pathName);
        fileChooser = new FileChooser();
    }

    public File getFile() {
        return file;
    }

    public void showMyFileChooser() {
        configureFileChooser(filter);
        file = fileChooser.showOpenDialog(stage);
    }

    public void SaveFile() {
        configureFileChooser(filter);
        file = fileChooser.showSaveDialog(stage);
    }

    public void OpenProject(ActionEvent event){
        configureFileChooser(filter);
        file = fileChooser.showOpenDialog(((MenuItem) event.getTarget()).getParentPopup().getScene().getWindow());
    }
    public void SaveProject(ActionEvent event) {
        configureFileChooser(filter);
//      MenuItem menuItem = (MenuItem)event.getTarget();
//      ContextMenu cm = menuItem.getParentPopup();
//      Scene scene = cm.getScene();
//      Window window = scene.getWindow();
//      fileChooser.showSaveDialog(window);
        file = fileChooser.showSaveDialog(((MenuItem) event.getTarget()).getParentPopup().getScene().getWindow());
    }

    private void configureFileChooser(String filter) {
        fileChooser.setTitle(str);
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(filter, "*." + filter.toLowerCase()),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    public void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(MyFileChooser.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}