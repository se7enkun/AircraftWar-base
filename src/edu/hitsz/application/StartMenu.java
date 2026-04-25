package edu.hitsz.application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;
    private JPanel mainPanel;

    public StartMenu() {
        // 简单模式
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame("EASY");
            }
        });
        // 普通模式
        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame("NORMAL");
            }
        });
        // 困难模式
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame("HARD");
            }
        });
    }

    private void startGame(String difficulty) {
        // 记录当前难度
        Main.difficulty = difficulty;
        // 实例化游戏面板并启动
        Game game = new Game();
        Main.cardPanel.add(game, "game");
        Main.cardLayout.show(Main.cardPanel, "game");
        game.action();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
