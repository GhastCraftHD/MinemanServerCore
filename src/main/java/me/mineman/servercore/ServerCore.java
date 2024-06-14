package me.mineman.servercore;

import me.mineman.servercore.command.PermsCommand;
import me.mineman.servercore.command.VanishCommand;
import me.mineman.servercore.listener.*;
import me.mineman.servercore.manager.ConfigManager;
import me.mineman.servercore.manager.PlayerDistributionManager;
import me.mineman.servercore.manager.VanishManager;
import me.mineman.servercore.permission.PermissionManager;
import me.mineman.servercore.rank.RankSystem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerCore extends JavaPlugin {

    public static String CHANNEL_IDENTIFIER = "mineman:main";

    private RankSystem rankSystem;
    //private NickSystem nickSystem;
    private PermissionManager permissionManager;
    private PlayerDistributionManager playerDistributionManager;
    private ConfigManager configManager;
    private VanishManager vanishManager = new VanishManager(this);

    @Override
    public void onLoad(){
        this.configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.rankSystem = new RankSystem(this);
        //this.nickSystem = new NickSystem();
        this.permissionManager = new PermissionManager(this);
        this.playerDistributionManager = new PlayerDistributionManager(this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL_IDENTIFIER);
        getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL_IDENTIFIER, new MainChannelListener(this));

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRankUpdateListener(this), this);
    }

    private void registerCommands(){
        getCommand("reloadperms").setExecutor(new PermsCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
    }

    public RankSystem getRankSystem() {
        return rankSystem;
    }

    /*public NickSystem getNickSystem() {
        return nickSystem;
    }*/

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public PlayerDistributionManager getPlayerDistributionManager() {
        return playerDistributionManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }
}
