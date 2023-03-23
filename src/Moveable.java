/**
 * The current entity moves to a target position
 */
public interface Moveable {
    boolean move(WorldModel world, Entity target, EventScheduler scheduler);
}
