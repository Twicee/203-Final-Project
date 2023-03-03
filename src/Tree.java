import processing.core.PImage;

import java.util.List;

public class Tree extends Entity implements Transformable<Boolean>, Executable{
    public static final String TREE_KEY = "tree";
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    public Tree(String id, Point position, List<PImage> images,
                    int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, images, 0, 0, actionPeriod, animationPeriod, health, 0);
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Activity activity = new Activity(this, world, imageStore);
        if (!transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
        }
    }
    public Boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (getHealth() <= 0) {
            Entity stump = Stump.createStump(Stump.STUMP_KEY + "_" + getId(), getPosition(), Functions.getImageList(imageStore, Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getClass() == Tree.class) {
            return transform(world, scheduler, imageStore);
        }
        else{
            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
        }
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        Activity activity = new Activity(this, world, imageStore);
        Animation animation = new Animation(this, 0);
        scheduler.scheduleEvent(this, activity.createActivityAction(world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, animation.createAnimationAction(0), getAnimationPeriod());
    }
}
