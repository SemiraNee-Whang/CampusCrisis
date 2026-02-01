package GUI;

import java.awt.*;
import main.GamePanel;

public class Dashboard {
    GamePanel gp;
    
    // LIVE STATS (Use these so they can change!)
    public int budget;
    public int approval;
    
    // TIMER VARIABLES
    public int secondCounter = 0;
    public int minutes = 5;
    public int seconds = 0;

    public Dashboard(GamePanel gp) {
        this.gp = gp;
        // Set live stats from your starting constants
        this.budget = gp.pSetup.STARTING_BUDGET;
        this.approval = gp.pSetup.STARTING_APPROVAL;
    }

    // THIS METHOD MUST EXIST FOR GAMEPANEL TO WORK
    public void updateTimer() {
        secondCounter++;
        if (secondCounter >= 60) { // 60 FPS
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

    public void draw(Graphics2D g2) {
        // 1. DASHBOARD PANEL
        g2.setColor(new Color(20, 20, 30, 220)); 
        g2.fillRect(0, 0, gp.screenWidth, 65);
        
        // 2. ACCENT BORDER
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 62, gp.screenWidth, 3);

        // 3. PRESIDENTIAL IDENTITY
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        String presidentLabel = "PRESIDENT: " + gp.pSetup.presidentName.toUpperCase();
        g2.drawString(presidentLabel, 20, 40);

        // 4. FINANCIAL STATUS (Use the 'budget' variable, not STARTING_BUDGET)
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.YELLOW);
        String budgetDisplay = "BUDGET: R " + String.format("%,d", budget);
        
        FontMetrics fm = g2.getFontMetrics();
        int budgetX = gp.screenWidth - fm.stringWidth(budgetDisplay) - 20;
        g2.drawString(budgetDisplay, budgetX, 35);

        // 5. APPROVAL RATING (Use the 'approval' variable)
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(new Color(50, 255, 150)); 
        String approvalRating = "APPROVAL: " + approval + "%";
        int approvalX = gp.screenWidth - fm.stringWidth(approvalRating) - 20;
        g2.drawString(approvalRating, approvalX, 55);
        
        // 6. TIMER DISPLAY
        g2.setColor(Color.WHITE);
        String timeText = String.format("%02d:%02d", minutes, seconds);
        g2.drawString("TIME: " + timeText, gp.screenWidth/2 - 30, 40);
    }
}