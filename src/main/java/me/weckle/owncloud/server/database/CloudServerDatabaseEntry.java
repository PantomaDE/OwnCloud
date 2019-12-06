package me.weckle.owncloud.server.database;

import me.weckle.owncloud.server.state.ServerState;

public class CloudServerDatabaseEntry {

    private int currentPlayers;
    private ServerState serverState;

    public CloudServerDatabaseEntry(int currentPlayers, ServerState serverState) {
        this.currentPlayers = currentPlayers;
        this.serverState = serverState;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public ServerState getServerState() {
        return serverState;
    }
}
