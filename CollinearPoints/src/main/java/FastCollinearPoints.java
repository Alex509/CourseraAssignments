import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> segments;
    private Point[] points;

    public FastCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points cannot be null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("point cannot be null");
            }
        }
        this.points = points.clone();
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicate(sortedPoints);
        // check to see if argument matches constraints

        this.segments = new ArrayList<>();
        LinkedList<Point> collinearPoints = new LinkedList<>();
        for (Point point : this.points) {
            Arrays.sort(sortedPoints, point.slopeOrder());
            Double prevSlope = 0.0;
            for (int j = 0; j < sortedPoints.length; j++) {
                double currentSlope = point.slopeTo(sortedPoints[j]);
                if (j == 0 || !Double.valueOf(currentSlope).equals(prevSlope)) {
                    if (collinearPoints.size() >= 3) {
                        if (!this.segments.contains(new LineSegment(collinearPoints.getLast(), collinearPoints.getFirst()))) {
                            this.segments.add(new LineSegment(collinearPoints.getFirst(), collinearPoints.getLast()));
                        }
                    }
                    collinearPoints.clear();
                }
                collinearPoints.add(sortedPoints[j]);
                prevSlope = currentSlope;
            }
        }


    }   // finds all line segments containing 4 or more points

    private void checkDuplicate(final Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate(s) found.");
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }       // the number of line segments

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }              // the line segments

    public static void main(final String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}