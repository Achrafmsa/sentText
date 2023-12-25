package DataAccessLayer;

import Json.Json;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MongoDb {

    private static ConnectionString connectionString;
    private static MongoClientSettings settings;
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static Response response;


    public static MongoCollection GetDataMongoDb(String DbName, String Collection) {
        connectionString = new ConnectionString("");
        settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(DbName);
        return database.getCollection(Collection);
    }

    public static String getCollection() throws IOException {
        OkHttpClient client;
        MediaType mediaType;
        RequestBody body;
        Request request;
        client = new OkHttpClient().newBuilder().build();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create(mediaType, "{\n \"collection\":\"commentaire\",\n    \"database\":\"Projet\",\n  \"dataSource\":\"Cluster0\"\n}");
        request = new Request.Builder()
                .url("https://us-east-1.aws.data.mongodb-api.com/app/data-ofsnr/endpoint/data/beta/action/find")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Access-Control-Request-Headers", "*")
                .addHeader("api-key", "")
                .build();

        response=client.newCall(request).execute();
        return response.body().string();
    }

    public static HashMap<String, List<String>> getCollectionAsHashMap(String jsonSource) throws IOException {
        JsonNode node = Json.parse(jsonSource);
        node = node.get("documents");
        HashMap<String,List<String>> hm = new HashMap<>();

        for (int i = 0; i < node.size(); i++) {
            String prod = node.get(i).get("articles_name").asText();
            String comm = node.get(i).get("comment").asText();
            List<String> commes = new ArrayList<>();
            commes.add(comm) ;
            if(!(hm.containsKey(prod))){
                hm.put(prod,commes);
            }
            else{
                hm.get(prod).add(comm);
            }
        }
        return hm ;
    }

    public static Response getResponse() {
        return response;
    }
}
