package me.bobulo.oneblock.breakblock;

import lombok.NonNull;
import me.bobulo.oneblock.OneBlock;
import me.bobulo.oneblock.breakblock.action.BreakAction;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import me.bobulo.oneblock.event.BreakOneBlockEvent;
import me.bobulo.oneblock.event.RegisterOneBlockEvent;
import me.bobulo.oneblock.phase.BlockPhase;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BreakBlock {

    private final Map<Integer, BlockPhase> phasesActions = new HashMap<>();

    public void registerAction(int phase, @NonNull BreakAction breakAction) {
        Validate.isTrue(phase > 0, "A etapa não pode ser negativa: " + phase);

        RegisterOneBlockEvent event = new RegisterOneBlockEvent(breakAction);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        BlockPhase blockPhase = phasesActions.get(phase);

        if (blockPhase == null) {
            blockPhase = new BlockPhase(phase);
            registerPhase(blockPhase);
        }

        blockPhase.addBreakAction(breakAction);
    }

    public void registerPhase(@NonNull BlockPhase blockPhase) {
        Validate.isTrue(blockPhase.getPhase() > 0, "A etapa não pode ser negativa: " + blockPhase.getPhase());
        Validate.isTrue(!this.phasesActions.containsKey(blockPhase.getPhase()), "Essa etapa já existe.");

        this.phasesActions.put(blockPhase.getPhase(), blockPhase);
        OneBlock.getInstance().getLogger().info("Registrando etapa " + blockPhase.getPhase() + " (" + blockPhase.getBlocksNeeded() + ")");
    }

    public void shuffle() {
        this.phasesActions.forEach((integer, breakActions) -> breakActions.shuffle());
    }

    public void unRegisterAll() {
        this.phasesActions.clear();
    }

    public BlockPhase getPhase(int phase) {
        return this.phasesActions.get(phase);
    }

    @NonNull
    public BreakAction getRandomBreakAction(int phase) {
        BlockPhase blockPhase = this.phasesActions.get(phase);

        if (blockPhase == null)
            throw new RuntimeException("Essa etapa não existe.");

        List<BreakAction> breakActions = blockPhase.getBreakActions();

        if (breakActions.isEmpty())
            throw new RuntimeException("Não tem valores na lista.");

        double size = 0;

        for (BreakAction breakAction : breakActions) {
            size += breakAction.getChance();
        }

        double v = ThreadLocalRandom.current().nextDouble(0, size);

        int value = 0;
        for (BreakAction breakAction : breakActions) {
            value += breakAction.getChance();
            if (value >= v) {
                return breakAction;
            }
        }

        throw new RuntimeException("Não tem valores na lista.");
    }

    public void randomAction(@NonNull BreakContext breakContext) {
        BreakAction randomBreakAction = getRandomBreakAction(breakContext.getUser().getPhase());

        BreakOneBlockEvent event = new BreakOneBlockEvent(breakContext, randomBreakAction);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        randomBreakAction.onExecute(breakContext);
    }

}
