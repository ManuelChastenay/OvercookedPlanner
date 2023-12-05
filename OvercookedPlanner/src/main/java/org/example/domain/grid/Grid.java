package org.example.domain.grid;

import java.util.Arrays;

public class Grid {
    private String[][] grid;
    private final GridElements[][] gridElements;

    public Grid(String[][] stringData){
        grid = stringData;
        gridElements = new GridElements[grid.length][grid[0].length];
        seedGridElementsInstances();
    }

    public String[][] getGrid(){
        return grid;
    }
    public GridElements[][] getGridElements(){
        return gridElements;
    }

    private void seedGridElementsInstances(){
        for (int i = 0; i < grid.length; i++) {
            String[] line = grid[i];
            for (int j = 0; j < line.length; j++) {
                gridElements[i][j] = GridElementsInstanceFactory.getInstanceFromGridCharValue(line[j]);
            }
        }
    }

    public String toString(){
        String print = "";
        for (int i = 0; i < grid.length; i++) {
            print = print.concat(Arrays.deepToString(grid[i]) + "    " + Arrays.deepToString(gridElements[i]) + "\n");
        }
        return print;
    }
}
