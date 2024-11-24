package com.synthestra.synth_testing.xeno_artifact.triggers;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class PositivePotionTrigger extends Trigger implements ListenerTrigger {

    public String customMessage() {
        return "Electricity is needed";
    }

    public boolean testListener(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        if (gameEvent.is(GameEvent.PROJECTILE_LAND)) {
            if (context.sourceEntity() instanceof ThrownPotion potion) {
                ItemStack itemStack = potion.getItem();
                PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                Iterable<MobEffectInstance> effects = potionContents.getAllEffects();
                for (MobEffectInstance effectInstance : effects) if (effectInstance.getEffect().value().isBeneficial()) return true;
            }
        }
        return false;
    }
}
