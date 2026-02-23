package main;

import java.awt.Rectangle;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Updated MouseHandler: Postpone now logs to history and moves to the next request.
 */
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

        // 1. REQUEST POP-UP INTERACTION
        if (gp.gameState == gp.requestState) {
            handleRequestPopUp(x, y);
            return; // Lock inputs while in request screen
        }

        // 2. OTHER GAME STATES
        if (gp.gameState == gp.titleState) {
            handleTitleClick();
        } else if (gp.gameState == gp.loginState) {
            handleLoginClick();
        } else if (gp.gameState == gp.setupState) {
            handleSetupClick(x, y);
        } else if (gp.gameState == gp.instructionState) {
            handleInstructionClick(x, y);
        } else if (gp.gameState == gp.historyState) {
            if (gp.historyView.backBtn.contains(x, y)) {
                gp.gameState = gp.playState;
            }
        } else if (gp.gameState == gp.reportState) {
            if (gp.reportView.backBtn.contains(e.getPoint())) {
                resetGame();
                gp.gameState = gp.titleState;
            }
        }
    }

    /**
     * Handles clicks within the request pop-up including Postpone.
     */
    private void handleRequestPopUp(int x, int y) {
        Rectangle requestBox = new Rectangle(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 4);
        
        // Click the box to reveal actions
        if (requestBox.contains(x, y) && !gp.reqList.showButtons) {
            gp.reqList.showButtons = true; 
        } 
        else if (gp.reqList.showButtons) {
            if (gp.reqList.approveBtn.contains(x, y)) {
                processDecision("Approve");
                gp.reqList.showButtons = false;
            }
            else if (gp.reqList.declineBtn.contains(x, y)) {
                processDecision("Decline");
                gp.reqList.showButtons = false;
            }
            // NEW: POSTPONE LOGIC
            else if (gp.reqList.postponeBtn.contains(x, y)) {
                processDecision("Postpone"); // Call decision logic for Postpone
                gp.reqList.showButtons = false;
                // Note: gameState stays in requestState so a new request appears immediately
            }
        }
    }

    /**
     * Logic for processing impacts and logging to history.
     */
    private void processDecision(String decision) {
        Request r = gp.reqList.currentRequest;
        if (r == null) return;

        if (decision.equals("Approve")) {
            gp.dashboard.budget -= r.cost;
            gp.dashboard.approval += r.impact;
            r.status = "Approved";
            r.outcome = "Budget -" + r.cost + ", Approval +" + r.impact;
        } 
        else if (decision.equals("Decline")) {
            // Penalty for declining
            gp.dashboard.approval -= 8; 
            r.status = "Declined";
            r.outcome = "Approval -8";
        }
        // NEW: Handle the Postpone status
        else if (decision.equals("Postpone")) {
            r.status = "Postponed";
            r.outcome = "No change (Deferred)"; // No budget or approval change
        }
        
      

        // Apply caps and floors
        if (gp.dashboard.approval > 100) gp.dashboard.approval = 100;
        if (gp.dashboard.approval < 0) gp.dashboard.approval = 0;

        // Save to internal history for the table
        gp.history.add(r);
        
        // Log to text file
        saveDecisionToFile(r);

        // Transition to the next available request immediately
        gp.reqList.getNextRandomRequest();
        
        
    }

    /**
     * Resets stats for a fresh game session.
     */
    public void resetGame() {
        gp.dashboard.budget = gp.pSetup.STARTING_BUDGET;
        gp.dashboard.approval = gp.pSetup.STARTING_APPROVAL;
        gp.dashboard.minutes = 5;
        gp.dashboard.seconds = 0;
        gp.history.clear();
        gp.reqList.history.clear();
        gp.player.setDefaultValues();
    }

    /**
     * Main menu button and Exit screen logic.
     */
    private void handleTitleClick() {
        if (!gp.ui.confirmExitState) {
            if (gp.ui.commandNum == 0) gp.gameState = gp.loginState;
            if (gp.ui.commandNum == 1) {
                gp.gameState = gp.instructionState; 
                gp.instructions.subState = 0;
            }
            if (gp.ui.commandNum == 2) gp.gameState = gp.reportState; 
            if (gp.ui.commandNum == 3) {
                gp.ui.confirmExitState = true;
                gp.ui.commandNum = -1;
            }
        } else {
            // Use indices from UI.java
            if (gp.ui.commandNum == 4) { // YES
                System.exit(0);
            }
            if (gp.ui.commandNum == 5) { // NO
                gp.ui.confirmExitState = false;
                gp.ui.commandNum = -1;
            }
        }
    }

    private void handleInstructionClick(int x, int y) {
        if (gp.instructions.nextBtn.contains(x, y)) {
            if (gp.instructions.subState < 8) gp.instructions.subState++;
            else {
                gp.gameState = gp.titleState;
                gp.instructions.subState = 0;
            }
        }
        else if (gp.instructions.backBtn.contains(x, y)) {
            if (gp.instructions.subState > 0) gp.instructions.subState--;
        }
    }

    private void handleLoginClick() {
        if (gp.loginM.subState == 4) {
            gp.gameState = gp.titleState;
            gp.ui.commandNum = -1;
        } else if (gp.loginM.subState == 2) {
            if (gp.loginM.isSignUp) gp.loginM.registerUser();
            else if (gp.loginM.validateLogin()) gp.gameState = gp.setupState;
        } else if (gp.loginM.subState == 3) {
            gp.loginM.isSignUp = !gp.loginM.isSignUp;
        }
    }

    public void handleSetupClick(int x, int y) {
        int boxX = gp.tileSize * 6;
        int boxY = gp.tileSize * 5 - 30;
        gp.pSetup.nameBoxSelected = (x >= boxX && x <= boxX + (gp.tileSize * 6) && y >= boxY && y <= boxY + 40);
        
        if (gp.pSetup.subState == 1 && !gp.pSetup.presidentName.trim().isEmpty()) gp.gameState = gp.playState;
        else if (gp.pSetup.subState == 2) gp.gameState = gp.loginState;
    }

    private void saveDecisionToFile(Request r) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("decisions.txt", true))) {
            writer.write(r.id + " | " + r.status + " | " + r.outcome + " | " + r.requestName);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void mouseMoved(MouseEvent e) {
        mouseX = e.getX(); mouseY = e.getY();
        if (gp.gameState == gp.titleState) handleTitleHover(mouseX, mouseY);
        else if (gp.gameState == gp.loginState) handleLoginHover(mouseX, mouseY);
        else if (gp.gameState == gp.setupState) handleSetupHover(mouseX, mouseY);
    }

    public void handleTitleHover(int x, int y) {
        if (!gp.ui.confirmExitState) {
            if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.ui.commandNum = 0;
                else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) gp.ui.commandNum = 1;
                else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 2;
                else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 3;
                else gp.ui.commandNum = -1;
            }
        } else {
            int centerY = (gp.screenHeight / 2) + (gp.tileSize * 2);
            if (y >= centerY - 40 && y <= centerY + 10) {
                if (x > gp.screenWidth/2 - 150 && x < gp.screenWidth/2 - 20) gp.ui.commandNum = 4;
                else if (x > gp.screenWidth/2 + 20 && x < gp.screenWidth/2 + 150) gp.ui.commandNum = 5;
                else gp.ui.commandNum = -1;
            } else {
                gp.ui.commandNum = -1;
            }
        }
    }

    @Override public void mouseWheelMoved(MouseWheelEvent e) {
        if (gp.gameState == gp.historyState) {
            gp.historyView.scrollOffset += e.getWheelRotation() * 20;
            if (gp.historyView.scrollOffset < 0) gp.historyView.scrollOffset = 0;
        }
    }

    @Override public void mouseDragged(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

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
}