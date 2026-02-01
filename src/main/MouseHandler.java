package main;

import java.awt.event.*;
import GUI.RequestList;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
    GamePanel gp;
    public int mouseX, mouseY;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (gp.gameState == gp.titleState) {
            handleTitleClick();
        } else if (gp.gameState == gp.loginState) {
            handleLoginClick();
        } else if (gp.gameState == gp.setupState) {
            handleSetupClick(x, y);
        } else if (gp.gameState == gp.requestState) {
            // DECISION BUTTONS
            if (gp.reqList.approveBtn.contains(x, y)) processDecision("Approve");
            else if (gp.reqList.declineBtn.contains(x, y)) processDecision("Decline");
            else if (gp.reqList.postponeBtn.contains(x, y)) gp.reqList.getNextRandomRequest();
        } else if (gp.gameState == gp.historyState) {
            if (gp.historyView.backBtn.contains(x, y)) gp.gameState = gp.playState;
        }
    }

    private void processDecision(String decision) {
        Request r = gp.reqList.currentRequest;
        if (r == null) return;

        if (decision.equals("Approve")) {
            gp.dashboard.budget -= r.cost;
            gp.dashboard.approval += r.impact;
            r.status = "Approved";
            r.outcome = "Budget -" + r.cost + ", Approval +" + r.impact;
        } else if (decision.equals("Decline")) {
            gp.dashboard.approval -= 4;
            r.status = "Declined";
            r.outcome = "Approval -4";
        }

        gp.reqList.history.add(r);
        gp.reqList.getNextRandomRequest();
    }

    // --- ACCESSING THE HOVER HELPERS ---
    public void handleTitleHover(int x, int y) {
        if (!gp.ui.confirmExitState) {
            if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.ui.commandNum = 0;
                else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) gp.ui.commandNum = 1;
                else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 2;
                else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 3;
                else gp.ui.commandNum = -1;
            } else gp.ui.commandNum = -1;
        }
    }

    public void handleLoginHover(int x, int y) {
        if (x >= 10 && x <= 100 && y >= 10 && y <= 50) gp.loginM.subState = 4;
        else if (y >= gp.tileSize * 4 - 40 && y <= gp.tileSize * 4 + 10) gp.loginM.subState = 0;
        else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.loginM.subState = 1;
        else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.loginM.subState = 2;
        else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.loginM.subState = 3;
        else gp.loginM.subState = -1;
    }

    public void handleSetupHover(int x, int y) {
        if (x >= 10 && x <= 100 && y >= 10 && y <= 50) gp.pSetup.subState = 2;
        else if (y >= gp.tileSize * 5 - 30 && y <= gp.tileSize * 6 - 30) gp.pSetup.subState = 0;
        else if (y >= gp.tileSize * 7 && y <= gp.tileSize * 8) gp.pSetup.subState = 1;
        else gp.pSetup.subState = -1;
    }

    public void handleSetupClick(int x, int y) {
        int boxX = gp.tileSize * 6;
        int boxY = gp.tileSize * 5 - 30;
        int boxW = gp.tileSize * 6;
        int boxH = 40;
        gp.pSetup.nameBoxSelected = (x >= boxX && x <= boxX + boxW && y >= boxY && y <= boxY + boxH);
        
        if (gp.pSetup.subState == 1 && !gp.pSetup.presidentName.trim().isEmpty()) gp.gameState = gp.playState;
        else if (gp.pSetup.subState == 2) gp.gameState = gp.loginState;
    }

    // Rest of your click helpers (handleTitleClick, handleLoginClick) go here...

    @Override public void mouseWheelMoved(MouseWheelEvent e) {
        if (gp.gameState == gp.historyState) {
            gp.historyView.scrollOffset += e.getWheelRotation() * 20; // Scroll by pixels
            if (gp.historyView.scrollOffset < 0) gp.historyView.scrollOffset = 0;
        }
    }

    // Standard Overrides
    @Override public void mouseMoved(MouseEvent e) {
        mouseX = e.getX(); mouseY = e.getY();
        if (gp.gameState == gp.titleState) handleTitleHover(mouseX, mouseY);
        else if (gp.gameState == gp.loginState) handleLoginHover(mouseX, mouseY);
        else if (gp.gameState == gp.setupState) handleSetupHover(mouseX, mouseY);
    }
    @Override public void mouseDragged(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    
    private void handleTitleClick() {
        if (!gp.ui.confirmExitState) {
            // Main Title Menu
            if (gp.ui.commandNum == 0) { // Start Game / Play
                gp.gameState = gp.loginState;
            }
            if (gp.ui.commandNum == 3) { // Exit
                gp.ui.confirmExitState = true;
                gp.ui.commandNum = -1; // Reset hover
            }
        } else {
            // Exit Confirmation Menu
            if (gp.ui.commandNum == 0) System.exit(0); // Yes
            if (gp.ui.commandNum == 1) { // No
                gp.ui.confirmExitState = false;
                gp.ui.commandNum = -1;
            }
        }
    }
    private void handleLoginClick() {
        // subState 4 is usually the "Back" button (top left)
        if (gp.loginM.subState == 4) {
            gp.gameState = gp.titleState;
            gp.ui.commandNum = -1;
        }
        // subState 2 is the Login/Sign Up Button
        else if (gp.loginM.subState == 2) {
            if (gp.loginM.isSignUp) {
                gp.loginM.registerUser();
            } else {
                if (gp.loginM.validateLogin()) {
                    gp.gameState = gp.setupState;
                }
            }
        }
        // subState 3 is the "Switch to Sign Up/Login" toggle
        else if (gp.loginM.subState == 3) {
            gp.loginM.isSignUp = !gp.loginM.isSignUp;
            gp.loginM.message = "";
        }
    }
}