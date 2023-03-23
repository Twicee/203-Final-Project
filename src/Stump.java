import processing.core.PImage;
import java.util.List;

public class Stump extends Entity{
    public static final String STUMP_KEY = "stump";
    public Stump(String id, Point position, List<PImage> images){
        super(id, position, images);
    }
    public static Entity createStump(String id, Point position, List<PImage> images) {
        return new Stump(id, position, images);
    }

}
