package com.synthestra.xeno_artifacts.common.network;

import com.synthestra.xeno_artifacts.XenoArtifacts;
import com.synthestra.xeno_artifacts.api.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class ClientBoundParticlePacket implements Message {

    public static final TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundParticlePacket> CODEC = Message.makeType(
            XenoArtifacts.res("s2c_particle"), ClientBoundParticlePacket::new);


    public final Kind type;
    @Nullable
    public final Vec3 pos;
    @Nullable
    public final Integer extraData;
    @Nullable
    public final Vec3 dir;

    public ClientBoundParticlePacket(RegistryFriendlyByteBuf buffer) {
        this.type = buffer.readEnum(Kind.class);
        if (buffer.readBoolean()) {
            this.extraData = buffer.readInt();
        } else this.extraData = null;
        if (buffer.readBoolean()) {
            this.pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        } else {
            this.pos = null;
        }
        if (buffer.readBoolean()) {
            this.dir = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        } else {
            this.dir = null;
        }
    }

    public ClientBoundParticlePacket(BlockPos pos, Kind type) {
        this(Vec3.atCenterOf(pos), type);
    }

    public ClientBoundParticlePacket(BlockPos pos, Kind type, BlockPos end) {
        this(Vec3.atCenterOf(pos), type, null, Vec3.atCenterOf(end));
    }

    public ClientBoundParticlePacket(Vec3 pos, Kind type) {
        this(pos, type, null);
    }

    public ClientBoundParticlePacket(Vec3 pos, Kind type, Integer extraData) {
        this(pos, type, extraData, null);
    }

    public ClientBoundParticlePacket(Vec3 pos, Kind type, Integer extraData, @Nullable Vec3 direction) {
        this.pos = pos;
        this.type = type;
        this.extraData = extraData;
        this.dir = direction;
    }

    public ClientBoundParticlePacket(Entity entity, Kind type) {
        this(entity, type, null);
    }

    public ClientBoundParticlePacket(Entity entity, Kind type, Vec3 dir) {
        this.extraData = entity.getId();
        this.type = type;
        this.pos = null;
        this.dir = dir;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(this.type);
        if (extraData != null) {
            buffer.writeBoolean(true);
            buffer.writeInt(extraData);
        } else {
            buffer.writeBoolean(false);
        }
        if (pos != null) {
            buffer.writeBoolean(true);
            buffer.writeDouble(this.pos.x);
            buffer.writeDouble(this.pos.y);
            buffer.writeDouble(this.pos.z);
        } else {
            buffer.writeBoolean(false);
        }
        if (dir != null) {
            buffer.writeBoolean(true);
            buffer.writeDouble(this.dir.x);
            buffer.writeDouble(this.dir.y);
            buffer.writeDouble(this.dir.z);
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public void handle(Context context) {
        ClientReceivers.handleSpawnBlockParticlePacket(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CODEC.type();
    }

    public enum Kind {
        MIMIC_TELEPORT
    }

}