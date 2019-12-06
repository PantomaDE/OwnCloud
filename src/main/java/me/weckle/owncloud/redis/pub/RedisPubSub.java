package me.weckle.owncloud.redis.pub;

import com.google.gson.Gson;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.file.FileCopyManager;
import me.weckle.owncloud.group.ServerGroup;
import me.weckle.owncloud.group.type.TemplateType;
import me.weckle.owncloud.server.CloudServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RedisPubSub extends JedisPubSub {

    private Gson gson;
    private FileCopyManager fileCopyManager;

    public RedisPubSub() {
        fileCopyManager = new FileCopyManager();
        gson = new Gson();
    }

    @Override
    public void onMessage(String channel, String message) {
        if(channel.equalsIgnoreCase("cloud")) {
            if(message.equalsIgnoreCase("playerjoin")) {
                String[] data = message.split(" ");
                if(OwnCloud.getCloudPlayerManager().existsPlayer(UUID.fromString(data[1]))) {
                    try(Jedis jedis = OwnCloud.getRedisConnector().getJedisPool().getResource()) {
                        jedis.set("players:" + data[1], gson.toJson(OwnCloud.getCloudPlayerManager().getCloudPlayer(UUID.fromString(data[1]))));
                    }
                } else {
                    OwnCloud.getCloudPlayerManager().createCloudPlayer(UUID.fromString(data[1]), "none", OwnCloud.getConfig().getString("DefaultPermissionGroup"));
                }
            } else if(message.startsWith("playerquit")) {
                String[] data = message.split(" ");
                try(Jedis jedis = OwnCloud.getRedisConnector().getJedisPool().getResource()) {
                    jedis.del("players:" + data[1]);
                }
            } else if(message.startsWith("serverstop")) {
                String[] data = message.split(" ");
                String serverId = data[1];
                CloudServer cloudServer = OwnCloud.getServerManager().getStartedServers().get(serverId);
                if(cloudServer.getTemplateType() == TemplateType.STATIC) {
                    ServerGroup serverGroup = OwnCloud.getGroupManager().getServerGroupByServerId(serverId);
                    try {
                        fileCopyManager.copyFilesInDirecotry(new File("temp/" + serverGroup.getName() + "/" + serverId), new File("templates/" + serverGroup.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cloudServer.getServerProcess().destroyForcibly();
                OwnCloud.getServerManager().getStartedServers().remove(serverId);
                OwnCloud.sendConsoleMessage("Der Server§c " + serverId + "§f wurde gestoppt");
            }
        }
    }
}
