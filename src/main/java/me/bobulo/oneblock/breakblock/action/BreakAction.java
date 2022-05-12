package me.bobulo.oneblock.breakblock.action;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import org.apache.commons.lang.Validate;

@Getter
@Setter
public abstract class BreakAction {

    private double chance;

    public BreakAction(double chance) {
        Validate.isTrue(chance > 0, "A chance não pode ser negativa: " + chance);
        this.chance = chance;
    }

    public abstract void onExecute(@NonNull BreakContext breakContext);

}
