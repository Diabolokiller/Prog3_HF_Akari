package edu.hazi.menus;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;

public class StartMenu implements Menu {
    JFrame frame;
    JTextField x;
    JTextField y;
    File maps;

    public void open(JFrame f) {
        open(f, new File("./src/main/resources/maps"));
    }

    public void open(JFrame f, File dir){
        maps = dir;
        frame = f;

        JPanel mapSelectPanel = new JPanel(new FlowLayout());
        JComboBox<String> mapSelect = new JComboBox<>(maps.list());
        mapSelectPanel.add(mapSelect);
        
        JButton playButton = new JButton("PLAY");
        JButton editorButton = new JButton("EDITOR");
        JButton exitButton = new JButton("EXIT");
        playButton.addActionListener((e) -> play(new File(maps, (String) mapSelect.getSelectedItem())));
        editorButton.addActionListener((e) -> editor());
        exitButton.addActionListener((e) -> close());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(playButton);
        buttonPanel.add(editorButton);
        buttonPanel.add(exitButton);

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.add(buttonPanel, BorderLayout.CENTER);
        startPanel.add(mapSelectPanel, BorderLayout.SOUTH);

        frame.add(startPanel);
        frame.pack();
    }
    private void play (File map) {
        frame.getContentPane().removeAll();
        PlayMenu playMenu = new PlayMenu(map);
        playMenu.open(frame);
        frame.revalidate();
        frame.repaint();
    }

    private void editor() {

    }

    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
