package me.bobulo.oneblock.breakblock.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class BreakContext {

    private Player player;
    private Block block;

}
