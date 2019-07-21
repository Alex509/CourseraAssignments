import edu.princeton.cs.algs4.Point2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;


public class KdTreeTest {

    @Test
    public void insertIntoKdTree() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.0, 0.625));
        kdTree.insert(new Point2D(0.5, 0.625));
        kdTree.insert(new Point2D(0.625, 0.75));
        kdTree.insert(new Point2D(0.25, 0.375));
        kdTree.insert(new Point2D(0.125, 0.75));
        kdTree.insert(new Point2D(0.75, 0.75));
        kdTree.insert(new Point2D(0.75, 0.75));
        Assertions.assertEquals(kdTree.size(), 6);
    }

    @Test
    public void contains() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.5, 0.5));
        Assertions.assertEquals(kdTree.contains(new Point2D(0.65, 0.5)), false);
    }

    @Test
    public void callOnEmptyTree() {
        KdTree kdTree = new KdTree();
        Point2D point2D = new Point2D(0.5, 0.5);
        Assertions.assertThrows(NoSuchElementException.class, () -> kdTree.nearest(point2D));
    }

    @Test
    public void path() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        Point2D query = new Point2D(0.89, 0.75);
        kdTree.nearest(query);
//        A  0.7 0.2
//        B  0.5 0.4
//        C  0.2 0.3
//        D  0.4 0.7
//        E  0.9 0.6
    }
}
