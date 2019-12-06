package me.weckle.owncloud.permissions;

import me.weckle.owncloud.configuration.Configuration;
import me.weckle.owncloud.configuration.ConfigurationProvider;
import me.weckle.owncloud.configuration.YamlConfiguration;
import me.weckle.owncloud.permissions.group.PermissionGroup;
import me.weckle.owncloud.permissions.permission.CloudPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PermissionManager {

    private List<PermissionGroup> permissionGroups;
    private Configuration configuration;

    public PermissionManager() {
        permissionGroups = new ArrayList<>();
        checkFile();
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("permissionsystem/Groups.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final void loadGroups() {
        configuration.getKeys().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                permissionGroups.add(getPermissionGroup(s));
            }
        });
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public PermissionGroup getPermissionGroup(String name) {
        List<String> permissions = configuration.getStringList(name + ".Permissions");
        List<CloudPermission> legacyPermissions = new ArrayList<>();
        permissions.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if(s.startsWith("-")) {
                    legacyPermissions.add(new CloudPermission(s, false));
                } else {
                    legacyPermissions.add(new CloudPermission(s, true));
                }
            }
        });
        PermissionGroup permissionGroup = new PermissionGroup(name, configuration.getString(name + ".ChatDisplay"), configuration.getString(name + ".TabDisplay"),
                configuration.getString(name + ".RankDisplay"), legacyPermissions);
        return permissionGroup;
    }

    public void createPermissionGroup(String name) {
        configuration.set(name + ".ChatDisplay", "DefaultChatDisplay | ");
        configuration.set(name + ".TabDisplay", "DefaultTabDisplay | ");
        configuration.set(name + ".RankDisplay", "DefaultRankDisplay");
        configuration.set(name + ".Permissions", new ArrayList<String>());
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File("permissionsystem/Groups.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final void checkFile() {
        File file = new File("permissionsystem/Groups.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
