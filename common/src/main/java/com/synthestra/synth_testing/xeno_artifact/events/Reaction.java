package com.synthestra.synth_testing.xeno_artifact.events;

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

    public void effect(Level level, BlockPos pos) {}
}
