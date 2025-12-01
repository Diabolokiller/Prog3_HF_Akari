package edu.hazi.menus;

import javax.swing.JFrame;

/**
 * Common interface for application menus/screens.
 * Implementations should attach their UI to the provided {@link JFrame}
 * in {@link #open(JFrame)} and perform teardown in {@link #close()}.
 */
public interface Menu {
    /**
     * Show this menu on the provided frame. Implementations should add
     * components to the frame's content pane and make any necessary layout
     * calls (revalidate/pack) as appropriate.
     *
     * @param f the application's main frame used to host the menu
     */
    public void open(JFrame f);

    /**
     * Close this menu and perform any cleanup. Implementations typically
     * remove their components from the frame and optionally navigate to
     * another menu or exit the application.
     */
    public void close();
}
