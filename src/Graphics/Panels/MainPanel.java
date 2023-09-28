package Graphics.Panels;

import Graphics.GraphicalAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {
    GraphicalAgent graphicalAgent;
    int GAME_WIDTH = 2000;
    int GAME_HEIGHT = 1000;
    JButton playButton;
    JButton exitButton;
    JButton singleButton;

    public MainPanel(GraphicalAgent graphicalAgent) {
        this.setLayout(null);
        this.setVisible(true);
        this.setSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        playButton = new JButton("Coop");
        playButton.setFont(new Font("Ink Free", Font.PLAIN, 30));
        playButton.setBounds(900, 400, 200, 100);
        playButton.addActionListener(this);
        playButton.setFocusable(false);
        singleButton = new JButton("SinglePlayer");
        singleButton.setFont(new Font("Ink Free", Font.PLAIN, 30));
        singleButton.setBounds(900, 545, 200, 100);
        singleButton.addActionListener(this);
        singleButton.setFocusable(false);
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Ink Free", Font.PLAIN, 30));
        exitButton.setBounds(900, 690, 200, 100);
        exitButton.addActionListener(this);
        exitButton.setFocusable(false);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.add(playButton);
        this.add(exitButton);
        this.add(singleButton);
        this.graphicalAgent = graphicalAgent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            System.exit(0);
        } else if (e.getSource() == playButton) {
            graphicalAgent.startGame(false);
        } else if (e.getSource() == singleButton) {
            graphicalAgent.startGame(true);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Ink Free", Font.PLAIN, 100));
        g.setColor(Color.white);
        g.drawString("RedBlackTree", 700, 300);
        requestFocus();
    }
}

