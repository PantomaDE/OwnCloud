package me.weckle.owncloud.command.commands;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;
import me.weckle.owncloud.server.CloudServer;

public class StartCommand extends ConsoleCommand {

    @Override
    public void execute(String[] args) {
        if(args.length == 0) {
            OwnCloud.sendConsoleMessage("Nutze 'start <GroupName>' oder 'start <GruppenName> <Menge>'");
        } else if(args.length == 1) {
            String group = args[0];
            if(OwnCloud.getGroupManager().getServerGroups().containsKey(group)) {
                OwnCloud.getServerManager().startNewCloudServer(OwnCloud.getGroupManager().getServerGroups().get(group));
            }
        } else if(args.length == 2) {
            String group = args[0];
            int amount = Integer.valueOf(args[1]);
            if(OwnCloud.getGroupManager().getServerGroups().containsKey(group)) {
                for (int i = 0; i < amount; i++) {
                    OwnCloud.getServerManager().startNewCloudServer(OwnCloud.getGroupManager().getServerGroups().get(group));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "start";
    }
}
