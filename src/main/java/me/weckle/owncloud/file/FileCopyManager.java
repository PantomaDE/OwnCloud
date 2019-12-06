package me.weckle.owncloud.file;

import me.weckle.owncloud.OwnCloud;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class FileCopyManager {

    public void copyFilesInDirecotry(File from, File to) throws IOException {
        if(!to.exists()) {
            to.mkdirs();
        }
        for (File file : from.listFiles()) {
            if(file.isDirectory()) {
                copyFilesInDirecotry(file, new File(to.getAbsolutePath() + "/" + file.getName()));
            } else {
                File n = new File(to.getAbsolutePath() + "/" + file.getName());

                Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public void addStartFile(String serverid, String filename, int maxram,
                             int minram) {
        String group = serverid.split(OwnCloud.getConfig().getString("Server-Split"))[0];
        File f = new File("temp/" + group + "/" + serverid + "/start.sh");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Writer w = new FileWriter(f);
            f.setReadable(true);
            f.setExecutable(true);
            w.write("java -Djava.awt.headless=true -Xmx"
                    + maxram
                    + "M -Xms"
                    + minram
                    + "M -XX:MaxPermSize=256M -server -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xincgc "
                    + "-XX:+CMSParallelRemarkEnabled -Djline.terminal=jline.UnsupportedTerminal -Xmn2M -Dcom.mojang.eula.agree=true -jar " + filename
                    + ".jar nogui");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBungeeStartFile(String filename, int maxram,
                                   int minram) {
        File f = new File("temp/Proxy/Proxy-1/start.sh");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Writer w = new FileWriter(f);
            f.setReadable(true);
            f.setExecutable(true);
            w.write("java -Xmx"
                    + maxram
                    + "M -Xms"
                    + minram
                    + "M -Dcom.mojang.eula.agree=true -jar " + filename
                    + ".jar nogui");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addUtilsFile(String serverid, int port, String redisHost, String redisPassword) {
        File f = new File("temp/"+serverid+"/CLOUD/utils.properties");
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(f));
            prop.setProperty("ServerID", serverid);
            prop.setProperty("Port", port+"");
            prop.save(new FileOutputStream(f), "Edited by OwnCloud.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDoubleFile(String serverid, String filename, int maxram,
                              int minram) {
        File f = new File("temp/" + serverid + "/to.sh");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Writer w = new FileWriter(f);
            f.setReadable(true);
            f.setExecutable(true);
            w.write("java -Djava.awt.headless=true -Xmx"
                    + maxram
                    + "M -Xms"
                    + minram
                    + "M -XX:MaxPermSize=256M -server -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -Xincgc "
                    + "-XX:+CMSParallelRemarkEnabled -jar " + filename
                    + ".jar nogui");
            w.flush();
            w.close();
            File file = new File("temp/" + serverid + "/start.sh");
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer ww = new FileWriter(file);
            file.setReadable(true);
            file.setExecutable(true);
            ww.write("screen -S " + serverid + " ./to.sh");
            ww.flush();
            ww.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFilesToDirecotry(File file, File to) throws IOException {
        if(!to.exists()) {
            to.mkdirs();
        }
        File n = new File(to.getAbsolutePath() + "/" + file.getName());
        Files.copy(file.toPath(), n.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
