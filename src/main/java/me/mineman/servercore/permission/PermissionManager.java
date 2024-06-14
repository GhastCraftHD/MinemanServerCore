package me.mineman.servercore.permission;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.Set;

public class PermissionManager {

    private final ServerCore main;
    private PermissionMap permissions;

    public PermissionManager(ServerCore main) {
        this.main = main;
        this.permissions = new PermissionMap(main);
    }

    public void addPermissions(Player player){
        removePermissions(player);

        Rank rank = main.getRankSystem().getRank(player.getUniqueId());

        permissions.getPermissions(rank)
                .forEach(
                        val -> player.addAttachment(main).setPermission(val, true)
                );

    }

    public void removePermissions(Player player){

        Set<PermissionAttachmentInfo> effectivePermissions = player.getEffectivePermissions();

        for (PermissionAttachmentInfo info : effectivePermissions) {
            if(info.getAttachment() == null) continue;
            try{
                player.removeAttachment(info.getAttachment());
            }catch (IllegalArgumentException ignore){}
        }

    }

    public void reloadPermissions(){
        this.permissions = new PermissionMap(main);
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPermissions(player);
        }
    }

}
