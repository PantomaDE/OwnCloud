package me.weckle.owncloud.permissions.permission;

public class CloudPermission {

    private String permission;
    private boolean legacyPermission;

    public CloudPermission(String permission, boolean legacyPermission) {
        this.permission = permission;
        this.legacyPermission = legacyPermission;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isLegacyPermission() {
        return legacyPermission;
    }
}
