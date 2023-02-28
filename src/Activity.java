/**
 * An action that can be taken by an entity
 */
public final class Activity implements Action{
    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    public void executeAction(EventScheduler scheduler) {
        switch (this.entity.getEntityKind()) {
            case SAPLING:
                entity.executeSaplingActivity(this.world, this.imageStore, scheduler);
                break;
            case TREE:
                entity.executeTreeActivity(this.world, this.imageStore, scheduler);
                break;
            case FAIRY:
                entity.executeFairyActivity(this.world, this.imageStore, scheduler);
                break;
            case DUDE_NOT_FULL:
                entity.executeDudeNotFullActivity(this.world, this.imageStore, scheduler);
                break;
            case DUDE_FULL:
                entity.executeDudeFullActivity(this.world, this.imageStore, scheduler);
                break;
            default:
                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.entity.getEntityKind()));
        }

    }

}

