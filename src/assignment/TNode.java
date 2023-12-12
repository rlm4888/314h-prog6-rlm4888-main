// package assignment;

// import java.awt.geom.Point2D;

// public class TNode extends BVHNode {
//     private Triangle triangle;

//     public TNode(Shape shape) {
//         if (!(shape instanceof Triangle)) {
//             throw new IllegalArgumentException();
//         }

//         this.triangle = (Triangle) shape;
//     }

//     @Override
//     BVHNode getLeft() {
//         return null;
//     }

//     @Override
//     BVHNode getRight() {
//         return null;
//     }

//     @Override
//     void setLeft(BVHNode node) {
//         return;
//     }

//     @Override
//     void setRight(BVHNode node) {
//         return;
//     }

//     @Override
//     boolean isLeaf() {
//         return true;
//     }

//     @Override
//     boolean containsPoint(Point2D.Double point) {
//         return this.triangle.containsPoint(point);
//     }

//     @Override
//     public Shape getShape() {
//         return this.triangle;
//     }

//     @Override
//     public String toString() {
//         StringBuilder sb = new StringBuilder();

//         for (int i = 0; i < height; i++) {
//             sb.append("\t");
//         }

//         sb.append(triangle.toString());

//         return sb.toString();
//     }

//     @Override
//     double bbArea(Shape shape) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'BBAreaDelta'");
//     }

//     @Override
//     void bbRecal() {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'recomputeBB'");
//     }

//     @Override
//     void depthRecal() {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'recomputeSubtreeDepth'");
//     }

//     @Override
//     void heightRecal(int change) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'recomputeHeights'");
//     }

// }
package assignment;

import java.awt.geom.Point2D;

public class TNode extends BVHNode {
    private Triangle triangle;

    public TNode(Shape shape) {
        if (!(shape instanceof Triangle)) {
            throw new IllegalArgumentException();
        }

        this.triangle = (Triangle) shape;
    }

    @Override
    BVHNode getLeft() {
        return null;
    }

    @Override
    BVHNode getRight() {
        return null;
    }

    @Override
    void setLeft(BVHNode node) {
        return;
    }

    @Override
    void setRight(BVHNode node) {
        return;
    }

    @Override
    boolean isInside() {
        return true;
    }

    @Override
    boolean containsPoint(Point2D.Double point) {
        return this.triangle.containsPoint(point);
    }

    @Override
    public Shape getShape() {
        return this.triangle;
    }

    @Override
    double bbArea(Shape shape) {
        throw new UnsupportedOperationException();
    }

    @Override
    void bbRecal() {
        throw new IllegalArgumentException();
    }

    @Override
    void depthRecal() {
        throw new IllegalArgumentException();
    }

    @Override
    void heightRecal(int change) {
        this.height += change;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            sb.append("\t");
        }

        sb.append(triangle.toString());

        return sb.toString();
    }

}