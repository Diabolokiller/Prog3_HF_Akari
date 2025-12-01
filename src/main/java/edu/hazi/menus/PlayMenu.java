package edu.hazi.menus;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import edu.hazi.map.AkariMap;
import edu.hazi.map.Cell;

/**
 * The PlayMenu displays a map and handles user interaction for playing
 * the Akari puzzle. It constructs the map UI from the supplied map file
 * and provides controls to exit back to the start menu.
 */
public class PlayMenu implements Menu{
    /** The hosting frame for the menu. */
    JFrame frame;
    /** Directory or file used to load the map. */
    File mapDir;
    /** The currently loaded map model. */
    AkariMap map;

    /**
     * Create a PlayMenu which will load its map from the provided file
     *
     * @param mapDir the File object pointing to a map file
     */
    public PlayMenu(File mapDir) {
        this.mapDir = mapDir;
    }

    /**
     * Open the play UI on the provided frame. This populates the frame with
     * the grid of cells and hooks up a win-check after each cell action.
     *
     * @param f the application frame to host the play UI
     */
    public void open(JFrame f){
        frame = f;
        map = new AkariMap(mapDir);
        JPanel playingPanel;
        try {
            playingPanel = new JPanel(new GridLayout(map.getCells()[0].length, map.getCells().length, 0, 0));
        } catch (NullPointerException e) {
            // If map failed to load, return to start menu
            close();
            return;
        }
        map.addToPanel(playingPanel);
        for(Cell[] row : map.getCells()){
            for(Cell c : row) {
                // Schedule win-check after the cell's action completes
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

    /**
     * Close the play menu and return to the start menu. This clears the
     * frame content and opens a fresh StartMenu instance.
     */
    public void close() {
        frame.getContentPane().removeAll();
        StartMenu startMenu = new StartMenu();
        frame.revalidate(); 
        frame.repaint();
        startMenu.open(frame);
    }

    /**
     * Check whether the current map is complete and, if so,
     * close the play menu.
     */
    public void checkWinCondition() {
        if(map.isComplete())
            close();
    }
}
