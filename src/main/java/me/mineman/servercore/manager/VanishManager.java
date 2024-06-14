package me.mineman.servercore.manager;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.event.PlayerEnterVanishEvent;
import me.mineman.servercore.event.PlayerLeaveVanishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VanishManager extends ArrayList<Player> {

    private final ServerCore main;

    public VanishManager(ServerCore main){
        this.main = main;
    }

    public boolean isVanished(Player player){
        return this.contains(player);
    }

    public void hide(Player player){
        if(isVanished(player)) return;
        this.add(player);
        Bukkit.getPluginManager().callEvent(new PlayerEnterVanishEvent(player));
        for (Player target : Bukkit.getOnlinePlayers()) {
            if(target != player && !target.hasPermission("mineman.admin")){
                target.hidePlayer(main, player);
            }
        }
    }

    public void show(Player player){
        if(!isVanished(player)) return;
        this.remove(player);
        Bukkit.getPluginManager().callEvent(new PlayerLeaveVanishEvent(player));
        for (Player target : Bukkit.getOnlinePlayers()) {
            if(target != player && !target.hasPermission("mineman.admin")){
                target.showPlayer(main, player);
            }
        }
    }

}
