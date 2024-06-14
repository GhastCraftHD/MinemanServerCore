package me.mineman.servercore.npc;

import me.mineman.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakePlayerManager {

    private final ServerCore main;
    private final List<FakePlayer> fakePlayers;
    private final List<UUID> fakeUUIDs;

    public FakePlayerManager(ServerCore main, List<NPC> npcs) {
      this.main = main;
      this.fakePlayers = new ArrayList<>();
      this.fakeUUIDs = new ArrayList<>();
      setupNPCS(npcs);
    }

    public void setupNPCS(List<NPC> npcs){
        BukkitRunnable removeTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity.getScoreboardTags().contains("forRemoval")) {
                          entity.remove();
                        }
                    }
                }
            }
        };
    removeTask.runTaskLater(main,50);

        BukkitRunnable setupTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (NPC npc : npcs){
                    try {
                        FakePlayer fakePlayer = npc.npcClass()
                                .getDeclaredConstructor(ServerCore.class, Location.class, Skin.class, Boolean.class)
                                .newInstance(npc.main(), npc.location(), npc.skin(), npc.lookAtPlayers());
                        createFakePlayer(fakePlayer);
                        fakeUUIDs.add(fakePlayer.getUuid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        setupTask.runTaskLater(main, 70);
    }

    private void createFakePlayer(FakePlayer fakePlayer){
        fakePlayers.add(fakePlayer);
    }

    public FakePlayer getFakePlayer(int id){
        for (FakePlayer fakePlayer : fakePlayers){
            if (fakePlayer.getInteraction().getEntityId() == id){
                return fakePlayer;
            }
        }
        return null;
    }
    public FakePlayer getFakePlayer(Interaction interaction){
        for (FakePlayer fakePlayer : fakePlayers){
            if (fakePlayer.getInteraction().equals(interaction)){
                return fakePlayer;
            }
        }
        return null;
    }

    public List<FakePlayer> getFakePlayers() {
        return fakePlayers;
    }

}
