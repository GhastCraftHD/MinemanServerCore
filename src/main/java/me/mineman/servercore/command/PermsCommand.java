package me.mineman.servercore.command;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.constant.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.ILoggerFactory;

public class PermsCommand implements CommandExecutor {

    private final ServerCore main;

    public PermsCommand(ServerCore main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return false;
        if(!player.isOp()) return false;

        main.getPermissionManager().reloadPermissions();
        player.sendMessage(Message.RELOADED_PERMISSIONS);

        return true;
    }
}
