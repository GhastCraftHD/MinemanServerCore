package me.mineman.servercore.constant;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Prefix {

    public static final Component NETWORK = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("MineMan", MineColor.NETWORK))
            .append(Component.text("] ", NamedTextColor.GRAY));

    public static final Component LOBBY = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("Lobby", MineColor.LOBBY))
            .append(Component.text("] ", NamedTextColor.GRAY));

    public static final Component SURVIVAL = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("Survival", MineColor.ERROR))
            .append(Component.text("] ", NamedTextColor.GRAY));

}
