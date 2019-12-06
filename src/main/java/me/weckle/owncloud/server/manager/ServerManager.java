package me.weckle.owncloud.server.manager;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.group.ServerGroup;
import me.weckle.owncloud.server.CloudServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class ServerManager {

    private HashMap<String, CloudServer> startedServers;

    private List<Integer> usedPorts;

    public ServerManager() {
        startedServers = new HashMap<>();
        usedPorts = new ArrayList<>();
    }


    public List<CloudServer> getServersFromGroup(ServerGroup serverGroup) {
        List<CloudServer> servers = new ArrayList<>();
        startedServers.forEach(new BiConsumer<String, CloudServer>() {
            @Override
            public void accept(String s, CloudServer cloudServer) {
                if(cloudServer.getName().startsWith(serverGroup.getName())) {
                    servers.add(cloudServer);
                }
            }
        });
        return servers;
    }

    public void startNewCloudServer(ServerGroup group) {
        CloudServer cloudServer = new CloudServer(getNewServerId(group), getPort());
        cloudServer.start();
        startedServers.put(cloudServer.getName(), cloudServer);
    }

    public HashMap<String, CloudServer> getStartedServers() {
        return startedServers;
    }

    public List<Integer> getUsedPorts() {
        return usedPorts;
    }

    public int getPort() {
        int start = 10000;
        while(usedPorts.contains(start)) {
            start++;
        }
        usedPorts.add(start);
        return start;
    }

    public void removeUsedPort(int port) {
        usedPorts.remove(port);
    }

    public String getNewServerId(ServerGroup group) {
        List<String> serversFromGroup = new ArrayList<>();
        startedServers.forEach(new BiConsumer<String, CloudServer>() {
            @Override
            public void accept(String s, CloudServer cloudServer) {
                if(s.contains(group.getName())) {
                    serversFromGroup.add(s);
                }
            }
        });
        return group.getName() + OwnCloud.getConfig().getString("Server-Split") + Integer.valueOf(serversFromGroup.size() + 1);
    }

    public void startAllServers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OwnCloud.getGroupManager().getServerGroups().forEach(new BiConsumer<String, ServerGroup>() {
                    @Override
                    public void accept(String s, ServerGroup serverGroup) {
                        for (int i = 0; i < serverGroup.getMinServers(); i++) {
                            CloudServer cloudServer = new CloudServer(getNewServerId(serverGroup), getPort());
                            cloudServer.start();
                            startedServers.put(cloudServer.getName(), cloudServer);
                        }
                    }
                });
            }
        }).start();
    }



}
