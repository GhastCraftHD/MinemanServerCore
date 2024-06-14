package me.mineman.servercore.constant;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class Message {

    public static final Component RELOADED_PERMISSIONS = Prefix.NETWORK.append(
            Component.text("Reloaded all permissions", MineColor.SUCCESS)
    );

    public static final Component VANISH_ENTER = Prefix.NETWORK.append(
            Component.text("You are now vanished", MineColor.SUCCESS)
    );

    public static final Component VANISH_EXIT = Prefix.NETWORK.append(
            Component.text("You are no longer vanished", MineColor.WARNING)
    );

    public static Component listHeader(){
        return Component.text("Mineman Network by the ", MineColor.SUCCESS)
                .append(Component.text("MoneyMaker Community", MineColor.ACCENT));
    }

    public static Component listFooter() {
        return Component.text("You are currently connected to ", MineColor.DEFAULT)
                .append(Bukkit.getServer().motd().color(MineColor.WARNING));
    }

}
