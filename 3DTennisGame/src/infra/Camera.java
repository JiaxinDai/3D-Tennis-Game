package infra;

import java.awt.Point;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class Camera {
    
    public static View view;
    public static Billboard followPlayer;
    public static double cameraHeight = 400;
    public static double cameraX = 0;
    public static double cameraZ = 0;
    
    static {
        cameraX = 0; //222 + 0;
        cameraZ = -1194.25;

        cameraX = 780;
        cameraZ = -414;        
    }
    
    public static void setView(View view) {
        Camera.view = view;
    }
    
    public static void follow(Billboard player) {
        followPlayer = player;
    }
    
    public static void update() {
//        if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
//            cameraX += 10;
//        }
//        else if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
//            cameraX -= 10;
//        }
//        
//        if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
//            cameraZ += 10;
//        }
//        else if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
//            cameraZ -= 10;
//        }        
        
        // camera height 
        if (Keyboard.isKeyPressed(KeyEvent.VK_R)) {
            cameraHeight += 10;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_F)) {
            cameraHeight -= 10;
        }        
        
        // camera followPlayer
        Point p = view.convertScreenPositionToTexture(128, 120);
        double difZ = (-300 + (followPlayer.worldX - p.getX()) - cameraZ) * 0.05;
        double difX = (670 + 10 * (followPlayer.worldZ - p.getY()) - cameraX) * 0.05;
        if (Math.abs(difZ) < 0.5) difZ = 0;
        if (Math.abs(difX) < 0.5) difX = 0;
        cameraZ = cameraZ + difZ;
        cameraX = cameraX + difX;
        //System.out.println(" camerax:" + cameraX);
        if (cameraX < 470) cameraX = 470;
        if (cameraX > 890) cameraX = 890;
        //System.out.println("cameraz: " + cameraZ + " camerax:" + cameraX);
        //System.out.println("playerZ: " + followPlayer.worldZ + " playerx:" + followPlayer.worldX);
        //System.out.println(p + "\n");
    }
    
}
