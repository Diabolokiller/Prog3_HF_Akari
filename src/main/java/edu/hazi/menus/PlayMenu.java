package edu.hazi.menus;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import edu.hazi.map.AkariMap;
import edu.hazi.map.Cell;

public class PlayMenu implements Menu{
    JFrame frame;
    File mapDir;
    AkariMap map;

    public PlayMenu(File mapDir) {
        this.mapDir = mapDir;
    }

    public void open(JFrame f){
        frame = f;
        map = new AkariMap(mapDir);
        JPanel playingPanel;
        try {
            playingPanel = new JPanel(new GridLayout(map.getCells()[0].length, map.getCells().length, 0, 0));
        } catch (NullPointerException e) {
            close();
            return;
        }
        map.addToPanel(playingPanel);
        for(Cell[] row : map.getCells()){
            for(Cell c : row) {
                c.addActionListener((e) -> SwingUtilities.invokeLater(() -> checkWinCondition()));
            }
        }

        JPanel outerPanel = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new GridBagLayout());
        center.add(playingPanel);
        outerPanel.add(center, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener((e) -> close());
        controlPanel.add(exitButton);
        outerPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(outerPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void close() {
        frame.getContentPane().removeAll();
        StartMenu startMenu = new StartMenu();
        frame.revalidate(); 
        frame.repaint();
        startMenu.open(frame);
    }

    public void checkWinCondition() {
        if(map.isComplete())
            close();
    }
}
