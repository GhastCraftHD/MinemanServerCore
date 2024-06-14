package me.mineman.servercore.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.mineman.servercore.ServerCore;
import org.bukkit.entity.Player;

public class PlayerDistributionManager {

    private ServerCore main;

    public PlayerDistributionManager(ServerCore main) {
        this.main = main;
    }

    public void sendConnectionRequest(Player player, String servername){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("ConnectionRequest");
        output.writeUTF(servername);
        player.sendPluginMessage(main, ServerCore.CHANNEL_IDENTIFIER, output.toByteArray());
    }

}
