package me.mineman.servercore.listener;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.event.PlayerRankUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRankUpdateListener implements Listener {

    private final ServerCore main;

    public PlayerRankUpdateListener(ServerCore main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerRankUpdate(PlayerRankUpdateEvent e){
        Player player = e.getPlayer();

        if(player.hasPermission("mineman.admin")) {
            for (Player vanished : main.getVanishManager()) {
                if(player != vanished){
                    player.showPlayer(main, vanished);
                }
            }
            return;
        }

        for (Player vanished : main.getVanishManager()) {
            if(player != vanished){
                player.hidePlayer(main, vanished);
            }
        }
    }

}
