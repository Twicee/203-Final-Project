import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Entity implements Transformable<Boolean>, Executable, Moveable{
    public DudeNotFull(String id, Point position, List<PImage> images,
                       int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Activity activity = new Activity(this, world, imageStore);
        Optional<Entity> target = world.findNearest(getPosition(), Tree.class);

        if (target.isEmpty() || !move(world, target.get(), scheduler) || !transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
        }
    }
    public Boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (getResourceCount() >= getResourceLimit()) {
            Entity dude = DudeFull.createDudeFull(getId(), getPosition(), getActionPeriod(), getAnimationPeriod(), getResourceLimit(), getImages());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            ((DudeFull)dude).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            setResourceCount(getResourceCount()+1);
            target.setHealth(getHealth()-1);
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
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(getPosition(),
                destPos,
                pos-> world.withinBounds(pos) && !world.isOccupied(pos) && !(world.getOccupancyCell(pos) instanceof Stump),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? getPosition() : path.get(0);
//        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
//        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
//            int vert = Integer.signum(destPos.getY() - getPosition().getY());
//            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
//                newPos = getPosition();
//            }
//        }
//
//        return newPos;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Activity activity = new Activity(this, world, imageStore);
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, animation.createAnimationAction(0), getAnimationPeriod());
    }
}
