import processing.core.PImage;

import java.util.List;

public class House extends Entity{
    public House(String id, Point position, List<PImage> images){
        super(id, position, images);
    }
    public static Entity createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

}
