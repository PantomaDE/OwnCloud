package me.weckle.owncloud.server.watcher;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.group.ServerGroup;

import java.util.function.BiConsumer;

public class ServerWatcher extends Thread {

    @Override
    public void run() {
        OwnCloud.getGroupManager().getServerGroups().forEach(new BiConsumer<String, ServerGroup>() {
            @Override
            public void accept(String s, ServerGroup serverGroup) {
                int online = OwnCloud.getServerManager().getServersFromGroup(serverGroup).size();
                if(online <= serverGroup.getMaxPlayers()) {
                    OwnCloud.getServerManager().startNewCloudServer(serverGroup);
                }
            }
        });
    }
}
