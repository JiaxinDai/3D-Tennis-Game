package infra;

import static infra.Camera.cameraHeight;
import static infra.Camera.cameraX;
import static infra.Camera.cameraZ;
import static infra.View.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author admin
 */
public class Floor extends Entity {

    private BufferedImage court;
    private int[] courtData;

    public Floor(View view) {
        super(view);
        try {
            BufferedImage tmp  = ImageIO.read(getClass().getResourceAsStream("/res/court.png"));
            court = new BufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            court.getGraphics().drawImage(tmp, 0, 0, null);
            courtData = ((DataBufferInt) court.getRaster().getDataBuffer()).getData();
            
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        zOrder = 1000000;
    }

    @Override
    public void draw(Graphics2D g) {
        for (int y = 240; y > 0; y--) {
            double z = (cameraHeight * view.d) / ((y + view.ajusteY) - SCREEN_HEIGHT / 2);
            //Math.toDegrees(Math.atan(120/477.70))
            for (int xScr = 0; xScr < SCREEN_WIDTH; xScr++) {
                double x = ((xScr - (SCREEN_WIDTH / 2)) * z) / view.d; 
                
                double tx = x + cameraX;
                double tz = z + cameraZ;
                
                int texU = ((int) (tz * 1 / view.scale));
                int texV = ((int) (tx * 1 / view.scale));
                if (texU < 0 || texU > court.getWidth() - 1) texU = 0;
                if (texV < 0 || texV > court.getHeight() - 1) texV = 0;
                int color = courtData[texU + texV * court.getWidth()];
                view.frameBuffer.setRGB(xScr, y - 1, color);
            }
        }
    }
    
}
