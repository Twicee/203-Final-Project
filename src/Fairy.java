import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends Entity implements Executable, Moveable{
    public Fairy(String id, Point position, List<PImage> images,
                 int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Activity activity = new Activity(this, world, imageStore);
        Optional<Entity> fairyTarget = world.findNearest(getPosition(), Stump.class);

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (move(world, fairyTarget.get(), scheduler)) {

                Entity sapling = Sapling.createSapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, Functions.getImageList(imageStore, Sapling.SAPLING_KEY), 0);

                world.addEntity(sapling);
                ((Sapling)sapling).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
    }
    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionFairy(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public Point nextPositionFairy(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(getPosition(),
                destPos,
                pos-> world.withinBounds(pos )&& !world.isOccupied(pos),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? getPosition() : path.get(0);
//        int horiz = Integer.signum(destPos.getX() - getPosition().getX());
//        Point newPos = new Point(getPosition().getX() + horiz, getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.getY() - getPosition().getY());
//            newPos = new Point(getPosition().getX(), getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
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
