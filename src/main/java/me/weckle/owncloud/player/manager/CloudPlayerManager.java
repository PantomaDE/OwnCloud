package me.weckle.owncloud.player.manager;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.player.CloudPlayer;
import org.bson.Document;

import java.util.UUID;

public class CloudPlayerManager {

    public void createCloudPlayer(UUID uuid, String name, String defaultPermissionGroup) {
        Document document = new Document("_id", uuid.toString()).append("name", name).append("permissionGroup", defaultPermissionGroup).append("currentServer", "none");
        OwnCloud.getMongoConnector().getPlayers().insertOne(document);
    }

    public CloudPlayer getCloudPlayer(UUID uuid) {
        FindIterable<Document> find = OwnCloud.getMongoConnector().getPlayers().find(Filters.eq("_id", uuid.toString()));
        Document document = find.first();
        return new CloudPlayer(uuid, document.getString("name"), document.getString("permissionGroup"), document.getString("currentServer"));
    }

    public boolean existsPlayer(UUID uuid) {
        FindIterable<Document> find = OwnCloud.getMongoConnector().getPlayers().find(Filters.eq("_id", uuid.toString()));
        if(find.first() == null) {
            return false;
        } else {
            return true;
        }
    }


}
