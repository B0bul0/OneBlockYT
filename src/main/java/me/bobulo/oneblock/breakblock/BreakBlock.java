package me.bobulo.oneblock.breakblock;

import com.google.common.collect.Lists;
import lombok.NonNull;
import me.bobulo.oneblock.breakblock.action.BreakAction;
import me.bobulo.oneblock.breakblock.context.BreakContext;
import me.bobulo.oneblock.event.BreakOneBlockEvent;
import me.bobulo.oneblock.event.RegisterOneBlockEvent;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BreakBlock {


    private final List<BreakAction> breakActions = new CopyOnWriteArrayList<>();

    public void registerAction(@NonNull BreakAction breakAction) {
        RegisterOneBlockEvent event = new RegisterOneBlockEvent(breakAction);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        breakActions.add(breakAction);
    }

    public void shuffle() {
        Collections.shuffle(breakActions);
    }

    public void unRegisterAll() {
        this.breakActions.clear();
    }

    @NonNull
    public BreakAction getRandomBreakAction() {
        if (breakActions.isEmpty())
            throw new RuntimeException("Não tem valores na lista.");

        double size = 0;

        for (BreakAction breakAction : breakActions) {
            size += breakAction.getChance();
        }

        double v = ThreadLocalRandom.current().nextDouble(0, size);

        System.out.println(v);

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
        BreakAction randomBreakAction = getRandomBreakAction();

        BreakOneBlockEvent event = new BreakOneBlockEvent(breakContext, randomBreakAction);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        randomBreakAction.onExecute(breakContext);
    }

}
