package org.example.utils;

import org.example.domain.grid.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class GridFileReader {
    public static Grid getGridfromConfigFile (String path) {
        try {
            File textfile = getFileFromResource(path);
            Scanner fileScanner = new Scanner(textfile);

            int height = fileScanner.nextInt();
            int width = fileScanner.nextInt();
            String[][] grid = new String[height][width];

            int i = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if(line.isEmpty()) continue;

                int j = 0;
                for (String word : line.split(" ")){
                    grid[i][j] = word;
                    j++;
                }
                i++;
            }
            fileScanner.close();
            return new Grid(grid);

        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getFileFromResource(String fileName) throws URISyntaxException{
        ClassLoader classLoader = GridFileReader.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }
}
