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
        g2.setColor(new Color(245, 245, 245));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.BLACK);
        g2.drawString("Official Term Reports", 100, 50);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        int y = 150;
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("decisions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                g2.drawString(line, 100, y);
                y += 30; // Move down for the next record
            }
        } catch (java.io.IOException e) {
            g2.drawString("No reports found.", 100, 150);
        }
    

        // Draw Back Button
        g2.setColor(new Color(255, 215, 0));
        g2.fill(backBtn);
        g2.setColor(Color.BLACK);
        g2.draw(backBtn);
        g2.drawString("BACK", backBtn.x + 40, backBtn.y + 25);
    }
}