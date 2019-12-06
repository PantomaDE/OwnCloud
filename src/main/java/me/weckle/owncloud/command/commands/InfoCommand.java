package me.weckle.owncloud.command.commands;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;
import me.weckle.owncloud.group.ServerGroup;
import me.weckle.owncloud.permissions.group.PermissionGroup;
import me.weckle.owncloud.server.CloudServer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InfoCommand extends ConsoleCommand {


    @Override
    public void execute(String[] args) {
        if(args.length == 0) {
            OwnCloud.sendConsoleMessage("Nutze info <servers | groups | global | permgroups>");
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("servers")) {
                OwnCloud.getGroupManager().getServerGroups().forEach(new BiConsumer<String, ServerGroup>() {
                    @Override
                    public void accept(String s, ServerGroup serverGroup) {
                        OwnCloud.sendConsoleMessage("§a" + s + "§8(§c" + OwnCloud.getServerManager().getServersFromGroup(serverGroup).size() + "§8)");
                        OwnCloud.getServerManager().getServersFromGroup(serverGroup).forEach(new Consumer<CloudServer>() {
                            @Override
                            public void accept(CloudServer cloudServer) {
                                OwnCloud.sendConsoleMessage(" §8»§f " + cloudServer.getName() + "§8 |§a" + cloudServer.getCurrentPlayers() + "§8/§7" + cloudServer.getMaxPlayers());
                            }
                        });
                    }
                });
            } else if(args[0].equalsIgnoreCase("groups")) {
                OwnCloud.sendConsoleMessage("Geladene Server Gruppen");
                OwnCloud.getGroupManager().getServerGroups().forEach(new BiConsumer<String, ServerGroup>() {
                    @Override
                    public void accept(String s, ServerGroup serverGroup) {
                        OwnCloud.sendConsoleMessage(" §8»§f " + s + "§8(§a" + OwnCloud.getServerManager().getServersFromGroup(serverGroup).size() + "§8)");
                    }
                });
            } else if(args[0].equalsIgnoreCase("global")) {
                OwnCloud.sendConsoleMessage("Soon...");
            } else if(args[0].equalsIgnoreCase("permgroups")) {
                OwnCloud.sendConsoleMessage("Permission Gruppen");
                OwnCloud.getPermissionManager().getPermissionGroups().forEach(new Consumer<PermissionGroup>() {
                    @Override
                    public void accept(PermissionGroup permissionGroup) {
                        OwnCloud.sendConsoleMessage(" §8»§f " + permissionGroup.getName());
                    }
                });
            } else {
                OwnCloud.sendConsoleMessage("Nutze info <servers | groups | global>");
            }
        }
    }

    @Override
    public String getName() {
        return "info";
    }
}
