package GUI;

import java.awt.*;
import main.GamePanel;

public class PresidentSetup {
    GamePanel gp;
    public String presidentName = "";
    // Removed the message string as requested
    public int subState = 0; // 0: Name Input, 1: Confirm Button, 2: Back Button
    
    // Initialising stats from specifications
    public final int STARTING_BUDGET = 50000;
    public final int STARTING_APPROVAL = 50;
    
    public PresidentSetup(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        // 1. BACKGROUND (Midnight theme)
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // 2. BACK BUTTON (Top-left)
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        if (subState == 2) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString("< BACK", 20, 40);

        // 3. TITLE
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        g2.setColor(Color.WHITE);
        String title = "PRESIDENT SETUP";
        g2.drawString(title, gp.tileSize * 5, gp.tileSize * 2);

        // 4. NAME INPUT LABEL
        g2.setFont(g2.getFont().deriveFont(24F));
        g2.setColor(Color.WHITE);
        g2.drawString("President Name:", gp.tileSize * 3, gp.tileSize * 5);
        
        // Drawing input box border
        int boxX = gp.tileSize * 6;
        int boxY = gp.tileSize * 5 - 30;
        int boxW = gp.tileSize * 6;
        int boxH = 40;
        
        // Highlight logic for the input field
        g2.setColor(subState == 0 ? Color.YELLOW : Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(boxX, boxY, boxW, boxH);
        
        // Render the name text and typing cursor
        g2.setColor(Color.WHITE);
        g2.drawString(presidentName + (subState == 0 ? "|" : ""), boxX + 10, boxY + 30);

        // 5. CONFIRM BUTTON (Simplified text)
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(subState == 1 ? Color.YELLOW : Color.WHITE);
        // Changed from "CONFIRM INAUGURATION" to just "CONFIRM"
        g2.drawString("CONFIRM", gp.tileSize * 7, gp.tileSize * 8);
    }
}