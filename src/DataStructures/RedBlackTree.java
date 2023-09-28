package DataStructures;

import Enums.Color;
import Enums.Player;

public class RedBlackTree {
    Node root = new NullNode();

    public int height(Node node) {
        int cnt = -1;
        if (!node.isNull()) {
            Node left = node.leftChild;
            Node right = node.rightChild;
            cnt = Math.max(height(left), height(right));
        }
        return cnt + 1;
    }

    public static Node successor(Node r) {
        if (!r.rightChild.isNull()) {
            return minimum(r.rightChild);
        }
        Node y = r.parent;
        while (!y.isNull() && r == y.rightChild) {
            r = y;
            y = y.parent;
        }
        return y;
    }

    public static Node minimum(Node r) {
        while (!r.leftChild.isNull()) {
            r = r.leftChild;
        }
        return r;
    }

    public boolean isEnded() {
        if (this.countNodes(root) == 32) return true;
        return height(this.root) >= 7;
    }

    public boolean contains(int value) {
        Node p = this.root;
        while (!p.isNull()) {
            if (value < p.value) {
                p = p.leftChild;
            } else if (value > p.value) {
                p = p.rightChild;
            } else {
                return true;
            }
        }
        return false;
    }

    public Node get(int value) {
        Node p = this.root;
        while (!p.isNull()) {
            if (value < p.value) {
                p = p.leftChild;
            } else if (value > p.value) {
                p = p.rightChild;
            } else {
                return p;
            }
        }
        return new NullNode();
    }

    public void bstInsert(int x, Player player) {
        Node n = new Node(x, Color.RED, player);
        Node p = this.root;
        Node prep = new NullNode();
        while (!p.isNull()) {
            prep = p;
            if (x < p.value) {
                p = p.leftChild;
            } else if (x > p.value) {
                p = p.rightChild;
            } else {
                if (p.player == player) {
                    rbtDelete(p);
                    bstInsert(x + 32, player);
                }
                return;
            }
        }
        n.parent = prep;
        if (prep.isNull()) this.root = n;
        else if (x < prep.value) prep.leftChild = n;
        else if (x > prep.value) prep.rightChild = n;
        rbtInsert(n);
    }

    public Node rbtInsert(Node x) {
        x.color = Color.RED;
        while (x.parent.color == Color.RED && x != this.root) {
            if (x.parent == x.parent.parent.leftChild) {
                Node y = x.parent.parent.rightChild;
                if (y.color == Color.RED) {
                    x.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.rightChild) {
                        x = x.parent;
                        leftRotate(x);
                    }
                    x.parent.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    rightRotate(x.parent.parent);
                }
            } else {
                Node y = x.parent.parent.leftChild;
                if (y.color == Color.RED) {
                    x.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.leftChild) {
                        x = x.parent;
                        rightRotate(x);
                    }
                    x.parent.color = Color.BLACK;
                    x.parent.parent.color = Color.RED;
                    leftRotate(x.parent.parent);
                }
            }
        }
        this.root.color = Color.BLACK;
        return x;
    }

    public Node rbtDelete(Node z) {
        Node y;
        Node x;
        if (z.leftChild.isNull() || z.rightChild.isNull()) {
            y = z;
        } else {
            y = successor(z);
        }
        if (!y.leftChild.isNull()) {
            x = y.leftChild;
        } else {
            x = y.rightChild;
        }
        x.parent = y.parent;
        if (y.parent.isNull()) {
            this.root = x;
        } else if (y == y.parent.leftChild) {
            y.parent.leftChild = x;
        } else {
            y.parent.rightChild = x;
        }
        if (y != z) {
            z.value = y.value;
            z.player = y.player;
        }
        if (y.color == Color.BLACK) {
            deleteFixer(x);
        }
        return y;
    }

    public void deleteFixer(Node x) {
        while (x != this.root && x.color == Color.BLACK) {
            if (x == x.parent.leftChild) {
                Node w = x.parent.rightChild;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.rightChild;
                }
                if (w.leftChild.color == Color.BLACK && w.rightChild.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.rightChild.color == Color.BLACK) {
                        w.leftChild.color = Color.BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.rightChild;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.rightChild.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = this.root;
                }
            } else {
                Node w = x.parent.leftChild;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.leftChild;
                }
                if (w.rightChild.color == Color.BLACK && w.leftChild.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.leftChild.color == Color.BLACK) {
                        w.rightChild.color = Color.BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.leftChild;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.leftChild.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = this.root;
                }
            }
        }
        x.color = Color.BLACK;
    }

    public void leftRotate(Node x) {
        Node y = x.rightChild;
        x.rightChild = y.leftChild;
        if (!y.leftChild.isNull()) {
            y.leftChild.parent = x;
        }
        y.parent = x.parent;
        if (x.parent.isNull()) {
            this.root = y;
        } else if (x == x.parent.leftChild) {
            x.parent.leftChild = y;
        } else {
            x.parent.rightChild = y;
        }
        y.leftChild = x;
        x.parent = y;
    }

    public int countNodes(Node n) {
        if (n.isNull()) return 0;
        int count = 1;
        count += countNodes(n.leftChild);
        count += countNodes(n.rightChild);
        return count;
    }

    public void rightRotate(Node x) {
        Node y = x.leftChild;
        x.leftChild = y.rightChild;
        if (!y.rightChild.isNull()) {
            y.rightChild.parent = x;
        }
        y.parent = x.parent;
        if (x.parent.isNull()) {
            this.root = y;
        } else if (x == x.parent.rightChild) {
            x.parent.rightChild = y;
        } else {
            x.parent.leftChild = y;
        }
        y.rightChild = x;
        x.parent = y;
    }

    public int countScore(Node n, Player player) {
        int ans = 0;
        if (!n.isNull()) {
            if (n.player == player && n.color == Color.RED) ans += height(n) * n.value;
            ans += countScore(n.leftChild, player);
            ans += countScore(n.rightChild, player);
        }
        return ans;
    }

    public int getDifference(Player player) {
        int playerOneScore = countScore(this.root, Player.PLAYER_ONE);
        int playerTwoScore = countScore(this.root, Player.PLAYER_TWO);
        if (player == Player.PLAYER_ONE) return playerTwoScore - playerOneScore;
        else return playerOneScore - playerTwoScore;
    }

    public Node getRoot() {
        return root;
    }

    public RedBlackTree getClone() {
        RedBlackTree tree = new RedBlackTree();
        tree.root = this.root.getClone();
        return tree;
    }

}