package me.weckle.owncloud.command.commands;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;

public class CreatePermissionGroupCommand extends ConsoleCommand {

    @Override
    public void execute(String[] args) {
        if(args.length == 0) {
            OwnCloud.sendConsoleMessage("Nutze createpermgroup <Name>");
        } else if(args.length == 1) {
            String name = args[0];
            OwnCloud.getPermissionManager().createPermissionGroup(name);
            OwnCloud.sendConsoleMessage("Die Permission Gruppe§a " + name + "§f wurde erstellt");
        }
    }

    @Override
    public String getName() {
        return "createpermgroup";
    }
}
