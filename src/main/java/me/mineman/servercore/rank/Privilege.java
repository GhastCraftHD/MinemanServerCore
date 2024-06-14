package me.mineman.servercore.rank;

import org.bukkit.entity.Player;

public interface Privilege {

    void grant(Player player);
    void revoke(Player player);

}
