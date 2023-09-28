package DataStructures;

import Enums.Color;
import Enums.Player;

public class Node {
    int value;
    Color color;
    Node rightChild;
    Node leftChild;
    Node parent;
    Player player;

    public Node() {
    }

    public Node(int value, Color color, Player player) {
        this.value = value;
        this.color = color;
        this.player = player;
        this.rightChild = new NullNode();
        this.leftChild = new NullNode();
        this.parent = new NullNode();
        this.rightChild.parent = this;
        this.leftChild.parent = this;
        this.parent.rightChild = this;
        this.parent.leftChild = this;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getParent() {
        return parent;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isNull() {
        return false;
    }

    public int height() {
        int cnt = -1;
        if (!this.isNull()) {
            Node left = this.leftChild;
            Node right = this.rightChild;
            cnt = Math.max(left.height(), right.height());
        }
        return cnt + 1;
    }
    public Node getClone(){
        if (isNull()) return new NullNode();
        Node node = new Node(this.value, this.color, this.player);
        if (!this.rightChild.isNull()) {
            node.rightChild = this.rightChild.getClone();
            node.rightChild.parent = node;
        }
        if (!this.leftChild.isNull()) {
            node.leftChild = this.leftChild.getClone();
            node.leftChild.parent = node;
        }
        return node;
    }
}

