package assignment;

import java.awt.geom.Point2D;
import java.util.*;

public class BoundedVolumeHierarchy implements BVH{
     private BVHNode root = null;
    private SplitMethod splitMethod_ = SPLIT_DEFAULT;

    public static enum splitWay {
        SPLITWAY_A,
        SPLITWAY_B
    }

    @Override
    public void setSplitMethod(SplitMethod splitMethod) {
        if (splitMethod == null) {
            return;
        }
        splitMethod_ = splitMethod;
        if(splitMethod != SplitMethod.SPLIT_MIDRANGE || splitMethod != SplitMethod.SPLIT_SURFACE_AREA ||splitMethod != SplitMethod.SPLIT_MEDIAN )
        {
            splitMethod = SPLIT_DEFAULT;
        }
    }

    @Override
    public void buildBVH(List<Shape> shapeList) {
        if (shapeList == null) return;
        switch (shapeList.size()) {
            case 0:
                return;
            case 1:
                this.root = new TNode(shapeList.get(0));
                return;
            default:
                this.root = buildHelp(splitWay.SPLITWAY_A, shapeList, 0);
                return;
        }
    }

    private BVHNode buildHelp(splitWay axis, List<Shape> shapes, int height) {
        if (shapes.size() == 1) {
            BVHNode triangleNode = new TNode(shapes.get(0));
            triangleNode.height = height;
            return triangleNode;
        }

        splitWay nextsplitWay = axis == splitWay.SPLITWAY_A ? splitWay.SPLITWAY_B : splitWay.SPLITWAY_A;

        List<Shape>[] split = splitMedian(shapes, axis);

        Point2D.Double[] boundingBoxCorners = calcBoundingBox(shapes);

        BoundingBox boundingBox = new BoundingBox(boundingBoxCorners);

        boundingBox.height = height;

        boundingBox.setLeft(buildHelp(nextsplitWay, split[0], height + 1));
        boundingBox.setRight(buildHelp(nextsplitWay, split[1], height + 1));

        if (boundingBox.getLeft().isInside()) {
            boundingBox.lDepth = 1;
        } else {
            boundingBox.lDepth = Math.max(boundingBox.getLeft().lDepth + 1, boundingBox.getLeft().rDepth + 1);
        }

        if (boundingBox.getRight().isInside()) {
            boundingBox.rDepth = 1;
        } else {
            boundingBox.rDepth = Math.max(boundingBox.getRight().lDepth + 1, boundingBox.getRight().rDepth + 1);
        }

        return boundingBox;
    }

    @Override
    public void insert(Shape shape) {
        Stack<BVHNode> nodes = new Stack<>();

        // if root not set
        if (this.root == null) {
            this.root = new TNode(shape);
            return;
        }

        this.root = insertHelp(shape, shape.getCenter(), this.root, nodes);


        rebalance(nodes);
    }

    private BVHNode insertHelp(Shape shape, Point2D.Double scenter, BVHNode node, Stack<BVHNode> nodes) {
        if (node.isInside()) {

            Point2D.Double[] bbDimensions = calcBoundingBox(new Shape[]{shape, node.getShape()});

            BVHNode newBB = new BoundingBox(bbDimensions);

            newBB.height = node.height;
            node.height++;

            newBB.setLeft(node);
            BVHNode triangleNode = new TNode(shape);

            triangleNode.height = node.height;

            newBB.setRight(triangleNode);

            newBB.lDepth = 1;
            newBB.rDepth = 1;

            return newBB;
        }

        int numBBChildren = 0;
        if (!node.getLeft().isInside()) numBBChildren++;
        if (!node.getRight().isInside()) numBBChildren++;

        // handle based on how many of the children are bounding boxes
        switch (numBBChildren) {
            case 0:
                Point2D.Double leftCenter = node.getLeft().getShape().getCenter();
                Point2D.Double rightCenter = node.getRight().getShape().getCenter();

                double distanceToLeft = leftCenter.distance(scenter);
                double distanceToRight = rightCenter.distance(scenter);

                if (distanceToLeft > distanceToRight) {
                    // put into right
                    nodes.add(node);
                    node.setRight(insertHelp(shape, scenter, node.getRight(), nodes));
                    node.depthRecal();
                } else {
                    // put into left
                    nodes.add(node);
                    node.setLeft(insertHelp(shape, scenter, node.getLeft(), nodes));
                    node.depthRecal();
                }

                break;
            case 1:
                // handle if one is bounding box
                if (node.getLeft().isInside()) {
                    // put into left
                    nodes.add(node);
                    node.setLeft(insertHelp(shape, scenter, node.getLeft(), nodes));
                    node.depthRecal();
                } else {
                    nodes.add(node);
                    node.setRight(insertHelp(shape, scenter, node.getRight(), nodes));
                    node.depthRecal();
                }

                break;
            case 2:
                double lArea = node.getLeft().bbArea(shape);
                double rArea = node.getRight().bbArea(shape);

                if (Shape.isClose(lArea, rArea)) {
                    if (node.lDepth > node.rDepth) {
                        nodes.add(node);
                        node.setRight(insertHelp(shape, scenter, node.getRight(), nodes));
                        node.depthRecal();
                    } else {
                        nodes.add(node);
                        node.setLeft(insertHelp(shape, scenter, node.getLeft(), nodes));
                        node.depthRecal();
                    }
                } else if (lArea < rArea) {
                    nodes.add(node);
                    node.setLeft(insertHelp(shape, scenter, node.getLeft(), nodes));
                    node.depthRecal();
                } else if (rArea < lArea){
                    nodes.add(node);
                    node.setRight(insertHelp(shape, scenter, node.getRight(), nodes));
                    node.depthRecal();
                }
                break;
        }

        node.bbRecal();
        return node;
    }


    @Override
    public void remove(Shape shape) {
        if (this.root != null) {
            // if the root is not a bounding box
            if (this.root.isInside()) {
                if (this.root.getShape().equals(shape)) this.root = null;
                return;
            }

            Stack<BVHNode> nodes = new Stack<>();

            this.root = removeHelp(shape, this.root, nodes);

            rebalance(nodes);
        }
    }

    // should only be run on bounding boxes
    private BVHNode removeHelp(Shape shape, BVHNode node, Stack<BVHNode> nodes) {
        if (!(node instanceof BoundingBox)) {
            throw new RuntimeException(String.valueOf(node.getShape()));
        }

        Point2D.Double sCenter = shape.getCenter();

        if (!node.getShape().containsPoint(sCenter)) {
            return node;
        }

        BVHNode leftNode = node.getLeft();
        BVHNode rightNode = node.getRight();

        int amountBBChildren = 2;
        if (leftNode.isInside()) amountBBChildren--;
        if (rightNode.isInside()) amountBBChildren--;

        switch (amountBBChildren) {
            case 0:
                if (node.getLeft().getShape().equals(shape)) {
                    BVHNode rightNodeTemp = node.getRight();
                    rightNodeTemp.heightRecal(-1);
                    return rightNodeTemp;
                } else if (node.getRight().getShape().equals(shape)) {
                    BVHNode leftNodeTemp = node.getLeft();
                    leftNodeTemp.heightRecal(-1);
                    return leftNodeTemp;
                }
                break;
            case 1:
                if (node.getLeft().isInside()) {
                    // check if isInside node is shape being searched for
                    if (shape.equals(node.getLeft().getShape())) {
                        node.getRight().heightRecal(-1);
                        return node.getRight();
                    }

                    // call recursive on the bounding box in the other side
                    nodes.push(node);
                    node.setRight(removeHelp(shape, node.getRight(), nodes));
                } else if (node.getRight().isInside()) {
                    // check if isInside node is shape being searched for
                    if (shape.equals(node.getRight().getShape())) {
                        node.getLeft().heightRecal(-1);
                        return node.getLeft();
                    }

                    // call recursive on the bounding box in the other side
                    nodes.push(node);
                    node.setLeft(removeHelp(shape, node.getLeft(), nodes));
                }
                break;
            case 2:
                nodes.push(node);
                node.setRight(removeHelp(shape, node.getRight(), nodes));
                node.setLeft(removeHelp(shape, node.getLeft(), nodes));
                break;
        }

        node.depthRecal();
        node.bbRecal();
        return node;
    }

    @Override
    public Set<Shape> findCollision(Point2D.Double point) {
        HashSet<Shape> shapes = new HashSet<>();

        collisionHelp(point, shapes, this.root);

        return shapes;
    }

    private void collisionHelp(Point2D.Double point, Set<Shape> shapes, BVHNode root) {
        if (root == null) return;
        if (root.isInside()) {
            if (root.containsPoint(point)) {
                shapes.add(root.getShape());
            }
            return;
        }

        if (root.containsPoint(point)) {
            collisionHelp(point, shapes, root.getLeft());
            collisionHelp(point, shapes, root.getRight());
        }
    }

    private void rebalance(Stack<BVHNode> nodes) {
        int stackLength = nodes.size();

        for (int x = stackLength; x > 1; x--) {
            BVHNode node = nodes.pop();
            BVHNode parentNode = nodes.pop();

            rebalanceHelp(node, parentNode);

            nodes.push(parentNode);
        }

        rebalanceRoot(this.root);
    }

    private void rebalanceHelp(BVHNode node, BVHNode parentNode) {
        String lrNode = "";
        if (parentNode.getRight() == node) lrNode = "right";
        if (parentNode.getLeft() == node) lrNode = "left";


        if (node.rDepth == node.lDepth) {
            return;
        } else if (node.rDepth > node.lDepth + 1) {
            // rotate left
            BVHNode rightKid = node.getRight();
            BVHNode rightGrandKidSwitch;
            BVHNode rightGrandKidRemain;

            if (rightKid.rDepth > rightKid.lDepth) {
                rightGrandKidSwitch = rightKid.getLeft();
                rightGrandKidRemain = rightKid.getRight();
            } else {
                rightGrandKidSwitch = rightKid.getRight();
                rightGrandKidRemain = rightKid.getLeft();
            }

            // recomputing heights and subtree and BB
            rightKid.height--;
            rightGrandKidRemain.heightRecal(-1);
            node.height++;
            node.getLeft().heightRecal(1);

            rightKid.setRight(rightGrandKidRemain);
            rightKid.setLeft(node);
            node.setRight(node.getLeft());
            node.setLeft(rightGrandKidSwitch);

            node.depthRecal();
            rightKid.depthRecal();
            node.bbRecal();
            rightKid.bbRecal();

            if (lrNode.equals("right")) {
                parentNode.setRight(rightKid);
            } else if (lrNode.equals("left")) {
                parentNode.setLeft(rightKid);
            }

        } else if (node.lDepth > node.rDepth + 1) {
            // rotate right
            BVHNode leftKid = node.getLeft();
            BVHNode leftGrandKidSwitch;
            BVHNode leftGrandKidRemain;

            if (leftKid.rDepth > leftKid.lDepth) {
                leftGrandKidSwitch = leftKid.getLeft();
                leftGrandKidRemain = leftKid.getRight();
            } else {
                leftGrandKidSwitch = leftKid.getRight();
                leftGrandKidRemain = leftKid.getLeft();
            }

            leftKid.height--;
            leftGrandKidRemain.heightRecal(-1);
            node.height++;
            node.getRight().heightRecal(1);

            leftKid.setLeft(leftGrandKidRemain);
            leftKid.setRight(node);
            node.setLeft(node.getRight());
            node.setRight(leftGrandKidSwitch);

            node.depthRecal();
            leftKid.depthRecal();
            node.bbRecal();
            leftKid.bbRecal();

            if (lrNode.equals("right")) {
                parentNode.setRight(leftKid);
            } else if (lrNode.equals("left")) {
                parentNode.setLeft(leftKid);
            }
        }
    }

    private void rebalanceRoot(BVHNode node) {
        if (node.rDepth == node.lDepth) {
            return;
        } else if (node.rDepth > node.lDepth + 1) {
            // rotate left
            BVHNode rightKid = node.getRight();
            BVHNode rightGrandKidSwitch;
            BVHNode rightGrandKidRemain;

            if (rightKid.rDepth > rightKid.lDepth) {
                rightGrandKidSwitch = rightKid.getLeft();
                rightGrandKidRemain = rightKid.getRight();
            } else {
                rightGrandKidSwitch = rightKid.getRight();
                rightGrandKidRemain = rightKid.getLeft();
            }

            // recomputing heights and subtree and BB
            rightKid.height--;
            rightGrandKidRemain.heightRecal(-1);
            node.height++;
            node.getLeft().heightRecal(1);

            rightKid.setRight(rightGrandKidRemain);
            rightKid.setLeft(node);
            node.setRight(node.getLeft());
            node.setLeft(rightGrandKidSwitch);

            node.depthRecal();
            rightKid.depthRecal();
            node.bbRecal();
            rightKid.bbRecal();

            this.root = rightKid;
        } else if (node.lDepth > node.rDepth + 1) {
            // rotate right
            BVHNode leftKid = node.getLeft();
            BVHNode leftGrandKidSwitch;
            BVHNode leftGrandKidRemain;

            if (leftKid.rDepth > leftKid.lDepth) {
                leftGrandKidSwitch = leftKid.getLeft();
                leftGrandKidRemain = leftKid.getRight();
            } else {
                leftGrandKidSwitch = leftKid.getRight();
                leftGrandKidRemain = leftKid.getLeft();
            }

            leftKid.height--;
            leftGrandKidRemain.heightRecal(-1);
            node.height++;
            node.getRight().heightRecal(1);

            leftKid.setLeft(leftGrandKidRemain);
            leftKid.setRight(node);
            node.setLeft(node.getRight());
            node.setRight(leftGrandKidSwitch);

            node.depthRecal();
            leftKid.depthRecal();
            node.bbRecal();
            leftKid.bbRecal();

            this.root = leftKid;
        }
    }

    @Override
    public Shape intersectRay(Point2D.Double origin, Vector2D direction) {

        ArrayList<Shape> shapes = new ArrayList<>();
        ArrayList<Point2D.Double> points = new ArrayList<>();

        if (this.root == null) {
            return null;
        }
        else if (this.root.isInside()) {
            if (this.root.getShape().findIntersection(origin, direction)== null) {
                return null;
            }
            else {
                return this.root.getShape();
            }
        }

        else {
            intersectRayRec(origin, direction, this.root, shapes, points);
            if (shapes.size() != points.size()) {
                throw new RuntimeException();
            }
            else if (shapes.isEmpty()) {
                return null;
            }
            else if (shapes.size() == 1) {
                return shapes.get(0);
            }
            else {
                int minIndex = 0;
                Point2D.Double minP = points.get(0);
                double distance = origin.distance(minP);

                for (int i = 1; i < shapes.size(); i++) {
                    if (origin.distance(points.get(i)) < distance) {
                        minIndex = i;
                    }
                }

                return shapes.get(minIndex);
            }
        }
    }

    private void intersectRayRec(Point2D.Double origin, Vector2D direction, BVHNode node, List<Shape> shapeList, List<Point2D.Double> points) {
        if (node.isInside()) {
            Triangle tri = (Triangle) node.getShape();
            Point2D.Double triPoint = tri.findIntersection(origin, direction);

            if (triPoint != null) {
                shapeList.add(tri);
                points.add(triPoint);
            }
            return;
        }
        
        Rectangle rect = (Rectangle) node.getShape();
        if(rect.doesRayIntersect(origin, direction)) {
            intersectRayRec(origin, direction, node.getLeft(), shapeList, points);
            intersectRayRec(origin, direction, node.getRight(), shapeList, points);
        }
    }


    private List<Shape>[] splitMedian(List<Shape> shapes, splitWay axis) {
        if (axis == splitWay.SPLITWAY_A) {
            ArrayList<Shape> leftShapes = new ArrayList<>();
            ArrayList<Shape> rightShapes = new ArrayList<>();
            ArrayList<Shape> extra = new ArrayList<>();
            boolean touch = false;

            Double[] xValues = new Double[shapes.size()];

            for (int i = 0; i < shapes.size(); i++) {
                xValues[i] = shapes.get(i).getCenter().x;
            }

            Arrays.sort(xValues);
            Double medianXValue = 0.0;

            if (xValues.length % 2 == 0) {
                medianXValue = (xValues[xValues.length/2] + xValues[(xValues.length/2) - 1]) / 2;
            } else {
                medianXValue = xValues[xValues.length/2];
            }

            for (Shape shape : shapes) {
                if (shape.getCenter().x < medianXValue) {
                    leftShapes.add(shape);
                } else if (shape.getCenter().x > medianXValue) {
                    rightShapes.add(shape);
                } else if (shape.getCenter().x == medianXValue) {
                    extra.add(shape);
                }
            }

            for (Shape shape : extra) {
                if (!touch) {
                    leftShapes.add(shape);
                } else {
                    rightShapes.add(shape);
                }

                touch = (!touch);
            }

            return new List[] {leftShapes, rightShapes};
        } else if (axis == splitWay.SPLITWAY_B){
            ArrayList<Shape> top = new ArrayList<>();
            ArrayList<Shape> below = new ArrayList<>();
            ArrayList<Shape> extra = new ArrayList<>();
            boolean touch = true;

            Double[] yValues = new Double[shapes.size()];

            for (int i = 0; i < shapes.size(); i++) {
                yValues[i] = shapes.get(i).getCenter().y;
            }

            Arrays.sort(yValues);
            Double medianXValue = 0.0;

            if (yValues.length % 2 == 0) {
                medianXValue = (yValues[yValues.length/2] + yValues[(yValues.length/2) - 1]) / 2;
            } else {
                medianXValue = yValues[yValues.length/2];
            }

            for (Shape shape : shapes) {
                if (shape.getCenter().y < medianXValue) {
                    top.add(shape);
                } else if (shape.getCenter().y > medianXValue) {
                    below.add(shape);
                } else if (shape.getCenter().y == medianXValue) {
                    extra.add(shape);
                }
            }

            for (Shape shape : extra) {
                if (!touch) {
                    top.add(shape);
                } else {
                    below.add(shape);
                }

                touch = (!touch);
            }

            return new List[] {top, below};
        }
        return null;
    }
     private Point2D.Double[] calcBoundingBox(List<Shape> shapes) {
        Point2D.Double minPoint = new Point2D.Double();
        Point2D.Double maxPoint = new Point2D.Double();
    
        if (shapes.isEmpty()) throw new RuntimeException("list is empty");
    
        Double minX = null;
        Double minY = null;
        Double maxX = null;
        Double maxY = null;
    
        for (Shape shape : shapes) {
            Point2D.Double minSurroundingPoint = shape.getMinSurroundingPoint();
            Point2D.Double maxSurroundingPoint = shape.getMaxSurroundingPoint();
    
            if (minX == null) minX = minSurroundingPoint.x;
            if (minY == null) minY = minSurroundingPoint.y;
            if (maxX == null) maxX = maxSurroundingPoint.x;
            if (maxY == null) maxY = maxSurroundingPoint.y;
    
    
            minX = minSurroundingPoint.x < minX ? minSurroundingPoint.x : minX;
            minY = minSurroundingPoint.y < minY ? minSurroundingPoint.y : minY;
            maxX = maxSurroundingPoint.x > maxX ? maxSurroundingPoint.x : maxX;
            maxY = maxSurroundingPoint.y > maxY ? maxSurroundingPoint.y : maxY;
        }
    
        minPoint.x = minX;
        minPoint.y = minY;
        maxPoint.x = maxX;
        maxPoint.y = maxY;
    
        return new Point2D.Double[]{minPoint, maxPoint};
    }

    private Point2D.Double[] calcBoundingBox(Shape[] shapes) {
        Point2D.Double minPoint = new Point2D.Double();
        Point2D.Double maxPoint = new Point2D.Double();

        if (shapes.length == 0) throw new RuntimeException("array is empty");

        Double minX = null;
        Double minY = null;
        Double maxX = null;
        Double maxY = null;

        for (Shape shape : shapes) {
            Point2D.Double minSurroundingPoint = shape.getMinSurroundingPoint();
            Point2D.Double maxSurroundingPoint = shape.getMaxSurroundingPoint();

            if (minX == null) minX = minSurroundingPoint.x;
            if (minY == null) minY = minSurroundingPoint.y;
            if (maxX == null) maxX = maxSurroundingPoint.x;
            if (maxY == null) maxY = maxSurroundingPoint.y;


            minX = minSurroundingPoint.x < minX ? minSurroundingPoint.x : minX;
            minY = minSurroundingPoint.y < minY ? minSurroundingPoint.y : minY;
            maxX = maxSurroundingPoint.x > maxX ? maxSurroundingPoint.x : maxX;
            maxY = maxSurroundingPoint.y > maxY ? maxSurroundingPoint.y : maxY;
        }

        minPoint.x = minX;
        minPoint.y = minY;
        maxPoint.x = maxX;
        maxPoint.y = maxY;

        return new Point2D.Double[]{minPoint, maxPoint};
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.root == null) return "";

        preOrder(this.root, sb);

        return sb.toString();
    }

    private void preOrder(BVHNode root, StringBuilder sb) {
        sb.append(root.toString());
        if (root.isInside()) return;

        preOrder(root.getLeft(), sb);
        preOrder(root.getRight(), sb);
    }
}
