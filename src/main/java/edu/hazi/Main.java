package edu.hazi;

import javax.swing.*;
import java.io.File;

import edu.hazi.menus.StartMenu;

public class Main {
    static File mapDir;
    public static void main(String[] args) {
        // Ensure GUI creation runs on the Event Dispatch Thread
        mapDir = new File("./src/main/resources/maps");
        SwingUtilities.invokeLater(() -> start());
    }

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
