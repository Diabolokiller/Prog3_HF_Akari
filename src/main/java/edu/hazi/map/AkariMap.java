package edu.hazi.map;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Model for an Akari puzzle map. Responsible for creating cells either from
 * a specified size or by loading a text map file, wiring neighbor references,
 * and providing utility methods to render and save the map.
 */
public class AkariMap implements Serializable {
    private Cell[][] cells;

    /**
     * Construct a new empty map of the given size. All cells are initially
     * non-wall and editable.
     * This constructor is primarily used by the map editor.
     *
     * @param size the width (x) and height (y) of the map in cells
     */
    public AkariMap(Dimension size) {
        cells = new Cell[size.width][size.height];
        for(int y = 0; y < size.height; y++) {
            for(int x = 0; x < size.width; x++) {
                cells[x][y] = new Cell();
                cells[x][y].setEditable(true);
            }
        }
        for(int x = 0; x < size.width; x++) {
            for(int y = 0; y < size.height; y++) {
                if(x > 0) {
                    cells[x][y].setLeft(cells[x - 1][y]);
                    cells[x - 1][y].setRight(cells[x][y]);
                }
                if(y > 0) {
                    cells[x][y].setUp(cells[x][y - 1]);
                    cells[x][y - 1].setDown(cells[x][y]);
                }
            }
        }
    }

    /**
     * Construct a map by reading the supplied text file. The file format
     * uses '#' for unnumbered walls, digits for numbered walls and space for
     * empty cells.
     *
     * @param mapFile the input file containing the map representation
     */
    public AkariMap(File mapFile){
        if(mapFile == null) return;
        char[][] mapCharMatrix;
        try {
            mapCharMatrix = readMap(mapFile);
            if(mapCharMatrix.length == 0) throw new IndexOutOfBoundsException();
        } catch(IOException e) {
            System.out.println("File \"" + mapFile.getAbsolutePath() + "\" isn't a map storing file");
            mapCharMatrix = null;
            return;
        } catch(IndexOutOfBoundsException e) {
            System.out.println("File \"" + mapFile.getAbsolutePath() + "\" has incorrect format");
            mapCharMatrix = null;
            return;
        }
        Dimension size = new Dimension(mapCharMatrix.length, mapCharMatrix[0].length);
        cells = new Cell[size.width][size.height];
        for(int x = 0; x < size.width; x++) {
            for(int y = 0; y < size.height; y++) {
                cells[x][y] = new Cell();
                char c = mapCharMatrix[x][y];
                if(c == '#') cells[x][y].setWall(-1);
                else if(c != ' ') {
                    cells[x][y].setWall((Integer.parseInt(((Character) c).toString())));
                }
            }
        }
        for(int x = 0; x < size.width; x++) {
            for(int y = 0; y < size.height; y++) {
                if(x > 0) {
                    cells[x][y].setLeft(cells[x - 1][y]);
                    cells[x - 1][y].setRight(cells[x][y]);
                }
                if(y > 0) {
                    cells[x][y].setUp(cells[x][y - 1]);
                    cells[x][y - 1].setDown(cells[x][y]);
                }
            }
        }
    }

    /**
     * Read a map file and return a 2D character matrix representing cells.
     * Only characters ' ', '#', and digits '0'..'4' are read, all other characters are ignored.
     *
     * @param map the map file to read
     * @return a 2D char array where [x][y] addresses the cell at column x and row y
     * @throws IOException if the file cannot be read
     */
    private char[][] readMap(File map) throws IOException {
        if(!map.getName().endsWith(".txt")) throw new FileNotFoundException();
        ArrayList<String> readMap = new ArrayList<>();
        List<String> lines = Files.readAllLines(Path.of(map.getAbsolutePath()));
        for(String line : lines) {
            String result = "";
            for(char c : line.toCharArray()){
                if(c == ' ' || c == '#' || c == '0' || c == '1' || c == '2' || c == '3' || c == '4'){
                    result += c;
                }
                if(!readMap.isEmpty() && result.length() >= readMap.getLast().length()) {
                    break;
                }
            }
            if(!result.isEmpty() && (readMap.isEmpty() || result.length() == readMap.getLast().length()))
                readMap.add(result);
        }

        char[][] charMap = new char[readMap.get(0).length()][readMap.size()];

        for(int x = 0; x < charMap.length; x++) {
            for(int y = 0; y < charMap[0].length; y++) {
                charMap[x][y] = readMap.get(y).charAt(x);
            }
        }

        return charMap;
    }

    /**
     * Save the current map representation into the provided file.
     * Walls and numbered walls are written using '#' and digits respectively.
     *
     * @param map the target file to write
     * @throws FileNotFoundException if the file cannot be created
     */
    public void saveMap(File map) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(map);
        char[][] charMap = new char[cells.length][cells[0].length];
        setEditable(false);
        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[0].length; y++) {
                if(cells[x][y].isWall()) {
                    if(cells[x][y].getWall() != -1){
                        charMap[x][y] = ((Integer) cells[x][y].getWall()).toString().charAt(0);
                    } else {
                        charMap[x][y] = '#';
                    }
                } else {
                    charMap[x][y] = ' ';
                }
            }
        }
        for (int x = 0; x < charMap.length; x++) {
            for (int y = 0; y < charMap[0].length; y++) {
                writer.print(charMap[x][y]);
            }
            if (x < charMap.length - 1) writer.println();
        }
        writer.close();
    }

    /**
     * Add all cell components to the provided Swing panel in row-major order
     * so they render in the intended grid layout.
     *
     * @param panel the Swing panel to add cell components to
     */
    public void addToPanel(JPanel panel) {
        for(int y = 0; y < cells[0].length; y++) {
            for(int x = 0; x < cells.length; x++) {
                panel.add(cells[x][y]);
            }
        }
    }

    /**
     * Set whether cells are editable (used by the editor). 
     * This also turns off any lights that were on to avoid buggy behavior.
     *
     * @param editable true to allow editing, false to lock cells
     */
    public void setEditable(boolean editable) {
        for(Cell[] row : cells) {
            for(Cell c : row) {
                c.setEditable(editable);
                if(c.isLight()) c.lightUp();
            }
        }
    }

    /**
     * Check whether the map is complete (every non-wall cell is lit and
     * all wall-number constraints are satisfied).
     *
     * @return true if the puzzle is complete, false otherwise
     */
    public boolean isComplete() {
        for(Cell[] row : cells) {
            for(Cell c : row) {
                if(!((c.isWall() && c.getWall() <= 0) || c.isLit())) return false;
            }
        }
        return true;
    }

    /**
     * Return the internal cell matrix. The first index is the x/column,
     * second index is the y/row: {@code cells[x][y]}.
     *
     * @return 2D array of {@link Cell}
     */
    public Cell[][] getCells() {
        return cells;
    }
}
