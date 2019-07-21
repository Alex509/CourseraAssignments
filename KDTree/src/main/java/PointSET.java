import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }                            // construct an empty set of points

    public boolean isEmpty() {
        return set.isEmpty();
    }                    // is the set empty?

    public int size() {
        return set.size();
    }            // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        set.add(p);
    }             // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }          // does the set contain point p?

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);

        for (Point2D p : set)
            StdDraw.point(p.x(), p.y());

    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        return set.stream().filter(rect::contains).collect(Collectors.toList());
    }            // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D resultingPoint = set.first();
        double distance = resultingPoint.distanceTo(p);
        for (Point2D point : set) {
            if (p.distanceTo(point) < distance) {
                distance = p.distanceTo(point);
                resultingPoint = point;
            }
        }
        return resultingPoint;
    }           // a nearest neighbor in the set to point p; null if the set is empty

    //
               // unit testing of the methods (optional)
}