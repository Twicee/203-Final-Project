public interface Transformable<T> {
    T transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
