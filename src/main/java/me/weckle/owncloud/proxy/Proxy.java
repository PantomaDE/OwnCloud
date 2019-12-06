package me.weckle.owncloud.proxy;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.configuration.Configuration;
import me.weckle.owncloud.configuration.ConfigurationProvider;
import me.weckle.owncloud.configuration.YamlConfiguration;
import me.weckle.owncloud.file.FileCopyManager;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Proxy {


    private Process process;

    private FileCopyManager fileCopyManager;

    public Proxy() {
        fileCopyManager = new FileCopyManager();
    }

    public void start() {
        File tempDir = new File("temp/Proxy/Proxy-1");
        if(!tempDir.exists())
            tempDir.mkdirs();
        fileCopyManager.addBungeeStartFile("BungeeCord", 512, 256);
        try {
            fileCopyManager.copyFilesInDirecotry(new File("templates/Proxy"), new File("temp/Proxy/Proxy-1"));
            fileCopyManager.copyFilesToDirecotry(new File("jars/BungeeCord.jar"), new File("temp/Proxy/Proxy-1"));
            writeHost(OwnCloud.getConfig().getString("bungeecord-host"));
            this.process = new ProcessBuilder("./start.sh").directory(new File("temp/Proxy/Proxy-1")).start();
            OwnCloud.sendConsoleMessage("Starte Proxy §8[§bProxy-1§8/§b25565§8]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final void writeHost(String host) throws Exception{
        File file = new File("temp/Proxy/Proxy-1/config.yml");
        file.setReadable(true);
        String input;
        FileInputStream in = new FileInputStream(file);
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        boolean value = false;
        while((input = reader.readLine()) != null) {
            if(value){
                list.add("  host: " + host + "\n");
                value=false;
                continue;
            }

            if(input.startsWith("  motd")) {
                list.add("  motd: '" + "OwnCloud Proxy" + "\n" + "OwnCloud Proxy" + "'\n");
                value = true;
                continue;
            }

            if (input.startsWith("  query_enabled")) {
                list.add(input + "\n");
                value = false;
                continue;
            }
            list.add(input + "\n");
        }
        file.delete();
        file.createNewFile();
        file.setReadable(true);
        FileOutputStream out = new FileOutputStream(file);
        PrintWriter w = new PrintWriter(out);
        for(String wert : list) {
            w.write(wert);
            w.flush();
        }
        reader.close();
        w.close();
    }

}
