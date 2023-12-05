package org.example.domain.grid;

public abstract class GridElements {
    public String toString(){
        return this.getClass().toString().split("\\$")[1];
    }

    public static class Counter extends GridElements{}
    public static class FreeSpace extends GridElements{}
    public static class Onion extends GridElements{}
    public static class Cauldron extends GridElements{}
    public static class Sink extends GridElements{}
    public static class Plate extends GridElements{}
    public static class NewPlate extends GridElements{}
    public static class ServePlate extends GridElements{}
    public static class CuttingBoard extends GridElements{}
}
