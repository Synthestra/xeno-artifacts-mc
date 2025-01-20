package com.synthestra.xeno_artifacts.api.network.fabric;

/*
    author = MehVahdJukaar
    repo = https://github.com/MehVahdJukaar/Moonlight

    purpose for usage = syncing particles
*/
import com.synthestra.xeno_artifacts.api.network.Message;
import com.synthestra.xeno_artifacts.api.network.NetworkHelper;
import com.synthestra.xeno_artifacts.registry.ModRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class NetworkHelperImpl {

    public static void addNetworkRegistration(Consumer<NetworkHelper.RegisterMessagesEvent> eventListener, int version) {
        eventListener.accept(new NetworkHelper.RegisterMessagesEvent() {

            @Override
            public <M extends Message> void registerServerBound(CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, M> messageType) {
                PayloadTypeRegistry.playC2S().register(messageType.type(), messageType.codec());

                ServerPlayNetworking.registerGlobalReceiver(messageType.type(),
                        (message, context) -> {
                            context.server().execute(() -> {
                                message.handle(new ContextWrapper(context));
                            });
                        });
            }


            @Override
            public <M extends Message> void registerClientBound(CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, M> messageType) {
                PayloadTypeRegistry.playS2C().register(messageType.type(), messageType.codec());

                if (!ModRegistry.getPhysicalSide().isClient()) return;

                NetworkHelperImplClient.register(messageType);
            }

            @Override
            public <M extends Message> void registerBidirectional(CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, M> messageType) {
                this.registerServerBound(messageType);
                this.registerClientBound(messageType);
            }
        });

    }

    public record ContextWrapper(ServerPlayNetworking.Context c) implements Message.Context {

        @Override
        public Message.NetworkDir getDirection() {
            return Message.NetworkDir.SERVER_BOUND;
        }

        @Override
        public Player getPlayer() {
            return c.player();
        }

        @Override
        public void disconnect(Component reason) {
            c.responseSender().disconnect(reason);
        }

        @Override
        public void reply(CustomPacketPayload message) {
            c.responseSender().sendPacket(message);
        }
    }


    public static void sendToClientPlayer(ServerPlayer serverPlayer, CustomPacketPayload message) {
        ServerPlayNetworking.send(serverPlayer, message);
    }

    public static void sendToAllClientPlayers(CustomPacketPayload message) {
        for (var p : ModRegistry.getCurrentServer().getPlayerList().getPlayers()) {
            sendToClientPlayer(p, message);
        }
    }

    public static void sendToAllClientPlayersInRange(ServerLevel level, BlockPos pos, double radius, CustomPacketPayload message) {
        MinecraftServer currentServer = ModRegistry.getCurrentServer();
        if (!level.isClientSide && currentServer != null) {
            PlayerList players = currentServer.getPlayerList();
            var dimension = level.dimension();

            players.broadcast(null, pos.getX(), pos.getY(), pos.getZ(),
                    radius, dimension, ServerPlayNetworking.createS2CPacket(message));
        } else throw makeAssertionError();

    }

    private static @NotNull AssertionError makeAssertionError() {
        return new AssertionError("Cant send message to clients from client side!");
    }

    public static void sendToAllClientPlayersTrackingEntity(Entity target, CustomPacketPayload message) {
        Level level = target.level();
        if (level.isClientSide) throw makeAssertionError();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().broadcast(target, ServerPlayNetworking.createS2CPacket(message));
        }
    }

    public static void sendToAllClientPlayersTrackingEntityAndSelf(Entity target, Message message) {
        Level level = target.level();
        if (level.isClientSide) throw makeAssertionError();
        if (level instanceof ServerLevel serverLevel) {
            var p = ServerPlayNetworking.createS2CPacket(message);
            serverLevel.getChunkSource().broadcast(target, p);
            if (target instanceof ServerPlayer player) {
                sendToClientPlayer(player, message);
            }
        }
    }

    public static void sendToServer(CustomPacketPayload message) {
        ClientPlayNetworking.send(message);
    }
}