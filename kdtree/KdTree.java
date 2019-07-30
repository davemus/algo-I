/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private class Node {
        private Point2D point;
        private boolean byX;
        private Node parent;
        private Node right;
        private Node left;

        public Node(Point2D point, Node parent) {
            this.point = point;
            this.parent = parent;
            if (parent == null) {
                this.byX = true;
            }
            else {
                this.byX = !parent.isByX();
            }
        }

        public boolean isByX() {
            return byX;
        }

        public void setByX(boolean byX) {
            this.byX = byX;
        }

        public Point2D getPoint() {
            return point;
        }

        public void setPoint(Point2D point) {
            this.point = point;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public int compareToPoint(Point2D p) {
            if (this.byX) {
                return (int) Math.signum(this.getPoint().x() - p.x());
            }
            else {
                return (int) Math.signum(this.getPoint().y() - p.y());
            }
        }

        public boolean pointGoesLeft(Point2D p) {
            return this.compareToPoint(p) >= 0;
        }

        public boolean subTreeContains(Point2D p) {
            if (p.equals(this.getPoint())) {
                return true;
            }
            boolean goesLeft = this.pointGoesLeft(p);
            if (goesLeft) {
                return this.getLeft() != null && this.getLeft().subTreeContains(p);
            }
            else {
                return this.getRight() != null && this.getRight().subTreeContains(p);
            }
        }

        private void recDraw(double xmin, double ymin, double xmax, double ymax) {
            if (this.byX) {
                StdDraw.setPenColor(255, 0, 0);
                StdDraw.line(this.point.x(), ymin, this.point.x(), ymax);
                StdDraw.setPenColor(0, 0, 0);
                StdDraw.filledCircle(this.point.x(), this.point.y(), 0.01);
                if (this.getLeft() != null) {
                    this.getLeft().recDraw(xmin, ymin, this.point.x(), ymax);
                }
                if (this.getRight() != null) {
                    this.getRight().recDraw(this.point.x(), ymin, xmax, ymax);
                }
            }
            else {
                StdDraw.setPenColor(0, 0, 255);
                StdDraw.line(xmin, this.point.y(), xmax, this.point.y());
                StdDraw.setPenColor(0, 0, 0);
                StdDraw.filledCircle(this.point.x(), this.point.y(), 0.01);
                if (this.getLeft() != null) {
                    this.getLeft().recDraw(xmin, ymin, xmax, this.point.y());
                }
                if (this.getRight() != null) {
                    this.getRight().recDraw(xmin, this.point.y(), xmax, ymax);
                }
            }
        }
    }

    private Node tree;

    public KdTree() {
        this.tree = null;
    }

    public boolean isEmpty() {
        return tree == null;
    }

    private int patchSize(Node node) {
        if (node == null) {
            return 0;
        }
        else {
            return 1 + this.patchSize(node.left) + this.patchSize(node.right);
        }
    }

    public int size() {
        return this.patchSize(this.tree);
    }

    private void insertRec(Node n, Point2D p) {
        boolean goLeft = n.pointGoesLeft(p);
        if (goLeft) {
            if (n.getLeft() == null) {
                n.setLeft(new Node(p, n));
            }
            else {
                this.insertRec(n.getLeft(), p);
            }
        }
        else {
            if (n.getRight() == null) {
                n.setRight(new Node(p, n));
            }
            else {
                this.insertRec(n.getRight(), p);
            }
        }
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.tree == null) {
            this.tree = new Node(p, null);
        }
        else {
            this.insertRec(this.tree, p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.tree != null && this.tree.subTreeContains(p);
    }

    public void draw() {
        this.tree.recDraw(0, 0, 1, 1);
    }

    private List<Point2D> recRange(Node node, RectHV rect, RectHV curr) {
        List<Point2D> it = new LinkedList<>();
        RectHV n;
        if (node == null) {
            return it;
        }
        if (rect.contains(node.point)) {
            it.add(node.point);
        }
        if (node.byX) {
            n = new RectHV(curr.xmin(), curr.ymin(), node.point.x(), curr.ymax());
            if (rect.intersects(n)) {
                it.addAll(this.recRange(node.getLeft(), rect, n));
            }
            n = new RectHV(node.point.x(), curr.ymin(), curr.xmax(), curr.ymax());
            if (rect.intersects(n)) {
                it.addAll(this.recRange(node.getRight(), rect, n));
            }
        }
        else {
            n = new RectHV(curr.xmin(), curr.ymin(), curr.xmax(), node.point.y());
            if (rect.intersects(n)) {
                it.addAll(this.recRange(node.getLeft(), rect, n));
            }
            n = new RectHV(curr.xmin(), node.point.y(), curr.xmax(), curr.ymax());
            if (rect.intersects(n)) {
                it.addAll(this.recRange(node.getRight(), rect, n));
            }
        }
        return it;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        return this.recRange(this.tree, rect, new RectHV(0, 0, 1, 1));
    }

    private Point2D nearestRec(Node node, Point2D p, RectHV curr, Point2D closest) {
        if (node == null) {
            return closest;
        }
        if (closest == null) {
            closest = node.point;
        }
        if (node.point.distanceTo(p) < closest.distanceTo(p)) {
            closest = node.point;
        }
        RectHV rr;
        if (node.byX) {
            rr = new RectHV(curr.xmin(), curr.ymin(), node.point.x(), curr.ymax());
            if (rr.distanceTo(p) < closest.distanceTo(p)) {
                closest = this.nearestRec(node.getLeft(), p, rr, closest);
            }
            rr = new RectHV(node.point.x(), curr.ymin(), curr.xmax(), curr.ymax());
            if (rr.distanceTo(p) < closest.distanceTo(p)) {
                closest = this.nearestRec(node.getRight(), p, rr, closest);
            }
        }
        else {
            rr = new RectHV(curr.xmin(), curr.ymin(), curr.xmax(), node.point.y());
            if (rr.distanceTo(p) < closest.distanceTo(p)) {
                closest = this.nearestRec(node.getLeft(), p, rr, closest);
            }
            rr = new RectHV(curr.xmin(), node.point.y(), curr.xmax(), curr.ymax());
            if (rr.distanceTo(p) < closest.distanceTo(p)) {
                closest = this.nearestRec(node.getRight(), p, rr, closest);
            }
        }
        return closest;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.tree == null) {
            return null;
        }
        return this.nearestRec(this.tree, p, new RectHV(0, 0, 1, 1), null);
    }

    public static void main(String[] args) {
    }
}
