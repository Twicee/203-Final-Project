/**
 * An action that can be taken by an entity
 */
public final class Activity implements Action{
    private final Executable entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(Executable entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public void executeAction(EventScheduler scheduler) {
        entity.execute(this.world, this.imageStore, scheduler);
    }
    public static Action createActivityAction(Executable entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }

}

