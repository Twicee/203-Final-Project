/**
 * An action that can be taken by an entity
 */
public final class Animation implements Action{
    private final AnimationableEntity entity;
    private final int repeatCount;

    public Animation(AnimationableEntity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity, createAnimationAction(this.entity, Math.max(this.repeatCount - 1, 0)), entity.getAnimationPeriod());
        }
    }
    public static Action createAnimationAction(AnimationableEntity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }
}

