package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, runPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            
            // Logic for the Main Title Menu
            if (!gp.ui.confirmExitState) {
                if (code == KeyEvent.VK_W) {
                    gp.ui.commandNum--;
                    if (gp.ui.commandNum < 0) gp.ui.commandNum = 1;
                }
                if (code == KeyEvent.VK_S) {
                    gp.ui.commandNum++;
                    if (gp.ui.commandNum > 1) gp.ui.commandNum = 0;
                }
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) gp.gameState = gp.playState;
                    if (gp.ui.commandNum == 1) {
                        gp.ui.confirmExitState = true; // Show confirmation
                        gp.ui.commandNum = 1; // Default to "NO" for safety
                    }
                }
            } 
            // Logic for the "Are you sure?" Menu
            else {
                if (code == KeyEvent.VK_A) gp.ui.commandNum = 0; // YES
                if (code == KeyEvent.VK_D) gp.ui.commandNum = 1; // NO
                
                if (code == KeyEvent.VK_ENTER) {
                    if (gp.ui.commandNum == 0) System.exit(0); // Exit game
                    if (gp.ui.commandNum == 1) {
                        gp.ui.confirmExitState = false; // Go back
                        gp.ui.commandNum = 1; // Reset cursor to Exit
                    }
                }
                if (code == KeyEvent.VK_ESCAPE) gp.ui.confirmExitState = false; // Back out
            }
        }        
        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_SHIFT) runPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_SHIFT) runPressed = false;
    }
}