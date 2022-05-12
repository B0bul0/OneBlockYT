package me.bobulo.oneblock.listener;

import me.bobulo.oneblock.OneBlock;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import me.bobulo.oneblock.phase.BlockPhase;
import me.bobulo.oneblock.user.User;
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
        User user = plugin.getUserMap().getUser(player);

        event.setCancelled(true);
        event.setDropItems(false);

        try {
            plugin.getBreakBlock().randomAction(new BreakContext(user, block));
            int i = user.addBrokenBlocks();

            BlockPhase phase = plugin.getBreakBlock().getPhase(user.getPhase() + 1);
            if (phase == null || phase.getBlocksNeeded() <= 0)
                return;

            if (phase.getBlocksNeeded() <= i) {
                user.upgradePhase();
                plugin.getLogger().info(player.getName() + " passou para etapa " + user.getPhase());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            player.sendMessage("§cOcorreu um erro ao executar a ação do bloco: " + exception.getMessage());
        }

    }

}
