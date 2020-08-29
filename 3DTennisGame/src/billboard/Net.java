package billboard;


import infra.View;
import infra.Billboard;
import infra.Keyboard;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class Net extends Billboard {

    public Net(View view) {
        super(view);
    }

    @Override
    public void start() {
        addFrame("net.png");
        worldX = 372;
        worldZ = 220;
        worldWidth = 345 - 92;
    }

    @Override
    public void update() {
    }

}
