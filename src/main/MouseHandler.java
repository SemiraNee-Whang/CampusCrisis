package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
    GamePanel gp;

    public MouseHandler(GamePanel gp) { this.gp = gp; }

   
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (gp.gameState == gp.titleState) {
            // Broad X-axis check 
            if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.ui.commandNum = 0;
                else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) gp.ui.commandNum = 1;
                else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 2;
                else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 3;
                else gp.ui.commandNum = -1;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.gameState == gp.titleState) {
            if (gp.ui.commandNum == 0) gp.gameState = gp.loginState; // Goes to Setup/Login
            if (gp.ui.commandNum == 1) { /* Load Instructions Screen */ }
            if (gp.ui.commandNum == 2) { /* Load Reports Screen */ }
            if (gp.ui.commandNum == 3) gp.ui.confirmExitState = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) 
    {
    	
    }
    @Override 
    public void mouseClicked(MouseEvent e)
    {
    	
    }
    @Override
    public void mouseReleased(MouseEvent e) 
    {
    	
    }
    @Override 
    public void mouseEntered(MouseEvent e) 
    {
    	
    }
    @Override 
    public void mouseExited(MouseEvent e) 
    {
    	
    }
}