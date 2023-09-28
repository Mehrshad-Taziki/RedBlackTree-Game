package Graphics.Panels;

import DataStructures.Node;
import DataStructures.RedBlackTree;
import Enums.Player;

import javax.swing.*;
import java.awt.*;


public class RBTPanel extends JPanel {

    private final int circleRadius;
    private final RedBlackTree tree;
    private final int levelY;
    private final int levelX;
    private boolean showNull = false;

    public RBTPanel(RedBlackTree tree, int circleRadius, int levelY, int levelX) {
        this.tree = tree;
        this.circleRadius = circleRadius;
        this.levelY = levelY;
        this.levelX = levelX;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (tree.getRoot().isNull())
            return;
        Graphics2D graphics2d = (Graphics2D) graphics;
        drawTree(graphics2d, tree.getRoot(), getWidth() / 2, levelY, calculateXLevel());

    }

    private void drawTree(Graphics2D g, Node node, int x, int y, int xLevel) {
        if (showNull) {
            if (node.isNull() && node == tree.getRoot()) return;
            if (node.isNull()) {
                drawNode(g, node, x, y);
                return;
            }
            g.setColor(Color.DARK_GRAY);
            if (node.getLeftChild() != null) {
                g.drawLine(x - xLevel, y + levelY, x, y);

            }
            if (node.getRightChild() != null) {
                g.drawLine(x + xLevel, y + levelY, x, y);
            }
            drawNode(g, node, x, y);
            if (node.getLeftChild() != null) {
                drawTree(g, node.getLeftChild(), x - xLevel, y + levelY, xLevel / 2);
            }
            if (node.getRightChild() != null) {
                drawTree(g, node.getRightChild(), x + xLevel, y + levelY, xLevel / 2);
            }
        } else {
            if (node.isNull()) {
                return;
            }
            g.setColor(Color.DARK_GRAY);
            if (!node.getLeftChild().isNull()) {
                g.drawLine(x - xLevel, y + levelY, x, y);
            }
            if (!node.getRightChild().isNull()) {
                g.drawLine(x + xLevel, y + levelY, x, y);
            }
            drawNode(g, node, x, y);
            if (!node.getLeftChild().isNull()) {
                drawTree(g, node.getLeftChild(), x - xLevel, y + levelY, xLevel / 2);
            }
            if (!node.getRightChild().isNull()) {
                drawTree(g, node.getRightChild(), x + xLevel, y + levelY, xLevel / 2);
            }
        }
    }


    private void drawNode(Graphics2D g, Node node, int x, int y) {
        if (node.getColor() == Enums.Color.RED) {
            if (node.getPlayer() == Player.PLAYER_TWO) g.setColor(new Color(166, 8, 53));
            else g.setColor(new Color(255, 97, 97));
        } else {
            if (node.getPlayer() == Player.PLAYER_TWO) g.setColor(Color.BLACK);
            else g.setColor(new Color(82, 81, 81));
        }
        g.fillOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);
        g.setColor(Color.WHITE);
        int value = node.getValue();
        if (value != 0) {
            int textLength;
            if (String.valueOf(value).length() == 2) textLength = 6;
            else textLength = 2;
            g.drawString(String.valueOf(value), x - textLength, y + 2);
        } else {
            g.drawString("NIL", x - 8, y + 2);
        }
    }

    private int calculateXLevel() {
        int depth = tree.getRoot().height();
        if (showNull) depth = depth + 1;
        int currentXLevel = levelX;
        if (depth > 3) {
            currentXLevel = currentXLevel * (int) (Math.ceil(depth / 3.0));
        }
        return currentXLevel * depth;

    }

    public void reverseShowNull() {
        this.showNull = !showNull;
    }
}
