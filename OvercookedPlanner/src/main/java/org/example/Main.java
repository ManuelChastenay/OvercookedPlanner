package org.example;

import org.example.domain.Grid;
import org.example.solver.OvercookedPlannerApp;
import org.example.utils.GridFileReader;

import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/MapGridConfiguration.txt");
        System.out.println(Arrays.deepToString(grid.getGrid()));
        OvercookedPlannerApp.plan(grid);
    }
}