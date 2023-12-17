package org.example;

import org.example.domain.grid.Grid;
import org.example.solver.OvercookedPlannerApp;
import org.example.utils.GridFileReader;
import org.example.utils.Pathfinding;

import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/MapGridConfiguration.txt");
        Pathfinding.setGrid(grid);
        System.out.println(grid);
        OvercookedPlannerApp.plan(grid);
    }
}