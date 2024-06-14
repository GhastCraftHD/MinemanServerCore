package me.mineman.servercore.permission;

import me.mineman.servercore.ServerCore;
import me.mineman.servercore.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionMap extends HashMap<Rank, Set<String>> {

    public PermissionMap(ServerCore main) {
        if (!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }

        File file = new File(main.getDataFolder(), "permissions.yml");

        if (!file.exists()) {
            main.getLogger().severe("Could not load permission file, shutting down!");
            Bukkit.getServer().shutdown();
            return;
        }

        YamlConfiguration permissions = YamlConfiguration.loadConfiguration(file);

        for (String rankName : permissions.getKeys(false)) {
            Rank rank = Rank.valueOf(rankName.toUpperCase());
            List<String> rankPermissionsList = permissions.getStringList(rankName);
            Set<String> rankPermissions = new HashSet<>(rankPermissionsList);
            this.put(rank, rankPermissions);
        }
    }

    public Set<String> getPermissions(Rank rank) {
        Set<String> allPermissions = new HashSet<>();

        boolean shouldAdd = false;
        for (Rank currentRank : Rank.values()) {
            if (currentRank == rank) {
                shouldAdd = true;
            }
            if (shouldAdd) {
                Set<String> rankPermissions = this.get(currentRank);
                if (rankPermissions != null) {
                    allPermissions.addAll(rankPermissions);
                }
            }
        }

        return allPermissions;
    }

}

