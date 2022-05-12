package me.bobulo.oneblock.listener;

import me.bobulo.oneblock.OneBlock;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final OneBlock plugin;

    public ConnectionListener(OneBlock oneBlock) {
        this.plugin = oneBlock;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getUserMap().createAndRegisterUser(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getUserMap().removeUser(player);
    }

}
