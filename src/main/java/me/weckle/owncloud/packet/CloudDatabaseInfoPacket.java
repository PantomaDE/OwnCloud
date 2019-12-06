package me.weckle.owncloud.packet;

import me.weckle.owncloud.server.state.ServerState;

public class CloudDatabaseInfoPacket {

    private String name;
    private int currentPlayers, maxPlayers;
    private ServerState serverState;

    public CloudDatabaseInfoPacket(String name, int currentPlayers, int maxPlayers, ServerState serverState) {
        this.name = name;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.serverState = serverState;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
