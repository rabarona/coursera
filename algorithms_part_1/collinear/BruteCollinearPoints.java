import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

/*************************************************************************
 *
 *  Created by Ricardo Barona 02/12/2017
 *
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints inputfile.txt
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
 *************************************************************************/

public class BruteCollinearPoints {

    private InnerLineSegment[] lineSegments;

    // Finds all line segments containing 4 points
    public BruteCollinearPoints(final Point[] points) {

        // To check whether the 4 points p, q, r, and s are collinear, check
        // whether the three slopes between p and q, between p and r, and
        // between p and s are all equal.

        final Point[] pointsTmp;
        final Point[] pointsLocal;

        final ArrayList<InnerLineSegment> lineSegmentArrayList =
                new ArrayList<>();

        if (points == null)
            throw new NullPointerException("input points can't" +
                    " be null");

        pointsTmp = new Point[points.length];
        pointsLocal = new Point[points.length];

        // Copy parameter array to pointsTmp
        System.arraycopy(points, 0, pointsTmp, 0, points.length);
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

        for (int i = 0; i < pointsTmp.length; i++) {

            Point point = pointsLocal[i];

            ArrayList<Point> pointsArrayList = new ArrayList<>();
            pointsArrayList.add(point);

            Arrays.sort(pointsTmp, point.slopeOrder());

            int continueCounter = 0;

            for (int k = 1; k < pointsTmp.length - 2; k++) {

                if (continueCounter > 0) {
                    continueCounter--;
                    continue;
                }

                pointsArrayList.add(pointsTmp[k]);

                int h = k + 1;

                while (h < pointsTmp.length) {
                    if (Double.compare(point.slopeTo(pointsTmp[k]),
                            point.slopeTo(pointsTmp[h])) == 0) {
                        pointsArrayList.add(pointsTmp[h]);

                        h++;
                    } else {
                        if (pointsArrayList.size() > 3) {
                            Point[] temp = pointsArrayList.toArray(new
                                    Point[pointsArrayList.size()]);
                            Arrays.sort(temp);

                            InnerLineSegment lineSegment =
                                    new InnerLineSegment(temp[0], temp[temp
                                            .length - 1]);

                            if (!containsLS(lineSegment)) {
                                lineSegmentArrayList.add(lineSegment);

                                this.lineSegments = lineSegmentArrayList
                                        .toArray(new
                                                InnerLineSegment[lineSegmentArrayList.size()]);
                            }

                            pointsArrayList = new ArrayList<>();
                            pointsArrayList.add(point);
                            continueCounter = h - k - 1;
                        } else {
                            pointsArrayList = new ArrayList<>();
                            pointsArrayList.add(point);
                        }
                        break;
                    }
                }
            }

            // last case, when the last element was collinear
            if (pointsArrayList.size() > 3) {
                Point[] temp = pointsArrayList
                        .toArray(new Point[pointsArrayList.size()]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
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
        } else {
            return new LineSegment[0];
        }
    }

    private static class InnerLineSegment {
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
