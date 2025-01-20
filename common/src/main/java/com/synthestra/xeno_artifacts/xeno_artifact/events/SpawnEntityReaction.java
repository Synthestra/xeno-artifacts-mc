package com.synthestra.xeno_artifacts.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SpawnEntityReaction extends Reaction {
    public SpawnEntityReaction() {
        super(5);
    }

    public boolean effect(Level level, BlockPos pos) {
        ItemStack stack = new ItemStack(Items.DIAMOND);
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
        level.addFreshEntity(itemEntity);
        return true;
    }
}
