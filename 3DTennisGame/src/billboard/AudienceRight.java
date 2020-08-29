package billboard;


import infra.View;
import infra.Billboard;
import infra.Keyboard;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class AudienceRight extends Billboard {

    public AudienceRight(View view) {
        super(view);
    }

    @Override
    public void start() {
        addFrame("audience_right.png");
        worldX = 675;
        worldZ = 454 + 122;
        worldWidth = 243;
        offsetY = 34;
    }

    @Override
    public void update() {
    }

}
