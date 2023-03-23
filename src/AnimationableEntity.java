import processing.core.PImage;

import java.util.List;

public class AnimationableEntity extends Entity{
    private double animationPeriod;
    public double getAnimationPeriod() {
        return animationPeriod;
    }
    public AnimationableEntity(String id, Point position, List<PImage> images, double animationPeriod){
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }
}
