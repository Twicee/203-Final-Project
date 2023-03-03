/**
 * An action that can be taken by an entity
 */
public final class Activity implements Action {
    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this.entity, world, imageStore);
    }
    public void executeAction(EventScheduler scheduler) {
        if(this.entity instanceof Sapling){
            ((Sapling) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        else if(this.entity instanceof Tree){
            ((Tree) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        else if(this.entity instanceof Fairy){
            ((Fairy) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        else if(this.entity instanceof DudeNotFull){
            ((DudeNotFull) this.entity).execute(this.world, this.imageStore, scheduler);
        }
        else if(this.entity instanceof DudeFull){
            ((DudeFull) this.entity).execute(this.world, this.imageStore, scheduler);
        } else {
            throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.entity));
        }
    }
}

