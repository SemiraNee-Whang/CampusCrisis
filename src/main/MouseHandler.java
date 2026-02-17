package main;

import java.awt.Rectangle;
import java.awt.event.*;


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
        } 
        
        else if (gp.gameState == gp.instructionState) {
            
            // Check the NEXT button inside gp.instructions
            if (gp.instructions.nextBtn.contains(x, y)) {
                if (gp.instructions.subState < 8) {
                    gp.instructions.subState++;
                } else {
                    // If on the last page, go back to the menu
                    gp.gameState = gp.titleState;
                    gp.instructions.subState = 0; // Reset to page 1 for next time
                }
            }
            
            // Check the BACK button inside gp.instructions
            else if (gp.instructions.backBtn.contains(x, y)) {
                if (gp.instructions.subState > 0) {
                    gp.instructions.subState--;
                }
            }
        }

        else if (gp.gameState == gp.requestState) {
            Rectangle requestBox = new Rectangle(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 4);
            
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
                    // Start cooldown even for postpone so they don't spam requests
                 
                    gp.reqList.showButtons = false;
                }
            }
        } 
        
        else if (gp.gameState == gp.historyState) {
            // Updated to match the new moved-up "BACK" button position
            if (gp.historyView.backBtn.contains(x, y)) {
                gp.gameState = gp.playState;
            }
        }
        else if (gp.gameState == gp.reportState) {
            // Check if the user clicked the BACK button on the ReportView screen
            if (gp.reportView.backBtn.contains(x, y)) {
                gp.gameState = gp.titleState; // Go back to main menu
            }
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

        // 1. Add to the active list
        gp.history.add(r);
        
        // 2. NEW: Save to the text file
        saveDecisionToFile(r);

        gp.reqList.getNextRandomRequest();
    }

    private void saveDecisionToFile(Request r) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("decisions.txt", true))) {
            // Format: ID | Status | Outcome | Request Name
            String record = r.id + " | " + r.status + " | " + r.outcome + " | " + r.requestName;
            writer.write(record);
            writer.newLine();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // --- ACCESSING THE HOVER HELPERS ---
    public void handleTitleHover(int x, int y) {
        if (!gp.ui.confirmExitState) {
            // Check if mouse is within the horizontal bounds of your menu
            if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) gp.ui.commandNum = 0; // Play
                else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) gp.ui.commandNum = 1; // Instructions <--- CHECK THIS
                else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) gp.ui.commandNum = 2; // Credits/Settings
                else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) gp.ui.commandNum = 3; // Exit
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
            if (gp.ui.commandNum == 0) gp.gameState = gp.loginState;
            
            if (gp.ui.commandNum == 1) {
                gp.gameState = gp.instructionState; 
                gp.instructions.subState = 0;
            }

            // --- REDIRECT TO YOUR NEW REPORT STATE ---
            if (gp.ui.commandNum == 2) {
                gp.gameState = gp.reportState; 
            }
            
            if (gp.ui.commandNum == 3) {
                gp.ui.confirmExitState = true;
                gp.ui.commandNum = -1;
            }
        } else {
            if (gp.ui.commandNum == 0) System.exit(0);
            if (gp.ui.commandNum == 1) gp.ui.confirmExitState = false;
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