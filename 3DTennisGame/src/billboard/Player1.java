package billboard;


import static billboard.Player.State.*;
import infra.View;
import infra.Keyboard;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class Player1 extends Player {
    
    protected boolean served;
    protected  boolean ballHit;
    
    public Player1(View view, Side side) {
        super(view, side);
    }

    @Override
    public void start() {
        // idle / moving
        addFrame("player_idle_0.png");
        addFrame("player_idle_1.png");
        
        // hitting
        addFrame("player_hit_0.png"); // 2
        addFrame("player_hit_1.png"); // 3
        addFrame("player_hit_2.png"); // 4
        addFrame("player_hit_3.png"); // 5
        addFrame("player_hit_4.png"); // 6
        addFrame("player_hit_5.png"); // 7

        // service
        addFrame("player_service_0.png"); // 8
        addFrame("player_service_1.png"); // 9
        addFrame("player_service_2.png"); // 10
        addFrame("player_service_3.png"); // 11
        
        worldX = 240;
        worldZ = 222;
        worldWidth = 20;
        
        startService();
    }
    
    @Override
    public void startService() {
        super.startService();
        currentFrame = 8;
        ball.hold();
        hitEnabled = true;
    }
    
    @Override
    public void updateService() {
        if (Keyboard.isKeyPressed(KeyEvent.VK_A) || Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
            worldZ -= 3;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_D) || Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
            worldZ += 3;
        } 

        if (Keyboard.isKeyPressedOnce(KeyEvent.VK_SPACE)) {
            ball.throwUp();
            ball.service();
            ball.worldY = 21;
            state = SERVICE2;
            served = false;
            hitBall = false;
            return;
        } 
        
        // limit player horizontal movement
        if (worldZ < 232) worldZ = 232;
        if (worldZ > 319) worldZ = 319;
        
        ball.worldX = worldX;
        ball.worldZ = worldZ + 8;
        ball.worldY = 20;
    }
    
    private boolean hitBall;
    private boolean hitBall2;
    
    @Override
    public void updateService2() {
        if (!served && Keyboard.isKeyPressedOnce(KeyEvent.VK_SPACE)) {
            served = true;
            if (ball.worldY > 40 && ball.worldY < 55) {
                hitBall = true;
                hitBall2 = false;
            }
            else {
                hitBall = false;
                hitBall2 = false;
            }
            state = SERVICE3;
        }
        else if (!served && ball.worldY <= 20) {
            ball.worldY = 20;
            ball.hold();
            state = SERVICE;
        }
    }

    @Override
    public void updateService3() {
        currentFrame += 0.34;
        
        if (hitBall && currentFrame >= 9) {
            hitBall = false;
            ball.play();
            ball.hit();
            ball.vz -= 1.0; // TODO just to throw to the left
            ball.predictBallPosition();
            ball.resetBounceCount();
            hitBall2 = true;
        }
        
        if (currentFrame >= 12) {
            currentFrame = 0;
            if (hitBall2) {
                state = IDLE;
            }
            else {
                state = UPSET;
                umpire.serviceFault(side);
            }
        }
    }

    @Override
    public void updateUpset() {
    }
    
    @Override
    public void updateIdle() {
        boolean moving = false;
        
        if (Keyboard.isKeyPressed(KeyEvent.VK_S) || Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
            worldX -= 3;
            moving = true;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_W) || Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
            worldX += 3;
            moving = true;
        }
        
        if (Keyboard.isKeyPressed(KeyEvent.VK_A) || Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
            worldZ -= 3;
            moving = true;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_D) || Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
            worldZ += 3;
            moving = true;
        }        
        
        // idle animation
        if (moving || true) {
            currentFrame += 0.1;
            if (currentFrame >= 2) {
                currentFrame = 0;
            }
        }
        
        if (Keyboard.isKeyPressedOnce(KeyEvent.VK_SPACE) && hitEnabled) {
            ballHit = false;
            state = State.HITTING;
            currentFrame = 5;
        }

        // restart game
        if (Keyboard.isKeyPressedOnce(KeyEvent.VK_ENTER)) {
            ball.hit();
            ball.predictBallPosition();
            ball.resetBounceCount();
        }
        
    }

    @Override
    public void updateHitting() {
        // pode girar a raquete e ficar no vacuo xD
        if (!ballHit && collidesWithBall()) {
            ballHit = true;
            ball.hit();
            ball.predictBallPosition();
            ball.resetBounceCount();
        }
        
        currentFrame += 0.34;
        if (currentFrame >= 8) {
            currentFrame = 0;
            state = IDLE;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g); 
        if (collidesWithBall()) {
            g.setColor(Color.RED);
            g.drawString("COLLIDES !", 150, 10);
        }
    }
    
}
