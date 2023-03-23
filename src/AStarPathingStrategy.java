import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;

import static java.lang.Math.abs;

public class AStarPathingStrategy implements PathingStrategy{
    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Node startNode = new Node(0, heuristic(start, end), null, start);
        Node currNode = null;
        Queue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<Point, Node> openListMap = new HashMap<>();
        Map<Point, Node> closedList = new HashMap<>();
        List<Point> computedPath = new ArrayList<>();
        openList.add(startNode);
        openListMap.put(start, startNode);
        while(!openList.isEmpty()) {
            currNode = openList.remove();
            openListMap.remove(currNode.getPosition());
            if (withinReach.test(currNode.getPosition(), end)) {
                return path(computedPath, currNode);
            }
            if (closedList.containsKey(currNode.getPosition())) {
                if (currNode.getF() > closedList.get(currNode.getPosition()).getF()) {
                    continue;
                }
            }
            List<Point> neighbors = potentialNeighbors.apply(currNode.getPosition())
                    .filter(canPassThrough)
                    .filter(pt -> !closedList.containsKey(pt))
                    .toList();
            for (Point point : neighbors) {
                Node neighborNode = new Node(currNode.getG() + 1, heuristic(point, end), currNode, point);
                if (openListMap.containsKey(point)) {
                    Node openListNode = openListMap.get(point);
                    if (openListNode.getG() > neighborNode.getG()) {
                        openList.remove(openListNode);
                        openListMap.remove(point);
                        openList.add(neighborNode);
                        openListMap.put(point, neighborNode);
                    }
                } else {
                    openList.add(neighborNode);
                    openListMap.put(point, neighborNode);
                }
            }
            closedList.put(currNode.getPosition(), currNode);
        }
        return computedPath;
    }

    public int heuristic(Point point, Point goal){
        return abs(point.getX() - goal.getX()) + abs(point.getY() - goal.getY());
    }
    public List<Point> path(List<Point> computedPath, Node node){
        if (node == null) return computedPath;
        while(node.getPrevNode() != null){
            computedPath.add(node.getPosition());
            node = node.getPrevNode();
        }
        List<Point> reverseList = new ArrayList<>(computedPath);
        Collections.reverse(reverseList);
        return reverseList;
    }
}
class Node{
    private int g;
    private int h;
    private int f;
    private Node prevNode;
    private Point position;
    public Node(int g, int h, Node prevNode, Point position){
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.prevNode = prevNode;
        this.position = position;
    }

    public int getF() {
        return f;
    }
    public Point getPosition() {
        return position;
    }
    public Node getPrevNode() {
        return prevNode;
    }

    public int getG() {
        return g;
    }
}