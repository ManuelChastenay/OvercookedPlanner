package org.example.utils;

import org.example.domain.grid.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;

public class Pathfinding {
    //À implémenter: Le calcul de poids/distance entre un lieu à l'autre sur la carte(Grid). Le poids le plus faible sera retourné.
    private static final String WALL = "X";
    private static final String FREE_SPACE = "O";
    private static Grid grid;
    private static Map<String, List<Point>> linkedSpecialPlaces = new HashMap<String, List<Point>>();

    public static long calculateDistance(String t1, Point t2){


        return 0;
    }

    public static void setGrid(Grid g) {
        grid = g;
        String[][] strgrid= g.getGrid();

        for (int y=0; y < strgrid.length; y++){
            for(int x=0; x<strgrid[y].length;x++){
                if(!strgrid[y][x].equals(WALL) && !strgrid[y][x].equals(FREE_SPACE)){
                    Pathfinding.linkedSpecialPlaces.putIfAbsent(strgrid[y][x], new ArrayList<Point>());
                    checkNeighbors(x,y,strgrid);
                }
            }
        }
    }

    private static void checkNeighbors(int x, int y, String[][] grid){
        int[] xOffset = {-1,1,0,0};
        int[] yOffset = {0,0,-1,1};
        for(int i=0; i< xOffset.length; i++){
            int newX = x+xOffset[i];
            int newY = y+yOffset[i];
            if(isValid(newX, newY, grid)&& grid[newY][newX].equals(FREE_SPACE)) {
                Pathfinding.linkedSpecialPlaces.get(grid[y][x]).add(new Point(newX, newY));
            }
        }
    }

    private static boolean isValid(int x, int y, String[][] grid){
        return (y>=0 && y<grid.length && x>=0 && x< grid[y].length);
    }
}