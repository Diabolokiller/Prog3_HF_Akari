package edu.hazi.menus;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;

/**
 * The application's start menu. Presents a map selector and navigation
 * controls to enter the game, open the editor, or exit the app.
 */
public class StartMenu implements Menu {
    /** The frame used to host menus. */
    JFrame frame;
    /** Directory containing map files shown in the selector. */
    File maps;

    /**
     * Open the start menu using the default maps directory.
     *
     * @param f the application frame to host the menu
     */
    public void open(JFrame f) {
        open(f, new File("./src/main/resources/maps"));
    }

    /**
     * Open the start menu and use the provided maps directory for the map
     * chooser.
     *
     * @param f the application frame to host the menu
     * @param dir the directory containing map files
     */
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

        JLabel title = new JLabel("Akari", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 48f));

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.add(title, BorderLayout.NORTH);
        startPanel.add(buttonPanel, BorderLayout.CENTER);
        startPanel.add(mapSelectPanel, BorderLayout.SOUTH);

        frame.add(startPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Start the PlayMenu on the given map file. This method clears the frame
     * content and opens a fresh PlayMenu instance so the play state is reset.
     *
     * @param map the map file to load and play
     */
    private void play (File map) {
        frame.getContentPane().removeAll();
        PlayMenu playMenu = new PlayMenu(map);
        playMenu.open(frame);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Open the map editor menu.
     */
    private void editor() {
        frame.getContentPane().removeAll();
        EditorMenu editorMenu = new EditorMenu(maps);
        editorMenu.open(frame);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Exit the application by dispatching a window closing event.
     */
    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
