package infra;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author admin
 */
public class Billboard extends Entity {
    
    protected List<BufferedImage> frames = new ArrayList<>();
    protected double currentFrame;
    
    public double worldX;
    public double worldZ;
    public double worldWidth;

    protected int offsetX;
    protected int offsetY;
    
    public Billboard(View view) {
        super(view);
        worldWidth = 20;
    }
    
    public void addFrame(String res) {
        try {
            frames.add(ImageIO.read(getClass().getResourceAsStream("/res/" + res)));
            currentFrame = frames.size() - 1;
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }        
    }
    
    @Override
    public void start() {
        
    }
    
    @Override
    public void update() {
    }
    
    protected final Point scr = new Point();
    protected final Point scr2 = new Point();
    
    @Override
    public void draw(Graphics2D g) {
        Point p = view.convertTextureToScreenPosition(worldX, worldZ - worldWidth / 2);
        scr.setLocation(p.getX(), p.getY());
        //g.setColor(Color.BLACK);
        //g.fillOval((int) (scr.x - 1), (int) (scr.y - 1), 2, 2);

        p = view.convertTextureToScreenPosition(worldX, worldZ + worldWidth / 2);
        scr2.setLocation(p.getX(), p.getY());
        //g.setColor(Color.BLACK);
        //g.fillOval((int) (scr2.x - 1), (int) (scr2.y - 1), 2, 2);

        double spriteWidth = scr2.getX() - scr.getX();
        double spriteHeight = (int) (frames.get((int) currentFrame).getHeight() * (spriteWidth / (double) frames.get((int) currentFrame).getWidth()));
        g.drawImage(frames.get((int) currentFrame), (scr.x + offsetX), (int) (scr.y - spriteHeight + offsetY), (int) spriteWidth, (int) spriteHeight, null);
    }

    @Override
    public int compareTo(Entity o) {
        zOrder = (int) worldX;
        return super.compareTo(o);
    }

    
}
