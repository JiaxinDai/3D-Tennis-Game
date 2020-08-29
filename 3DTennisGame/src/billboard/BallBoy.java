package billboard;


import infra.View;
import infra.Billboard;

/**
 *
 * @author admin
 */
public class BallBoy extends Billboard {
    
    public BallBoy(View view, int wx, int wz) {
        super(view);
        worldX = wx; // 680;
        worldZ = wz; //223;
    }

    @Override
    public void start() {
        addFrame("ballboy.png");
        worldWidth = 24;
    }

    @Override
    public void update() {
    }

}
