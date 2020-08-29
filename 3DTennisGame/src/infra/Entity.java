package infra;


import java.awt.Graphics2D;

/**
 *
 * @author admin
 */
public class Entity implements Comparable<Entity> {
    
    protected final View view;
    protected int zOrder;
    
    public Entity(View view) {
        this.view = view;
    }
    
    public void start() {
        
    }
    
    public void update() {
    }
    
    public void draw(Graphics2D g) {
    }

    @Override
    public int compareTo(Entity o) {
        return o.zOrder - zOrder;
    }
    
}
