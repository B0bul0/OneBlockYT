package me.bobulo.oneblock.phase;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.bobulo.oneblock.breakblock.action.BreakAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BlockPhase {

    private final int phase;
    private List<BreakAction> breakActions;

    private int blocksNeeded;

    public BlockPhase(int phase, int blocksNeeded) {
        this.phase = phase;
        this.blocksNeeded = blocksNeeded;
    }

    @NonNull
    public List<BreakAction> getBreakActions() {
        if (breakActions == null)
            this.breakActions = new ArrayList<>();
        return breakActions;
    }

    public void addBreakAction(BreakAction breakAction) {
        getBreakActions().add(breakAction);
    }

    public void shuffle() {
        if (this.breakActions == null)
            return;

        Collections.shuffle(getBreakActions());
    }

}
