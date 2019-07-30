import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segmentsArray = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        double slope1;
        double slope2;
        double slope3;
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0 && i != j) {
                    throw new IllegalArgumentException();
                }
                for (int k = 0; k < points.length; k++) {
                    for (int m = 0; m < points.length; m++) {
                        slope1 = points[i].slopeTo(points[j]);
                        slope2 = points[i].slopeTo(points[k]);
                        slope3 = points[i].slopeTo(points[m]);
                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope2, slope3) == 0) {
                            if (points[i].compareTo(points[j]) < 0
                                    && points[j].compareTo(points[k]) < 0
                                    && points[k].compareTo(points[m]) < 0) {
                                this.segmentsArray.add(new LineSegment(points[i], points[m]));
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.segmentsArray.size();
    }

    public LineSegment[] segments() {
        return this.segmentsArray.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
