package me.weckle.owncloud.group.manager;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.configuration.Configuration;
import me.weckle.owncloud.configuration.ConfigurationProvider;
import me.weckle.owncloud.configuration.YamlConfiguration;
import me.weckle.owncloud.group.ServerGroup;
import me.weckle.owncloud.group.type.TemplateType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupManager {

    private HashMap<String, ServerGroup> serverGroups;

    public GroupManager() {
        serverGroups = new HashMap<>();
        loadGroups();
    }

    private final void loadGroups() {
        File dir = new File("groups");
        if(dir.listFiles().length == 0) {
            OwnCloud.sendConsoleWarn("§cKonnte keine Gruppen laden");
        } else {
            for (File file : dir.listFiles()) {
                try {
                    Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                    ServerGroup group = new ServerGroup(file.getName().replace(".yml", ""), cfg.getInt("minMB"), cfg.getInt("maxMB"), TemplateType.valueOf(cfg.getString("TemplateType").toUpperCase()),
                            cfg.getInt("minServers"), cfg.getInt("maxServers"), cfg.getInt("maxPlayers"));
                    serverGroups.put(group.getName(), group);
                    OwnCloud.sendConsoleMessage("Lade Servergruppe§a " + group.getName() + "§7...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
