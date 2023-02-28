import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {


    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;

    private Set<Entity> entities;
    public int getNumRows() {
        return numRows;
    }
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
    public int getNumCols() {
        return numCols;
    }
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public WorldModel() {

    }
    public Background[][] getBackground() {
        return background;
    }
    public void setBackground(Background[][] background) {
        this.background = background;
    }
    public Entity[][] getOccupancy() {
        return occupancy;
    }
    public void setOccupancy(Entity[][] occupancy) {
        this.occupancy = occupancy;
    }
    public Set<Entity> getEntities() {
        return entities;
    }
    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }
    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }
    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0 && pos.getX() < this.numCols;
    }
    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }
    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = getOccupant(pos);
            occupant.ifPresent(target -> removeEntity(scheduler, target));
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }
    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }
    public Optional<PImage> getBackgroundImage(Point pos) {
        if (this.withinBounds(pos)) {
            return Optional.of(getCurrentImage(getBackgroundCell(pos)));
        } else {
            return Optional.empty();
        }
    }
    public static PImage getCurrentImage(Object object) {
        if (object instanceof Background background) {
            return background.getImages().get(background.getImageIndex());
        } else if (object instanceof Entity entity) {
            return entity.getImages().get(entity.getImageIndex() % entity.getImages().size());
        } else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not supported for %s", object));
        }
    }
    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }
    public void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }
    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.getPosition());
    }
    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }
    public Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }
    public Optional<Entity> findNearest(Point pos, List<EntityKind> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (EntityKind kind : kinds) {
            for (Entity entity : this.entities) {
                if (entity.getEntityKind() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }
    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = Functions.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = Functions.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }
}
