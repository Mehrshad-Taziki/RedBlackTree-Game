package DataStructures;

import Enums.Color;

public class NullNode extends Node {

    public NullNode() {
        this.color = Color.BLACK;
        this.value = 0;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
    }

    @Override
    public boolean isNull() {
        return true;
    }
}