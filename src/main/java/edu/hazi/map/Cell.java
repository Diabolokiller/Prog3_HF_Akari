package edu.hazi.map;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

/**
 * A single cell on the Akari board. Cells may be empty, walls (numbered or
 * unnumbered), or light sources. The Cell class extends {@link JButton}
 * to provide interactive behavior in the Swing UI.
 */
public class Cell extends JButton {
    private Cell up;
    private Cell right;
    private Cell down;
    private Cell left;
    private int lit;
    private int wall;
    private boolean isWall;
    private boolean isEditable;

    private static final int DEFAULT_RGB = 0x000000;
    private static final int DEFAULT_ONCE_LIT_RGB = 0x665555;
    private static final int DEFAULT_TWICE_LIT_RGB = 0x887777;
    private static final int DEFAULT_LIGHT_RGB = 0xFFFFFF;
    private static final int DEFAULT_WALL_RGB = 0xCC5555;

    private enum Direction {
        UP,
        RIGHT,
        LEFT,
        DOWN;
    }

    /**
     * Construct a cell with explicit neighbors. The cell is initially
     * unlit and non-wall.
     *
     * @param up neighbor cell above (may be null)
     * @param right neighbor cell to the right (may be null)
     * @param down neighbor cell below (may be null)
     * @param left neighbor cell to the left (may be null)
     */
    public Cell (Cell up, Cell right, Cell down, Cell left) {
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
        lit = 0;
        wall = 0;
        isWall = false;
        addActionListener((e) -> {
            lightUp();
        });
        setPreferredSize(new Dimension(50, 50));
        setMinimumSize(new Dimension(50, 50));
        setBackground(new Color(DEFAULT_RGB));
    }
    /**
     * Default constructor creating an isolated, empty cell.
     */
    public Cell (){
        this(null, null, null, null);
    }
    
    /**
     * Set this cell to a wall. Use -1 for an unnumbered wall, or 0..4 for a numbered wall.
     * An unnumbered wall can not be changed into a numbered wall using this method.
     *
     * @param i the wall number or -1 for an unnumbered wall
     */
    public void setWall(int i){
        isWall = true;
        updateBackgroundColor();
        if(wall == -1) return;
        wall = Math.max(i, -1);
        if(wall != -1)
            setText(Integer.toString(wall));
    }
    /**
     * Enable or disable edit mode for this cell. In edit mode clicks adjust
     * wall/state; in play mode clicks toggle lights.
     *
     * @param editable true to enable editor behavior, false for play behavior
     */
    public void setEditable(boolean editable) { 
        if(isEditable != editable) {
            for(ActionListener l : getActionListeners()){
                removeActionListener(l);
            }
            if(editable) {
                addActionListener((e) -> edit());
            } else {
                addActionListener((e) -> lightUp());
            }
        }
        isEditable = editable;
    }
    /** Set neighbor references. */
    public void setUp(Cell c) { up = c; }
    public void setRight(Cell c) { right = c; }
    public void setDown(Cell c) { down = c; }
    public void setLeft(Cell c) { left = c; }
    
    /** Return neighbors and state. */
    public Cell getUp() { return up; }
    public Cell getRight() { return right; }
    public Cell getDown() { return down; }
    public Cell getLeft() { return left; }
    public boolean isLit() { return lit > 0; }
    public boolean isLight() { return lit == 3; }
    public boolean isWall() { return isWall; }
    public int getWall() { return wall; }
    
    /** Toggle or cycle wall states while in editor mode. */
    private void edit(){
        if(!isWall) {
            isWall = true;
            wall = -1;
        }
        else {
            wall++;
            if(wall > 4) {
                wall = -1;
                isWall = false;
                setText("");
            }
        }
        if(wall != -1)
            setText(Integer.toString(wall));
        updateBackgroundColor();
    }

    /**
     * Update the cell background color based on current light/wall state.
     */
    private void updateBackgroundColor() {
        if(isWall()){
            setBackground(new Color(DEFAULT_WALL_RGB));
            return;
        }
        switch(lit){
            case 1: setBackground(new Color(DEFAULT_ONCE_LIT_RGB));break;
            case 2: setBackground(new Color(DEFAULT_TWICE_LIT_RGB)); break;
            case 3: setBackground(new Color(DEFAULT_LIGHT_RGB)); break;
            default: setBackground(new Color(DEFAULT_RGB)); break;
        }
    }

    /**
     * Return the neighbor in the given direction.
     */
    private Cell getNextCell(Direction dir) {
        switch (dir) {
            case UP : return up;
            case RIGHT : return right;
            case DOWN : return down;
            case LEFT : return left;
        }
        return null;
    }

    /**
     * increase the illumination of the next cells in the given direction until a wall or the edge of the map is reached.
     */
    private void illuminate(Direction dir) {
        if(isWall()) return;
        lit++;
        updateBackgroundColor();
        if(getNextCell(dir) == null) return;
        getNextCell(dir).illuminate(dir);
    }

    /**
     * Decrease the illumination of the next cells in the given direction until a wall or the edge of the map is reached.
     */
    private void deIlluminate(Direction dir) {
        if(isWall()) return;
        lit--;
        updateBackgroundColor();
        if(getNextCell(dir) == null) return;
        getNextCell(dir).deIlluminate(dir);
    }

    /**
     * Turn off this light and propagate de-illumination to neighbors.
     */
    private void lightDown() {
        lit = 0;
        setBackground(new Color(DEFAULT_RGB));
        if (up != null) {
            up.deIlluminate(Direction.UP);
            if(up.isWall) up.setWall(up.wall+1);
        }
        if (right != null) {
            right.deIlluminate(Direction.RIGHT);
            if(right.isWall) right.setWall(right.wall+1);
        }
        if (down != null) {
            down.deIlluminate(Direction.DOWN);
            if(down.isWall) down.setWall(down.wall+1);
        }
        if (left != null) {
            left.deIlluminate(Direction.LEFT);
            if(left.isWall) left.setWall(left.wall+1);
        }
    }

    /**
     * Toggle this cell as a light source. If currently a light, turn it off.
     * Otherwise attempt to light this cell and propagate illumination.
     */
    public void lightUp() {
        if(isLight()) {
            lightDown();
            return;
        }
        if(!canLightUp()) return;
        lit = 3;
        setBackground(new Color(DEFAULT_LIGHT_RGB));
        if (up != null) {
            up.illuminate(Direction.UP);
            if(up.isWall) up.setWall(up.wall-1);
        }
        if (right != null) {
            right.illuminate(Direction.RIGHT);
            if(right.isWall) right.setWall(right.wall-1);
        }
        if (down != null) {
            down.illuminate(Direction.DOWN);
            if(down.isWall) down.setWall(down.wall-1);
        }
        if (left != null) {
            left.illuminate(Direction.LEFT);
            if(left.isWall) left.setWall(left.wall-1);
        }
    }

    /**
     * Check whether this cell can be lit (not a wall and not blocked by numbered walls).
     *
     * @return true if the cell can be lit, false otherwise
     */
    private boolean canLightUp() {
        if(isLit() || isWall()) return false;
        if(up != null && up.isWall && up.wall == 0) return false;
        if(right != null && right.isWall && right.wall == 0) return false;
        if(down != null && down.isWall && down.wall == 0) return false;
        if(left != null && left.isWall && left.wall == 0) return false;
        return true;
    }
}
