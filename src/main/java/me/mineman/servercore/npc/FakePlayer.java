package me.mineman.servercore.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.mineman.servercore.ServerCore;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Transformation;

import java.util.*;

public abstract class FakePlayer implements Listener {

    private final ServerCore main;
    private final Map<UUID, ServerPlayer> serverPlayers;
    private final UUID uuid;
    private Interaction interaction;
    private TextDisplay textDisplay;
    private final Location location;
    private final String skinValue;
    private final String skinSignature;
    private final boolean lookAtPlayers;
    private Item itemInMainHand = null;

    public FakePlayer(ServerCore main, Location location, Skin skin, boolean lookAtPlayers) {
        this.main = main;
        this.location = location;
        this.skinSignature = skin.skinSignature();
        this.skinValue = skin.skinValue();
        this.serverPlayers = new HashMap<>();
        this.uuid = UUID.randomUUID();
        this.lookAtPlayers = lookAtPlayers;
        setUpInteraction();
        setUpTextDisplay();
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public FakePlayer(ServerCore main, Location location, Skin skin, boolean lookAtPlayers, Item itemInMainHand){
        this(main, location, skin, lookAtPlayers);
        this.itemInMainHand = itemInMainHand;
    }


    private void setUpTextDisplay(){
        textDisplay = location.getWorld().spawn(location.clone().add(0, 2, 0), TextDisplay.class);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.text(displayName());
        textDisplay.setDefaultBackground(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(0.9);
        textDisplay.setTransformation(transformation);
        textDisplay.addScoreboardTag("forRemoval");
    }

    private void setUpInteraction(){
        interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(1.25F);
        interaction.setInteractionHeight(2.0F);
        interaction.addScoreboardTag("forRemoval");
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    public void delete(List<FakePlayer> fakePlayers) {
        unregister();
        interaction.remove();
        textDisplay.remove();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            if (serverPlayers.containsKey(player.getUniqueId())){
                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayers.get(player.getUniqueId()).getUUID())));
                connection.send(new ClientboundRemoveEntitiesPacket(serverPlayers.get(player.getUniqueId()).getId()));
            }
        }
        fakePlayers.remove(this);

    }

    public abstract void execute(Player player);

    public abstract Component displayName();

    public void spawn(Player player) {
        GameProfile gameProfile = new GameProfile(uuid, "null");
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();

        gameProfile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));

        assert minecraftServer != null;
        final ServerPlayer serverPlayer = new ServerPlayer(minecraftServer, serverLevel, gameProfile);
        serverPlayers.put(player.getUniqueId(), serverPlayer);
        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());

        if(itemInMainHand != null){
            serverPlayer.getMainHandItem().setItem(itemInMainHand);
        }

        SynchedEntityData synchedEntityData = serverPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
        serverPlayer.connection = ((CraftPlayer) player).getHandle().connection;
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer));
        connection.send(new ClientboundAddPlayerPacket(serverPlayer));
        connection.send(new ClientboundSetEntityDataPacket(serverPlayer.getId(), synchedEntityData.getNonDefaultValues()));

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        connection.send(new ClientboundRotateHeadPacket(serverPlayer, (byte) ((yaw % 360) * 256 / 360)));
        connection.send(new ClientboundMoveEntityPacket.Rot(serverPlayer.getBukkitEntity().getEntityId(), (byte) ((yaw % 360) * 256 / 360), (byte) ((pitch % 360) * 256 / 360), true));

        PlayerTeam playerTeam = new PlayerTeam(new Scoreboard(), serverPlayer.getName().toString());
        playerTeam.getPlayers().add(ChatColor.stripColor(serverPlayer.getDisplayName().getString()));
        playerTeam.setNameTagVisibility(Team.Visibility.NEVER);
        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayer.getUUID()))), 40);

    }



    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Interaction interaction)) return;
        if (interaction.getEntityId() != this.interaction.getEntityId()) return;
        execute(event.getPlayer());
    }
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Interaction interaction)) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (interaction.getEntityId() != this.interaction.getEntityId()) return;
        execute(player);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!(player.getWorld().equals(location.getWorld()))) return;
        spawn(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!(player.getWorld().equals(location.getWorld()))) return;
        spawn(player);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        serverPlayers.remove(uuid);

    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (!lookAtPlayers) return;
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location playerLocation = player.getLocation();
        if (!(player.getWorld().equals(location.getWorld()))) return;
        // check if the distance between the npc and the player is less than 10 blocks
        if (playerLocation.distance(location) > 50) return;

        location.setDirection(playerLocation.subtract(location).toVector());
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        if (!serverPlayers.containsKey(uuid)) return;
        rotateHead(connection, yaw, pitch,serverPlayers.get(uuid));
    }

    private void rotateHead(ServerGamePacketListenerImpl connection, float yaw, float pitch, ServerPlayer serverPlayer){
        connection.send(new ClientboundRotateHeadPacket(serverPlayer, (byte) ((yaw % 360) * 256 / 360)));
        connection.send(new ClientboundMoveEntityPacket.Rot(serverPlayer.getBukkitEntity().getEntityId(), (byte) ((yaw % 360) * 256 / 360), (byte) ((pitch % 360) * 256 / 360), true));
    }


    public Interaction getInteraction() {return interaction;}

    public TextDisplay getTextDisplay() {return textDisplay;}

    public boolean isLookAtPlayers() {return lookAtPlayers;}

    public UUID getUuid() {
        return uuid;
    }
}
