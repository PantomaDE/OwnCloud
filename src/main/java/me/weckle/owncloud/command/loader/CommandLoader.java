package me.weckle.owncloud.command.loader;


import com.google.common.reflect.ClassPath;
import me.weckle.owncloud.command.ConsoleCommand;
import me.weckle.owncloud.command.commands.*;

import java.util.HashMap;

public class CommandLoader {

    private HashMap<String, ConsoleCommand> commands;

    public CommandLoader() {
        commands = new HashMap<>();
        commands.put("stop", new StopCommand());
        commands.put("creategroup", new CreateGroupCommand());
        commands.put("sc", new ScreenCommand());
        commands.put("start", new StartCommand());
        commands.put("info", new InfoCommand());
        commands.put("createpermgroup", new CreatePermissionGroupCommand());
    }

    public HashMap<String, ConsoleCommand> getCommands() {
        return commands;
    }

    private final void loadCommands() {
        try {
            for(ClassPath.ClassInfo classInfo : ClassPath.from(this.getClass().getClassLoader()).getTopLevelClasses("me.weckle.owncloud.command.commands")) {
                Class<?> clazz = Class.forName(classInfo.getName());

                if(ConsoleCommand.class.isAssignableFrom(clazz)) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
