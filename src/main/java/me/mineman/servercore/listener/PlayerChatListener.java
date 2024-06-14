package me.mineman.servercore.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.mineman.servercore.ServerCore;
import me.mineman.servercore.rank.Rank;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerChatListener implements Listener {

    private final ServerCore main;

    public PlayerChatListener(ServerCore main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncChatEvent e){
        if(e.isCancelled()) return;

        Player player = e.getPlayer();
        Rank rank = main.getRankSystem().getRank(player.getUniqueId());
        e.setCancelled(true);
        Bukkit.getServer().sendMessage(
                player.name().color(rank.getColor())
                        .append(Component.text(" » ", NamedTextColor.GRAY)
                                .append(MiniMessage.miniMessage().deserialize("<#f7f7f7>" + ((TextComponent) e.message()).content())))
        );
    }

}
