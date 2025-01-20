package com.synthestra.xeno_artifacts.xeno_artifact.triggers;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class PotionTrigger extends Trigger implements ListenerTrigger, ItemUseOnTrigger {
    MobEffectCategory category;
    public PotionTrigger(MobEffectCategory category) {
        super();
        this.category = category;
    }

    public String customMessage() {
        return "Electricity is needed";
    }

    public boolean testListener(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        if (gameEvent.is(GameEvent.PROJECTILE_LAND)) {
            if (context.sourceEntity() instanceof ThrownPotion potion) {
                ItemStack stack = potion.getItem();
                if (isCorrectType(stack)) {
                    stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                    return true;
                };
            }
        }
        return false;
    }

    @Override
    public boolean testItem(ItemStack stack, Player player) {
        if (isCorrectType(stack)) {
            stack.consume(1, player);

            if (player != null) {
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            return true;
        }
        return false;
    }

    public boolean isCorrectType(ItemStack stack) {
        PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        Iterable<MobEffectInstance> effects = potionContents.getAllEffects();
        for (MobEffectInstance effectInstance : effects) if (effectInstance.getEffect().value().getCategory() == this.category) return true;
        return false;
    }

    @Override
    public int listenRadius() {
        return 4;
    }
}
