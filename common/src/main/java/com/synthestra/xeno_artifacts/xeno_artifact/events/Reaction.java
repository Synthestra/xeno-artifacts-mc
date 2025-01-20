package com.synthestra.xeno_artifacts.xeno_artifact.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public class Reaction {
    int times;
    public Reaction(int times) {
        this.times = times;
    }

    public Reaction() {
        this(-1);
    }

    public boolean effect(Level level, BlockPos pos) {
        return true;
    }
}
