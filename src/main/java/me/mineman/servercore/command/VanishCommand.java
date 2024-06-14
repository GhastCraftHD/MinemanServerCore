package me.mineman.servercore.command;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.constant.Message;
import me.mineman.servercore.manager.VanishManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    private final VanishManager vanishManager;

    public VanishCommand(ServerCore main) {
        this.vanishManager = main.getVanishManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return false;

        if(vanishManager.isVanished(player)){
            vanishManager.show(player);
            player.sendMessage(Message.VANISH_EXIT);
        }else{
            vanishManager.hide(player);
            player.sendMessage(Message.VANISH_ENTER);
        }

        return true;
    }
}
