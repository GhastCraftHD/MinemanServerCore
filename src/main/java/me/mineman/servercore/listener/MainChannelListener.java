package me.mineman.servercore.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.mineman.servercore.ServerCore;
import me.mineman.servercore.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class MainChannelListener implements PluginMessageListener {

    private final ServerCore main;

    public MainChannelListener(ServerCore main) {
        this.main = main;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] bytes) {
        if(!channel.equals(ServerCore.CHANNEL_IDENTIFIER)) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);

        String subchannel = input.readUTF();

        if(subchannel.equals("RankInformation")){
            Rank rank = Rank.valueOf(input.readUTF());
            main.getRankSystem().addRank(player.getUniqueId(), rank);
        }
    }
}
