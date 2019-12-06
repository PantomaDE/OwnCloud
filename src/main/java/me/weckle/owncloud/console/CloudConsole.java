package me.weckle.owncloud.console;

import jline.console.ConsoleReader;
import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.console.writer.ColouredWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class CloudConsole {

    private PrintWriter w;
    ConsoleReader reader;
    ColouredWriter writer;

    private SimpleDateFormat dateFormat;

    public CloudConsole() {
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        w = new PrintWriter(System.out);
        try{
            reader = new ConsoleReader();
            reader.setPrompt("> ");
            writer = new ColouredWriter(reader);
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public String readLine() throws Exception {
        return reader.readLine();
    }

    public void writeBlank(String message) {
        writer.print(message);
        writer.flush();
    }

    public void write(String message) {
        if(!OwnCloud.getScreenManager().isInScreen()) {
            writer.print("§8[§f" + dateFormat.format(System.currentTimeMillis()) + " §eINFO§8]§7: §f" + message  + "\n");
            writer.flush();
        } else {
            OwnCloud.getScreenManager().addMessage("§8[§f" + dateFormat.format(System.currentTimeMillis()) + " §eINFO§8]§7: §f" + message  + "\n");
        }
    }
    public void screen(String serverid, String message) {
        writer.print("§8[" + serverid + "§8]§f " + message  + "\n");
        writer.flush();
    }

    public void writeHeader(String message) {
        writer.print("§7" + message);
        writer.flush();
    }

    public void debug(String message) {
        writer.print("§8[§f" + dateFormat.format(System.currentTimeMillis()) + " §eDebug§8]§7: §f" + message  + "\n");
        writer.flush();
    }

    public void warn(String message) {
        if(!OwnCloud.getScreenManager().isInScreen()) {
            writer.print("§8[§f" + dateFormat.format(System.currentTimeMillis()) + " §4WARN§8]§7: §f" + message  + "\n");
            writer.flush();
        } else {
            OwnCloud.getScreenManager().addMessage("§8[§f" + dateFormat.format(System.currentTimeMillis()) + " §4WARN§8]§7: §f" + message  + "\n");
        }
    }



    public ConsoleReader getReader() {
        return reader;
    }

}
