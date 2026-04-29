
package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {
    private JPanel mainPanel;
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;

    public StartMenu() {


        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 传入简单模式的具体实现
                startGame("EASY", new EasyGame());
            }
        });

        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 传入普通模式的具体实现
                startGame("NORMAL", new NormalGame());
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 传入困难模式的具体实现
                startGame("HARD", new HardGame());
            }
        });
    }

    private void startGame(String difficulty, BaseGame game) {
        Main.difficulty = difficulty;
        Main.cardPanel.add(game, "game");
        Main.cardLayout.show(Main.cardPanel, "game");
        game.requestFocusInWindow();
        game.action(); // 启动模板模式主循环
    }

    public JPanel getMainPanel() { return mainPanel; }
}