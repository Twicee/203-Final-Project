import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {
    private String id;

    private Point position;

    private List<PImage> images;

    private int imageIndex;
    private int resourceLimit;

    private int resourceCount;

    private double actionPeriod;
    private double animationPeriod;

    private int health;
    private int healthLimit;

    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }
    public List<PImage> getImages() {
        return images;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public int getResourceLimit() {
        return resourceLimit;
    }
    public int getResourceCount() {
        return resourceCount;
    }
    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }
    public double getActionPeriod() {
        return actionPeriod;
    }
    public double getAnimationPeriod() {
        return this.animationPeriod;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public int getHealthLimit() {
        return healthLimit;
    }

    public Entity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

}

