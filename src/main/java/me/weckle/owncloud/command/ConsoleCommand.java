package me.weckle.owncloud.command;

public abstract class ConsoleCommand {

    public abstract void execute(String[] args);
    public abstract String getName();

}
