package com.example.brawlheroes.engine;

import com.example.brawlheroes.Consts;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Level {
    public static void loadLevel(World world, String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            world.setFloor(world.getLoader().getFloorImage());
            int row = -1;
            int column = 0;
            while(reader.ready()) {
                String str = reader.readLine();
                if(!str.startsWith(";")) {
                    row++;
                    column = -1;
                    for(String tile : str.split(" ")) {
                        column++;
                        if(tile.equals("w")) {
                            world.getObjects().add(new Entity(new Point2D(Consts.TILE_SIZE * column, Consts.TILE_SIZE * row),
                                    new Rectangle2D(0,0, Consts.TILE_SIZE, Consts.TILE_SIZE),
                                    world.getLoader().getWallImage()));
                        }
                    }
                } else {

                }
            }
            world.setMapSize(new Point2D((column + 1) * Consts.TILE_SIZE, (row + 1) * Consts.TILE_SIZE));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
