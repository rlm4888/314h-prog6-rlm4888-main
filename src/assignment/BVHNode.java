package assignment;

import java.awt.geom.Point2D;

public abstract class BVHNode {
    abstract BVHNode getLeft();
    abstract BVHNode getRight();
    abstract void setLeft(BVHNode node);
    abstract void setRight(BVHNode node);
    abstract double bbArea(Shape shape);
    abstract void bbRecal();
    abstract void depthRecal();
    abstract void heightRecal(int change);
 


    abstract boolean isInside();

    abstract boolean containsPoint(Point2D.Double point);

    abstract Shape getShape();

    public int height;

    public int lDepth;
    public int rDepth;

    public int getBalance() {
        return lDepth - rDepth;
    }
}