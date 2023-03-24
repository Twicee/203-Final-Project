import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Zombie extends AnimationableEntity implements Schedulable, Moveable, Pathable, Executable{
    private double actionPeriod;
    public static final String ZOMBIE_KEY = "zombie";
    public static final String SLUDGE_KEY = "Sludge";
    public double getActionPeriod() {
        return actionPeriod;
    }
    public Zombie(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> humanPrey = world.findNearest(getPosition(), List.of(DudeNotFull.class, DudeFull.class));

        if (humanPrey.isEmpty() || !move(world, humanPrey.get(), scheduler)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
        }
    }
    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            ((HealthAnimationableEntity)target).setHealth(0);
        }
        Point nextPos = nextPosition(world, target.getPosition());
        if (!this.getPosition().equals(nextPos)) {
            world.moveEntity(scheduler, this, nextPos);
        }
        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(getPosition(),
                destPos,
                pos-> world.withinBounds(pos)&& !world.isOccupied(pos),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? getPosition() : path.get(0);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), getAnimationPeriod());
    }
    public static Entity createZombie(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Zombie(id, position, images, actionPeriod, animationPeriod);
    }
}
