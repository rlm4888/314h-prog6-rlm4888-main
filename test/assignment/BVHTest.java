// package assignment;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;

// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import assignment.BVH.SplitMethod;

// import java.awt.geom.Point2D;


// /* 
//  * Any comments and methods here are purely descriptions or suggestions.
//  * This is your test file. Feel free to change this as much as you want.
//  */


// public class BVHTest {

//     // This will run ONCE before all other tests. It can be useful to setup up
//     // global variables and anything needed for all of the tests.
//     @BeforeAll
//     static void setupAll() {

//     }

//     // This will run before EACH test.
//     @BeforeEach
//     void setupEach() {

//     }

//     // You can test your BVH here
//     @Test
//     void testBVH() {
//         @Test
//         public void testBuildBVH() {
//             BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();
    
//             // Creating a list of triangles for building BVH
//             List<Triangle> triangles = new ArrayList<>();
//             triangles.add(new Triangle(new Point(0, 0, 0),
//                                        new Point(1, 0, 0),
//                                        new Point(0, 1, 0))); // Replace with your Triangle constructor
    
//             triangles.add(new Triangle(new Point(0, 0, 0),
//                                        new Point(0, 1, 0),
//                                        new Point(0, 0, 1))); // Replace with your Triangle constructor
    
//             // Building BVH from the list of triangles
//             bvh.buildBVH(triangles);
    
//             // Add comprehensive tests to ensure BVH is built correctly
    
//             // Test the size of the BVH
//             assertEquals(2, bvh.getSize());
//         }
//     }
// //        BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();
// //         bvh.setSplitMethod(SplitMethod.SPLIT_MEDIAN);

// //         List<Shape> shapeList = new ArrayList<>();
// //         shapeList.add(new Triangle(
// //                 new Point2D.Double(1, 1),
// //                 new Point2D.Double(2, 3),
// //                 new Point2D.Double(4, 1))
// //         );
// //         shapeList.add(new Triangle(
// //                 new Point2D.Double(5, 5),
// //                 new Point2D.Double(6, 7),
// //                 new Point2D.Double(8, 5))
// //         );
// //         shapeList.add(new Triangle(
// //                 new Point2D.Double(9, 9),
// //                 new Point2D.Double(10, 11),
// //                 new Point2D.Double(12, 9))
// //         );

// //         //bvh.buildBVH(shapeList);

// //         // Test the root
// //         assertEquals(bvh.root_.shape.getCenter(), new Point2D.Double(6, 7));

// //         // Test the left child
// //         assertEquals(bvh.root_.left.shape.getCenter(), new Point2D.Double(2, 3));

// //         // Test the right child
// //         assertEquals(bvh.root_.right.shape.getCenter(), new Point2D.Double(10, 11));
// //     }





// //     // You can test your Visualization here
// //     @Test
// //     void testVisualization() {
        
// //     }

// // }