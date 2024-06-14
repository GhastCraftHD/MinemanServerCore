package me.mineman.servercore.rank;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.event.PlayerRankUpdateEvent;
import me.mineman.servercore.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class RankSystem {

    private final ServerCore main;

    private final HashMap<UUID, Rank> rankCache;
    private final NameTagManager nameTagManager;

    private boolean displayRanks = true;

    public RankSystem(ServerCore main){
        this.main = main;
        this.rankCache = new HashMap<>();
        this.nameTagManager = new NameTagManager(this);
        ConfigManager.INSTANCE.getBoolean("display-ranks").ifPresent(val -> this.displayRanks = val);
    }

    public Rank getRank(UUID uuid){
        return rankCache.get(uuid);
    }

    public void addRank(UUID uuid, Rank rank){
        if(Bukkit.getPlayer(uuid) == null) return;
        Player player = Bukkit.getPlayer(uuid);
        rankCache.put(uuid, rank);
        main.getPermissionManager().addPermissions(player);
        if(displayRanks){
            nameTagManager.setNameTags(player);
            nameTagManager.updateTags(player);
        }
        player.updateCommands();
        player.setOp(rank == Rank.ADMINISTRATOR);
        Bukkit.getPluginManager().callEvent(new PlayerRankUpdateEvent(Bukkit.getPlayer(uuid), rank));
    }

    public void removeRank(UUID uuid){
        if(Bukkit.getPlayer(uuid) == null) return;
        if(!rankCache.containsKey(uuid)) return;
        rankCache.remove(uuid);
        if(displayRanks){
            nameTagManager.removeNameTag(Bukkit.getPlayer(uuid));
        }
    }

}
