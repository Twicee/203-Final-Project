import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends AnimationableEntity implements Schedulable, Moveable, Pathable, Executable{
    private double actionPeriod;
    private Animation animation;
    private Activity activity;
    private static final String FAIRY_KEY = "fairy";
    private static final int FAIRY_ANIMATION_PERIOD = 0;
    private static final int FAIRY_ACTION_PERIOD = 1;
    private static final int FAIRY_NUM_PROPERTIES = 2;
    public double getActionPeriod() {
        return actionPeriod;
    }
    public Fairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), getAnimationPeriod());
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.getPosition(), List.of(Stump.class));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (move(world, fairyTarget.get(), scheduler)) {

                Entity sapling = Sapling.createSapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, Functions.getImageList(imageStore, Sapling.SAPLING_KEY), 0);

                world.addEntity(sapling);
                ((Sapling)sapling).scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }
    public boolean move(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public static Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, images, actionPeriod, animationPeriod);
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
}
