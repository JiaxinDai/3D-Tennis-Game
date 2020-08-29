package main;

import infra.View;
import static infra.View.SCALE;
import static infra.View.SCREEN_HEIGHT;
import static infra.View.SCREEN_WIDTH;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author admin
 */
public class Main {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            view.setPreferredSize(new Dimension(SCREEN_WIDTH * SCALE, SCREEN_HEIGHT * SCALE));
            JFrame frame = new JFrame();
            frame.setTitle("3D Tennis Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            view.requestFocus();
            view.start();
        });
    }
    
}
