package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
    GamePanel gp;

    public MouseHandler(GamePanel gp) { this.gp = gp; }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX(); int y = e.getY();

        if (gp.gameState == gp.titleState) {
            if (x >= gp.tileSize * 3 && x <= gp.tileSize * 10) {
                if (y >= gp.tileSize * 8 - 35 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 0;
                else if (y >= gp.tileSize * 9 - 35 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 1;
                else gp.ui.commandNum = -1;
            }
        } else if (gp.gameState == gp.loginState) {
            if (y >= gp.tileSize * 4 - 40 && y <= gp.tileSize * 4 + 10) gp.loginM.subState = 0;
            else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.loginM.subState = 1;
            else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.loginM.subState = 2;
            else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.loginM.subState = 3;
            else gp.loginM.subState = -1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.gameState == gp.titleState) {
            if (gp.ui.commandNum == 0) gp.gameState = gp.loginState;
            if (gp.ui.commandNum == 1) System.exit(0);
        } else if (gp.gameState == gp.loginState) {
            if (gp.loginM.subState == 2) {
                if (gp.loginM.isSignUp) gp.loginM.registerUser();
                else if (gp.loginM.validateLogin()) gp.gameState = gp.playState;
            }
            if (gp.loginM.subState == 3) { gp.loginM.isSignUp = !gp.loginM.isSignUp; gp.loginM.message = ""; }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	
    }
    @Override 
    public void mouseClicked(MouseEvent e) {
    	
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	
    }
    @Override 
    public void mouseEntered(MouseEvent e) {
    	
    }
    @Override 
    public void mouseExited(MouseEvent e) {
    	
    }
}