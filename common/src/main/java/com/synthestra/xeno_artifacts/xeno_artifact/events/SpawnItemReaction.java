package com.synthestra.xeno_artifacts.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpawnItemReaction extends Reaction {
    ItemStack item;
    public SpawnItemReaction(ItemStack item) {
        super(10);
        this.item = item;
    }

    public boolean effect(Level level, BlockPos pos) {
        if (level.isClientSide) return true;
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, this.item);
        level.addFreshEntity(itemEntity);
        return true;
    }
}
