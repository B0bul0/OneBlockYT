package me.bobulo.oneblock;

import lombok.Getter;
import me.bobulo.oneblock.breakblock.BreakBlock;
import me.bobulo.oneblock.breakblock.action.DropItemBreakAction;
import me.bobulo.oneblock.breakblock.action.SpawnMobBreakAction;
import me.bobulo.oneblock.listener.BreakBlockListener;
import me.bobulo.oneblock.listener.ConnectionListener;
import me.bobulo.oneblock.phase.BlockPhase;
import me.bobulo.oneblock.user.UserMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OneBlock extends JavaPlugin {

    private static OneBlock instance;
    @Getter
    private BreakBlock breakBlock;

    @Getter
    private Set<Block> oneBlocks;

    @Getter
    private UserMap userMap;

    @Override
    public void onLoad() {
        instance = this;

        this.userMap = new UserMap();
        this.breakBlock = new BreakBlock();
        this.oneBlocks = new HashSet<>();

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        List<String> locations = getConfig().getStringList("locations");

        for (String text : locations) {
            String[] split = text.split(";");

            World world = Bukkit.getWorld(split[0]);
            Validate.notNull(world, "World " + split[0] + " não encontrado.");

            Location location = new Location(world, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));

            this.oneBlocks.add(location.getBlock());
        }

        Material matchMaterial = null;

        String configBlock = getConfig().getString("blockType");
        if (StringUtils.isNotBlank(configBlock)) {
            matchMaterial = Material.matchMaterial(configBlock);

            if (matchMaterial == null)
                matchMaterial = Material.getMaterial(configBlock);
        }

        if (matchMaterial == null)
            matchMaterial = Material.GRASS_BLOCK;

        for (Block oneBlock : this.oneBlocks) {
            oneBlock.setType(matchMaterial);
        }

        ConfigurationSection phases = getConfig().createSection("phases");

        for (int i = 1; i < 100; i++) {
            ConfigurationSection section = phases.getConfigurationSection(String.valueOf(i));
            if (section == null)
                continue;

            int phase = i;
            int blocksNeeded = section.getInt("blocksNeeded");
            List<String> drops = section.getStringList("drops");

            BlockPhase blockPhase = new BlockPhase(phase, blocksNeeded);

            breakBlock.registerPhase(blockPhase);

            for (String drop : drops) {
                if (drop.equalsIgnoreCase("AllItens")) {
                    registerAllItens(phase);
                }
                else if (drop.equalsIgnoreCase("AllMobs")) {
                    registerAllMobs(phase);
                }
                else if (drop.startsWith("item;")) {
                    String[] split = drop.split(";");
                    Material material = Material.getMaterial(split[1]);

                    breakBlock.registerAction(phase, new DropItemBreakAction(1, new ItemStack(material)));
                }
                else if (drop.startsWith("entity;")) {
                    String[] split = drop.split(";");
                    EntityType entityType = EntityType.valueOf(split[1]);

                    breakBlock.registerAction(phase, new SpawnMobBreakAction(1, entityType));
                }
            }
        }

        this.breakBlock.shuffle();

        registerEvents(
                new BreakBlockListener(this),
                new ConnectionListener(this)
        );
    }

    @Override
    public void onDisable() {
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
        return instance;
    }

}
