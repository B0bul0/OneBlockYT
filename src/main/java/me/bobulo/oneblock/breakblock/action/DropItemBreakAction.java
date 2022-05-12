package me.bobulo.oneblock.breakblock.action;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

@Setter
@Getter
@ToString
public class DropItemBreakAction extends BreakAction {

    private ItemStack itemStack;

    public DropItemBreakAction(double chance, ItemStack itemStack) {
        super(chance);
        this.itemStack = itemStack;
    }

    @Override
    public void onExecute(BreakContext breakContext) {
        Location location = breakContext.getBlock().getLocation().add(0.5, 0.3, 0.5);
        World world = location.getWorld();

        if (world == null)
            return;

        world.dropItemNaturally(location, itemStack);
    }

}
