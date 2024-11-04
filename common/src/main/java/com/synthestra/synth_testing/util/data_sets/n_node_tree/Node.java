package com.synthestra.synth_testing.util.data_sets.n_node_tree;

import java.util.ArrayList;

public class Node<T> {
    T data;
    ArrayList<Node<T>> children;

    public Node(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public Node() {}

    public ArrayList<Node<T>> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void addChildren(Node<T> child) {
        this.children.add(child);
    }
}