import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends AnimationableEntity implements Transformable, Schedulable, Moveable, Pathable, Executable{
    private double actionPeriod;
    private int resourceLimit;
    public double getActionPeriod() {
        return actionPeriod;
    }
    public int getResourceLimit() {
        return resourceLimit;
    }
    public DudeFull(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int resourceLimit){
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
        this.resourceLimit = resourceLimit;
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(), List.of(House.class));

        if (fullTarget.isPresent() && move(world, fullTarget.get(), scheduler)) {
            transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }
    // don't technically need resource count ... full
    public static Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeFull(id, position, images, actionPeriod, animationPeriod, resourceLimit);
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
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), getAnimationPeriod());
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(getPosition(),
                destPos,
                pos-> world.withinBounds(pos) && !world.isOccupied(pos) && !(world.getOccupancyCell(pos) instanceof Stump),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? getPosition() : path.get(0);
    }
}
