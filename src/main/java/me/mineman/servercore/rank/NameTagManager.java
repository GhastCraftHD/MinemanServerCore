package me.mineman.servercore.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class NameTagManager {

    private final RankSystem rankSystem;

    public NameTagManager(RankSystem rankSystem) {
        this.rankSystem = rankSystem;
    }

    public void setNameTags(Player player){
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        for(Rank rank : Rank.values()){
            Team team = player.getScoreboard().registerNewTeam(rank.getTabSorter() + rank.name());
            team.prefix(
                    Component.text(rank.getDisplay(), rank.getColor())
                            .append(Component.text(" | ", NamedTextColor.GRAY))
            );
        }

        Rank playerRank = rankSystem.getRank(player.getUniqueId());

        Team team = player.getScoreboard().getTeam(playerRank.getTabSorter() + playerRank.name());

        player.playerListName(
                team.prefix().append(player.name().color(playerRank.getColor()))
        );
        player.displayName(
                team.prefix().append(player.name().color(playerRank.getColor()))
        );

        for(Player target : Bukkit.getOnlinePlayers()){
            if(player.getUniqueId() == target.getUniqueId()) continue;
            Rank targetRank = rankSystem.getRank(target.getUniqueId());
            player.getScoreboard().getTeam(targetRank.getTabSorter() + targetRank.name()).addEntry(target.getName());
        }
    }

    public void newNameTag(Player player){
        Rank rank = rankSystem.getRank(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(
                target -> target.getScoreboard().getTeam(rank.getTabSorter() + rank.name()).addEntry(player.getName())
        );
    }

    public void removeNameTag(Player player){
        for(Player target : Bukkit.getOnlinePlayers()){
            Team team = target.getScoreboard().getEntryTeam(player.getName());
            if(team == null) return;
            team.removeEntry(player.getName());
        }

    }

    public void updateTags(Player player){
        removeNameTag(player);
        newNameTag(player);
    }

}
