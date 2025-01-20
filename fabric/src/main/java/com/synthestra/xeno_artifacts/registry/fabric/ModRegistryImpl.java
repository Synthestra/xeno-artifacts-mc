package com.synthestra.xeno_artifacts.registry.fabric;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.XenoArtifactsClient;
import com.synthestra.xeno_artifacts.client.fabric.IFabricMenuType;
//import com.synthestra.xeno_artifacts.client.model.fabric.MLFabricModelLoaderRegistry;
import com.synthestra.xeno_artifacts.fabric.XenoArtifactsFabric;
import com.synthestra.xeno_artifacts.fabric.XenoArtifactsFabricClient;
import com.synthestra.xeno_artifacts.registry.ModRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModRegistryImpl {

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        var registry = Registry.register(BuiltInRegistries.BLOCK, XenoArtifacts.res(name), block.get());
        return () -> registry;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item, String tab_id) {
        var registry = Registry.register(BuiltInRegistries.ITEM, XenoArtifacts.res(name), item.get());
        itemList.add(registry.getDefaultInstance());
        return () -> registry;
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        var registry = Registry.register(BuiltInRegistries.SOUND_EVENT, XenoArtifacts.res(name), soundEvent.get());
        return () -> registry;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        var registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, XenoArtifacts.res(name), EntityType.Builder.of(factory, category).sized(width, height).build(null));
        return () -> registry;
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> registerBlockEntityType(String name, Supplier<T> blockEntity) {
        var registry = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, XenoArtifacts.res(name), blockEntity.get());
        return () -> registry;
    }

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(ModRegistry.BlockEntitySupplier<T> blockEntity, Block... validBlocks) {
        return FabricBlockEntityTypeBuilder.create(blockEntity::create, validBlocks).build();
    }

    public static <T> Supplier<DataComponentType<T>> registerDataComponent(String name, Supplier<DataComponentType<T>> component) {
        var registry = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, XenoArtifacts.res(name), component.get());
        return () -> registry;
    }

    public static <C extends AbstractContainerMenu> Supplier<MenuType<C>> registerMenuType(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, C> containerFactory) {
        var registry = Registry.register(BuiltInRegistries.MENU, XenoArtifacts.res(name), IFabricMenuType.create(containerFactory::apply));
        return () -> registry;
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider) {
        EntityRendererRegistry.register(type.get(), renderProvider);
    }

    public static <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<T> renderProvider) {
        BlockEntityRenderers.register(type.get(), renderProvider);
    }

    public static <T extends Block> void setFlammable(Block fireBlock, Supplier<T> block, int encouragement, int flammability) {
        FlammableBlockRegistry.getInstance(fireBlock).add(block.get(), encouragement, flammability);
    }

    public static boolean isModLoaded(String mod) {
        return FabricLoader.getInstance().isModLoaded(mod);
    }

    public static boolean isFakePlayer(Player player) {
        return player instanceof ServerPlayer && player.getClass() != ServerPlayer.class;
    }

//    public static void addModelLoaderRegistration(Consumer<XenoArtifactsClient.ModelLoaderEvent> eventListener) {
//        //Moonlight.assertInitPhase();
//
//        XenoArtifactsFabricClient.PRE_CLIENT_SETUP_WORK.add(() -> eventListener.accept(MLFabricModelLoaderRegistry::registerLoader));
//    }

    // TODO this is a really unclean implementation
    static List<ItemStack> itemList = new ArrayList<>();
    public static Collection<ItemStack> getAllModItems() {
        return itemList;
    }


    @Nullable
    public static MinecraftServer getCurrentServer() {
        return XenoArtifactsFabric.getCurrentServer();
    }


    public static ModRegistry.Side getPhysicalSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? ModRegistry.Side.CLIENT : ModRegistry.Side.SERVER;
    }



}
