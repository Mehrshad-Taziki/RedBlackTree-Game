package Graphics.Panels;

import Graphics.GraphicalAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameOverPanel extends JPanel implements ActionListener {
    private final GraphicalAgent graphicalAgent;
    private final JButton restartButton;
    private final JButton mainMenuButton;
    private final int scoreOne;
    private final int scoreTwo;
    private final boolean singlePlayer;

    public GameOverPanel(GraphicalAgent graphicalAgent, int scoreOne, int scoreTwo, boolean singlePlayer) {
        this.setLayout(null);
        this.setVisible(true);
        this.setSize(new Dimension(2000, 1000));
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Ink Free", Font.PLAIN, 30));
        restartButton.setBounds(850, 550, 200, 100);
        restartButton.addActionListener(this);
        restartButton.setFocusable(false);
        mainMenuButton = new JButton("MainMenu");
        mainMenuButton.setFont(new Font("Ink Free", Font.PLAIN, 30));
        mainMenuButton.setBounds(850, 770, 200, 100);
        mainMenuButton.addActionListener(this);
        mainMenuButton.setFocusable(false);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.add(restartButton);
        this.add(mainMenuButton);
        this.graphicalAgent = graphicalAgent;
        this.scoreOne = scoreOne;
        this.scoreTwo = scoreTwo;
        this.singlePlayer = singlePlayer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            graphicalAgent.startGame(singlePlayer);
        } else if (e.getSource() == mainMenuButton) {
            graphicalAgent.mainMenu();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        String winnerString = "Game Ended In A Draw";
        if (scoreOne > scoreTwo) {
            winnerString = "Player Two Won The Game";
        }
        if (scoreTwo > scoreOne) {
            winnerString = "Player One Won The Game";
        }
        super.paintComponent(g);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 85));
        g.setColor(Color.white);
        g.drawString("Game Finished", 650, 300);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
        g.drawString(winnerString, 750, 460);
        g.drawString("Player One Score : " + scoreOne, 750, 410);
        g.drawString("Player Two Score : " + scoreTwo, 750, 360);
        requestFocus();
    }
}
