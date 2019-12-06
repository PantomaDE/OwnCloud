package me.weckle.owncloud.console.reader;

import jline.console.ConsoleReader;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CommandReader {

    private ConsoleReader consoleReader;

    public CommandReader() {
        try {
            consoleReader = new ConsoleReader();
            consoleReader.setPrompt("> ");
            startReading();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void startReading() {
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    try {
                        String message = consoleReader.readLine();
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
    }



    private final String[] toArray(List<String> list) {
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

}
