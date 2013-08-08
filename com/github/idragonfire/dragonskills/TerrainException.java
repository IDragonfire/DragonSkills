package com.github.idragonfire.dragonskills;

import org.bukkit.block.Block;

public class TerrainException extends Exception {
    private static final long serialVersionUID = 1L;

    private Block invalidBlock;

    public TerrainException(Block invalidBlock) {
        super(new StringBuilder("terrain exception ").append(
                invalidBlock.getType()).toString());
    }

    public Block getInvalidBlock() {
        return invalidBlock;
    }
}
