package assignment;

import java.awt.geom.Point2D;
import java.util.Objects;

public class BoundingBox extends BVHNode {

    // Member variables
    private Rectangle boundingBox = new Rectangle();
    private BVHNode left = null;
    private BVHNode right = null;

    // Satisfying BVHNode Abstract class
    @Override
    BVHNode getLeft() {
        return this.left;
    }

    @Override
    BVHNode getRight() {
        return this.right;
    }

    @Override
    void setLeft(BVHNode node) {
        this.left = node;
    }

    @Override
    void setRight(BVHNode node) {
        this.right = node;
    }

    @Override
    boolean isInside() {
        return false;
    }

    @Override
    boolean containsPoint(Point2D.Double point) {
        return this.boundingBox.containsPoint(point);
    }

    @Override
    public Shape getShape() {
        return this.boundingBox;
    }

    @Override
    double bbArea(Shape shape) {
        double currentArea = (this.boundingBox.maxPos.x - this.boundingBox.minPos.x) * (this.boundingBox.maxPos.y - this.boundingBox.minPos.y);
        Point2D.Double currentMinPos = this.boundingBox.minPos;
        Point2D.Double currentMaxPos = this.boundingBox.maxPos;
        Point2D.Double shapeMinPos = shape.getMinSurroundingPoint();
        Point2D.Double shapeMaxPos = shape.getMaxSurroundingPoint();

        Point2D.Double newMinPos = new Point2D.Double();
        Point2D.Double newMaxPos = new Point2D.Double();
        newMinPos.x = Math.min(shapeMinPos.x, currentMinPos.x);
        newMinPos.y = Math.min(shapeMinPos.y, currentMinPos.y);
        newMaxPos.x = Math.max(shapeMaxPos.x, currentMaxPos.x);
        newMaxPos.y = Math.max(shapeMaxPos.y, currentMaxPos.y);

        double newArea = (newMaxPos.x - newMinPos.x) * (newMaxPos.y - newMinPos.y);

        return newArea - currentArea;
    }
    @Override
    void bbRecal() {
        Shape leftShape = this.getLeft().getShape();
        Shape rightShape = this.getRight().getShape();

        Point2D.Double leftMin = leftShape.getMinSurroundingPoint();
        Point2D.Double leftMax = leftShape.getMaxSurroundingPoint();
        Point2D.Double rightMin = rightShape.getMinSurroundingPoint();
        Point2D.Double rightMax = rightShape.getMaxSurroundingPoint();

        Point2D.Double newMin = new Point2D.Double();
        Point2D.Double newMax = new Point2D.Double();

        newMin.x = Math.min(leftMin.x, rightMin.x);
        newMin.y = Math.min(leftMin.y, rightMin.y);
        newMax.x = Math.max(leftMax.x, rightMax.x);
        newMax.y = Math.max(leftMax.y, rightMax.y);

        this.boundingBox.minPos = newMin;
        this.boundingBox.maxPos = newMax;
    }
    @Override
    void depthRecal() {
        if (this.getLeft().isInside()) {
            this.lDepth = 1;
        } else {
            this.lDepth = Math.max(this.getLeft().lDepth, this.getLeft().rDepth) + 1;
        }

        if (this.getRight().isInside()) {
            this.rDepth = 1;
        } else {
            this.rDepth = Math.max(this.getRight().lDepth, this.getRight().rDepth) + 1;
        }
    }
    @Override
    void heightRecal(int change) {
        this.height+=change;
        recomputeHeightHelp(this, change);
    }

    private void recomputeHeightHelp(BVHNode node, int change) {
        node.height += change;

        if (node.isInside()) return;

        recomputeHeightHelp(node.getLeft(), change);
        recomputeHeightHelp(node.getRight(), change);
    }

    public BoundingBox(BVHNode triangle1, BVHNode triangle2) {

    }

    public BoundingBox(Point2D.Double[] corners) {
        this.boundingBox.minPos = corners[0];
        this.boundingBox.maxPos = corners[1];
    }


    @Override
    public String toString() {
        Point2D.Double minPos = this.boundingBox.minPos;
        Point2D.Double maxPos = this.boundingBox.maxPos;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            sb.append("\t");
        }

        sb.append("[(").append(minPos.x).append(", ").append(minPos.y).append("), (").append(maxPos.x).append(", ").append(maxPos.y).append(")]\n");

        return sb.toString();
    }

}
