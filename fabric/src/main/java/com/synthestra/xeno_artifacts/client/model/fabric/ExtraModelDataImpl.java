package com.synthestra.xeno_artifacts.client.model.fabric;

import com.google.common.collect.ImmutableMap;
import com.synthestra.xeno_artifacts.client.model.ExtraModelData;
import com.synthestra.xeno_artifacts.client.model.ModelDataKey;

import java.util.*;

public class ExtraModelDataImpl implements ExtraModelData {

    private final Map<ModelDataKey<?>, Object> backingMap;

    public ExtraModelDataImpl(Map<ModelDataKey<?>, Object> map) {
        this.backingMap = new IdentityHashMap<>(map);
    }

    public <T> T get(ModelDataKey<T> prop) {
        return (T) this.backingMap.get(prop);
    }

    @Override
    public Map<ModelDataKey<?>, Object> values() {
        return ImmutableMap.copyOf(backingMap);
    }

    public static ExtraModelData.Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraModelDataImpl that = (ExtraModelDataImpl) o;
        return Objects.equals(backingMap, that.backingMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backingMap);
    }

    private static class Builder implements ExtraModelData.Builder {

        private final Map<ModelDataKey<?>, Object> map;

        Builder() {
            this.map = new HashMap<>();
        }

        @Override
        public <A> ExtraModelData.Builder with(ModelDataKey<A> prop, A data) {
            map.put(prop, data);
            return this;
        }

        @Override
        public ExtraModelData build() {
            return new ExtraModelDataImpl(map);
        }
    }
}