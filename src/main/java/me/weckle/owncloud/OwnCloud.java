package me.weckle.owncloud;

import me.weckle.owncloud.command.loader.CommandLoader;
import me.weckle.owncloud.configuration.Configuration;
import me.weckle.owncloud.configuration.ConfigurationProvider;
import me.weckle.owncloud.configuration.YamlConfiguration;
import me.weckle.owncloud.console.CloudConsole;
import me.weckle.owncloud.console.reader.CommandReader;
import me.weckle.owncloud.database.MongoConnector;
import me.weckle.owncloud.group.manager.GroupManager;
import me.weckle.owncloud.permissions.PermissionManager;
import me.weckle.owncloud.player.manager.CloudPlayerManager;
import me.weckle.owncloud.proxy.Proxy;
import me.weckle.owncloud.redis.RedisConnector;
import me.weckle.owncloud.redis.pub.RedisPubSub;
import me.weckle.owncloud.scheduler.CloudScheduler;
import me.weckle.owncloud.screen.manager.ScreenManager;
import me.weckle.owncloud.server.manager.ServerManager;
import me.weckle.owncloud.server.watcher.ServerWatcher;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OwnCloud {

    private static CloudConsole cloudConsole;
    private static Configuration config;

    private static CommandReader commandReader;
    private static CommandLoader commandLoader;

    private static RedisConnector redisConnector;

    private static GroupManager groupManager;

    private static CloudScheduler cloudScheduler;

    private static ServerManager serverManager;

    private static ScreenManager screenManager;

    private static PermissionManager permissionManager;

    private static CloudPlayerManager cloudPlayerManager;

    private static MongoConnector mongoConnector;

    private static ServerWatcher serverWatcher;

    public static void main(String[] args) {
        try {
            FileUtils.deleteDirectory(new File("temp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        screenManager = new ScreenManager();
        cloudConsole = new CloudConsole();
        initConfig();
        cloudScheduler = new CloudScheduler();
        cloudConsole.writeHeader("§f  ____                  _____ _                 _ \n" +
                " / __ \\                / ____| |               | |\n" +
                "| |  | |_      ___ __ | |    | | ___  _   _  __| |\n" +
                "| |  | \\ \\ /\\ / / '_ \\| |    | |/ _ \\| | | |/ _` |\n" +
                "| |__| |\\ V  V /| | | | |____| | (_) | |_| | (_| |\n" +
                " \\____/  \\_/\\_/ |_| |_|\\_____|_|\\___/ \\__,_|\\__,_|\n" +
                "                                                  ");
        cloudConsole.write(" ");
        commandLoader = new CommandLoader();
        cloudScheduler.scheduleAsyncDelay(new Runnable() {
            @Override
            public void run() {
                commandReader = new CommandReader();
            }
        }, 5);
        cloudConsole.write("Verbinde zu Redis...");
        redisConnector = new RedisConnector(new JedisPool("127.0.0.1", 6379), new RedisPubSub());
        Logger.getLogger("org.mongodb").setLevel(Level.OFF);
        mongoConnector = new MongoConnector();
        Logger.getLogger("org.mongodb").setLevel(Level.OFF);
        cloudConsole.write("Verbindung zu Redis wurde erfolgreich aufgebaut");
        groupManager = new GroupManager();
        serverManager = new ServerManager();
        //permissionManager = new PermissionManager();
        //cloudPlayerManager = new CloudPlayerManager();
        new Proxy().start();
        cloudConsole.write("Starte vorkonfigurierte Server in §a15§f Sekunden");
        cloudScheduler.scheduleAsyncDelay(new Runnable() {
            @Override
            public void run() {
                serverManager.startAllServers();
            }
        }, 15000);
        cloudScheduler.scheduleAsyncDelay(new Runnable() {
            @Override
            public void run() {
                serverWatcher.start();
            }
        }, 15000);
    }

    public static void sendConsoleMessage(String message) {
        cloudConsole.write(message);
    }
    public static void sendConsoleDebugMessage(String message) {
        cloudConsole.debug(message);
    }

    public static MongoConnector getMongoConnector() {
        return mongoConnector;
    }

    public static ServerWatcher getServerWatcher() {
        return serverWatcher;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }

    public static ScreenManager getScreenManager() {
        return screenManager;
    }

    public static CloudPlayerManager getCloudPlayerManager() {
        return cloudPlayerManager;
    }

    public static void sendConsoleWarn(String message) {
        cloudConsole.warn(message);
    }

    public static Configuration getConfig() {
        return config;
    }

    public static CommandLoader getCommandLoader() {
        return commandLoader;
    }

    public static CloudScheduler getCloudScheduler() {
        return cloudScheduler;
    }

    public static GroupManager getGroupManager() {
        return groupManager;
    }

    public static RedisConnector getRedisConnector() {
        return redisConnector;
    }

    public static CloudConsole getCloudConsole() {
        return cloudConsole;
    }

    public static PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public static void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File("configuration/config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static void initConfig() {
        File[] files = new File[] {new File("groups"), new File("configuration"), new File("templates"), new File("jars"), new File("global"),
        new File("permissionsystem")};
        for (File file : files) {
            if(!file.exists()) {
                file.mkdirs();
            }
        }
        File file = new File("configuration/config.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                config.set("bungeecord-host", "45.81.232.205:25565");
                config.set("Server-Split", "*");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
