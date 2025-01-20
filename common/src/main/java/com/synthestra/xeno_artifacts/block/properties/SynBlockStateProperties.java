package com.synthestra.xeno_artifacts.block.properties;

import com.synthestra.xeno_artifacts.client.model.ModelDataKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

public class SynBlockStateProperties {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public static final BooleanProperty HAS_MIMIC = BooleanProperty.create("has_mimic");

    // model properties
    public static final ModelDataKey<BlockState> MIMIC = new ModelDataKey<>(BlockState.class);
}