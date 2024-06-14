package me.mineman.servercore.manager;

import me.mineman.servercore.ServerCore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class ConfigManager {

    public static ConfigManager INSTANCE;
    private final ServerCore main;
    private final FileConfiguration config;

    public ConfigManager(ServerCore main){
        this.main = main;
        ConfigManager.INSTANCE = this;
        this.config = main.getConfig();
    }

    public Optional<Boolean> getBoolean(String path){
        return Optional.of(config.getBoolean(path));
    }

}
