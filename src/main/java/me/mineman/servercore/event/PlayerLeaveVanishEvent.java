package me.mineman.servercore.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveVanishEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public PlayerLeaveVanishEvent(Player player) {
        this.player = player;
    }

    public @NotNull HandlerList getHandlers(){
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

}
