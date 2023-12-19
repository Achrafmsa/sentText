package BagOfWords;
import DataAccessLayer.MongoDb;

import java.io.IOException;


public class Run {

    public static void main(String[] args) throws IOException {
        System.out.println(MongoDb.getCollection());
    }

}
