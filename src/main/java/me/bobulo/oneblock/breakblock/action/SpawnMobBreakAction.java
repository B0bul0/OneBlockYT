package me.bobulo.oneblock.breakblock.action;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

@Setter
@Getter
@ToString
public class SpawnMobBreakAction extends BreakAction {

    private EntityType entityType;

    public SpawnMobBreakAction(double chance, @NonNull EntityType entityType) {
        super(chance);
        this.entityType = entityType;
    }

    @Override
    public void onExecute(BreakContext breakContext) {
        Location location = breakContext.getBlock().getLocation().add(0.5, 1, 0.5);
        World world = location.getWorld();

        if (world == null)
            return;

        world.spawnEntity(location, entityType);
    }

}
