import processing.core.PImage;

import java.util.List;

public class House extends Entity{
    public House(String id, Point position, List<PImage> images,
                    int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images, 0, 0, 0, 0, 0, 0);
    }
}
