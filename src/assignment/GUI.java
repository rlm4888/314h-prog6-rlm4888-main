package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GUI {
    public static Visualization visualization = new Visualization();

    public static void main(String[] args) {
    //     Triangle triangle = new Triangle(new Point2D.Double(1000, 30), new Point2D.Double(30, 50), new Point2D.Double(50, 60));
    //     Triangle triangle2 = new Triangle(new Point2D.Double(100, 50), new Point2D.Double(50, 50), new Point2D.Double(50, 100));
    //     Triangle triangle3 = new Triangle(new Point2D.Double(2, 2), new Point2D.Double(3, 1), new Point2D.Double(4, 2));
    //     Triangle triangle4 = new Triangle(new Point2D.Double(4, 5), new Point2D.Double(5, 4), new Point2D.Double(5, 5));
    //     Triangle triangle5 = new Triangle(new Point2D.Double(2, 5), new Point2D.Double(5, 4), new Point2D.Double(5, 5));

    // List<Shape> shapeList = new ArrayList<>();
    //     shapeList.add(triangle);
    //     shapeList.add(triangle2);
    //     shapeList.add(triangle3);
    //     shapeList.add(triangle4);
    //     visualization.boundingVolumeHierarchy.buildBVH(shapeList);
    // BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();

    //  Point2D.Double origin = new Point2D.Double(0, 1);
    // Vector2D rayVector = new Vector2D(1, 0);
    // Shape intersectedShape = bvh.intersectRay(origin, rayVector);
    // System.out.println(intersectedShape);
    // if (intersectedShape != null) {
    //     System.out.println("Intersection found with shape: " + intersectedShape.toString());
    // } else {
    //     System.out.println("No intersection found.");
    // }
 
//    BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();
// Point2D.Double point1 = new Point2D.Double(0,0);
// Point2D.Double point2 = new Point2D.Double(1,0);

// Point2D.Double point3 = new Point2D.Double(1,2);
// Triangle triangle1 = new Triangle(point1, point2, point3);
// point1 = new Point2D.Double(1, 5);
// point2 = new Point2D.Double(2, 3);
// point3 = new Point2D.Double(3, 5);
// Triangle triangle2 = new Triangle(point1, point2, point3);
// point1 = new Point2D.Double(2, 2);
// point2 = new Point2D.Double(3, 1);
// point3 = new Point2D.Double(4, 2);
// Triangle triangle3 = new Triangle(point1, point2, point3);
// point1 = new Point2D.Double(4, 5);
// point2 = new Point2D.Double(5, 4);
// point3 = new Point2D.Double(5, 5);
// Triangle triangle4 = new Triangle(point1, point2, point3);
// ArrayList<Shape> shapesList = new ArrayList<Shape>();
// shapesList.add(triangle1);
// shapesList.add(triangle2);
// shapesList.add(triangle3);
// shapesList.add(triangle4);
// bvh.buildBVH(shapesList);
// System.out.println(bvh.toString());

// point1 = new Point2D.Double(3,2);
// point2 = new Point2D.Double(2,3);

// point3 = new Point2D.Double(3,3);
// Triangle triangle5 = new Triangle(point1, point2, point3);
// System.out.println("got here");
// bvh.insert(triangle5);
// System.out.println("After insertion: ");
// System.out.println(bvh.toString());

// point1 = new Point2D.Double(5,0);
// point2 = new Point2D.Double(4,1);
// point3 = new Point2D.Double(5,1);
// Triangle triangle6 = new Triangle(point1, point2, point3);
// bvh.insert(triangle6);
// System.out.println("After insertion: ");
// System.out.println(bvh.toString());

// Point2D.Double point1 = new Point2D.Double(0,0);
// Point2D.Double point2 = new Point2D.Double(0,2);
// Point2D.Double point3 = new Point2D.Double(2,2);
// Triangle triangle1 = new Triangle(point1, point2, point3);
// point1 = new Point2D.Double(1,4);
// point2 = new Point2D.Double(0,5);
// point3 = new Point2D.Double(2,5);
// Triangle triangle2 = new Triangle(point1, point2, point3);
// point1 = new Point2D.Double(4,1);
// point2 = new Point2D.Double(5,1);

// point3 = new Point2D.Double(5,0);
// Triangle triangle3 = new Triangle(point1, point2, point3);
// ArrayList<Shape> shapesList = new ArrayList<Shape>();
// shapesList.add(triangle1);
// shapesList.add(triangle2);
// shapesList.add(triangle3);
// bvh.buildBVH(shapesList);
// bvh.remove(triangle2);
// bvh.remove(triangle3);
// System.out.println(bvh.toString());
// point1 = new Point2D.Double(4,2);
// point2 = new Point2D.Double(3,4);
// point3 = new Point2D.Double(4,4);
// Triangle triangle5 = new Triangle(point1, point2, point3);
// bvh.insert(triangle5);
// System.out.println("After insertion: ");
// visualization.boundingVolumeHierarchy.buildBVH(shapesList);
// System.out.println(bvh.toString());


        JFrame frame = new JFrame("BVH Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        BVHVisualization bvhVisualization = new BVHVisualization();
        frame.add(bvhVisualization);
        frame.setVisible(true);
        frame.setResizable(false);
}
}
class BVHVisualization extends JPanel {
    public BVHVisualization() {
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (GUI.visualization.moveSquare(e.getKeyChar())) {
                    repaint();
                }
            }
        });
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        LinkedList<Point> points = GUI.visualization.drawGUI();
        for (Point point : points) {
            g.fillRect(point.x, point.y, 1, 1);
        }

        g.setColor(Color.BLUE);
        g.fillRect(190, 190, 21, 21);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
}
