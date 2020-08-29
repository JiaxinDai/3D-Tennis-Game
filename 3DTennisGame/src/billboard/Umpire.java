package billboard;


import billboard.Player.Side;
import static billboard.Player.Side.*;
import static billboard.Umpire.State.*;
import infra.View;
import infra.Billboard;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author admin
 */
public class Umpire extends Billboard {
    
    private Player player1;
    private Player player2;
    
    private boolean playing = false;

    public static enum State { WATCHING, RESULT }
    private State state = WATCHING;
    private int resultTimeLeft;
    private Side lostSide;
    
    private int scoreTop;
    private int scoreDown;
    
    private String reason = "";
    
    public Umpire(View view, Player player1, Player player2) {
        super(view);
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void start() {
        addFrame("umpire.png");
        worldX = 368;
        worldZ = 366;
        worldWidth = 20;
    }

    @Override
    public void update() {
        switch (state) {
            case WATCHING:
                updateWatching();
                break;
            case RESULT:
                updateResult();
                break;
        }
    }

    private void updateWatching() {
    }

    private void updateResult() {
        if (resultTimeLeft-- < 0) {
            if (lostSide == TOP) {
                scoreDown += 1;
            }
            else if (lostSide == DOWN) {
                scoreTop += 1;
            }
            
            player1.startService();
            player2.startReceive();
            player1.setHitEnabled(true);
            player2.setHitEnabled(true);
            state = WATCHING;
            reason = "";
        }
    }
    
    public void serviceFault(Side lostPlayerSide) {
        if (state == WATCHING) {
            lostSide = lostPlayerSide;
            state = RESULT;
            resultTimeLeft = 120;
            player1.setHitEnabled(false);
            player2.setHitEnabled(false);
            reason = "SERVICE FAULT";
        }
    }

    public void serviceNet(Side lostPlayerSide) {
        if (state == WATCHING) {
            lostSide = lostPlayerSide;
            state = RESULT;
            resultTimeLeft = 120;
            player1.setHitEnabled(false);
            player2.setHitEnabled(false);
            reason = "SERVICE NET";
        }
    }

    public void playingNet(Side lostPlayerSide) {
        if (state == WATCHING) {
            lostSide = lostPlayerSide;
            state = RESULT;
            resultTimeLeft = 120;
            player1.setHitEnabled(false);
            player2.setHitEnabled(false);
            reason = "NET";
        }
    }

    public void playingBounceMoreThan2Times(Side lostPlayerSide) {
        if (state == WATCHING) {
            lostSide = lostPlayerSide;
            state = RESULT;
            resultTimeLeft = 120;
            player1.setHitEnabled(false);
            player2.setHitEnabled(false);
            reason = "BOUNCE >=2 TIMES";
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.setColor(Color.WHITE);
        g.drawString("TOP: " + scoreTop + " / DOWN: " + scoreDown, 20, 230);
        if (!reason.trim().isEmpty()) {
            g.drawString("REASON: " + reason, 20, 220);
        }
    }
    
}
