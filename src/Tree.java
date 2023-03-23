import processing.core.PImage;

import java.util.List;

public class Tree extends HealthAnimationableEntity implements Transformable, Schedulable, Executable {
    private double actionPeriod;
    public static final String TREE_KEY = "tree";
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    private static final int TREE_ANIMATION_PERIOD = 0;
    private static final int TREE_ACTION_PERIOD = 1;
    private static final int TREE_HEALTH = 2;
    private static final int TREE_NUM_PROPERTIES = 3;

    public double getActionPeriod() {
        return actionPeriod;
    }

    public Tree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health) {
        super(id, position, images, animationPeriod, health);
        this.actionPeriod = actionPeriod;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    public Boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Entity stump = Stump.createStump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), Functions.getImageList(imageStore, Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public static Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, images, actionPeriod, animationPeriod, health);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Activity.createActivityAction(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Animation.createAnimationAction(this, 0), getAnimationPeriod());
    }

    boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getClass() == Tree.class) {
            return ((Tree) this).transform(world, scheduler, imageStore);
        }
        return false;
    }
}