package me.mineman.servercore.listener;

import me.mineman.servercore.ServerCore;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private ServerCore main;

    public PlayerQuitListener(ServerCore main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        e.quitMessage(Component.empty());
        main.getRankSystem().removeRank(e.getPlayer().getUniqueId());
    }
}
