package billboard;


import infra.View;
import infra.Billboard;
import infra.Keyboard;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class AudienceLeft extends Billboard {

    public AudienceLeft(View view) {
        super(view);
    }

    @Override
    public void start() {
        addFrame("audience_left.png");
        worldX = 675;
        worldZ = 0 - 122;
        worldWidth = 243;
        offsetY = 34;
    }

    @Override
    public void update() {
    }

}
