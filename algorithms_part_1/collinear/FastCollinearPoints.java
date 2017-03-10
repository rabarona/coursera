import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*************************************************************************
 *
 * Created by Ricardo Barona on 3/8/17
 *
 * Compilation: javac FastCollinearPoints.java
 * Execution:   java FastCollinearPoints inputfile.txt
 *
 *  Where input file contains the number of total points in the first line
 *  and then for each point a new line of x y values. Example input3.txt
 *
 *  3
 *  16000 16000
 *  10000 10000
 *  20000 20000
 *
 *  Dependencies: Point.java, algs4.jar
 *
 *  Program that examines 4 points at a time and checks whether they all lie
 *  on the same line segment, returning all such line segments. Brute force
 *  implementation.
 *
 *
 *************************************************************************/

public class FastCollinearPoints {

    private InnerLineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(final Point[] points) {

        final Point[] pointsTmp;
        final Point[] pointsLocal;

        final ArrayList<InnerLineSegment> lineSegmentArrayList = new
                ArrayList<>();

        if (points == null)
            throw new NullPointerException("input points can't" +
                    " be null");
        pointsLocal = new Point[points.length];
        System.arraycopy(points, 0, pointsLocal, 0, points.length);

        try {
            Arrays.sort(pointsLocal);
        } catch (NullPointerException nullPointerException) {
            // Catch null pointer exception when array contains a null point
            throw new NullPointerException("Points can't be null");
        }

        // Check for duplicates
        for (int i = 0; i < pointsLocal.length; i++) {

            // Get current point
            Point point = pointsLocal[i];

            // Look for a duplicate Point
            if (i + 1 < pointsLocal.length) {
                if (point.compareTo(pointsLocal[i + 1]) == 0) {
                    throw new IllegalArgumentException("There is a repeated " +
                            "point in argument points " + "Iteration before " +
                            "finding duplicated: " + i);
                }
            }
        }

        pointsTmp = new Point[points.length];

        // Copy parameter array to this.points
        System.arraycopy(points, 0, pointsTmp, 0, points.length);

        // Check for collinear points
        for (int i = 0; i < pointsLocal.length; i++) {

            Point point = pointsLocal[i];
            List<PointSlope> pointsSlopeArrayList = new ArrayList<>();
            List<Point> pointsArrayList = new ArrayList<>();

            Arrays.sort(pointsTmp, point.slopeOrder());

            for (int j = 1; j < pointsLocal.length; j++) {
                pointsSlopeArrayList
                        .add(new PointSlope(pointsTmp[j],
                                point.slopeTo(pointsTmp[j])));
            }

            PointSlope[] pointsSlope = pointsSlopeArrayList.toArray(new
                    PointSlope[pointsSlopeArrayList.size()]);


            Arrays.sort(pointsSlope);

            pointsArrayList.add(point);

            int continueCounter = 0;

            for (int j = 0; j < pointsSlope.length; j++) {

                if (continueCounter > 0) {
                    continueCounter--;
                    continue;
                }

                int tmpJ = j;

                pointsArrayList.add(pointsSlope[tmpJ].getPoint());

                while (tmpJ < pointsSlope.length) {

                    if (!(tmpJ + 1 >= pointsSlope.length)) {
                        if (Double.compare(pointsSlope[tmpJ].getSlope(),
                                pointsSlope[tmpJ + 1].getSlope()) == 0) {
                            pointsArrayList.add(pointsSlope[tmpJ + 1].getPoint());
                        } else {
                            break;
                        }
                    }
                    tmpJ++;
                }

                // check if it made it to more than 3
                if (pointsArrayList.size() > 3) {
                    Point[] temp = pointsArrayList.toArray(new
                            Point[pointsArrayList.size()]);
                    Arrays.sort(temp);

                    InnerLineSegment lineSegment =
                            new InnerLineSegment(temp[0], temp[temp.length -
                                    1]);

                    if (!containsLS(lineSegment)) {
                        lineSegmentArrayList.add(lineSegment);

                        this.lineSegments = lineSegmentArrayList
                                .toArray(new
                                        InnerLineSegment[lineSegmentArrayList
                                        .size()]);
                    }

                    pointsArrayList = new ArrayList<>();
                    pointsArrayList.add(point);
                    continueCounter = tmpJ - j;
                } else {
                    pointsArrayList = new ArrayList<>();
                    pointsArrayList.add(point);
                }
            }

            if (pointsArrayList.size() > 3) {
                Point[] temp = pointsArrayList.toArray(new
                        Point[pointsArrayList.size()]);
                Arrays.sort(temp);

                InnerLineSegment lineSegment =
                        new InnerLineSegment(temp[0], temp[temp.length - 1]);

                if (!containsLS(lineSegment)) {
                    lineSegmentArrayList.add(lineSegment);

                    this.lineSegments = lineSegmentArrayList
                            .toArray(new InnerLineSegment[lineSegmentArrayList.size()]);
                }
            }
        }
    }

    public static void main(String[] args) {

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

    // the number of line segments
    public int numberOfSegments() {
        if (this.lineSegments != null) return this.lineSegments.length;
        return 0;
    }

    // the line segments
    public LineSegment[] segments() {
        if (this.lineSegments != null) {
            LineSegment[] returnLineSegments = new LineSegment[this.lineSegments.length];
            for (int i = 0; i < this.lineSegments.length; i++) {
                returnLineSegments[i] = new LineSegment(this.lineSegments[i]
                        .p, this.lineSegments[i].q);
            }
            return returnLineSegments;
        } else return new LineSegment[0];

    }

    private boolean containsLS(InnerLineSegment thatLS) {
        if (this.lineSegments != null) {
            for (int i = 0; i < this.lineSegments.length; i++) {
                InnerLineSegment thisLS = this.lineSegments[i];
                if (thisLS.equals(thatLS))
                    return true;
            }
        }
        return false;
    }

    private static class PointSlope implements Comparable<PointSlope> {

        private Point point;
        private double slope;

        public PointSlope(Point point, double slope) {
            this.point = point;
            this.slope = slope;
        }

        public double getSlope() {
            return slope;
        }

        public Point getPoint() {
            return point;
        }

        @Override
        public int compareTo(PointSlope o) {
            return Double.compare(this.slope, o.slope);
        }

    }

    private class InnerLineSegment {
        private Point p;
        private Point q;

        InnerLineSegment(Point p, Point q) {
            if (p == null || q == null) {
                throw new NullPointerException("argument is null");
            }
            this.p = p;
            this.q = q;
        }

        @Override
        public boolean equals(Object ls) {
            if (ls == null) return false;
            if (!(ls.getClass() == this.getClass())) return false;

            InnerLineSegment that = (InnerLineSegment) ls;
            return this.p.equals(that.p) && this.q.equals(that.q);
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException();
        }
    }
}
