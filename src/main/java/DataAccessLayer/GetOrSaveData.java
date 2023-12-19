package DataAccessLayer;

import BuisnessLayer.Comment;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GetOrSaveData {

    public static ArrayList<Comment> getFromFile(File file) {
        ArrayList<Comment> ac = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            Comment c;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                } else {
                    c = new Comment();
                    c.setComment(line.trim());
                    ac.add(c);
                }
            }
            br.close();
        } catch (Exception e) {

        }
        return ac;
    }

    public static void saveToFile(ArrayList<Comment> v, File file, String separator) {
        ArrayList<Comment> vc = v;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("");
            for (int i = 0; i < vc.size(); i++) {
                bw.write(vc.get(i).getComment() + separator);
                bw.write(vc.get(i).getAnno() + "");
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(ArrayList<String> a, File file) {
        ArrayList<String> comments = a;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("");
            for (int i = 0; i < comments.size(); i++) {
                bw.write(comments.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
