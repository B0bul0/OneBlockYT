package me.bobulo.oneblock.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class User {

    private final Player player;
    private int phase;
    private int brokenBlocks;

    public User(Player player) {
        this.player = player;
    }

    public void upgradePhase() {
        this.phase++;
    }

    public int addBrokenBlocks() {
        return ++brokenBlocks;
    }

}
