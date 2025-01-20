package com.synthestra.xeno_artifacts.block.entity;

import com.synthestra.xeno_artifacts.api.block.IBlockHolder;
import com.synthestra.xeno_artifacts.api.block.IExtraModelDataProvider;
import com.synthestra.xeno_artifacts.block.XenoArtifactBlock;
import com.synthestra.xeno_artifacts.block.properties.SynBlockStateProperties;
import com.synthestra.xeno_artifacts.client.model.ExtraModelData;
import com.synthestra.xeno_artifacts.client.model.ModelDataKey;
import com.synthestra.xeno_artifacts.item.component.NodeScannerPrintOutData;
import com.synthestra.xeno_artifacts.item.component.XenoArtifactData;
import com.synthestra.xeno_artifacts.item.component.XenoArtifactDataGenerator;
import com.synthestra.xeno_artifacts.registry.ModBlockEntityTypes;
import com.synthestra.xeno_artifacts.registry.ModDataComponents;
import com.synthestra.xeno_artifacts.registry.ModItems;
import com.synthestra.xeno_artifacts.registry.ModSoundEvents;
import com.synthestra.xeno_artifacts.util.block.NbtUtil;
import com.synthestra.xeno_artifacts.xeno_artifact.XenoArtifactNode;
import com.synthestra.xeno_artifacts.xeno_artifact.XenoArtifactReaction;
import com.synthestra.xeno_artifacts.xeno_artifact.XenoArtifactTrigger;
import com.synthestra.xeno_artifacts.xeno_artifact.triggers.ListenerTrigger;
import com.synthestra.xeno_artifacts.xeno_artifact.triggers.Trigger;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class XenoArtifactBlockEntity extends BlockEntity implements GameEventListener.Provider<XenoArtifactBlockEntity.XenoListener>, IBlockHolder, IExtraModelDataProvider {

    public final RandomSource random = RandomSource.create();

    protected List<XenoArtifactNode> nodeTree = new ArrayList<>();
    protected int currentNodeId;
    protected Direction directionBias = Direction.DOWN;
    protected int activatedCooldown;
    protected UUID uuid = Mth.createInsecureUUID(this.random);

    protected Integer nodesMin;
    protected Integer nodesMax;
    protected Integer nodeMaxEdges;

    public static final ModelDataKey<BlockState> MIMIC = SynBlockStateProperties.MIMIC;
    private BlockState mimic = Blocks.AIR.defaultBlockState();

    public XenoArtifactNode currentNode = new XenoArtifactNode();


    private final XenoArtifactBlockEntity.XenoListener xenoListener;
    HashSet<Integer> usedNodeIds = new HashSet<>();


    public XenoArtifactBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.XENO_ARTIFACT.get(), pos, blockState);
        this.xenoListener = new XenoArtifactBlockEntity.XenoListener(blockState, new BlockPositionSource(pos));
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

//    public int getCurrentNodeId() {
//        return currentNodeId;
//    }


    public void setNodeTree(List<XenoArtifactNode> nodeTree) {
        this.nodeTree = nodeTree;
    }

    public XenoArtifactNode getCurrentNode() {
        return this.currentNode;
    }

    public Direction getDirectionBias() {
        return directionBias;
    }

    public int getDepth() {
        return this.getCurrentNode().getDepth();
    }

    public int getCooldown() {
        return this.activatedCooldown;
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
            if (xenoArtifactBE.isSuppressed()) return 0;
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
            if (xenoArtifactBE.isSuppressed()) return false;

            XenoArtifactNode node = xenoArtifactBE.getCurrentNode();
            Trigger trigger = node.getTriggerValue();
            if (!(trigger instanceof ListenerTrigger listenerTrigger)) return false;
            if (listenerTrigger.testListener(level, gameEvent, context, pos)) {
                xenoArtifactBE.activateNode(level, xenoArtifactBE.worldPosition);
                return true;
            }
            return false;
        }
    }
    public boolean isSuppressed() {
        return this.isActivated() || !this.isGenerated() || !this.getHeldBlock().isAir();
    }

    public boolean isGenerated() {
        // for making sure gameeventlistener doesnt trigger on block place and the blockentity is finished generating
        return nodesMin == null;
    }

    public void activateNode(Level level, BlockPos pos) {
        XenoArtifactNode node = this.getCurrentNode();
        if (node.getReactionValue().effect(level, pos)) {
            node.setActivated(true);
            level.setBlock(pos, this.getBlockState().setValue(XenoArtifactBlock.ACTIVATED, true), 3);
            this.activatedCooldown = 100;
            level.playSound(null, pos, ModSoundEvents.ARTIFACT_ACTIVATE.get(), SoundSource.BLOCKS);
            this.getNewNode();
        };

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

    public int getCurrentNodeId() {
        return this.currentNodeId;
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

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public List<XenoArtifactNode> getEdgesOf(XenoArtifactNode node) {
        List<XenoArtifactNode> edges = new ArrayList<>();
        node.getEdges().forEach(e -> edges.add(this.getNodeFromId(e)));
        return edges;
    }

    public void setCurrentNodeId(int id) {
        this.currentNodeId = id;
    }

    public void setCurrentNode(int id) {
        this.setCurrentNode(this.getNodeFromId(id));
    }

    public void setCurrentNode(XenoArtifactNode node) {
        this.currentNode = node;
    }


    public void generate(int nodesMin, int nodesMax, int nodeMaxEdges) {
        int nodesToCreate = this.random.nextIntBetweenInclusive(nodesMin, nodesMax);

        Queue<XenoArtifactNode> uninitializedNodes = new LinkedList<>();
        uninitializedNodes.add(new XenoArtifactNode(this.getValidNodeId()));
        int createdNodes = 1;

        while(!uninitializedNodes.isEmpty()) {
            XenoArtifactNode node = uninitializedNodes.remove();

            node.setTrigger(XenoArtifactTrigger.pick(random));
            node.setReaction(XenoArtifactReaction.pick(random));

            int maxChildren = this.random.nextIntBetweenInclusive(1, Math.max(1, nodeMaxEdges - 1));
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
        this.currentNode = this.getRootNode();
        this.currentNodeId = this.currentNode.getId();
        this.usedNodeIds.clear();

        this.nodesMin = null;
        this.nodesMax = null;
        this.nodeMaxEdges = null;
    }

    private int getValidNodeId() {
        int id = this.random.nextIntBetweenInclusive(100, 999);
        while (this.usedNodeIds.contains(id)) id = this.random.nextIntBetweenInclusive(100, 999);
        this.usedNodeIds.add(id);
        return id;
    }

    public void finishScan(Player player) {
        ItemStack print_out = new ItemStack(ModItems.NODE_SCANNER_PRINT_OUT.get());
        print_out.set(ModDataComponents.NODE_SCANNER_DATA.get(), new NodeScannerPrintOutData(this.uuid, this.currentNode));
        player.addItem(print_out);
    }

    public final String NBT_NAME_TREE = "tree";
    public final String NBT_NAME_COOLDOWN = "cooldown";
    public final String NBT_NAME_DIRECTION = "dir";
    public final String NBT_NAME_ID = "id";
    public final String NBT_NAME_DEPTH = "d";
    public final String NBT_NAME_EDGES = "e";
    public final String NBT_NAME_TRIGGER = "t";
    public final String NBT_NAME_REACTION = "r";
    public final String NBT_NAME_ACTIVATED = "a";
    public final String NBT_NAME_CURRENT_ID = "current_id";

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.uuid = tag.getUUID("uuid");
        this.directionBias = Direction.byName(tag.getString(NBT_NAME_DIRECTION));
        this.activatedCooldown = tag.getInt(NBT_NAME_COOLDOWN);

        ListTag nodeTreeNBT = tag.getList(NBT_NAME_TREE, Tag.TAG_COMPOUND);
        this.nodeTree = new ArrayList<>();
        for (int i = 0; i < nodeTreeNBT.size(); i++) {
            CompoundTag nodeNBT = nodeTreeNBT.getCompound(i);
            XenoArtifactNode node = new XenoArtifactNode();

            node.setId(nodeNBT.getInt(NBT_NAME_ID));
            node.setDepth(nodeNBT.getInt(NBT_NAME_DEPTH));
            node.setEdges(IntList.of(nodeNBT.getIntArray(NBT_NAME_EDGES)));
            node.setTrigger(XenoArtifactTrigger.byName(nodeNBT.getString(NBT_NAME_TRIGGER)));
            node.setReaction(XenoArtifactReaction.byName(nodeNBT.getString(NBT_NAME_REACTION)));
            node.setActivated(nodeNBT.getBoolean(NBT_NAME_ACTIVATED));

            this.addNode(node);
        }
        this.currentNodeId = tag.getInt("current_id");
        this.currentNode = this.getNodeFromId(this.currentNodeId);
        if (tag.contains("mimic")) setHeldBlock(NbtUtil.readBlockState(tag.getCompound("mimic"), level), 0);

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (this.nodesMin != null && this.nodesMax != null && this.nodeMaxEdges != null) {
            generate(this.nodesMin, this.nodesMax, this.nodeMaxEdges);
        }
        tag.putUUID("uuid", this.uuid);
        tag.putInt(NBT_NAME_CURRENT_ID, this.currentNodeId);
        tag.putString(NBT_NAME_DIRECTION, this.directionBias.getName());
        tag.putInt(NBT_NAME_COOLDOWN, this.activatedCooldown);

        ListTag nodeTreeNBT = new ListTag();
        for (XenoArtifactNode node : this.getNodeTree()) {
            CompoundTag nodeNBT = new CompoundTag();

            nodeNBT.putInt(NBT_NAME_ID, node.getId());
            nodeNBT.putInt(NBT_NAME_DEPTH, node.getDepth());
            nodeNBT.putIntArray(NBT_NAME_EDGES, node.getEdges().toIntArray());
            nodeNBT.putString(NBT_NAME_TRIGGER, node.getTrigger().getSerializedName());
            nodeNBT.putString(NBT_NAME_REACTION, node.getReaction().getSerializedName());
            nodeNBT.putBoolean(NBT_NAME_ACTIVATED, node.isActivated());

            nodeTreeNBT.add(nodeNBT);
        }
        tag.put(NBT_NAME_TREE, nodeTreeNBT);
        if (this.mimic != null) tag.put("mimic", NbtUtils.writeBlockState(mimic));
    }

    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        XenoArtifactDataGenerator generator = componentInput.get(ModDataComponents.XENO_ARTIFACT_GENERATOR.get());
        if (generator != null) {
            this.nodesMin = generator.nodesMin();
            this.nodesMax = generator.nodesMax();
            this.nodeMaxEdges = generator.nodeMaxEdges();
        }
        XenoArtifactData xenoArtifactData = componentInput.get(ModDataComponents.XENO_ARTIFACT.get());
        if (xenoArtifactData != null) {
            this.currentNodeId = xenoArtifactData.currentId();
            this.nodeTree = xenoArtifactData.tree();
            this.directionBias = xenoArtifactData.directionBias() ? Direction.DOWN : Direction.UP;
            this.currentNode = this.getNodeFromId(this.currentNodeId);
        }
    }

    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
//        if (this.nodesMin != null && this.nodesMax != null && this.nodeMaxEdges != null) {
//            components.set(ModDataComponents.XENO_ARTIFACT_GENERATOR.get(), new XenoArtifactDataGenerator(this.nodesMin, this.nodesMax, this.nodeMaxEdges));
//        }

        components.set(ModDataComponents.XENO_ARTIFACT.get(), new XenoArtifactData(this.uuid, this.currentNodeId, this.nodeTree, this.getDirectionBias().toString().equals("down")));
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("current_id");
        tag.remove("cooldown");
        tag.remove("tree");
        tag.remove("dir");
        tag.remove("mimic");
        tag.remove("uuid");
//        tag.remove("nodes_min");
//        tag.remove("nodes_max");
//        tag.remove("node_max_edges");
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        builder.with(MIMIC, mimic);
    }

    @Override
    public BlockState getHeldBlock(int index) {
        return mimic;
    }

    @Override
    public boolean setHeldBlock(BlockState state, int index) {
        this.mimic = state;
        if (this.level instanceof ServerLevel) {
            this.setChanged();
        }
        if (level != null && this.level.isClientSide) {
            this.requestModelReload();
        }

        return true;
    }

    @Override
    public boolean setHeldBlock(BlockState state) {
        return IBlockHolder.super.setHeldBlock(state);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    public void printTree(XenoArtifactNode node, String indent) {
        System.out.println(indent + "└" + node);
        List<XenoArtifactNode> edges = getEdgesOf(node);
        System.out.println(indent + "└ children: " + edges.size());
        List<XenoArtifactNode> children = edges.stream().filter(e -> e.getDepth() > node.getDepth()).toList();
        for (XenoArtifactNode child : children) {
            printTree(child, indent + " ");
        }

//        for (XenoArtifactNode child : nodeTree) {
//            System.out.println(child);
//        }

    }
}
