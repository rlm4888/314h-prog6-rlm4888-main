package assignment;

import java.awt.geom.Point2D;

public class Rectangle implements Shape{

    public Point2D.Double minPos;
    public Point2D.Double maxPos;

    /**
     * Calculates if a given ray intersects the rectangle
     *
     * @param origin        the starting position of the ray
     * @param direction     a vector that starts at (0,0) to represent the direction of the ray
     * @return              if the ray intersects the rectangle or not
     */
    public boolean doesRayIntersect (Point2D.Double origin, Vector2D direction) {
        Triangle triangle = new Triangle(new Point2D.Double(minPos.getX(), minPos.getY()), new Point2D.Double(minPos.getX(), maxPos.getY()), new Point2D.Double(maxPos.getX(), minPos.getY()));
        if (triangle.findIntersection(origin, direction) != null) {
            return true;
        }

        triangle = new Triangle(new Point2D.Double(maxPos.getX(), maxPos.getY()), new Point2D.Double(minPos.getX(), maxPos.getY()), new Point2D.Double(maxPos.getX(), minPos.getY()));
        return triangle.findIntersection(origin, direction) != null;
    }

    @Override
    public Point2D.Double getMinSurroundingPoint() {
        return minPos;
    }

    @Override
    public Point2D.Double getMaxSurroundingPoint() {
        return maxPos;
        //return new Point2D.Double(maxPos.getX(), maxPos.getY());
    }

    @Override
    public Point2D.Double getCenter() {
        double centerX = (minPos.getX() + maxPos.getX()) / 2.0;
        double centerY = (minPos.getY() + maxPos.getY()) / 2.0;
        return new Point2D.Double(centerX, centerY);
    }

    @Override
    public Point2D.Double findIntersection(Point2D.Double origin, Vector2D direction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsPoint(Point2D.Double point) {
        if (point.getX() < minPos.getX() || point.getX() > maxPos.getX()) {
            return false;
        }

        if (point.getY() < minPos.getY() || point.getY() > maxPos.getY()) {
            return false;
        }

        return true;
    }
}
