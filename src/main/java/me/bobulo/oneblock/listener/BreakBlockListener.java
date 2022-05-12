package me.bobulo.oneblock.listener;

import me.bobulo.oneblock.OneBlock;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

    private final OneBlock plugin;

    public BreakBlockListener(OneBlock oneBlock) {
        this.plugin = oneBlock;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!plugin.getOneBlocks().contains(block)) {
            return;
        }
        Player player = event.getPlayer();

        event.setCancelled(true);
        event.setDropItems(false);

        try {
            plugin.getBreakBlock().randomAction(new BreakContext(player, block));
        } catch (Exception exception) {
            exception.printStackTrace();
            player.sendMessage("§cOcorreu um erro ao executar a ação do bloco.");

        }

    }

}
