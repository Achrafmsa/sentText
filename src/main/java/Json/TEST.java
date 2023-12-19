package Json;

import DataAccessLayer.MongoDb;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DataAccessLayer.MongoDb.getCollectionAsHashMap;


public class TEST {

    public static void main(String[] args) throws IOException {
//        String jsonsource = "{\"documents\":[{\"_id\":\"629502818ae1949779b3b4f9\",\"review\":\"جميل\",\"product\":\"iphone\"},{\"_id\":\"629502b48ae1949779b3b4fa\",\"review\":\"متوسط\",\"product\":\"pc\"},{\"_id\":\"62a5fdb3ad4457a10635d67f\",\"review\":\"رائع\",\"product\":\"iphone\"}]}\n";
//        HashMap<String,List<String>> hmColl = new HashMap<>();
//        hmColl = getCollectionAsHashMap(jsonsource);
//        System.out.println(hmColl.get("iphone"));
        System.out.println(MongoDb.GetDataMongoDb("Projet","review"));
    }
}
