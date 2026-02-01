package GUI;

import java.awt.*;
import main.GamePanel;

public class Dashboard {
    GamePanel gp;
    
    // Timer variables (Example: 5 minutes per term)
    public int secondCounter = 0;
    public int minutes = 5;
    public int seconds = 0;

    public Dashboard(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        // --- 1. TOP STATUS BAR (Current Values) ---
        g2.setColor(new Color(20, 20, 30, 230)); 
        g2.fillRect(0, 0, gp.screenWidth, 70);
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 68, gp.screenWidth, 2); // Thin accent line

        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.setColor(Color.WHITE);

        // President Name (Left)
        g2.drawString("PRESIDENT: " + gp.pSetup.presidentName.toUpperCase(), 20, 30);

        // Remaining Time (Centre)
        String timeText = String.format("TIME REMAINING: %02d:%02d", minutes, seconds);
        g2.drawString(timeText, gp.screenWidth/2 - 80, 30);

        // Budget & Approval (Right)
        g2.setColor(Color.YELLOW);
        String budgetText = "BUDGET: R " + String.format("%,d", gp.pSetup.STARTING_BUDGET);
        int budgetX = gp.screenWidth - g2.getFontMetrics().stringWidth(budgetText) - 20;
        g2.drawString(budgetText, budgetX, 30);

        g2.setColor(new Color(50, 255, 150));
        String approvalText = "APPROVAL: " + gp.pSetup.STARTING_APPROVAL + "%";
        int approvalX = gp.screenWidth - g2.getFontMetrics().stringWidth(approvalText) - 20;
        g2.drawString(approvalText, approvalX, 55);

        // --- 2. MENU OPTIONS (Bottom Action Bar) ---
        // We'll place these at the bottom of the screen for easy access
        int menuY = gp.screenHeight - 50;
        g2.setColor(new Color(20, 20, 30, 200));
        g2.fillRect(0, menuY, gp.screenWidth, 50);
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, menuY, gp.screenWidth, 2); // Top border for menu

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        
        // Menu Items
        g2.drawString("[1] VIEW REQUESTS", 50, menuY + 30);
        g2.drawString("[2] DECISION HISTORY", 300, menuY + 30);
        g2.setColor(Color.ORANGE);
        g2.drawString("[ESC] END TERM", gp.screenWidth - 150, menuY + 30);
    }

    // Logic to count down the time
    public void updateTimer() {
        secondCounter++;
        if (secondCounter >= 60) { // Assuming 60 FPS
            if (seconds == 0) {
                if (minutes > 0) {
                    minutes--;
                    seconds = 59;
                }
            } else {
                seconds--;
            }
            secondCounter = 0;
        }
    }
}