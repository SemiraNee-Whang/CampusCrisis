package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        //Login Screen Typing
        if (gp.gameState == gp.loginState) {
            //subState 0 = Username, subState 1 = Password
        	if (gp.loginM.activeField == 0 || gp.loginM.activeField == 1) {
        	    if (c == KeyEvent.VK_BACK_SPACE) {
        	        if (gp.loginM.activeField == 0 && gp.loginM.userText.length() > 0) {
        	            gp.loginM.userText = gp.loginM.userText.substring(0, gp.loginM.userText.length() - 1);
        	        } else if (gp.loginM.activeField == 1 && gp.loginM.passText.length() > 0) {
        	            gp.loginM.passText = gp.loginM.passText.substring(0, gp.loginM.passText.length() - 1);
        	        }
        	    } else if (c >= 32 && c <= 126) {
        	        if (gp.loginM.activeField == 0 && gp.loginM.userText.length() < 16) {
        	            gp.loginM.userText += c;
        	        } else if (gp.loginM.activeField == 1 && gp.loginM.passText.length() < 16) {
        	            gp.loginM.passText += c;
        	        }
        	    }
        	}
        }
        
        //PRESIDENT SETUP TYPING
        else if (gp.gameState == gp.setupState && gp.pSetup.nameBoxSelected) {
            if (c == KeyEvent.VK_BACK_SPACE) {
                if (gp.pSetup.presidentName.length() > 0) {
                    gp.pSetup.presidentName = gp.pSetup.presidentName.substring(0, gp.pSetup.presidentName.length() - 1);
                }
            } else if (c >= 32 && c <= 126) {
                if (gp.pSetup.presidentName.length() < 20) {
                    gp.pSetup.presidentName += c;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_1) gp.gameState = gp.requestState;
            if (code == KeyEvent.VK_2) gp.gameState = gp.historyState; // New state
        }
        else if (gp.gameState == gp.requestState || gp.gameState == gp.historyState) {
            if (code == KeyEvent.VK_ESCAPE) gp.gameState = gp.playState;
        }
    
        //Handles keyboard controls when pressing the buttons
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
        }
        
        // Quick Enter for Login
        if (code == KeyEvent.VK_ENTER && gp.gameState == gp.loginState) {
            gp.loginM.activeField = -1; // deselect field
            gp.loginM.subState = 2;     // focus login button
        }
        }
    

    //Handles keyboard controls when not pressing the buttons
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
    }
}