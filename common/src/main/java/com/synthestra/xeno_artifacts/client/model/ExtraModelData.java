package com.synthestra.xeno_artifacts.client.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ExtraModelData {

    ExtraModelData EMPTY = ExtraModelData.builder().build();

    @ExpectPlatform
    static Builder builder() {
        throw new AssertionError();
    }

    @Nullable <T> T get(ModelDataKey<T> key);

    Map<ModelDataKey<?>, Object> values();

    interface Builder {
        <A> Builder with(ModelDataKey<A> key, A data);

        ExtraModelData build();
    }

    default boolean isEmpty() {
        return this == EMPTY;
    }
}