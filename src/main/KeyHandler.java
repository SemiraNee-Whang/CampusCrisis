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
    public void keyTyped(KeyEvent e) {
        if (gp.gameState == gp.loginState) {
            char c = e.getKeyChar();
            // Typing logic remains so user can enter credentials
            if (gp.loginM.subState == 0) { // Username
                if (Character.isLetterOrDigit(c) || c == ' ') gp.loginM.userText += c;
                if (c == KeyEvent.VK_BACK_SPACE && gp.loginM.userText.length() > 0) 
                    gp.loginM.userText = gp.loginM.userText.substring(0, gp.loginM.userText.length() - 1);
            }
            else if (gp.loginM.subState == 1) { // Password
                if (Character.isLetterOrDigit(c)) gp.loginM.passText += c;
                if (c == KeyEvent.VK_BACK_SPACE && gp.loginM.passText.length() > 0)
                    gp.loginM.passText = gp.loginM.passText.substring(0, gp.loginM.passText.length() - 1);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // WSAD only works in PLAY STATE
        if (gp.gameState == gp.playState) {
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
        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) upPressed = false;
            if (code == KeyEvent.VK_S) downPressed = false;
            if (code == KeyEvent.VK_A) leftPressed = false;
            if (code == KeyEvent.VK_D) rightPressed = false;
            if (code == KeyEvent.VK_SHIFT) runPressed = false;
        }
    }
}