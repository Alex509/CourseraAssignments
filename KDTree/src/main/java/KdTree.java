import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private Node champion;

    public KdTree() {
    }

    private enum Division {
        HORIZONTAL,
        VERTICAL;

        public Division getNext() {
            return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
        }
    }

    private class Node {
        private Point2D val;
        private Node left, right;
        private int size;
        private Division division;

        public Node(final Point2D val, final int size, final Division division) {
            this.val = val;
            this.size = size;
            this.division = division;
        }
    }

    /**
     *
     * @param val
     */
    public void insert(final Point2D val) {
        if (val == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        root = put(root, val, Division.VERTICAL);
    }

    /**
     *
     * @param node
     * @param point
     * @param division
     * @return
     */
    private Node put(final Node node, final Point2D point, final Division division) {
        if (node == null) {
            return new Node(point, 1, division);
        }
        if (isSameNodeValue(node.val, point)) {
            return node;
        }
        if (isLeftChild(node, point, division)) {
            node.left = put(node.left, point, division.getNext());

        } else if (isRightNode(node, point, division)) {
            node.right = put(node.right, point, division.getNext());
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    /**
     *
     * @param node
     * @param point
     * @param division
     * @return
     */
    private boolean isRightNode(final Node node, final Point2D point, final Division division) {
        return division == Division.VERTICAL && point.x() > node.val.x()
                || division == Division.HORIZONTAL && point.y() > node.val.y();
    }

    /**
     *
     * @param node
     * @param point
     * @param division
     * @return
     */
    private boolean isLeftChild(final Node node, final Point2D point, final Division division) {
        return division == Division.VERTICAL && point.x() <= node.val.x()
                || division == Division.HORIZONTAL && point.y() <= node.val.y();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     *
     * @return
     */
    public int size() {
        return size(root);
    }

    /**
     *
     * @param node
     * @return
     */
    private int size(final Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    /**
     *
     * @param node
     * @param point
     * @return
     */
    private Point2D get(final Node node, final Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return null;
        }
        if (isSameNodeValue(point, node.val)) {
            return node.val;
        }
        if (isLeftChild(node, point, node.division)) {
            return get(node.left, point);
        } else {
            return get(node.right, point);
        }
    }

    /**
     *
     * @param point
     * @param val
     * @return
     */
    private boolean isSameNodeValue(final Point2D point, final Point2D val) {
        return Double.valueOf(point.x()).equals(val.x()) && Double.valueOf(point.y()).equals(val.y());
    }

    /**
     *
     * @param point
     * @return
     */
    public boolean contains(final Point2D point) {
        return get(root, point) != null;
    }

    /**
     *
     */
    public void draw() {
    }

    /**
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(final RectHV rect) {
        final List<Point2D> foundPoints = new ArrayList<>();
        if (root == null) {
            return foundPoints;
        }
        range(root, rect, foundPoints);
        return foundPoints;
    }

    /**
     *
     * @param rect
     * @param point
     * @param division
     * @return
     */
    private int rectIntersects(final RectHV rect, final Point2D point, final Division division) {
        if (division == Division.VERTICAL) {
            if (rect.xmin() < point.x() && rect.xmax() < point.x()) {
                return -1;
            } else if (rect.xmax() > point.x() && rect.xmin() > point.x()) {
                return 1;
            }
            return 0;
        } else {
            if (rect.ymin() < point.y() && rect.ymax() < point.y()) {
                return -1;
            } else if (rect.ymax() > point.y() && rect.ymin() > point.y()) {
                return 1;
            }
            return 0;
        }
    }

    /**
     *
     * @param node
     * @param rect
     * @param foundPoints
     */
    private void range(final Node node, final RectHV rect, final List<Point2D> foundPoints) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.val)) {
            foundPoints.add(node.val);
        }
        if (rectIntersects(rect, node.val, node.division) > 0) {
            range(node.right, rect, foundPoints);
        } else if (rectIntersects(rect, node.val, node.division) < 0) {
            range(node.left, rect, foundPoints);
        } else {
            range(node.right, rect, foundPoints);
            range(node.left, rect, foundPoints);
        }

    }

    /**
     * @param query
     * @return
     */
    public Point2D nearest(final Point2D query) {
        if (root == null) {
            return null;
        }
        nearest(root, query);
        return champion.val;
    }

    private void nearest(final Node node, final Point2D query) {
        if (node == null) {
            return;
        }
        if (champion == null) {
            champion = node;
        }
        if (node.val.distanceSquaredTo(query) < champion.val.distanceSquaredTo(query)) {
            champion = node;
        }
        if (node.division == Division.VERTICAL) {
            if (query.x() < node.val.x()) {
                nearest(node.left, query);
                if (champion.val.distanceTo(query) + query.x() >= node.val.x()) {
                    nearest(node.right, query);
                }
            } else {
                nearest(node.right, query);
                if (query.x() - champion.val.distanceTo(query) <= node.val.x()) {
                    nearest(node.left, query);

                }
            }
        } else {// if horizontal
            if (query.y() <= node.val.y()) {
                nearest(node.left, query);
                if (champion.val.distanceTo(query) + query.y() >= node.val.y()) {
                    nearest(node.right, query);
                }
            } else {
                nearest(node.right, query);
                if (query.y() - champion.val.distanceTo(query) <= node.val.y()) {
                    nearest(node.left, query);
                }
            }
        }
    }


    public static void main(final String[] args) {


    }
}
