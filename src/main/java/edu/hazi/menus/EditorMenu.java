package edu.hazi.menus;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import java.awt.BorderLayout;

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
        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener((e) -> close());
        JButton editButton = new JButton("EDIT");
        buttonPanel.add(editButton);
        buttonPanel.add(exitButton);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(sizePanel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.add(outerPanel);
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
