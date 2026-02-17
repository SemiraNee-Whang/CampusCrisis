package GUI;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import main.GamePanel;

public class ReportView {
    GamePanel gp;
    public Rectangle backBtn;

    public ReportView(GamePanel gp) {
        this.gp = gp;
        backBtn = new Rectangle(gp.screenWidth / 2 - 60, gp.screenHeight - 80, 120, 40);
    }

    public void draw(Graphics2D g2) {
        // Background
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("TERM SUMMARY", gp.tileSize * 5, 100);

        // 1. Summary Display
        g2.setFont(new Font("Arial", Font.PLAIN, 22));
        g2.drawString("Final Approval: " + gp.dashboard.approval + "%", 150, 200);
        g2.drawString("Budget Remaining: R " + String.format("%,d", gp.dashboard.budget), 150, 250);
        g2.drawString("Total Decisions Made: " + gp.history.size(), 150, 300);

        // 2. Win/Lose Message
        if (gp.dashboard.budget > 0 && gp.dashboard.approval >= 50) {
            g2.setColor(Color.GREEN);
            if (gp.dashboard.approval >= 80) {
                g2.drawString("EXCELLENT TERM: ELITE PRESIDENT!", 150, 400);
            } else {
                g2.drawString("TERM SUCCESSFUL: YOU WIN!", 150, 400);
            }
        } else {
            g2.setColor(Color.RED);
            g2.drawString("TERM FAILED: YOU ARE DISMISSED!", 150, 400);
        }
        
        // 3. Back to Main Menu Button
        drawBackButton(g2);
    }

    private void drawBackButton(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fill(backBtn);
        g2.setColor(Color.BLACK);
        g2.draw(backBtn);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("MAIN MENU", backBtn.x + 20, backBtn.y + 25);
    }
}