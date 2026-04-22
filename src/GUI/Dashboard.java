package GUI;

import java.awt.*;
import main.GamePanel;

public class Dashboard {
    GamePanel gp;
    
    //Live Stats
    public int budget;
    public int approval;
    
    //Timer Variables
    public int secondCounter = 0;
    public int minutes = 5;
    public int seconds = 0;

    public Dashboard(GamePanel gp) {
        this.gp = gp;
        //Set live stats from your starting constants
        this.budget = gp.pSetup.STARTING_BUDGET;
        this.approval = gp.pSetup.STARTING_APPROVAL;
    }

    //The method exists so that gamepanel can continue working 
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
        //Top Status Bar
        g2.setColor(new Color(20, 20, 30, 220)); 
        g2.fillRect(0, 0, gp.screenWidth, 65);
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 62, gp.screenWidth, 3);

        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.WHITE);
        g2.drawString("PRESIDENT: " + gp.pSetup.presidentName.toUpperCase(), 20, 40);

        //Timer Button
        String timeText = String.format("TIME: %02d:%02d", minutes, seconds);
        int timeX = gp.screenWidth / 2 - (g2.getFontMetrics().stringWidth(timeText) / 2);
        g2.drawString(timeText, timeX, 40);

        //Budget Button
        g2.setColor(Color.YELLOW);
        String budgetDisplay = "BUDGET: R " + String.format("%,d", budget);
        g2.drawString(budgetDisplay, gp.screenWidth - 250, 30);

        //Approval Button
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(new Color(50, 255, 150)); 
        String approvalText = "APPROVAL: " + approval + "%";
        g2.drawString(approvalText, gp.screenWidth - 250, 55);

        //Bottom Action Button
        int barHeight = 50;
        int menuY = gp.screenHeight - barHeight;

        //Dashboard Design
        g2.setColor(new Color(20, 20, 30, 230));
        g2.fillRect(0, menuY, gp.screenWidth, barHeight);
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, menuY, gp.screenWidth, 2);

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("[1] VIEW REQUESTS", 50, menuY + 30);
        g2.drawString("[2] DECISION HISTORY", 300, menuY + 30);
        g2.setColor(Color.ORANGE);
        g2.drawString("[ESC] BACK", gp.screenWidth - 180, menuY + 30);
    }
}