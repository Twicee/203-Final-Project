public interface Executable extends Schedulable{
    void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
