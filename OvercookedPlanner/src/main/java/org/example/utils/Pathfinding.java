package org.example.utils;

import com.github.javaparser.utils.Pair;
import org.example.domain.actions.Task;
import org.example.domain.grid.Grid;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import javax.swing.*;
import java.awt.geom.PathIterator;
import java.util.*;
import java.awt.Point;

public class Pathfinding {
    //À implémenter: Le calcul de poids/distance entre un lieu à l'autre sur la carte(Grid). Le poids le plus faible sera retourné.
    private static final String WALL = "X";
    private static final String FREE_SPACE = "O";
    private static Grid grid;
    private static Map<String, List<Point>> linkedSpecialPlaces = new HashMap<String, List<Point>>();

    public static long calculateDistance(Task t, Task pt){
        Point lastPoint = t.getLastPosition();
        Pair<Double, List<Point>> newPath = aStar(lastPoint, t.getPosition());
        return newPath.a.longValue();
    }

    private static Pair<Double, List<Point>> aStar (Point start, Point goal){
        Map<Point, Double> g = new HashMap<Point, Double>(); // Calcule le coût de déplacement pour chaques points à partir du point de départ
        Map<Point, Point> parents = new HashMap<Point, Point>(); // Permet de garder en mémoire le chemin parcouru par l'algorithme

        // Voisins candidats aux noeuds déjà solutionnés, l'heuristique utilisé est le coût du point de départ à ce point (trouvable dans g)
        // additionné par le coût de manathan
        PriorityQueue<Point> candidates = new PriorityQueue<Point>(Comparator.comparingDouble(p -> g.get(p) + ManathanCost(p, goal)));
        candidates.add(start);
        g.put(start, 0.0);
        parents.put(start, null);

        //Tant qu'il y a des candidats,
        while(!candidates.isEmpty()){
            Point candidate = candidates.poll();

            if(candidate.x == goal.x && candidate.y == goal.y){ //Cela veux dire que l'algorithme a trouvé la solution finale.
                return new Pair<Double, List<Point>>(g.get(candidate), getPath(goal, parents));
            }

            //Ajout des prochains candidats.
            for(Point neighbor : getNeighbors(candidate)){
                double newCost = g.get(candidate) + moveCost(candidate, neighbor);
                if(!g.containsKey(neighbor) || newCost< g.get(neighbor)){
                    g.put(neighbor, newCost);
                    candidates.add(neighbor);
                    parents.put(neighbor, candidate);
                }
            }
        }

        return new Pair<Double, List<Point>>(Double.MAX_VALUE, new ArrayList<>());
    }

    private static List<Point> getNeighbors(Point current){
        List<Point> neighbors = new ArrayList<Point>();
        for(int i =-1; i<=1; i++){
            for(int j = -1; j<= 1; j++){
                if(i != 0 && j != 0){
                    int newX = current.x + i;
                    int newY = current.y + j;

                    if(isValid(newX, newY, grid.getGrid())){
                        if(grid.getGrid()[newY][newX].equals('O')){
                            neighbors.add(new Point(newX, newY));
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    private static List<Point> getPath(Point end, Map<Point, Point> parents ){
        Point current = end;
        List<Point> path = new ArrayList<>();

        while(current!=null) {
            path.add(current);
            current = parents.get(current);
        }

        return path.reversed();
    }

    private static int ManathanCost(Point a, Point b){
        return Math.abs(a.x-b.x) + (a.y+b.y);
    }

    private static double moveCost (Point a, Point b){
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y-b.y,2));
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

    public static List<Point> getRelatedPositionsOf(String location){
        return linkedSpecialPlaces.get(location);
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