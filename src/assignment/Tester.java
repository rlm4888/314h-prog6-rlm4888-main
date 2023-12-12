// package assignment; 
// import java.awt.geom.Point2D;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.LinkedList;
// import java.awt.Point;

// public class Tester {
//     public static void main(String[] args) {
//         BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();
//         System.out.println("Created BoundedVolumeHierarchy."); 

//         Point2D.Double vertex1 = new Point2D.Double(0, 0);
//         Point2D.Double vertex2 = new Point2D.Double(0, 5);
//         Point2D.Double vertex3 = new Point2D.Double(5, 0);
//         Triangle triangle = new Triangle(vertex1, vertex2, vertex3);

//         Point2D.Double vertex4 = new Point2D.Double(1, 1);
//         Point2D.Double vertex5 = new Point2D.Double(1, 6);
//         Point2D.Double vertex6 = new Point2D.Double(6, 1);
//         Triangle triangle2 = new Triangle(vertex4, vertex5, vertex6);

//         List<Shape> shapes = Arrays.asList(triangle, triangle2);
//         bvh.buildBVH(shapes);
//         System.out.println("Built BVH with triangles.");

//         //Test insert
//         Point2D.Double vertex7 = new Point2D.Double(2, 2);
//         Point2D.Double vertex8 = new Point2D.Double(2, 7);
//         Point2D.Double vertex9 = new Point2D.Double(7, 2);
//         Triangle triangle3 = new Triangle(vertex7, vertex8, vertex9);
//         bvh.insert(triangle3);
//         System.out.println(bvh.toString());
//         System.out.println("Inserted new triangle into BVH.");

//         // Test intersectRay
//         Point2D.Double rayPoint = new Point2D.Double(2, 2);
//         Vector2D rayVector = new Vector2D(1, 1);
//         Shape intersectedShape = bvh.intersectRay(rayPoint, rayVector);
//         if (intersectedShape != null) {
//             System.out.println("Intersection found with shape: " + intersectedShape.toString());
//         } else {
//             System.out.println("No intersection found.");
//         }

//         // Test findCollision
//         Point2D.Double collisionPoint = new Point2D.Double(2, 2);
//         System.out.println("Finding collisions at point: " + collisionPoint.toString());
//         for (Shape shape : bvh.findCollision(collisionPoint)) {
//             System.out.println("Found collision with shape: " + shape.toString());
//         }
//         //Edge case 1: Empty tree
//         System.out.println("Edge case 1: Testing with an empty tree.");
//         bvh.buildBVH(new ArrayList<>());
//         System.out.println("Built BVH with triangles.");

//         // Edge case 2: Single node tree
//         System.out.println("Edge case 2: Testing with a single node tree.");
//         bvh.insert(triangle);
//         System.out.println("Inserted new triangle into BVH.");

//         // Edge case 3: Specific arrangement of nodes
//         System.out.println("Edge case 3: Testing with specific arrangement of nodes.");
//         bvh.insert(triangle);
//         bvh.insert(triangle2);
//         //bvh.insert(triangle3);
//         System.out.println("Intersection found with shape: (2.0, 2.0) (2.0, 7.0) (7.0, 2.0)");
//         System.out.println("Finding collisions at point: Point2D.Double[2.0, 2.0]");
//         for (Shape shape : bvh.findCollision(new Point2D.Double(2.0, 2.0))) {
//             System.out.println("Found collision with shape: " + shape.toString());
//         }
//         //Edge Case 4: Lots of triangles
//         System.out.println("Edge case 4: Testing with a large number of triangles for buildBVH.");
//         List<Shape> largeListOfTriangles = new ArrayList<>();
//         for (int i = 0; i < 1000000; i++) {
//             Point2D.Double v1 = new Point2D.Double(i, i);
//             Point2D.Double v2 = new Point2D.Double(i + 1, i + 1);
//             Point2D.Double v3 = new Point2D.Double(i + 2, i + 2);
//             Triangle t = new Triangle(v1, v2, v3);
//             largeListOfTriangles.add(t);
//         }
//         bvh.buildBVH(largeListOfTriangles);
//         System.out.println("Built BVH with an extremely large number of triangles.");

//         //Edge Case 5: removing triangle not present in BVH
//         System.out.println("Edge case 5: Testing removal of a triangle that is not present in the BVH.");
//         Point2D.Double notPresentVertex1 = new Point2D.Double(10, 10);
//         Point2D.Double notPresentVertex2 = new Point2D.Double(10, 15);
//         Point2D.Double notPresentVertex3 = new Point2D.Double(15, 10);
//         Triangle notPresentTriangle = new Triangle(notPresentVertex1, notPresentVertex2, notPresentVertex3);
//         bvh.remove(notPresentTriangle);
//         System.out.println("Removed non-existent triangle from BVH.");

//         //Edge Case 6: no intersection
//         System.out.println("Edge case 6: Testing intersectRay method with no intersection.");
//         Point2D.Double nonIntersectingRayPoint = new Point2D.Double(10, 10);
//         Vector2D nonIntersectingRayVector = new Vector2D(1, 0);
//         Shape nonIntersectedShape = bvh.intersectRay(nonIntersectingRayPoint, nonIntersectingRayVector);
//         if (nonIntersectedShape != null) {
//             System.out.println("Intersection found with shape: " + nonIntersectedShape.toString());
//         } else {
//             System.out.println("No intersection found.");
//         }

//         // Test remove
//         bvh.remove(triangle);
//         System.out.println("Removed triangle from BVH.");
//         System.out.println("Finding collisions at point after removal: " + collisionPoint.toString());
//         for (Shape shape : bvh.findCollision(collisionPoint)) {
//             System.out.println("Found collision with shape: " + shape.toString());
//         }

//       Visualization vis = new Visualization();
//       System.out.println("Setting Bounding Volume Hierarchy.");
//       vis.setBoundingVolumeHierarchy(bvh);
//       System.out.println("Bounding Volume Hierarchy set.");

//     //   char character = 'a';
//     //   boolean result = vis.moveSquare(character);
//     //   System.out.println("Result of checking character " + character + ": " + result);

//     //   LinkedList<Point> points = vis.drawGUI();
//     //   System.out.println("Points obtained: " + points.toString());
//    }
// }


   