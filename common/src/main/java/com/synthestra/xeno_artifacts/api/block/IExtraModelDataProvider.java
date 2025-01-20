package com.synthestra.xeno_artifacts.api.block;

import com.synthestra.xeno_artifacts.client.model.ExtraModelData;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public interface IExtraModelDataProvider {

    @ApiStatus.Internal
    default ExtraModelData getExtraModelData() {
        var builder = ExtraModelData.builder();
        addExtraModelData(builder);
        return builder.build();
    }

    default void addExtraModelData(ExtraModelData.Builder builder) {
    }

    default void requestModelReload() {
    }

    default void afterDataPacket(ExtraModelData oldData) {
        if (!Objects.equals(oldData, this.getExtraModelData())) {
            //this request render data refresh
            this.requestModelReload();
        }
    }
}