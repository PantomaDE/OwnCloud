package me.weckle.owncloud.screen.manager;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.screen.Screen;
import me.weckle.owncloud.server.CloudServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ScreenManager {

    private List<Screen> currentScreen;
    private boolean isInScreen;
    private List<String> cachedMessages;

    public ScreenManager() {
        currentScreen = new CopyOnWriteArrayList<>();
        isInScreen = false;
        cachedMessages = new ArrayList<>();
    }

    public Screen openScreen(String serverid) {
        CloudServer cloudServer = OwnCloud.getServerManager().getStartedServers().get(serverid);
        Screen screen = new Screen(cloudServer);
        screen.openScreen();
        return screen;
    }

    public void closeCurrentScreen() {
        currentScreen.get(0).closeScreen();
        setCurrentScreen(null);
        setInScreen(false);
        cachedMessages.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                OwnCloud.getCloudConsole().writeBlank(s);
            }
        });
        cachedMessages.clear();
    }

    public void addMessage(String message) {
        cachedMessages.add(message);
    }

    public void setInScreen(boolean inScreen) {
        isInScreen = inScreen;
    }

    public void setCurrentScreen(Screen currentScreen) {
        this.currentScreen.clear();
        this.currentScreen.add(currentScreen);
    }

    public List<String> getCachedMessages() {
        return cachedMessages;
    }

    public Screen getCurrentScreen() {
        return currentScreen.get(0);
    }


    public boolean isInScreen() {
        return isInScreen;
    }

}
