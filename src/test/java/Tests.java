import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.hazi.map.AkariMap;
import edu.hazi.map.Cell;

public class Tests {
    @Test
    public void testLoadingMapsAndComments() {
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/comments/test1NoComment.txt"));
        AkariMap map2 = new AkariMap(new File("src/test/java/testMaps/comments/test1Comment.txt"));
        AkariMap assertMap = new AkariMap(new Dimension(2, 3));
        Assertions.assertNotNull(map);
        Assertions.assertNotNull(map2);

        Cell[][] mapCells = map.getCells();
        Cell[][] map2Cells = map2.getCells();
        Cell[][] assertMapCells = assertMap.getCells();
        Assertions.assertNotNull(mapCells);
        Assertions.assertNotNull(map2Cells);
        Assertions.assertNotNull(assertMapCells);

        assertMapCells[0][0].setWall(-1); assertMapCells[1][0].setWall(-1);
        assertMapCells[0][1].setWall(1); assertMapCells[1][1].setWall(1);
        assertMapCells[0][2].setWall(-1); assertMapCells[1][2].setWall(-1);
        
        for(int x = 0; x < mapCells.length; x++) {
            for(int y = 0; y < mapCells[0].length; y++) {
                cellsEqual(mapCells[x][y], map2Cells[x][y]);
                cellsEqual(mapCells[x][y], assertMapCells[x][y]);
            }
        }
    }

    @Test
    public void testLoadingNonExistentMap() {
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/nonExistent/nonexistent.txt"));
        Assertions.assertNotNull(map);
        Assertions.assertNull(map.getCells());
    }

    @Test
    public void testLightUp(){
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/lightUp/lightUpTest.txt"));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        Cell cellToLight = cells[2][2];
        Assertions.assertFalse(cellToLight.isLight());
        Assertions.assertFalse(cellToLight.isLit());
        cellToLight.getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(cellToLight.isLight());
        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[0].length; y++) {
                if(x == 2 || y == 2) {
                    Assertions.assertTrue(cells[x][y].isLit(), "Cell at (" + x + ", " + y + ") should be lit.");
                } else {
                    Assertions.assertFalse(cells[x][y].isLit(), "Cell at (" + x + ", " + y + ") should not be lit.");
                }
            }
        }
    }

    @Test
    public void testWallBlockingLight(){
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/wallBlockingLight/wallBlockingLight.txt"));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        Cell cellToLight = cells[8][1];
        Cell wallCell = cells[8][4];
        Assertions.assertTrue(wallCell.isWall());
        Assertions.assertFalse(cellToLight.isLight());
        Assertions.assertFalse(cellToLight.isLit());
        cellToLight.getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(cellToLight.isLight());

        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[0].length; y++) {
                if((x == 8 || y == 1) && y < 4) {
                    Assertions.assertTrue(cells[x][y].isLit(), "Cell at (" + x + ", " + y + ") should be lit.");
                } else {
                    Assertions.assertFalse(cells[x][y].isLit(), "Cell at (" + x + ", " + y + ") should not be lit.");
                }
            }
        }
    }

    @Test
    public void testWallNumbering() {
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/wallNumbering/wallNumbering.txt"));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        Cell wall = cells[1][1];
        Assertions.assertTrue(wall.isWall());
        Assertions.assertEquals(4, wall.getWall());

        Cell[] cellsToLightUp = {
            cells[0][1],
            cells[1][0],
            cells[1][2],
            cells[2][1]
        };

        int i = 4;
        for(Cell c : cellsToLightUp) {
            c.getActionListeners()[0].actionPerformed(null);
            Assertions.assertEquals(--i, wall.getWall());
        }
        for(Cell c : cellsToLightUp) {
            c.getActionListeners()[0].actionPerformed(null);
            Assertions.assertEquals(++i, wall.getWall());
        }
    }

    @Test
    public void testLightUpLitCell() {
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/lightUp/lightUpTest.txt"));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        Cell firstCellToLight = cells[0][0];
        Cell secondCellToLight = cells[0][2];

        firstCellToLight.getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(firstCellToLight.isLight());
        secondCellToLight.getActionListeners()[0].actionPerformed(null);
        Assertions.assertFalse(secondCellToLight.isLight());
        Assertions.assertTrue(secondCellToLight.isLit());
    }

    @Test
    public void testWallBlockingLightUp(){
        AkariMap map = new AkariMap(new File("src/test/java/testMaps/wallBlockingLightUp/wallBlockingLightUp.txt"));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        Cell wall = cells[1][1];
        Assertions.assertTrue(wall.isWall());
        Assertions.assertEquals(0, wall.getWall());

        Cell cellToLight = cells[0][1];
        Assertions.assertFalse(cellToLight.isLight());
        Assertions.assertFalse(cellToLight.isLit());
        cellToLight.getActionListeners()[0].actionPerformed(null);
        Assertions.assertFalse(cellToLight.isLight());
        Assertions.assertFalse(cellToLight.isLit());
    }

    @Test
    public void testMapEditing() {
        AkariMap map = new AkariMap(new Dimension(3, 3));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        Cell cellToEdit = cells[1][1];
        Assertions.assertFalse(cellToEdit.isWall());
        for(int j = 0; j < 5; j++) {
            for(int i = -1; i <= 4; i++) {
                cellToEdit.getActionListeners()[0].actionPerformed(null);
                Assertions.assertTrue(cellToEdit.isWall());
                Assertions.assertEquals(i, cellToEdit.getWall());
            }
            cellToEdit.getActionListeners()[0].actionPerformed(null);
            Assertions.assertFalse(cellToEdit.isWall());
        }
        map.setEditable(false);
        cellToEdit.getActionListeners()[0].actionPerformed(null);
        Assertions.assertFalse(cellToEdit.isWall());
        Assertions.assertTrue(cellToEdit.isLight());
        map.setEditable(true);
        Assertions.assertFalse(cellToEdit.isLight());
    }

    @Test
    public void testSavingAndLoadingMap() throws Exception {
        File tempFile = File.createTempFile("testMap", ".txt");
        tempFile.deleteOnExit();

        AkariMap map = new AkariMap(new Dimension(2, 2));
        Cell[][] cells = map.getCells();
        cells[0][0].getActionListeners()[0].actionPerformed(null);
        cells[0][1].getActionListeners()[0].actionPerformed(null);
        cells[1][0].getActionListeners()[0].actionPerformed(null);

        map.saveMap(tempFile);

        AkariMap loadedMap = new AkariMap(tempFile);
        Cell[][] loadedCells = loadedMap.getCells();

        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[0].length; y++) {
                cellsEqual(cells[x][y], loadedCells[x][y]);
            }
        }
    }

    @Test
    public void testAddToPanel() {
        AkariMap map = new AkariMap(new Dimension(2, 2));
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        for(Cell[] o : cells){
            for(Cell c : o){
                Assertions.assertFalse(c.isWall());
            }
        }
        cells[0][0].getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(cells[0][0].isWall());
        cells[1][1].getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(cells[1][1].isWall());

        JPanel panel = new JPanel();
        map.addToPanel(panel);

        ArrayList<ArrayList<Cell>> addedCells = new ArrayList<>();
        addedCells.add(new ArrayList<>());

        for(Component comp : panel.getComponents()){
            Assertions.assertTrue(comp instanceof Cell);
            if(addedCells.getLast().size() >= 2){
                 addedCells.add(new ArrayList<>());
            }
            addedCells.getLast().add((Cell) comp);
        }
        for(int x = 0; x < cells.length; x++) {
            for(int y = 0; y < cells[0].length; y++) {
                cellsEqual(cells[x][y], addedCells.get(y).get(x));
            }
        }
    }

    @Test
    public void testIsComplete(){
        AkariMap map = new AkariMap(new Dimension(2, 2));
        map.setEditable(false);
        Cell[][] cells = map.getCells();
        Assertions.assertNotNull(cells);

        
        cells[0][0].getActionListeners()[0].actionPerformed(null);
        Assertions.assertFalse(map.isComplete());

        cells[1][1].getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(map.isComplete());

        cells[1][1].getActionListeners()[0].actionPerformed(null);
        Assertions.assertFalse(map.isComplete());

        map.setEditable(true);
        cells[0][1].getActionListeners()[0].actionPerformed(null);
        cells[0][1].getActionListeners()[0].actionPerformed(null);
        cells[0][1].getActionListeners()[0].actionPerformed(null);
        cells[0][1].getActionListeners()[0].actionPerformed(null);
        map.setEditable(false);

        Assertions.assertFalse(map.isComplete());

        cells[1][0].getActionListeners()[0].actionPerformed(null);
        Assertions.assertFalse(map.isComplete());
        cells[1][0].getActionListeners()[0].actionPerformed(null);
        cells[0][0].getActionListeners()[0].actionPerformed(null);
        cells[1][1].getActionListeners()[0].actionPerformed(null);
        Assertions.assertTrue(map.isComplete());

    }
        

    private void cellsEqual(Cell cell1, Cell cell2) {
        Assertions.assertEquals(cell1.getWall(), cell2.getWall());
        Assertions.assertEquals(cell1.isLight(), cell2.isLight());
        Assertions.assertEquals(cell1.isLit(), cell2.isLit());
        Assertions.assertEquals(cell1.getWall(), cell2.getWall());
    }
}
