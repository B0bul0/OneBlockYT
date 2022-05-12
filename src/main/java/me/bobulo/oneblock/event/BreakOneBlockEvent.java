package me.bobulo.oneblock.event;

import lombok.Getter;
import lombok.Setter;
import me.bobulo.oneblock.breakblock.action.BreakAction;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class BreakOneBlockEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    private final BreakContext breakContext;
    private final BreakAction breakAction;

    @Setter
    private boolean cancelled;

    public BreakOneBlockEvent(BreakContext breakContext, BreakAction breakAction) {
        this.breakContext = breakContext;
        this.breakAction = breakAction;
    }

    public Player getPlayer() {
        return breakContext.getPlayer();
    }

    public Block getBlock() {
        return breakContext.getBlock();
    }

}
