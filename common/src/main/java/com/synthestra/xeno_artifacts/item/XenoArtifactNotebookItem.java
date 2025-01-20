package com.synthestra.xeno_artifacts.item;

import com.synthestra.xeno_artifacts.client.gui.screen.XenoArtifactNotebookScreen;
import com.synthestra.xeno_artifacts.item.component.XenoArtifactScrapbookData;
import com.synthestra.xeno_artifacts.registry.ModDataComponents;
import com.synthestra.xeno_artifacts.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class XenoArtifactNotebookItem extends Item {
    public XenoArtifactNotebookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        XenoArtifactScrapbookData data = player.getItemInHand(usedHand).get(ModDataComponents.XENO_ARTIFACT_SCRAPBOOK_DATA.get());
        if (data != null) {
            //player.openMenu()
            if (level.isClientSide) Minecraft.getInstance().setScreen(new XenoArtifactNotebookScreen(data));
        }

        return super.use(level, player, usedHand);
    }

//    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
//        if (action != ClickAction.SECONDARY) return false;
//
//        XenoArtifactScrapbookData bundleContents = stack.get(ModDataComponents.XENO_ARTIFACT_SCRAPBOOK_DATA.get());
//        if (bundleContents == null) return false;
//
//        ItemStack itemStack = slot.getItem();
//        XenoArtifactScrapbookData.Mutable mutable = new XenoArtifactScrapbookData.Mutable(bundleContents);
//        if (itemStack.isEmpty()) {
//            ItemStack takeStack = mutable.removeOne();
//            if (takeStack != null) {
//                ItemStack itemStack3 = slot.safeInsert(takeStack);
//                mutable.tryInsert(itemStack3);
//            }
//        }
//
//        stack.set(ModDataComponents.XENO_ARTIFACT_SCRAPBOOK_DATA.get(), mutable.toImmutable());
//        return true;
//
//    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        XenoArtifactScrapbookData data = stack.get(ModDataComponents.XENO_ARTIFACT_SCRAPBOOK_DATA.get());
        if (data == null) return false;

        XenoArtifactScrapbookData.Mutable mutable = new XenoArtifactScrapbookData.Mutable(data);
        if (other.isEmpty()) {
            ItemStack itemStack = mutable.removeOne();
            if (itemStack != null) {
                access.set(itemStack);
            }
        } else if (other.is(ModItems.NODE_SCANNER_PRINT_OUT.get())) {
            mutable.tryInsert(other);
        }

        stack.set(ModDataComponents.XENO_ARTIFACT_SCRAPBOOK_DATA.get(), mutable.toImmutable());
        return true;


    }
}
