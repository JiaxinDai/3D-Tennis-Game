package billboard;


import static billboard.Player.State.*;
import infra.View;
import infra.Keyboard;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * CPU
 * 
 * @author admin
 */
public class Player2 extends Player {

    private boolean hitBall;
    
    public Player2(View view, Side side) {
        super(view, side);
    }

    @Override
    public void start() {
        addFrame("player2_idle_0.png"); // 0
        addFrame("player2_idle_1.png");
        
        addFrame("player2_hit_0.png"); // 2
        addFrame("player2_hit_1.png");
        addFrame("player2_hit_2.png");
        addFrame("player2_hit_3.png");
        worldWidth = 34;
        startReceive();
    }
    
    @Override
    public void startReceive() {
        super.startReceive();
        currentFrame = 0;
    }

    private boolean moving = false;
    @Override
    public void updateIdle() {
        moving = false;
        
        if (ball.vx > 0) { // || ball.worldX > 370) {
            moveToTargetPosition(ball.predictBallPosition.x, ball.predictBallPosition.y);
            
            if (collidesWithBall() && hitEnabled) {
                hitBall = false;
                currentFrame = 2;
                state = HITTING;
            }
        }

        
//        if (moving) {
//            currentFrame += 0.5;
//            if (currentFrame >= 2) {
//                currentFrame = 0;
//            }
//        }
    }

    private void moveToTargetPosition(double targetX, double targetZ) {
        double difX = targetX - worldX;
        if ( Math.abs(difX) > 3) {
            if (difX > 0) {
                worldX += 1 + Math.random();
                moving = true;
            }
            else {
                worldX -= 2 + Math.random();
                moving = true;
            }
        }

        double difZ = targetZ - worldZ;
        if (Math.abs(difZ) > 3) {
            if (difZ > 0) {
                worldZ += 2 + Math.random();
                moving = true;
            }
            else {
                worldZ -= 2 + Math.random();
                moving = true;
            }
        }        
    }
    
    @Override
    public void updateHitting() {
        currentFrame += 0.34;
        
        if (!hitBall && currentFrame >= 2.25) {
            hitBall = true;
            ball.hit();
            ball.resetBounceCount();
        }
        
        if (currentFrame >= 6) {
            currentFrame = 0;
            state = IDLE;
        }
    }
    
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g); 
        if (collidesWithBall()) {
            g.setColor(Color.BLUE);
            g.drawString("COLLIDES !", 150, 10);
        }
    }
    
}
