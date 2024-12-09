package com.synthestra.synth_testing.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class SpawnLootReaction extends Reaction {
    ResourceKey<LootTable> table;
    public SpawnLootReaction(ResourceKey<LootTable> table) {
        super(10);
        this.table = table;
    }

    public void effect(Level level, BlockPos pos) {
        if (level.isClientSide) return;

        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(this.table);
        LootParams.Builder builder = new LootParams.Builder((ServerLevel)level);
        LootParams lootParams = builder.create(LootContextParamSets.EMPTY);
        lootTable.getRandomItems(lootParams, 0, stack -> {
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            level.addFreshEntity(itemEntity);
        });
    }
}