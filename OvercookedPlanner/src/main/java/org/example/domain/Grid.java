package org.example.domain;

public class Grid {
    private String[][] grid;

    public Grid(String[][] stringData){
        grid = stringData;
    }

    public String[][] getGrid(){
        return grid;
    }
}
