/**
 * Removes the current entity and adds a new one
 */
public interface Transformable <T>{
     T transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
