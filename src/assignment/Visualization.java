package assignment;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Set;

public class Visualization {
    BoundedVolumeHierarchy boundingVolumeHierarchy = new BoundedVolumeHierarchy();
    private Point2D.Double state;

    /**
     * Initialize state of square to be at (0, 0)
     */
    public Visualization () {
        // create a point at 0,0
        state = new Point2D.Double(0,0);
    }

    /**
     * @param boundingVolumeHierarchy        the BVH to set the boundedVolumeHierarchy to
     */
    public void setBoundingVolumeHierarchy(BoundedVolumeHierarchy boundingVolumeHierarchy) {
        this.boundingVolumeHierarchy = boundingVolumeHierarchy;
    }

    /**
     * Moves the square within the environment if it's able to move in the given direction
     *
     * @param keyPressed        the character of the keyPressed
     * @return              if the environment should be redrawn or not
     */
    boolean moveSquare (char keyPressed) {
        int x = (int) state.getX();
        int y = (int) state.getY();
        keyPressed = Character.toLowerCase(keyPressed);
        
        if (keyPressed== 'w') {
            y--;
        }
        if (keyPressed== 'a') {
            x--;
        }
        if (keyPressed== 's') {
            y++;
        }
        if (keyPressed== 'd') {
            x++;
        }

        boolean collides = false;

        for (int a = x - 10; a < x + 11; a++){
            for (int b = y-10; b<y+11; b++) {
                boolean noCollision = boundingVolumeHierarchy.findCollision(new Point2D.Double(a, b)).isEmpty();
                collides = collides || !noCollision;
            }
        }

        if (collides == true) {
            //System.out.println("Key is invalid");
        }
        if (collides==false) {
            //System.out.println("Key pressed");
            state = new Point2D.Double(x, y);
        }

        return !collides;
    }

    /**
     * Calculates all the pixels visible from the square in the center
     *
     * @return              All points that should be drawn other than the square
     */
    LinkedList<Point> drawGUI() {
        // double for loops
        LinkedList<Point> vis = new LinkedList<>();

        if (boundingVolumeHierarchy == null) {
            return vis;
        }

        //System.out.println(state);

        int stateX = (int) state.getX();
        int stateY = (int) state.getY();

        for (int x = stateX - 200; x < stateX + 201; x++) {
            for (int y = stateY - 200; y < stateY + 201; y++) {
                Set<Shape> allCollisions = boundingVolumeHierarchy.findCollision(new Point2D.Double(x,y));
                if(!allCollisions.isEmpty()) {
                    int newX = 200 - stateX + x;
                    int newY = 200 - stateY + y;
                    vis.add(new Point(newX, newY));
                }
            }
        }
        return vis;
    }
}
