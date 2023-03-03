import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Entity implements Transformable <Void>, Executable, Moveable{
    public DudeFull(String id, Point position, List<PImage> images,
                    int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Activity activity = new Activity(this, world, imageStore);
        Optional<Entity> fullTarget = world.findNearest(getPosition(), House.class);

        if (fullTarget.isPresent() && move(world, fullTarget.get(), scheduler)) {
            transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
        }
    }
    public Void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = DudeNotFull.createDudeNotFull(getId(), getPosition(), getActionPeriod(), getAnimationPeriod(), getResourceLimit(), getImages());

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        ((DudeNotFull)dude).scheduleActions(scheduler, world, imageStore);
        return null;
    }
    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPositionDude(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public Point nextPositionDude(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
            int vert = Integer.signum(destPos.getY() - getPosition().getY());
            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
                newPos = getPosition();
            }
        }

        return newPos;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Activity activity = new Activity(this, world, imageStore);
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, animation.createAnimationAction(0), getAnimationPeriod());
    }
}
