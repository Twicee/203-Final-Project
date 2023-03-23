/**
 * Able to define an event that is an Action
 */
public interface Schedulable {
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
