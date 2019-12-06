package me.weckle.owncloud.command.commands;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.chatcolor.ChatColor;
import me.weckle.owncloud.command.ConsoleCommand;
import me.weckle.owncloud.screen.Screen;
import me.weckle.owncloud.server.CloudServer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ScreenCommand extends ConsoleCommand {
    boolean found = false;
    @Override
    public void execute(String[] args) {
        if(args.length == 0) {
            OwnCloud.sendConsoleMessage("§7Nutze §fsc -s <ServerID>§7 um die Server Konsole des angegebenen Server's zu öfnnen");
            OwnCloud.sendConsoleMessage("§7Oder nutze§f sc -l §7 um die Server Konsole zu verlassen");
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("-l")) {
                OwnCloud.getScreenManager().closeCurrentScreen();
                try{
                }catch(Exception ex) {
                    OwnCloud.sendConsoleMessage("§7Du befindest dich in keiner Server Konsole");
                }
            } else {
                OwnCloud.sendConsoleMessage("§7Nutze §fsc -s <ServerID>§7 um die Server Konsole des angegebenen Server's zu öfnnen");
                OwnCloud.sendConsoleMessage("§7Oder nutze§f sc -l §7 um die Server Konsole zu verlassen");
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("-s")) {
                String serverId = args[1];
                OwnCloud.getServerManager().getStartedServers().forEach(new BiConsumer<String, CloudServer>() {
                    @Override
                    public void accept(String s, CloudServer cloudServer) {
                        if(ChatColor.stripColor(s).equalsIgnoreCase(ChatColor.stripColor(serverId))) {
                            Screen screen = OwnCloud.getScreenManager().openScreen(s);
                            OwnCloud.getScreenManager().setInScreen(true);
                            OwnCloud.getScreenManager().setCurrentScreen(screen);
                            System.out.println(OwnCloud.getScreenManager().isInScreen() + " | " + OwnCloud.getScreenManager().getCurrentScreen().getCloudServer().getName());
                            found = true;
                        }
                    }
                });
                if(!found) {
                    OwnCloud.sendConsoleMessage("§7Diese Server sind gerade Online:");
                    OwnCloud.getServerManager().getStartedServers().keySet().forEach(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            OwnCloud.sendConsoleMessage("§8»§f " + s);
                        }
                    });
                    found = false;
                }
            } else {
                OwnCloud.sendConsoleMessage("§7Nutze §fsc -s <ServerID>§7 um die Server Konsole des angegebenen Server's zu öfnnen");
                OwnCloud.sendConsoleMessage("§7Oder nutze§f sc -l §7 um die Server Konsole zu verlassen");
            }
        }
    }

    @Override
    public String getName() {
        return "sc";
    }
}
