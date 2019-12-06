package me.weckle.owncloud.group;

import me.weckle.owncloud.group.type.TemplateType;

public class ServerGroup {

    private String name;
    private int minMb, maxMb, minServers, maxServers, maxPlayers;
    private TemplateType templateType;

    public ServerGroup(String name, int minMb, int maxMb, TemplateType templateType, int minServers, int maxServers, int maxPlayers) {
        this.name = name;
        this.minMb = minMb;
        this.maxMb = maxMb;
        this.maxServers = maxServers;
        this.minServers = minServers;
        this.templateType = templateType;
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public int getMinMb() {
        return minMb;
    }

    public int getMaxMb() {
        return maxMb;
    }

    public int getMinServers() {
        return minServers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMaxServers() {
        return maxServers;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }
}
