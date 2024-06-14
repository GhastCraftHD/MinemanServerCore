package me.mineman.servercore.nick;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Deprecated
public class NickSystem {

    private Map<UUID, String> nickCache;

    public NickSystem() {
        this.nickCache = new HashMap<>();
    }

    public String getNick(UUID uuid){
        return nickCache.get(uuid);
    }

    public void addNick(UUID uuid, String nick){
        if(Bukkit.getPlayer(uuid) == null) return;
        Player player = Bukkit.getPlayer(uuid);
        if(nick == "") return;
        nickCache.put(uuid, nick);
        player.playerListName(Component.text(nick));
        player.displayName(Component.text(nick));
        player.updateCommands();
    }

    public void removeNick(UUID uuid){
        nickCache.remove(uuid);
    }
}
