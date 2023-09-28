package Graphics.Panels;

import Bot.AutoPlayer;
import DataStructures.Node;
import DataStructures.RedBlackTree;
import Enums.Player;
import Graphics.GraphicalAgent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private final GraphicalAgent graphicalAgent;
    private final RBTPanel RBTPanel;
    private final int GAME_WIDTH = 2000;
    private final int GAME_HEIGHT = 900;
    private final boolean singlePlayer;
    Player turn;
    JButton insertButton;
    JButton showNull;
    JButton showEnd;
    JLabel playerOneLastPick;
    JLabel playerTwoLastPick;
    JLabel playerOneScore;
    JLabel playerTwoScore;
    JTextField insertTextField;
    boolean gameOver;
    RedBlackTree tree;
    private AutoPlayer autoPlayer;

    public GamePanel(GraphicalAgent graphicalAgent, RedBlackTree tree, boolean singlePlayer) {
        super.setLayout(new BorderLayout());
        this.setSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        Border border = BorderFactory.createLineBorder(Color.black);

        this.insertButton = new JButton("Insert");
        this.insertButton.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.insertButton.addActionListener(this);
        this.insertButton.setFocusable(false);
        this.insertButton.setBackground(Color.WHITE);

        this.showNull = new JButton("Show Null");
        this.showNull.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.showNull.addActionListener(this);
        this.showNull.setFocusable(false);
        this.showNull.setBackground(Color.WHITE);

        this.showEnd = new JButton("Show End");
        this.showEnd.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.showEnd.addActionListener(this);
        this.showEnd.setFocusable(false);
        this.showEnd.setBackground(Color.WHITE);
        this.showEnd.setVisible(false);

        this.playerOneLastPick = new JLabel("Player 1 Pick :  ");
        this.playerOneLastPick.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.playerOneLastPick.setFocusable(false);
        this.playerOneLastPick.setBackground(Color.WHITE);
        this.playerOneLastPick.setBorder(border);

        this.playerTwoLastPick = new JLabel("Player 2 Pick :  ");
        this.playerTwoLastPick.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.playerTwoLastPick.setFocusable(false);
        this.playerTwoLastPick.setBackground(Color.WHITE);
        this.playerTwoLastPick.setBorder(border);


        this.playerOneScore = new JLabel("Player 1 Score : 0 ");
        this.playerOneScore.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.playerOneScore.setFocusable(false);
        this.playerOneScore.setBackground(Color.WHITE);
        this.playerOneScore.setBorder(border);

        this.playerTwoScore = new JLabel("Player 2 Score : 0 ");
        this.playerTwoScore.setFont(new Font("Ink Free", Font.PLAIN, 30));
        this.playerTwoScore.setFocusable(false);
        this.playerTwoScore.setBackground(Color.WHITE);
        this.playerTwoScore.setBorder(border);

        this.gameOver = false;
        this.insertTextField = new JTextField(15);
        this.setBackground(Color.BLACK);
        this.graphicalAgent = graphicalAgent;
        this.tree = tree;
        this.RBTPanel = new RBTPanel(tree, 20, 50, 40);
        this.RBTPanel.setPreferredSize(new Dimension(9000, 4096));
        JPanel panel = new JPanel();
        panel.add(insertButton);
        panel.add(insertTextField);
        panel.add(showNull);
        panel.add(showEnd);
        panel.setBackground(Color.GRAY);
        JPanel scorePanel = new JPanel();
        scorePanel.add(playerOneLastPick);
        scorePanel.add(playerTwoLastPick);
        scorePanel.add(playerOneScore);
        scorePanel.add(playerTwoScore);
        scorePanel.setBackground(Color.GRAY);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(RBTPanel);
        scroll.setPreferredSize(new Dimension(750, 500));
        scroll.getViewport().setViewPosition(new Point(3500, 0));
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(panel, BorderLayout.SOUTH);
        mainPanel.add(scorePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        this.turn = Player.PLAYER_ONE;
        this.singlePlayer = singlePlayer;
        this.autoPlayer = new AutoPlayer(tree);
        this.setFocusable(false);
        this.setFocusTraversalKeysEnabled(false);
        this.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        requestFocus();
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertButton) {
            if (!gameOver) {
                if (insertTextField.getText().equals("")) {
                    return;
                }
                try {
                    Integer number = Integer.parseInt(insertTextField.getText());
                    if (tree.contains(number + 32)) {
                        JOptionPane.showMessageDialog(null,
                                "This Element Has Been Added Previously");
                        return;
                    } else if (number < 1 || number > 32) {
                        JOptionPane.showMessageDialog(null,
                                "Number Must Be Between 1 And 32");
                        return;
                    }
                    if (tree.contains(number)) {
                        Node node = tree.get(number);
                        if (node.getPlayer() != turn) {
                            JOptionPane.showMessageDialog(null,
                                    "You Cant Delete A Node That You Didn't Insert");
                            return;
                        }
                    }
                    updatePick(turn, number);
                    tree.bstInsert(number, turn);
                    turn = turn.next();
                    RBTPanel.repaint();
                    insertTextField.requestFocus();
                    insertTextField.selectAll();
                    insertTextField.setText("");
                    if (tree.isEnded()) {
                        showEnd.setVisible(true);
                        gameOver = true;
                        repaint();
                    }
                } catch (Throwable throwable) {
                    insertTextField.setText("");
                }
            }
            if (!tree.isEnded() && turn == Player.PLAYER_TWO && singlePlayer) {
                int number = autoPlayer.choose(tree);
                updatePick(turn, number);
                tree.bstInsert(number, turn);
                turn = turn.next();
                RBTPanel.repaint();
                if (tree.isEnded()) {
                    showEnd.setVisible(true);
                    gameOver = true;
                    repaint();
                }
            }
            updateScores();
        }
        if (e.getSource() == showNull) {
            RBTPanel.reverseShowNull();
            RBTPanel.repaint();
        }
        if (e.getSource() == showEnd) {
            graphicalAgent.gameOver(true);
        }
    }

    public void updatePick(Player player, int pick) {
        if (player == Player.PLAYER_ONE) playerOneLastPick.setText("Player 1 Pick : " + pick + " ");
        else playerTwoLastPick.setText("Player 2 Pick : " + pick + " ");
    }

    public void updateScores() {
        int scoreOne = tree.countScore(tree.getRoot(), Player.PLAYER_ONE);
        int scoreTwo = tree.countScore(tree.getRoot(), Player.PLAYER_TWO);
        playerOneScore.setText("Player 1 Score : " + scoreOne + " ");
        playerTwoScore.setText("Player 2 Score : " + scoreTwo + " ");
    }
}
