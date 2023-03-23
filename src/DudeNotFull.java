import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends AnimationableEntity implements Transformable, Schedulable, Moveable, Pathable, Executable{
    private double actionPeriod;
    private int resourceCount;
    private int resourceLimit;
    public double getActionPeriod() {
        return actionPeriod;
    }
    public int getResourceCount() {
        return resourceCount;
    }
    public int getResourceLimit() {
        return resourceLimit;
    }
    public DudeNotFull(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int resourceCount, int resourceLimit){
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.getPosition(), List.of(Tree.class, Sapling.class));
        if (target.isEmpty() || !move(world, target.get(), scheduler) || !transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
//        target = world.findNearest(this.getPosition(), Sapling.class);
//        if (target.isEmpty() || !move(world, target.get(), scheduler) || !transform(world, scheduler, imageStore)) {
//            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
//        }
    }
    public static Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeNotFull(id, position, images, actionPeriod, animationPeriod, 0, resourceLimit);
    }
    public Boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Entity dude = DudeFull.createDudeFull(this.getId(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            ((DudeFull)dude).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            this.resourceCount += 1;
            ((HealthAnimationableEntity)target).setHealth(((HealthAnimationableEntity)target).getHealth() - 1);
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
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
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
