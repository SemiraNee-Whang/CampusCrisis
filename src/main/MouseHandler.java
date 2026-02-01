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

        //TITLE STATE NAVIGATION
        if (gp.gameState == gp.titleState) {
            if (!gp.ui.confirmExitState) {
                // Main Menu hover detection
                if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                    if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.ui.commandNum = 0; // Start New Term
                    else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) gp.ui.commandNum = 1; // Instructions
                    else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 2; // View Reports
                    else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 3; // Exit
                    else gp.ui.commandNum = -1;
                } else gp.ui.commandNum = -1;
            } else {
                // Exit Confirmation hover detection (YES/NO)
                int exitY = gp.screenHeight / 2 + gp.tileSize;
                if (y >= exitY - 40 && y <= exitY + 20) {
                    if (x >= gp.screenWidth / 2 - 150 && x <= gp.screenWidth / 2 - 20) gp.ui.commandNum = 0; // YES
                    else if (x >= gp.screenWidth / 2 + 20 && x <= gp.screenWidth / 2 + 150) gp.ui.commandNum = 1; // NO
                    else gp.ui.commandNum = -1;
                } else gp.ui.commandNum = -1;
            }
        } 
        
        //LOGIN STATE NAVIGATION
        else if (gp.gameState == gp.loginState) {
            // Detect Back Button (Top Left)
            if (x >= 10 && x <= 100 && y >= 10 && y <= 50) gp.loginM.subState = 4;
            // Detect Username (subState 0) and Password (subState 1) fields
            else if (y >= gp.tileSize * 4 - 40 && y <= gp.tileSize * 4 + 10) gp.loginM.subState = 0;
            else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.loginM.subState = 1;
            // Detect Login/Create Button
            else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.loginM.subState = 2;
            // Detect Signup/Login toggle link
            else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.loginM.subState = 3;
            else gp.loginM.subState = -1;
        }
        
        //PRESIDENT SETUP STATE NAVIGATION
        else if (gp.gameState == gp.setupState) {
            // Detect Back Button
            if (x >= 10 && x <= 100 && y >= 10 && y <= 50) gp.pSetup.subState = 2;
            // Detect Name Input Field
            else if (y >= gp.tileSize * 5 - 30 && y <= gp.tileSize * 6 - 30) gp.pSetup.subState = 0;
            // Detect Confirm Button
            else if (y >= gp.tileSize * 7 && y <= gp.tileSize * 8) gp.pSetup.subState = 1;
            else gp.pSetup.subState = -1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TITLE STATE ACTIONS
        if (gp.gameState == gp.titleState) {
            if (!gp.ui.confirmExitState) {
                if (gp.ui.commandNum == 0) gp.gameState = gp.loginState;
                if (gp.ui.commandNum == 3) { gp.ui.confirmExitState = true; gp.ui.commandNum = -1; }
            } else {
                if (gp.ui.commandNum == 0) System.exit(0);
                if (gp.ui.commandNum == 1) { gp.ui.confirmExitState = false; gp.ui.commandNum = -1; }
            }
        } 
        
        //LOGIN STATE ACTIONS
        else if (gp.gameState == gp.loginState) {
            // Back to main menu
            if (gp.loginM.subState == 4) { 
                gp.gameState = gp.titleState; 
                gp.ui.commandNum = -1; 
            }
            // Execute Login or Registration
            else if (gp.loginM.subState == 2) {
                if (gp.loginM.isSignUp) gp.loginM.registerUser();
                else if (gp.loginM.validateLogin()) gp.gameState = gp.setupState; 
            }
            // Toggle Login/Signup view
            else if (gp.loginM.subState == 3) {
                gp.loginM.isSignUp = !gp.loginM.isSignUp;
                gp.loginM.message = "";
            }
        }
        
        //PRESIDENT SETUP ACTIONS
        else if (gp.gameState == gp.setupState) {
            // Confirm and start game
            if (gp.pSetup.subState == 1) {
                if (!gp.pSetup.presidentName.trim().isEmpty()) {
                    gp.gameState = gp.playState;
                }
            }
            // Return to login screen
            else if (gp.pSetup.subState == 2) {
                gp.gameState = gp.loginState;
            }
        }
    }

    // Unused mandatory methods
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}