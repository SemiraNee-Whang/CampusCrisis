package GUI;

import java.awt.*;
import main.GamePanel;

public class PresidentSetup {
    GamePanel gp;
    public String presidentName = "";
    public int subState = 0; // 0: Name Input, 1: Confirm Button, 2: Back Button
    
    // Initialising stats from specifications 
    public final int STARTING_BUDGET = 20000;
    public int currentBudget;
    public final int STARTING_APPROVAL = 40;
    public int currentApproval;
    public int remainingTime = 12; // Example: 12 weeks/turns [cite: 38]
    
    // Click-to-type state
    public boolean nameBoxSelected = false;
    
    public PresidentSetup(GamePanel gp) {
        this.gp = gp;
        this.currentBudget = STARTING_BUDGET;
        this.currentApproval = STARTING_APPROVAL;
    }

    public void draw(Graphics2D g2) {
        // 1. BACKGROUND (Midnight theme to match current UI)
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // 2. BACK BUTTON (Top-left)
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(subState == 2 ? Color.YELLOW : Color.WHITE);
        g2.drawString("< BACK", 20, 40);

        // 3. TITLE
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        g2.setColor(Color.WHITE);
        g2.drawString("PRESIDENT SETUP", gp.tileSize * 5, gp.tileSize * 2);

        // 4. NAME INPUT LABEL [cite: 173]
        g2.setFont(g2.getFont().deriveFont(24F));
        g2.setColor(Color.WHITE);
        g2.drawString("President Name:", gp.tileSize * 3, gp.tileSize * 5);
        
        // Input box coordinates
        int boxX = gp.tileSize * 6;
        int boxY = gp.tileSize * 5 - 30;
        int boxW = gp.tileSize * 6;
        int boxH = 40;
        
        // 5. INPUT BOX [cite: 173]
        // Highlight logic now uses nameBoxSelected for a true "click to focus" feel
        g2.setColor(nameBoxSelected ? Color.YELLOW : Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(boxX, boxY, boxW, boxH);
        
        // Render name and cursor only if box is selected
        g2.setColor(Color.WHITE);
        g2.drawString(presidentName + (nameBoxSelected ? "|" : ""), boxX + 10, boxY + 30);

        // 6. CONFIRM BUTTON [cite: 173]
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(subState == 1 ? Color.YELLOW : Color.WHITE);
        g2.drawString("CONFIRM", gp.tileSize * 7, gp.tileSize * 8);
    }
}