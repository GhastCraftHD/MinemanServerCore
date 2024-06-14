package me.mineman.servercore.npc;

import me.mineman.servercore.ServerCore;
import org.bukkit.Location;

public record NPC(ServerCore main, Location location, Skin skin, Boolean lookAtPlayers, Class<? extends FakePlayer> npcClass) {}
