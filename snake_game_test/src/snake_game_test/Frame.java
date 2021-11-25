package snake_game_test;

import javax.swing.*;

public class Frame extends JFrame {
    public Frame(){
        setResizable(false);
        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new gamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
