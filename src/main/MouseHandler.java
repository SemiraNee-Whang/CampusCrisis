package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
    GamePanel gp;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        //	TITLE STATE NAVIGATION
        if (gp.gameState == gp.titleState) {
            if (!gp.ui.confirmExitState) {
                // Main Menu: Start New Term, Instructions, View Reports, Exit
                // Checks if mouse is within the horizontal center column
                if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                    if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.ui.commandNum = 0;
                    else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) gp.ui.commandNum = 1;
                    else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 2;
                    else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 3;
                    else gp.ui.commandNum = -1;
                } else {
                    gp.ui.commandNum = -1;
                }
            } else {
                // Exit Confirmation: YES / NO
                // Detects vertical row for buttons
                int exitY = gp.screenHeight / 2 + gp.tileSize;
                if (y >= exitY - 40 && y <= exitY + 20) {
                    // Detects "YES" button on the left
                    if (x >= gp.screenWidth / 2 - 150 && x <= gp.screenWidth / 2 - 20) gp.ui.commandNum = 0;
                    // Detects "NO" button on the right
                    else if (x >= gp.screenWidth / 2 + 20 && x <= gp.screenWidth / 2 + 150) gp.ui.commandNum = 1;
                    else gp.ui.commandNum = -1;
                } else {
                    gp.ui.commandNum = -1;
                }
            }
        } 
        //	LOGIN STATE NAVIGATION 
        else if (gp.gameState == gp.loginState) {
            // Back Button Detection (Top Left Corner)
            if (x >= 10 && x <= 100 && y >= 10 && y <= 50) {
                gp.loginM.subState = 4;
            } 
            // Username Field
            else if (y >= gp.tileSize * 4 - 40 && y <= gp.tileSize * 4 + 10) gp.loginM.subState = 0;
            // Password Field
            else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.loginM.subState = 1;
            // Login/Create Account Button
            else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.loginM.subState = 2;
            // Switch Mode (Login/Sign-up) Link
            else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.loginM.subState = 3;
            else gp.loginM.subState = -1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // 	 TITLE STATE ACTIONS 
        if (gp.gameState == gp.titleState) {
            if (!gp.ui.confirmExitState) {
                // Transition to Login Screen
                if (gp.ui.commandNum == 0) gp.gameState = gp.loginState;
                // Future implementation: Instructions or Reports
                if (gp.ui.commandNum == 1) { /* Instructions */ }
                if (gp.ui.commandNum == 2) { /* Reports */ }
                // Trigger Exit Confirmation Overlay
                if (gp.ui.commandNum == 3) {
                    gp.ui.confirmExitState = true;
                    gp.ui.commandNum = -1; 
                }
            } else {
                // Exit Confirmation: YES
                if (gp.ui.commandNum == 0) System.exit(0);
                // Exit Confirmation: NO (Return to main menu)
                if (gp.ui.commandNum == 1) {
                    gp.ui.confirmExitState = false;
                    gp.ui.commandNum = -1;
                }
            }
        } 
        // LOGIN STATE ACTIONS 
        else if (gp.gameState == gp.loginState) {
            // Return to Title Screen via Back Button
            if (gp.loginM.subState == 4) {
                gp.gameState = gp.titleState;
                gp.ui.commandNum = -1;
            }
            // Execute Login or Registration
            else if (gp.loginM.subState == 2) {
                if (gp.loginM.isSignUp) gp.loginM.registerUser();
                else if (gp.loginM.validateLogin()) gp.gameState = gp.playState;
            }
            // Toggle Login/Signup view
            else if (gp.loginM.subState == 3) {
                gp.loginM.isSignUp = !gp.loginM.isSignUp;
                gp.loginM.message = "";
            }
        }
    }

    // Required by Interfaces but not currently utilized
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