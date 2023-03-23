import processing.core.PImage;
import java.util.List;
public class Obstacle extends AnimationableEntity implements Schedulable{
    private String id;
    private Point position;
    private List<PImage> images;
    public Obstacle(String id, Point position, List<PImage> images, double animationPeriod){
        super(id, position, images, animationPeriod);
    }

    public static Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, images, animationPeriod);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), getAnimationPeriod());
    }
}
