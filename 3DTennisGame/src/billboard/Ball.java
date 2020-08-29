package billboard;

import static billboard.Player.Side.*;
import static billboard.Ball.State.*;
import infra.Audio;
import infra.View;
import infra.Billboard;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author admin
 */
public class Ball extends Billboard {
    
    public static enum State { HOLDING, SERVICE, PLAYING }
    protected State state = SERVICE;
    
    public double vx;
    public double vz;
    public double vy;
    public double worldY;
    
    private double previousX;
    private double previousY;
    private double previousZ;
    
    private int bounceCount;
    private Umpire umpire;
    
    public Ball(View view) {
        super(view);
    }

    public Umpire getUmpire() {
        return umpire;
    }

    public void setUmpire(Umpire umpire) {
        this.umpire = umpire;
    }

    public int getBounceCount() {
        return bounceCount;
    }

    public void resetBounceCount() {
        this.bounceCount = 0;
    }

    @Override
    public void start() {
        addFrame("ball.png");
        worldX = 240;
        worldZ = 222;
        worldWidth = 5;
    }
    
    public void hold() {
        state = HOLDING;
    }
    
    public void play() {
        state = PLAYING;
    }

    public void service() {
        state = SERVICE;
    }

    @Override
    public void update() {
        switch (state) {
            case HOLDING:
                updateHolding();
                break;
            case SERVICE:
                updateService();
                break;
            case PLAYING:
                updatePlaying();
                break;
        }
    }
    
    public void updateHolding() {
    }
    
    public void updateService() {
        previousY = worldY;
        worldY = worldY + vy;
        if (worldY < 0) {
            if (Math.abs(vy) > 1) {
                //Audio.stopSound("ball_bounce");
                //Audio.playSound("ball_bounce");
            }
            worldY = 0;
            vy = -vy  * 0.8;
            if (Math.abs(vy) < 0.75) vy = 0;
        }
        else {
            vy -= 0.25;
            vy = vy * 0.99;
        }
    }
    
    public void updatePlaying() {
        previousX = worldX;
        previousY = worldY;
        previousZ = worldZ;
        
        worldX = worldX + vx;
        worldZ = worldZ + vz;
        worldY = worldY + vy;
        
        // check net ?
        if ((previousX < 370 && worldX >= 370) ||
            (previousX > 370 && worldX <= 370)) {
            
            if (previousY < 16) {
                worldX = previousX;
                vx = -vx;
                vx = vx * 0.1;
                
                if (worldX > 370) {
                    umpire.playingNet(TOP);
                }
                else {
                    umpire.playingNet(DOWN);
                }
                
            }
            
        }
        
        if (worldY < 0) {
            worldY = 0;
            vy = -vy  * 0.8;
            if (Math.abs(vy) < 0.75) vy = 0;
            bounceCount++;
            if (Math.abs(vy) > 1) {
                //Audio.stopSound("ball_bounce");
                //Audio.playSound("ball_bounce");
            }
            
            // TODO: falta checar quando
            //       o jogador rebate a bola e pinga na sua propria quadra
            //       precisa armazenar algo como lastHitPlayer (?)
            if (bounceCount > 1) {
                
                if (worldX > 370) {
                    umpire.playingBounceMoreThan2Times(TOP);
                }
                else {
                    umpire.playingBounceMoreThan2Times(DOWN);
                }
                resetBounceCount();
            }
        }
        else {
            vy -= 0.25;
            vy = vy * 0.99;
        }
        vx = vx * 0.98;
        vz = vz * 0.98;

        // provisory enemy AI
        //if (worldX > 488) {
        //    hit();
        //}
    }
    
    protected final Point scrZ = new Point();

    @Override
    public void draw(Graphics2D g) {
        Point p = view.convertTextureToScreenPosition(worldX, worldZ - worldWidth / 2);
        scr.setLocation(p.getX(), p.getY());
        //g.setColor(Color.BLACK);
        //g.fillOval((int) (scr.x - 1), (int) (scr.y - 1), 2, 2);

        p = view.convertTextureToScreenPosition(worldX, worldZ + worldWidth / 2);
        scr2.setLocation(p.getX(), p.getY());

        p = view.convertTextureToScreenPosition(worldX, worldZ + worldWidth / 2 + worldY);
        scrZ.setLocation(p.getX(), p.getY());
        int scrHeight = scr2.x - scrZ.x;
        //g.setColor(Color.BLACK);
        //g.fillOval((int) (scr2.x - 1), (int) (scr2.y - 1), 2, 2);
        
        // draw ball shadow
        g.setColor(Color.BLACK);
        g.drawLine(scr.x, scr.y, scr2.x, scr2.y);

        double spriteWidth = scr2.getX() - scr.getX();
        double spriteHeight = (int) (frames.get((int) currentFrame).getHeight() * (spriteWidth / (double) frames.get((int) currentFrame).getWidth()));
        g.drawImage(frames.get((int) currentFrame), (scr.x + offsetX), (int) (scr.y - spriteHeight + offsetY + scrHeight), (int) spriteWidth, (int) spriteHeight, null);
    }
    
    public void hit() {
            // forward velocity
            if (worldX > 370) {
                double k = ((worldX - 310) / 20);
                vx = -5 - k/2 - k/2 * Math.random();
            }
            else {
                vx = 11 + 4 * Math.random();
            }
            
            // left right
            if (worldZ > 222) {
                vz = - 3 * Math.random();
            }
            else {
                vz = 3 * Math.random();
            }
            
            // up down
            vy = 3 + 1 * Math.random();     
            
            Audio.playSound("ball_hit_" + (int) (3 * Math.random()));
    }
    
    public final Point predictBallPosition = new Point();

    public Point predictBallPosition() {
        double tmpWorldX = worldX;
        double tmpWorldY = worldY;
        double tmpWorldZ = worldZ;
        double tmpPreviousX;
        double tmpPreviousY;
        double tmpPreviousZ;
        double tmpVx = vx;
        double tmpVy = vy;
        double tmpVz = vz;
        int tmpBounceCount = 0;
        
        int pingCountRandom = Math.random() < 0.25 ? 1 : 2;
        
        // simulate until ball pings 1 or 2 times or velocity = 0
        while (tmpBounceCount < pingCountRandom && Math.abs(vx + vy + vz) > 0.5) {
            tmpPreviousX = tmpWorldX;
            tmpPreviousY = tmpWorldY;
            tmpPreviousZ = tmpWorldZ;

            tmpWorldX = tmpWorldX + tmpVx;
            tmpWorldZ = tmpWorldZ + tmpVz;
            tmpWorldY = tmpWorldY + tmpVy;
            
            // check net ?
            if ((tmpPreviousX < 370 && tmpWorldX >= 370) ||
                (tmpPreviousX > 370 && tmpWorldX <= 370)) {

                if (tmpPreviousY < 16) {
                    tmpWorldX = tmpPreviousX;
                    tmpVx = -tmpVx;
                    tmpVx = tmpVx * 0.1;
                }

            }

            if (tmpWorldY < 0) {
                tmpWorldY = 0;
                tmpVy = -tmpVy  * 0.8;
                if (Math.abs(tmpVy) < 0.75) tmpVy = 0;
                tmpBounceCount++;
            }
            else {
                tmpVy -= 0.25;
                tmpVy = tmpVy * 0.99;
            }
            tmpVx = tmpVx * 0.98;
            tmpVz = tmpVz * 0.98;
        }
        
        predictBallPosition.setLocation(tmpWorldX, tmpWorldZ);
        return predictBallPosition;
    }

    public void throwUp() {
        vy = 4;
    }
        
}
