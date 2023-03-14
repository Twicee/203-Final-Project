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
        Map<Point, Node> closedList = new HashMap<>();
        Queue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        List<Point> computedPath = new ArrayList<>();
        openList.add(startNode);
        while(!openList.isEmpty()){
            currNode = openList.remove();
            if(withinReach.test(currNode.getPosition(), end)){
                return path(computedPath, currNode);
            }
            if(closedList.containsKey(currNode.getPosition())){
                if(currNode.getF() > closedList.get(currNode.getPosition()).getF()){
                    continue;
                }
            }
            List<Point> neighbors = potentialNeighbors.apply(currNode.getPosition())
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end)) //maybe delete
                    .toList();
            for (Point point : neighbors){
                if (closedList.containsKey(point)){
                    Node temp = new Node(currNode.getG() + 1, heuristic(point, end), currNode, point);
                    if(temp.getF() > closedList.get(point).getF()){
                        continue;
                    }
                }
                openList.add(new Node(currNode.getG() + 1, heuristic(point, end), currNode, point));
            }
            closedList.put(currNode.getPosition(), currNode);
        }
        return path(computedPath, currNode);
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