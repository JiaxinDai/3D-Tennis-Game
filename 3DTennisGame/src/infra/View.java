package infra;

import billboard.Audience;
import billboard.AudienceLeft;
import billboard.AudienceRight;
import billboard.Ball;
import billboard.BallBoy;
import billboard.Net;
import static billboard.Player.Side.*;
import billboard.Player1;
import billboard.Player2;
import billboard.Umpire;
import static infra.Camera.cameraHeight;
import static infra.Camera.cameraX;
import static infra.Camera.cameraZ;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class View extends Canvas {

    public static final int SCREEN_WIDTH = 256;
    public static final int SCREEN_HEIGHT = 240;
    public static final int SCALE = 2;
    
    private Thread mainLoop;
    private BufferStrategy bs;
    
    private double fov = Math.toRadians(30);
    public double d = (0.5 * SCREEN_WIDTH) / (Math.tan(0.5 * fov));
    
    // used in Floor
    public final BufferedImage frameBuffer;
    public final int ajusteY = 150; // inicia a renderizacao mais para bixo, rola a tela para baixo para poder ver a imagem pois eh renderizada laa embaixo por causa da altura muito alta da camera
    public final double scale = 3;
    
    private List<Entity> entities = new ArrayList<>();
    
    private Player1 player1;
    private Player2 player2;
    private Ball ball;
    private Umpire umpire;
    
    public View() {
        frameBuffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void start() {
        createBufferStrategy(2);
        bs = getBufferStrategy();
        Audio.start();
        addKeyListener(new Keyboard());
        createAllEntities();
        startAllEntities();
        startMainLoop();
    }
    private void createAllEntities() {
        entities.add(new Floor(this));
        entities.add(new Audience(this));
        entities.add(new BallBoy(this, 780, 223));
        entities.add(new BallBoy(this, 780, 223 - 100));
        entities.add(new BallBoy(this, 780, 223 + 100));
        entities.add(new BallBoy(this, 780, 223 - 240));
        entities.add(new BallBoy(this, 780, 223 + 240));
        entities.add(new AudienceLeft(this));
        entities.add(new AudienceRight(this));
        entities.add(player2 = new Player2(this, TOP));
        entities.add(ball = new Ball(this));
        entities.add(new Net(this));
        entities.add(player1 = new Player1(this, DOWN));
        entities.add(umpire = new Umpire(this, player1, player2));
        ball.setUmpire(umpire);
        player1.setUmpire(umpire);
        player2.setUmpire(umpire);
        player1.setBall(ball);
        player2.setBall(ball);
    }

    private void startAllEntities() {
        for (Entity entity : entities) {
            entity.start();
        }
        Camera.setView(this);
        Camera.follow(player1);
    }

    private void startMainLoop() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                update();
//                repaint();
//            }
//        }, 100, 1000 / 30);
        
        mainLoop = new Thread(() -> {
            while (true) {
                update();
                Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                draw((Graphics2D) frameBuffer.getGraphics());
                g.drawImage(frameBuffer, 0, 0, SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE, null);
                g.dispose();
                bs.show();

                try {
                    Thread.sleep(1000 / 30);
                } catch (InterruptedException ex) {
                }
            }
        });
        mainLoop.start();
    }
    
    private void update() {
        Camera.update();
        for (Entity entity : entities) {
            entity.update();
        }
    }
    
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g); 
//        draw((Graphics2D) frameBuffer.getGraphics());
//        g.drawImage(frameBuffer, 0, 0, SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE, null);
//    }

    private void draw(Graphics2D g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        Collections.sort(entities);
        for (Entity entity : entities) {
            entity.draw(g);
        }
    }
    
    private final Point textureToScreenPosition = new Point();
    
    public Point convertTextureToScreenPosition(double textureX, double textureY) {
            double fz = -textureX * scale;
            double fx = textureY * scale;
            double fsx = (((fx - cameraX) * d) / -(fz + cameraZ)) + (SCREEN_WIDTH / 2);
            double fsz = (((cameraHeight) * d) / -(fz + cameraZ)) + (SCREEN_HEIGHT / 2 - ajusteY);
            textureToScreenPosition.setLocation(fsx, fsz);
            return textureToScreenPosition;
    }

    private final Point ScreenToTexturePosition = new Point();
    
    public Point convertScreenPositionToTexture(int fsx, int fsz) {
            double fz  = -(((cameraHeight) * d) / (fsz - (SCREEN_HEIGHT / 2 - ajusteY))) - cameraZ;
            double textureX = -fz / scale;

            double fx = ((fsx - (SCREEN_WIDTH / 2)) * (-fz - cameraZ) / d) + cameraX;
            double textureY = fx / scale;
            ScreenToTexturePosition.setLocation(textureX, textureY);
            return ScreenToTexturePosition;
    }

}
