package com.synthestra.xeno_artifacts.item;

import com.synthestra.xeno_artifacts.item.component.NodeScannerPrintOutData;
import com.synthestra.xeno_artifacts.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class NodeScannerPrintOutItem extends Item {
    public NodeScannerPrintOutItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        NodeScannerPrintOutData data = stack.getComponents().get(ModDataComponents.NODE_SCANNER_DATA.get());
        if (data == null || data.node() == null) return;

        String trigger = "trigger.xeno_artifacts." + data.node().getTrigger().toString();
        String reaction = "reaction.xeno_artifacts." + data.node().getReaction().toString();

        Style noItalic = Style.EMPTY.withColor(ChatFormatting.WHITE).withItalic(false);
        tooltipComponents.add(Component.translatable("note.xeno_artifacts.node_id", data.node().getId()).setStyle(noItalic));
        tooltipComponents.add(Component.translatable("note.xeno_artifacts.depth", data.node().getDepth()).setStyle(noItalic));
        tooltipComponents.add(Component.translatable("note.xeno_artifacts.edges", data.node().getEdges()).setStyle(noItalic));
        tooltipComponents.add(Component.translatable("note.xeno_artifacts.trigger", Component.translatable(trigger)).setStyle(noItalic));
        tooltipComponents.add(Component.translatable("note.xeno_artifacts.reaction", Component.translatable(reaction)).setStyle(noItalic));
    }
}
