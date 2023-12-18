package org.example;

import org.example.domain.grid.Grid;
import org.example.solver.OvercookedPlannerApp;
import org.example.utils.GridFileReader;
import org.example.utils.Pathfinding;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_1/MapGridConfiguration.txt");
        //Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_2/MapGridConfiguration.txt");
        //Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_3/MapGridConfiguration.txt");
        //Grid grid = GridFileReader.getGridfromConfigFile("Overcooked 1_4/MapGridConfiguration.txt");
        Pathfinding.setGrid(grid);

        System.out.println(grid);

        OvercookedPlannerApp.plan();
    }
}