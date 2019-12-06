package me.weckle.owncloud.command.commands;

import me.weckle.owncloud.OwnCloud;
import me.weckle.owncloud.command.ConsoleCommand;
import me.weckle.owncloud.configuration.Configuration;
import me.weckle.owncloud.configuration.ConfigurationProvider;
import me.weckle.owncloud.configuration.YamlConfiguration;
import me.weckle.owncloud.group.type.TemplateType;

import java.io.File;

public class CreateGroupCommand extends ConsoleCommand {

    @Override
    public void execute(String[] args) {
        if(args.length == 0) {
            OwnCloud.sendConsoleMessage("Nutze creategroup <Name> <minMB> <maxMB> <minServers> <maxServers> <templateType> <maxPlayers>");
        } else if(args.length == 7) {
            String name = args[0];
            int minMB = Integer.valueOf(args[1]);
            int maxMB = Integer.valueOf(args[2]);
            int minServers = Integer.valueOf(args[3]);
            int maxServers = Integer.valueOf(args[4]);
            try {
                TemplateType type = TemplateType.valueOf(args[5].toUpperCase());
                if(!OwnCloud.getGroupManager().getServerGroups().containsKey(name)) {
                    File file = new File("groups/" + name + ".yml");
                    file.createNewFile();
                    Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                    cfg.set("Name", name);
                    cfg.set("minMB", minMB);
                    cfg.set("maxMB", maxMB);
                    cfg.set("TemplateType", type.name());
                    cfg.set("minServers", minServers);
                    cfg.set("maxServers", maxServers);
                    cfg.set("maxPlayers", Integer.valueOf(args[6]));
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
                    OwnCloud.sendConsoleMessage("Die Gruppe §6" + name + "§f wurde erfolgreich erstellt!");
                    File dir = new File("templates/" + name);
                    if(!dir.exists()) {
                        OwnCloud.sendConsoleMessage("Template Ordner für " + name + "" + dir.createNewFile());
                    }
                } else {
                    OwnCloud.sendConsoleWarn("§cDiese Gruppe existiert bereits!");
                }
            } catch (Exception ex) {
                OwnCloud.sendConsoleMessage("TemplateTypes:");
                for (TemplateType templateType : TemplateType.values()) {
                    OwnCloud.sendConsoleMessage("§8»§f " + templateType.name());
                }
            }
        }
    }

    @Override
    public String getName() {
        return "creategroup";
    }
}
