import processing.core.PImage;

import java.util.List;

public class HealthAnimationableEntity extends AnimationableEntity{
    private int health;
    public HealthAnimationableEntity(String id, Point position, List<PImage> images, double animationPeriod, int health) {
        super(id, position, images, animationPeriod);
        this.health = health;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
