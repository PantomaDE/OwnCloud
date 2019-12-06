package me.weckle.owncloud.player;

import me.weckle.owncloud.permissions.group.PermissionGroup;

import java.util.UUID;

public class CloudPlayer {

    private UUID uuid;
    private String name;
    private String permissionGroup;
    private String currentServer;

    public CloudPlayer(UUID uuid, String name, String permissionGroup, String currentServer) {
        this.uuid = uuid;
        this.name = name;
        this.permissionGroup = permissionGroup;
        this.currentServer = currentServer;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getPermissionGroup() {
        return permissionGroup;
    }

    public String getCurrentServer() {
        return currentServer;
    }
}
