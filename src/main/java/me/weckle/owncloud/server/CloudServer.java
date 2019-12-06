package me.weckle.owncloud.server;

import com.google.gson.Gson;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.file.FileCopyManager;
import me.weckle.owncloud.group.ServerGroup;
import me.weckle.owncloud.group.type.TemplateType;
import me.weckle.owncloud.server.database.CloudServerDatabaseEntry;
import me.weckle.owncloud.server.state.ServerState;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CloudServer {

    private Process serverProcess;
    private String name;
    private int port;
    private ServerState serverState;
    private ServerGroup group;
    private TemplateType templateType;

    private FileCopyManager fileCopyManager;

    private Gson gson;

    public CloudServer(String name, int port) {
        this.name = name;
        this.port = port;
        gson = new Gson();
        group = OwnCloud.getGroupManager().getServerGroupByServerId(name);
        fileCopyManager = new FileCopyManager();
        templateType = group.getTemplateType();
    }



    public void start() {
        File template = new File("templates/" + group.getName());
        if(template.exists()) {
            try {
                File tempG = new File("temp/" + group.getName() + "/" + name);
                if(!tempG.exists()) {
                    tempG.mkdirs();
                }
                fileCopyManager.copyFilesInDirecotry(new File("templates/" + group.getName()), new File("temp/" + group.getName() + "/" + name));
                fileCopyManager.addStartFile(name, "spigot", group.getMaxMb(), group.getMinMb());
                fileCopyManager.copyFilesInDirecotry(new File("global"), new File("temp/" + group.getName() + "/" + name));
                fileCopyManager.copyFilesToDirecotry(new File("jars/spigot.jar"), new File("temp/" + group.getName() + "/" + name));
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File("temp/" + group.getName() + "/" + name + "/server.properties")));
                properties.setProperty("online-mode", "false");
                properties.setProperty("server-port", port + "");
                properties.setProperty("max-players", group.getMaxPlayers() + "");
                properties.setProperty("motd", "A OwnCloud Server");
                properties.setProperty("server-id", name);
                properties.save(new FileOutputStream(new File("temp/" + group.getName() + "/" + name + "/server.properties")), "Edited by OwnCloud");
                serverProcess = new ProcessBuilder("./start.sh").directory(new File("temp/" + group.getName() + "/" + name)).start();
                OwnCloud.sendConsoleMessage("Starte Server§8 [§b" + name + "§8/§b" + port + "§8]");
                CloudServerDatabaseEntry cloudServerDatabaseEntry = new CloudServerDatabaseEntry(0, ServerState.LOBBY);
                try(Jedis jedis = OwnCloud.getRedisConnector().getJedisPool().getResource()) {
                    jedis.set("server:" + name, gson.toJson(cloudServerDatabaseEntry));
                }
                OwnCloud.getRedisConnector().sendMessage("addserver " + name + " " + port);
                OwnCloud.getRedisConnector().sendMessage("sendadminmessage Testlul");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            OwnCloud.sendConsoleWarn("§cKonnte Server nicht starten§8 -§c Template existiert nicht!");
        }
    }


    public Process getServerProcess() {
        return serverProcess;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public int getMaxPlayers() {
        return group.getMaxPlayers();
    }

    public ServerState getServerState() {
        try(Jedis jedis = OwnCloud.getRedisConnector().getJedisPool().getResource()) {
            return gson.fromJson(jedis.get("server:" + name), CloudServerDatabaseEntry.class).getServerState();
        }
    }

    public int getCurrentPlayers() {
        try(Jedis jedis = OwnCloud.getRedisConnector().getJedisPool().getResource()) {
            return gson.fromJson(jedis.get("server:" + name), CloudServerDatabaseEntry.class).getCurrentPlayers();
        }
    }
}
