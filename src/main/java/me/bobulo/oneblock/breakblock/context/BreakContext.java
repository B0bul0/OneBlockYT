package me.bobulo.oneblock.breakblock.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.bobulo.oneblock.user.User;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class BreakContext {

    private User user;
    private Block block;

    public Player getPlayer() {
        return user.getPlayer();
    }

}
