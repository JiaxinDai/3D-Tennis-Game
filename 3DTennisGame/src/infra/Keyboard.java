package infra;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author admin
 */
public class Keyboard implements KeyListener {

    public static boolean[] keys = new boolean[256];
    public static boolean[] keysConsumed = new boolean[256];

    public static boolean isKeyPressed(int keyCode) {
        return keys[keyCode];
    }
    
    public static boolean isKeyPressedOnce(int keyCode) {
        if (!keysConsumed[keyCode] && keys[keyCode]) {
            keysConsumed[keyCode] = true;
            return true;
        }
        return false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        keysConsumed[e.getKeyCode()] = false;
    }
    
}
