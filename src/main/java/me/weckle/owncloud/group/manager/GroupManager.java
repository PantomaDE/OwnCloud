package me.weckle.owncloud.group.manager;

import com.google.gson.Gson;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.configuration.Configuration;
import me.weckle.owncloud.configuration.ConfigurationProvider;
import me.weckle.owncloud.configuration.YamlConfiguration;
import me.weckle.owncloud.group.ServerGroup;
import me.weckle.owncloud.group.type.TemplateType;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class GroupManager {

    private HashMap<String, ServerGroup> serverGroups;
    private Gson gson;
    public GroupManager() {
        gson = new Gson();
        serverGroups = new HashMap<>();
        loadGroups();
    }

    private final void loadGroups() {
        List<ServerGroup> groups = new ArrayList<>();
        File dir = new File("groups");
        if(dir.listFiles().length == 0) {
            OwnCloud.sendConsoleWarn("§cKonnte keine Gruppen laden");
        } else {
            for (File file : dir.listFiles()) {
                try {
                    Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                    String name = file.getName().replace(".yml", "");
                    ServerGroup group = new ServerGroup(name, cfg.getInt("minMB"), cfg.getInt("maxMB"), TemplateType.valueOf(cfg.getString("TemplateType").toUpperCase()),
                            cfg.getInt("minServers"), cfg.getInt("maxServers"), cfg.getInt("maxPlayers"));
                    groups.add(group);
                    serverGroups.put(group.getName(), group);
                    OwnCloud.sendConsoleMessage("Lade Servergruppe§a " + group.getName() + "§7...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try(Jedis jedis = OwnCloud.getRedisConnector().getJedisPool().getResource()) {
                groups.forEach(new Consumer<ServerGroup>() {
                    @Override
                    public void accept(ServerGroup serverGroup) {
                        jedis.hset("groups", serverGroup.getName(), gson.toJson(serverGroup));
                    }
                });
            }
        }
    }

    public HashMap<String, ServerGroup> getServerGroups() {
        return serverGroups;
    }

    public ServerGroup getServerGroupByServerId(String serverid) {
        String[] data = serverid.split(OwnCloud.getConfig().getString("Server-Split"));
        return serverGroups.get(data[0]);
    }

}
