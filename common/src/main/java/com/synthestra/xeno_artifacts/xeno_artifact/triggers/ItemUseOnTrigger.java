package com.synthestra.xeno_artifacts.xeno_artifact.triggers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ItemUseOnTrigger {
    boolean testItem(ItemStack stack, Player player);
}
