import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segmentsArray = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        Double slope = null;
        double currentSlope;
        Point point;
        Point[] copy = points.clone();

        int sliceStart;
        int sliceEnd;
        for (Point currentPoint : points) {
            slope = null;
            sliceStart = 1;
            Arrays.sort(copy, currentPoint.slopeOrder());
            for (int i = 1; i < copy.length; i++) {
                point = copy[i];
                if (currentPoint.compareTo(point) == 0 && point != currentPoint) {
                    throw new IllegalArgumentException();
                }
                currentSlope = currentPoint.slopeTo(point);
                if (slope == null) {
                    slope = currentSlope;
                } else if (Double.compare(currentSlope, slope) != 0 || i == copy.length - 1) {
                    if (i == copy.length - 1) {
                        sliceEnd = i;
                    } else {
                        sliceEnd = i - 1;
                    }
                    if (sliceEnd - sliceStart + 2 >= 4) {
                        Point[] slice = Arrays.copyOfRange(copy, sliceStart, sliceEnd + 1);
                        Arrays.sort(slice);
                        if (currentPoint.compareTo(slice[0]) < 0) {
                            this.segmentsArray.add(new LineSegment(currentPoint, slice[slice.length - 1]));
                        }
                    }
                    sliceStart = i;
                    slope = currentSlope;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
