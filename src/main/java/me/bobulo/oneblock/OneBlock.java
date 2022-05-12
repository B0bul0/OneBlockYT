package me.bobulo.oneblock;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.bobulo.oneblock.breakblock.BreakBlock;
import me.bobulo.oneblock.breakblock.action.DropItemBreakAction;
import me.bobulo.oneblock.breakblock.action.SpawnMobBreakAction;
import me.bobulo.oneblock.listener.BreakBlockListener;
import me.bobulo.oneblock.listener.ConnectionListener;
import me.bobulo.oneblock.phase.BlockPhase;
import me.bobulo.oneblock.user.UserMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class OneBlock extends JavaPlugin {

    @Getter
    private BreakBlock breakBlock;

    @Getter
    private Set<Block> oneBlocks;

    @Getter
    private UserMap userMap;

    @Override
    public void onEnable() {
        super.onEnable();
        this.userMap = new UserMap();
        this.breakBlock = new BreakBlock();
        this.oneBlocks = Sets.newHashSet(Bukkit.getWorlds().get(0).getBlockAt(0, 80, 0));


        for (Block oneBlock : this.oneBlocks) {
            oneBlock.setType(Material.GRASS_BLOCK);
        }

        registerEvents(new BreakBlockListener(this), new ConnectionListener(this));

        BlockPhase blockPhase1 = new BlockPhase(1);
        blockPhase1.addBreakAction(new DropItemBreakAction(1, new ItemStack(Material.ACACIA_WOOD)));

        BlockPhase blockPhase2 = new BlockPhase(2, 5);

        breakBlock.registerPhase(blockPhase1);
        breakBlock.registerPhase(blockPhase2);

        registerAllItens(2);
        registerAllMobs(2);

        this.breakBlock.shuffle();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.breakBlock.unRegisterAll();
        this.oneBlocks.clear();
    }

    public void registerAllItens(int phase) {
        // Registrar todos os itens do minecraft

        for (Material value : Material.values()) {
            if (value.isLegacy() || !value.isItem())
                continue;

            if (value == Material.STRUCTURE_BLOCK)
                continue;

            if (value.name().endsWith("SPAWN_EGG"))
                continue;

            ItemStack itemStack = new ItemStack(value);

            DropItemBreakAction action = new DropItemBreakAction(1, itemStack);
            this.breakBlock.registerAction(phase, action);
        }
    }

    public void registerAllMobs(int phase) {

        // Registrar todos os mobs do minecraft

        for (EntityType value : EntityType.values()) {
            if (!value.isSpawnable() || !value.isAlive())
                continue;
            if (value == EntityType.WITHER || value == EntityType.ENDER_DRAGON)
                continue;

            SpawnMobBreakAction action = new SpawnMobBreakAction(1, value);
            this.breakBlock.registerAction(phase, action);
        }
    }


    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public static OneBlock getInstance() {
        return getPlugin(OneBlock.class);
    }

}
