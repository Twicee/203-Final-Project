import processing.core.PImage;

import java.util.List;

public class Sapling extends Entity implements Transformable<Boolean>, Executable{
    public static final String SAPLING_KEY = "sapling";
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public Sapling(String id, Point position, List<PImage> images,
                    int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public static Entity createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position, images, 0, 0, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Activity activity = new Activity(this, world, imageStore);
        setHealth(getHealth()+1);
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
        } else if (getHealth() >= getHealthLimit()) {
            Entity tree = Tree.createTree(Tree.TREE_KEY + "_" + getId(), getPosition(), Functions.getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN), Functions.getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN), Functions.getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN), Functions.getImageList(imageStore, Tree.TREE_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            ((Tree)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getClass() == Sapling.class) {
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
