package assignment;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;

public interface BVH {
    public static enum SplitMethod {
        SPLIT_MIDRANGE,
        SPLIT_MEDIAN,
        SPLIT_SURFACE_AREA
    }

    public static final SplitMethod SPLIT_DEFAULT = SplitMethod.SPLIT_MIDRANGE;

    /**
     * Changes the current split method that buildBVH() uses
     *
     * @param splitMethod   the new split method that buildBVH() uses
     */
    void setSplitMethod (SplitMethod splitMethod);

    /**
     * Builds a BVH of the triangles contained in buildBVH
     *
     * @param shapeList   the list of triangles that the BVH should contain
     */
    void buildBVH (List<Shape> shapeList);

    /**
     * Inserts the triangle provided and balances according to AVL properties
     *
     * @param shape   the new triangle to insert
     */
    void insert (Shape shape);

    /**
     * Removes the triangle provided and balances according to AVL properties
     *
     * @param shape   the triangle to remove
     */
    void remove (Shape shape);

    /**
     * Finds objects in the BVH where a point would be inside the object
     *
     * @param point   the point to detect collisions against
     * @return        a set of triangles that contains the passed in point
     */
    Set<Shape> findCollision (Point2D.Double point);

    /**
     * Finds the first triangle that a ray would hit
     *
     * @param origin        the starting position of the ray
     * @param direction     a vector that starts at (0,0) to represent the direction of the ray
     * @return              the triangle that the given origin and direction first intersects with
     */
    Shape intersectRay (Point2D.Double origin, Vector2D direction);

    /**
     * A human-readable version of a BVH.
     *
     * The bounding box should be represented as [(min_X, min_Y), (max_X, max_Y)]
     * Triangles should be represented as (a_X, a_Y), (b_X, b_Y), (c_X, c_Y)
     *
     * @return string representation of a BVH
     */
    String toString ();


    /**
     * Calculates the distance between two distinct points
     *
     * @param p1 The first point
     * @param p2 The second point
     * @return the distance between the two points
     */
    static double distanceBetweenPoints(Point2D.Double p1, Point2D.Double p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
