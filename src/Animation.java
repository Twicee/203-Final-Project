/**
 * An action that can be taken by an entity
 */
public final class Animation implements Action{
    private final Entity entity;
    private final int repeatCount;
    public Animation(Entity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }
    public Action createAnimationAction(int repeatCount) {
        return new Animation( this.entity, repeatCount);
    }
    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity, createAnimationAction(Math.max(this.repeatCount - 1, 0)), entity.getAnimationPeriod());
        }
    }

}

