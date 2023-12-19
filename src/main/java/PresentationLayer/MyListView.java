package PresentationLayer;

import BuisnessLayer.Comments;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Vector;

public class MyListView {

    public static ArrayList getFromListView(ListView listView) {
        ArrayList ac = new ArrayList<>();
        for (Object obj : listView.getItems().stream().toList()) {
            ac.add(obj);
        }
        return ac;
    }

    public static void showCommentInListView(ArrayList List, ListView src) {
        ListView lv = src;
        lv.getItems().clear();
        lv.getItems().addAll(List);
    }
}
