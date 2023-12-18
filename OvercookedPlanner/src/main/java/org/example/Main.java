package org.example;

import org.example.domain.grid.Grid;
import org.example.solver.OvercookedPlannerApp;
import org.example.utils.GridFileReader;
import org.example.utils.Pathfinding;

public class Main {
    public static void main(String[] args) {
        Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/map_1.txt");
        //Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/map_2.txt");
        //Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/map_3.txt");
        //Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/map_4.txt");
        Pathfinding.setGrid(grid);

        System.out.println(grid);

        OvercookedPlannerApp.plan();
    }
}