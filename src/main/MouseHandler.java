package main;

import java.awt.Rectangle;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * MouseHandler manages all user interactions via mouse.
 * It tracks decision counts, manages state transitions, and enforces UI locks.
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

        // 1. REQUEST POP-UP LOCK
        // Prevents interacting with background elements while a request is active.
        if (gp.gameState == gp.requestState) {
            handleRequestPopUp(x, y);
            return; 
        }

        // 2. STATE-BASED CLICK DELEGATION
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
     * Logic for the student requests. 
     * Screen stays open after choice to allow sequential decision making.
     */
    private void handleRequestPopUp(int x, int y) {
        Rectangle requestBox = new Rectangle(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 4);
        
        // Reveals buttons on initial click of the request card
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
            else if (gp.reqList.postponeBtn.contains(x, y)) {
                processDecision("Postpone");
                gp.reqList.showButtons = false;
            }
        }
    }

    /**
     * Updates game statistics, increments the handled request counter,
     * and logs the outcome to both memory and a text file.
     */
    private void processDecision(String decision) {
        Request r = gp.reqList.currentRequest;
        if (r == null) return;

        // 1. PROCESS IMPACTS
        if (decision.equals("Approve")) {
            gp.dashboard.budget -= r.cost;
            gp.dashboard.approval += r.impact;
            r.status = "Approved";
            r.outcome = "Budget -" + r.cost + ", Approval +" + r.impact;
        } 
        else if (decision.equals("Decline")) {
            gp.dashboard.approval -= 8; // Declining penalty
            r.status = "Declined";
            r.outcome = "Approval -8";
        }
        else if (decision.equals("Postpone")) {
            r.status = "Postponed";
            r.outcome = "No change (Deferred)";
        }

        // 2. ENFORCE BOUNDARIES (Approval 0-100)
        if (gp.dashboard.approval > 100) gp.dashboard.approval = 100;
        if (gp.dashboard.approval < 0) gp.dashboard.approval = 0;

        // 3. INCREMENT COUNTER (To ensure at least 2 are handled before game end)
        gp.requestsHandled++;

        // 4. DATA LOGGING
        gp.history.add(r);
        saveDecisionToFile(r);

        // 5. REFRESH FOR NEXT REQUEST
        gp.reqList.getNextRandomRequest();
    }

    /**
     * Wipes session data for a new game, including the request counter.
     */
    public void resetGame() {
        gp.dashboard.budget = gp.pSetup.STARTING_BUDGET;
        gp.dashboard.approval = gp.pSetup.STARTING_APPROVAL;
        gp.dashboard.minutes = 5;
        gp.dashboard.seconds = 0;
        gp.requestsHandled = 0; // Reset counter
        gp.history.clear();
        gp.reqList.history.clear();
        gp.player.setDefaultValues();
    }

    /**
     * Handles Main Menu clicks and the Exit Confirmation logic using indices.
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
            // YES Button (Index 4)
            if (gp.ui.commandNum == 4) {
                System.exit(0);
            }
            // NO Button (Index 5)
            if (gp.ui.commandNum == 5) {
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
            // YES/NO Hover logic based on centerX from UI.java
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