package com.synthestra.xeno_artifacts.util.block;

import java.util.ArrayList;
import java.util.List;

public class NoteContainer {
    Integer data;
    List<NoteContainer> children;

    public NoteContainer(Integer data) {
        this.data = data;
        this.children = new ArrayList<>();
    }
}
