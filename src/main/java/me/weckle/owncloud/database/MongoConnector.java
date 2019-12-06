package me.weckle.owncloud.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnector {

    private MongoClient client;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> players;

    public MongoConnector() {
        client = new MongoClient("127.0.0.1", 27017);
        mongoDatabase = client.getDatabase("OwnCloud");
        players = mongoDatabase.getCollection("cloudPlayers");
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoCollection<Document> getPlayers() {
        return players;
    }
}
