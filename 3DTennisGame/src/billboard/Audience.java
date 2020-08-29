package billboard;


import infra.View;
import infra.Billboard;
import infra.Keyboard;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class Audience extends Billboard {

    public Audience(View view) {
        super(view);
    }

    @Override
    public void start() {
        addFrame("audience.png");
        worldX = 820;
        worldZ = 223;
        worldWidth = 960;
    }

    @Override
    public void update() {
    }

}
