package edu.hazi.menus;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import edu.hazi.map.AkariMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class EditorMenu implements Menu{
    JFrame frame;
    AkariMap map;
    File maps;

    public EditorMenu(File maps) {
        this.maps = maps;
    }

    @Override
    public void open(JFrame f) {
        frame = f;
        JPanel sizePanel = new JPanel();
        JSpinner x = new JSpinner(new SpinnerNumberModel(2, 2, 150, 1));
        JSpinner y = new JSpinner(new SpinnerNumberModel(2, 2, 150, 1));
        sizePanel.add(x);
        sizePanel.add(y);

        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("EDIT");
        editButton.addActionListener((e) -> edit(new Dimension((int) x.getValue(), (int) y.getValue())));
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener((e) -> close());
        buttonPanel.add(editButton);
        buttonPanel.add(exitButton);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(sizePanel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.add(outerPanel);
        frame.pack();
    }

    private void edit(Dimension size) {
        map = new AkariMap(size);
        frame.getContentPane().removeAll();
        
        JPanel editorPanel = new JPanel(new GridLayout(size.width, size.height));
        map.addToPanel(editorPanel);
        JPanel center = new JPanel(new GridBagLayout());
        center.add(editorPanel);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener((e) -> save());
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener((e) -> close());
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(center, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(outerPanel);
        
        frame.revalidate(); 
        frame.repaint();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void save() {
        JFrame saveFrame = new JFrame();
        JPanel savePanel = new JPanel(new BorderLayout());
        saveFrame.add(savePanel);

        JLabel label = new JLabel("Name: ");
        JTextField name = new JTextField(20);
        JButton backButton = new JButton("BACK");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("SAVE");
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        saveButton.addActionListener((ae) -> {
            try {
                map.saveMap(new File(maps, name.getText() + ".txt"));
            } catch (FileNotFoundException e) {
                System.out.println("File couldn't be created with that name");
                return;
            }
            saveFrame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            close();
        });
        backButton.addActionListener((e) -> {
            saveFrame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        savePanel.add(label, BorderLayout.WEST);
        savePanel.add(name, BorderLayout.EAST);
        savePanel.add(buttonPanel, BorderLayout.SOUTH);

        saveFrame.pack();
        saveFrame.setLocationRelativeTo(null);
        saveFrame.setVisible(true);
    }

    @Override
    public void close() {
        frame.getContentPane().removeAll();
        StartMenu startMenu = new StartMenu();
        frame.revalidate(); 
        frame.repaint();
        startMenu.open(frame);
    }
    
}
