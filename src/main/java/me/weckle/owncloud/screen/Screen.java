package me.weckle.owncloud.screen;

import jline.console.ConsoleReader;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;
import me.weckle.owncloud.console.CloudConsole;
import me.weckle.owncloud.server.CloudServer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Screen {

    private CloudServer cloudServer;
    private InputStreamReader inputStreamReader;
    private BufferedReader reader;

    private CloudConsole cloudConsole;

    public Screen(CloudServer cloudServer) {
        this.cloudServer = cloudServer;
        cloudConsole = new CloudConsole();
        inputStreamReader = new InputStreamReader(cloudServer.getServerProcess().getInputStream());
        reader = new BufferedReader(inputStreamReader);
    }

    public void openScreen() {
        String message = "";
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    try {
                        String message = cloudConsole.getReader().readLine();
                        if(!message.equalsIgnoreCase("")) {
                            if(OwnCloud.getCommandLoader().getCommands().containsKey(message.split(" ")[0])) {
                                ConsoleCommand consoleCommand = OwnCloud.getCommandLoader().getCommands().get(message.split(" ")[0]);
                                List<String> args = new ArrayList<>();
                                for (String s : message.split(" ")) {
                                    if(!s.equalsIgnoreCase(consoleCommand.getName())) {
                                        args.add(s);
                                    }
                                }
                                consoleCommand.execute(toArray(args));

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readerThread.start();
        try {
            while((message = reader.readLine()) != null) {
                OwnCloud.getCloudConsole().screen(cloudServer.getName(), message);
            }
        }catch (Exception ex) {}
    }

    public void closeScreen() {
        try {
            reader.close();
            OwnCloud.sendConsoleMessage("Du hast die Server Konsole von dem Server§e " + cloudServer.getName() + "§f verlassen!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String[] toArray(List<String> list) {
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public CloudServer getCloudServer() {
        return cloudServer;
    }
}
