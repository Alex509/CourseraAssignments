import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(final Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points cannot be null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("point cannot be null");
            }
        }
        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicate(sortedPoints);
        for (int i = 0; i < (sortedPoints.length - 3); ++i) {
            for (int j = i + 1; j < (sortedPoints.length - 2); ++j) {
                for (int k = j + 1; k < (sortedPoints.length - 1); ++k) {
                    for (int l = k + 1; l < (sortedPoints.length); ++l) {
                        if (sortedPoints[i].slopeTo(sortedPoints[j]) == sortedPoints[i].slopeTo(sortedPoints[l]) &&
                                sortedPoints[i].slopeTo(sortedPoints[j]) == sortedPoints[i].slopeTo(sortedPoints[k])) {
                            LineSegment tempLineSegment = new LineSegment(sortedPoints[i], sortedPoints[l]);
                            if (!segmentsList.contains(tempLineSegment)) {
                                segmentsList.add(tempLineSegment);
                            }
                        }
                    }
                }

            }

        }

        lineSegments = segmentsList.toArray(new LineSegment[segmentsList.size()]);

    }  // finds all line segments containing 4 points

    public int numberOfSegments() {
        return lineSegments.length;
    }      // the number of line segments

    public LineSegment[] segments() {
        return lineSegments.clone();
    }               // the line segments

    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate(s) found.");
            }
        }
    }
}