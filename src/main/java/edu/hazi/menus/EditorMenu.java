package edu.hazi.menus;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.hazi.map.AkariMap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class EditorMenu implements Menu{
    JFrame frame;

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
        AkariMap map = new AkariMap(size);
        frame.getContentPane().removeAll();
        
        JPanel editorPanel = new JPanel(new GridLayout(size.width, size.height));
        map.addToPanel(editorPanel);
        JPanel center = new JPanel(new GridBagLayout());
        center.add(editorPanel);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("SAVE");
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
