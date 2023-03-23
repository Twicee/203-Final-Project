/**
 * The current entity moves in a manner as specified by the implementation
 */
public interface Pathable {
    Point nextPosition(WorldModel world, Point destPos);
}
