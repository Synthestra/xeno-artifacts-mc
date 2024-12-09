package com.synthestra.synth_testing.block.entity;

import com.synthestra.synth_testing.block.XenoArtifactBlock;
import com.synthestra.synth_testing.registry.SynBlockEntityTypes;
import com.synthestra.synth_testing.registry.SynItems;
import com.synthestra.synth_testing.registry.SynSoundEvents;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactNode;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactReaction;
import com.synthestra.synth_testing.xeno_artifact.XenoArtifactTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.ListenerTrigger;
import com.synthestra.synth_testing.xeno_artifact.triggers.Trigger;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class XenoArtifactBlockEntity extends BlockEntity implements GameEventListener.Provider<XenoArtifactBlockEntity.XenoListener> {
    protected List<XenoArtifactNode> nodeTree = new ArrayList<>();
    //protected int currentNodeId;
    protected Direction directionBias = Direction.DOWN;
    int activatedCooldown;

    public XenoArtifactNode currentNode = new XenoArtifactNode();

    public final RandomSource random = RandomSource.create();
    private final XenoArtifactBlockEntity.XenoListener xenoListener;
    public final int NODE_MIN = 3;
    public final int NODE_MAX = 8;
    public final int NODE_MAX_EDGES = 4;
    HashSet<Integer> usedNodeIds = new HashSet<>();


    public XenoArtifactBlockEntity(BlockPos pos, BlockState blockState) {
        super(SynBlockEntityTypes.XENO_ARTIFACT.get(), pos, blockState);
        this.xenoListener = new XenoArtifactBlockEntity.XenoListener(blockState, new BlockPositionSource(pos));
//        if (!nodeTree.isEmpty()) {
//            this.currentNode = this.getRootNode();
//        }
    }

    public void generateTree() {
        this.generate();
        this.currentNode = this.getRootNode();
        this.setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, XenoArtifactBlockEntity xenoArtifactBE) {
        boolean setChanged = false;
        boolean wasActivated = xenoArtifactBE.isActivated();
        if (xenoArtifactBE.isActivated()) --xenoArtifactBE.activatedCooldown;
        if (wasActivated != xenoArtifactBE.isActivated()) {
            setChanged = true;
            state = state.setValue(XenoArtifactBlock.ACTIVATED, xenoArtifactBE.isActivated());
            level.setBlock(pos, state, 3);
        }
        if (setChanged) xenoArtifactBE.setChanged();
    }

//    public void setCurrentNodeId(int currentNodeId) {
//        this.currentNodeId = currentNodeId;
//    }
//
//    public int getCurrentNodeId() {
//        return currentNodeId;
//    }

    public XenoArtifactNode getCurrentNode() {
        return this.currentNode;
    }

    public Direction getDirectionBias() {
        return directionBias;
    }

    public int getDepth() {
        return this.getCurrentNode().getDepth();
    }

    public void setDirectionBias(Direction direction) {
        this.directionBias = direction;
        this.setChanged();
    }

    private boolean isActivated() {
        return this.activatedCooldown > 0;
    }

    public XenoListener getListener() {
        return this.xenoListener;
    }

    public class XenoListener implements GameEventListener {
        private final BlockState blockState;
        private final PositionSource positionSource;

        public XenoListener(BlockState blockState, PositionSource positionSource) {
            this.blockState = blockState;
            this.positionSource = positionSource;
        }

        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        public int getListenerRadius() {
            XenoArtifactBlockEntity xenoArtifactBE = XenoArtifactBlockEntity.this;
            XenoArtifactNode node = xenoArtifactBE.getCurrentNode();
            Trigger trigger = node.getTriggerValue();
            if (!(trigger instanceof ListenerTrigger listenerTrigger)) return 0;
            return listenerTrigger.listenRadius();
        }

        public GameEventListener.DeliveryMode getDeliveryMode() {
            return DeliveryMode.BY_DISTANCE;
        }

        public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {

            //Optional<Vec3> vec3 = this.positionSource.getPosition(level);
            //if (vec3.isEmpty()) return false;
            //BlockPos xenoPos = BlockPos.containing(vec3.get());

            XenoArtifactBlockEntity xenoArtifactBE = XenoArtifactBlockEntity.this;
            if (!xenoArtifactBE.canActivate()) return false;

            XenoArtifactNode node = xenoArtifactBE.getCurrentNode();
            Trigger trigger = node.getTriggerValue();
            if (!(trigger instanceof ListenerTrigger listenerTrigger)) return false;
            if (listenerTrigger.testListener(level, gameEvent, context, pos)) {
                xenoArtifactBE.activateNode(level, xenoArtifactBE.worldPosition, node);
                return true;
            }
            return false;
        }
    }

    public boolean canActivate() {
        return !this.isActivated();
    }

    public void activateNode(Level level, BlockPos pos, XenoArtifactNode node) {
        node.getReactionValue().effect(level, pos);

        level.setBlock(pos, this.getBlockState().setValue(XenoArtifactBlock.ACTIVATED, true), 3);
        this.activatedCooldown = 100;
        level.playSound(null, pos, SynSoundEvents.ARTIFACT_ACTIVATE.get(), SoundSource.BLOCKS);
        this.getNewNode();
    }

    public void getNewNode() {

        List<XenoArtifactNode> edges = getEdgesOf(currentNode);
        switch (this.directionBias) {
            case UP -> {
                List<XenoArtifactNode> upNodes = edges.stream().filter(e -> e.getDepth() < currentNode.getDepth()).toList();
                if (!upNodes.isEmpty()) edges = upNodes;
            }
            case DOWN -> {
                List<XenoArtifactNode> downNodes = edges.stream().filter(e -> e.getDepth() > currentNode.getDepth()).toList();
                if (!downNodes.isEmpty()) edges = downNodes;
            }
        }
        XenoArtifactNode newNode = edges.get(random.nextInt(edges.size()));
        this.setCurrentNode(newNode.getId());
        this.setChanged();
    }


    public void addNode(XenoArtifactNode node) {
        this.nodeTree.add(node);
    }

    public List<XenoArtifactNode> getNodeTree() {
        return nodeTree;
    }

    public XenoArtifactNode getRootNode() {
        return this.nodeTree.stream().filter(e -> e.getDepth() == 0).findFirst().orElseThrow();
    }

    public XenoArtifactNode getNodeFromId(int id) {
        return this.nodeTree.stream().filter(e -> e.getId() == id).findFirst().orElseThrow();
    }

    public List<XenoArtifactNode> getEdgesOf(XenoArtifactNode node) {
        List<XenoArtifactNode> edges = new ArrayList<>();
        node.getEdges().forEach(e -> edges.add(this.getNodeFromId(e)));
        return edges;
    }

    public void setCurrentNode(int id) {
        this.setCurrentNode(this.getNodeFromId(id));
    }

    public void setCurrentNode(XenoArtifactNode node) {
        this.currentNode = node;
    }


    public void generate() {
        int nodesToCreate = this.random.nextIntBetweenInclusive(NODE_MIN, NODE_MAX);

        Queue<XenoArtifactNode> uninitializedNodes = new LinkedList<>();
        uninitializedNodes.add(new XenoArtifactNode(this.getValidNodeId()));
        int createdNodes = 1;

        while(!uninitializedNodes.isEmpty()) {
            XenoArtifactNode node = uninitializedNodes.remove();

            node.setTrigger(XenoArtifactTrigger.pick(random));
            node.setReaction(XenoArtifactReaction.pick(random));

            int maxChildren = this.random.nextIntBetweenInclusive(1, NODE_MAX_EDGES - 1);
            for (int i = 0; i < maxChildren; i++) {
                if (nodesToCreate <= createdNodes) break;

                XenoArtifactNode child = new XenoArtifactNode(this.getValidNodeId(), node.getDepth() + 1);
                node.addEdges(child.getId());
                child.addEdges(node.getId());

                uninitializedNodes.add(child);
                createdNodes++;
            }
            this.nodeTree.add(node);
        }
        this.usedNodeIds.clear();
    }

    private int getValidNodeId() {
        int id = this.random.nextIntBetweenInclusive(100, 999);
        while (this.usedNodeIds.contains(id)) id = this.random.nextIntBetweenInclusive(100, 999);
        this.usedNodeIds.add(id);
        return id;
    }

    public void finishScan(Player player) {
        XenoArtifactNode node = this.getCurrentNode();
        int nodeId = node.getId();
        String biasDirection = this.getDirectionBias() == Direction.DOWN ? "↓" : "↑";
        int edges = this.getEdgesOf(node).size();
        int depth = this.getDepth();
        String trigger = "trigger.xeno_artifacts." + node.getTrigger().toString();
        String reaction = "reaction.xeno_artifacts." + node.getReaction().toString();

        Style noItalic = Style.EMPTY.withColor(ChatFormatting.WHITE).withItalic(false);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.translatable("note.xeno_artifacts.node_id", nodeId).setStyle(noItalic));
        lore.add(Component.translatable("note.xeno_artifacts.bias_direction", biasDirection).setStyle(noItalic));
        lore.add(Component.translatable("note.xeno_artifacts.depth", depth).setStyle(noItalic));
        lore.add(Component.translatable("note.xeno_artifacts.edges", edges).setStyle(noItalic));
        lore.add(Component.translatable("note.xeno_artifacts.trigger", Component.translatable(trigger)).setStyle(noItalic));
        lore.add(Component.translatable("note.xeno_artifacts.reaction", Component.translatable(reaction)).setStyle(noItalic));

        ItemStack scanner = new ItemStack(SynItems.NODE_SCANNER.get());
        scanner.set(DataComponents.LORE, new ItemLore(lore));
        player.setItemInHand(player.getUsedItemHand(), scanner);
    }

    public final String NBT_NAME_TREE = "tree";
    public final String NBT_NAME_COOLDOWN = "cooldown";
    public final String NBT_NAME_DIRECTION = "dir";
    public final String NBT_NAME_ID = "id";
    public final String NBT_NAME_DEPTH = "d";
    public final String NBT_NAME_EDGES = "e";
    public final String NBT_NAME_TRIGGER = "t";
    public final String NBT_NAME_REACTION = "r";
    public final String NBT_NAME_CURRENT_ID = "current_id";

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.directionBias = Direction.byName(tag.getString(NBT_NAME_DIRECTION));
        this.activatedCooldown = tag.getInt(NBT_NAME_COOLDOWN);

        ListTag nodeTreeNBT = tag.getList(NBT_NAME_TREE, Tag.TAG_COMPOUND);
        this.nodeTree = new ArrayList<>();
        for (int i = 0; i < nodeTreeNBT.size(); i++) {
            CompoundTag nodeNBT = nodeTreeNBT.getCompound(i);
            XenoArtifactNode node = new XenoArtifactNode();

            node.setId(nodeNBT.getInt(NBT_NAME_ID));
            node.setDepth(nodeNBT.getInt(NBT_NAME_DEPTH));
            node.setEdges(node.getEdgesFromIntArray(nodeNBT.getIntArray(NBT_NAME_EDGES)));
            node.setTrigger(XenoArtifactTrigger.byName(nodeNBT.getString(NBT_NAME_TRIGGER)));
            node.setReaction(XenoArtifactReaction.byName(nodeNBT.getString(NBT_NAME_REACTION)));

            this.addNode(node);
        }
        this.currentNode = this.getNodeFromId(tag.getInt(NBT_NAME_CURRENT_ID));

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(NBT_NAME_CURRENT_ID, this.currentNode.getId());
        tag.putString(NBT_NAME_DIRECTION, this.directionBias.getName());
        tag.putInt(NBT_NAME_COOLDOWN, this.activatedCooldown);

        ListTag nodeTreeNBT = new ListTag();
        for (XenoArtifactNode node : this.getNodeTree()) {
            CompoundTag nodeNBT = new CompoundTag();

            nodeNBT.putInt(NBT_NAME_ID, node.getId());
            nodeNBT.putInt(NBT_NAME_DEPTH, node.getDepth());
            nodeNBT.putIntArray(NBT_NAME_EDGES, node.getEdgesIntArray());
            nodeNBT.putString(NBT_NAME_TRIGGER, node.getTrigger().getSerializedName());
            nodeNBT.putString(NBT_NAME_REACTION, node.getReaction().getSerializedName());

            nodeTreeNBT.add(nodeNBT);
        }
        tag.put(NBT_NAME_TREE, nodeTreeNBT);
    }
}
