package me.bobulo.oneblock.breakblock.action;

import lombok.Getter;
import lombok.Setter;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.bukkit.util.Consumer;

@Setter
@Getter
public class ConsumerBreakAction extends BreakAction {

    private Consumer<BreakContext> consumer;

    public ConsumerBreakAction(double chance, Consumer<BreakContext> consumer) {
        super(chance);
        this.consumer = consumer;
    }

    @Override
    public void onExecute(BreakContext breakContext) {
        consumer.accept(breakContext);
    }

}
