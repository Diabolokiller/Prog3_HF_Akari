package edu.hazi.map;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

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
    public Cell (){
        this(null, null, null, null);
    }
    
    public void setWall(int i){
        isWall = true;
        updateBackgroundColor();
        if(wall == -1) return;
        wall = Math.max(i, -1);
        if(wall != -1)
            setText(Integer.toString(wall));
    }
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
    public void setUp(Cell c) { up = c; }
    public void setRight(Cell c) { right = c; }
    public void setDown(Cell c) { down = c; }
    public void setLeft(Cell c) { left = c; }
    
    public Cell getUp() { return up; }
    public Cell getRight() { return right; }
    public Cell getDown() { return down; }
    public Cell getLeft() { return left; }
    public boolean isLit() { return lit > 0; }
    public boolean isLight() { return lit == 3; }
    public boolean isWall() { return isWall; }
    public int getWall() { return wall; }
    
    private void edit(){
        if(!isWall) {
            isWall = true;
            wall = -1;
        }
        else if(isWall()) {
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

    private Cell getNextCell(Direction dir) {
        switch (dir) {
            case UP : return up;
            case RIGHT : return right;
            case DOWN : return down;
            case LEFT : return left;
        }
        return null;
    }

    private void illuminate(Direction dir) {
        if(isWall()) return;
        lit++;
        updateBackgroundColor();
        if(getNextCell(dir) == null) return;
        getNextCell(dir).illuminate(dir);
    }

    private void deIlluminate(Direction dir) {
        if(isWall()) return;
        lit--;
        updateBackgroundColor();
        if(getNextCell(dir) == null) return;
        getNextCell(dir).deIlluminate(dir);
    }

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

    private boolean canLightUp() {
        if(isLit() || isWall()) return false;
        if(up != null && up.isWall && up.wall == 0) return false;
        if(right != null && right.isWall && right.wall == 0) return false;
        if(down != null && down.isWall && down.wall == 0) return false;
        if(left != null && left.isWall && left.wall == 0) return false;
        return true;
    }
}
