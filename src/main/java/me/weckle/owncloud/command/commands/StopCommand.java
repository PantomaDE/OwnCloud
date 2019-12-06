package me.weckle.owncloud.command.commands;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;

public class StopCommand extends ConsoleCommand {

    @Override
    public void execute(String[] args) {
        OwnCloud.sendConsoleMessage("Stoppe OwnCloud");
        OwnCloud.sendConsoleMessage("Bye!");
        OwnCloud.getServerWatcher().stop();
        OwnCloud.getCloudScheduler().scheduleAsyncDelay(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 6);
    }

    @Override
    public String getName() {
        return "stop";
    }
}
