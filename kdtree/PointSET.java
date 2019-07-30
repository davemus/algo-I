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
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> tree;

    public PointSET() {
        this.tree = new TreeSet<>();
    }

    public boolean isEmpty() {
        return this.tree.isEmpty();
    }

    public int size() {
        return this.tree.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.tree.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.tree.contains(p);
    }

    public void draw() {
        for (Point2D p : this.tree) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> inner = new LinkedList<>();
        for (Point2D p : this.tree) {
            if (rect.contains(p)) {
                inner.add(p);
            }
        }
        return inner;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D winner = null;
        double dist = Double.MAX_VALUE;
        double curr;
        for (Point2D pith : this.tree) {
            curr = p.distanceTo(pith);
            if (curr < dist) {
                winner = pith;
                dist = curr;
            }
        }
        return winner;
    }

    public static void main(String[] args) {
    }
}
