package me.mineman.servercore.listener;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.constant.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ServerCore main;

    public PlayerJoinListener(ServerCore main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        e.joinMessage(Component.empty());
        player.sendPlayerListHeader(Message.listHeader());
        player.sendPlayerListFooter(Message.listFooter());
    }

}
