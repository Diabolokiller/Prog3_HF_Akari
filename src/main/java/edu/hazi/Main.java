package edu.hazi;

import javax.swing.*;
import java.io.File;

import edu.hazi.menus.StartMenu;

/**
 * Application entry point for the Akari puzzle UI.
 * Responsible for initializing the main window and showing the start menu.
 */
public class Main {
    /**
     * Directory where bundled or user maps reside. Defaults to
     * `src/main/resources/maps` inside the project workspace.
     */
    static File mapDir;

    /**
     * Application main method. Initializes the map directory and schedules
     * the UI creation on the Swing Event Dispatch Thread.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Ensure GUI creation runs on the Event Dispatch Thread
        mapDir = new File("./src/main/resources/maps");
        SwingUtilities.invokeLater(() -> start());
    }

    /**
     * Create and show the main application frame and open the StartMenu.
     * This method runs on the EDT.
     */
    private static void start() {
        JFrame frame = new JFrame("Akari");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartMenu startMenu = new StartMenu();
        startMenu.open(frame, mapDir);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
