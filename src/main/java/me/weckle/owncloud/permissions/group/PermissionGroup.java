package me.weckle.owncloud.permissions.group;

import me.weckle.owncloud.permissions.permission.CloudPermission;

import java.util.List;

public class PermissionGroup {

    private String name, chatDisplay, tabDisplay, rankDisplay;
    private List<CloudPermission> permissions;

    public PermissionGroup(String name, String chatDisplay, String tabDisplay, String rankDisplay, List<CloudPermission> permissions) {
        this.name = name;
        this.chatDisplay = chatDisplay;
        this.tabDisplay = tabDisplay;
        this.rankDisplay = rankDisplay;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getChatDisplay() {
        return chatDisplay;
    }

    public String getTabDisplay() {
        return tabDisplay;
    }

    public String getRankDisplay() {
        return rankDisplay;
    }

    public List<CloudPermission> getPermissions() {
        return permissions;
    }
}
