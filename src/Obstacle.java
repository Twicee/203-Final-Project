import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity{
    public Obstacle(String id, Point position, List<PImage> images,
                    int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, images, 0, 0, 0, animationPeriod, 0, 0);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, animation.createAnimationAction(0), getAnimationPeriod());
    }
}
