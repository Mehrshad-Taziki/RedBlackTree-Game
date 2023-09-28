package Graphics;

import DataStructures.RedBlackTree;
import Enums.Player;
import Graphics.Panels.GameOverPanel;
import Graphics.Panels.GamePanel;
import Graphics.Panels.MainPanel;
import com.sun.org.apache.bcel.internal.generic.SIPUSH;

public class GraphicalAgent {
    GameFrame gameFrame;
    GamePanel gamePanel;
    RedBlackTree tree;

    public GraphicalAgent() {
        tree = new RedBlackTree();
        gameFrame = new GameFrame();
        gameFrame.add(new MainPanel(this));
        gameFrame.setVisible(true);
    }

    public void startGame(boolean singlePlayer) {
        tree = new RedBlackTree();
        gameFrame.getContentPane().removeAll();
        this.gamePanel = new GamePanel(this, tree, singlePlayer);
        this.gamePanel.repaint();
        this.gamePanel.validate();
        gameFrame.add(this.gamePanel);
        gameFrame.getContentPane().repaint();
        gameFrame.setVisible(false);
        gameFrame.setVisible(true);
    }

    public void gameOver(boolean singlePlayer) {
        gameFrame.getContentPane().removeAll();
        int scoreOne = tree.countScore(tree.getRoot(), Player.PLAYER_ONE);
        int scoreTwo = tree.countScore(tree.getRoot(), Player.PLAYER_TWO);
        gameFrame.setContentPane(new GameOverPanel(this, scoreOne, scoreTwo, singlePlayer));
        gameFrame.getContentPane().repaint();
        gameFrame.setVisible(false);
        gameFrame.setVisible(true);
    }

    public void mainMenu() {
        gameFrame.getContentPane().removeAll();
        gameFrame.setContentPane(new MainPanel(this));
        gameFrame.getContentPane().repaint();
        gameFrame.setVisible(false);
        gameFrame.setVisible(true);
    }

}
