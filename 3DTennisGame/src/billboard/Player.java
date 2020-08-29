package billboard;


import static billboard.Player.Side.*;
import static billboard.Player.State.*;
import infra.View;
import infra.Billboard;

/**
 *
 * @author admin
 */
public class Player extends Billboard {

    public static enum State { SERVICE, SERVICE2, SERVICE3, IDLE, HITTING, UPSET }
    protected State state = IDLE;
    protected Ball ball;
    
    public static enum Side { TOP, DOWN }
    protected Side side;
    protected Umpire umpire;
    
    protected boolean hitEnabled;
    
    public Player(View view, Side side) {
        super(view);
        this.side = side;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public boolean isHitEnabled() {
        return hitEnabled;
    }

    public void setHitEnabled(boolean hitEnabled) {
        this.hitEnabled = hitEnabled;
    }

    public Umpire getUmpire() {
        return umpire;
    }

    public void setUmpire(Umpire umpire) {
        this.umpire = umpire;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        switch (state) {
            case SERVICE:
                updateService();
                break;
            case SERVICE2:
                updateService2();
                break;
            case SERVICE3:
                updateService3();
                break;
            case IDLE:
                updateIdle();
                break;
            case HITTING:
                updateHitting();
                break;
            case UPSET:
                updateUpset();
                break;
        }
    }

    public void updateService() {
    }

    public void updateService2() {
    }

    public void updateService3() {
    }
        
    public void updateIdle() {
    }

    public void updateHitting() {
    }

    public void updateUpset() {
    }

    public boolean collidesWithBall() {
        return Math.abs(worldX - ball.worldX) < 16 &&
            Math.abs(0 - ball.worldY) < 32 &&
            Math.abs(worldZ - ball.worldZ) < 16;
    }
    

    public void startService() {
        if (side == TOP) {
            
        }
        else if (side == DOWN) {
            state = SERVICE;
            worldX = 118;
            worldZ = 280;
        }
    }

    public void startReceive() {
        if (side == TOP) {
            state = IDLE;
            worldX = 615;
            worldZ = 163;
            hitEnabled = true;
            ball.predictBallPosition.setLocation(worldX, worldZ);
        }
        else if (side == DOWN) {
            
        }
    }
        
    
}
