package GUI;

import java.awt.*;
import main.GamePanel;
import main.Request;

public class DecisionHistory {
    GamePanel gp;
    public int scrollOffset = 0; // Public for MouseHandler
    public Rectangle backBtn;

    public DecisionHistory(GamePanel gp) {
        this.gp = gp;
        // Back button at the bottom centre
        backBtn = new Rectangle(gp.screenWidth / 2 - 60, gp.screenHeight - 80, 120, 40);
    }

    public void draw(Graphics2D g2) {
        // 1. WHITE TABLE BACKGROUND
        g2.setColor(Color.WHITE);
        g2.fillRect(gp.tileSize, gp.tileSize, gp.screenWidth - (gp.tileSize * 2), gp.screenHeight - (gp.tileSize * 3));

        // 2. COLUMN LABELS
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        int yHead = gp.tileSize + 40;
        g2.drawString("ID", gp.tileSize + 20, yHead);
        g2.drawString("STATUS", gp.tileSize + 100, yHead);
        g2.drawString("OUTCOME", gp.tileSize + 250, yHead);
        
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(gp.tileSize + 10, yHead + 10, gp.screenWidth - gp.tileSize - 10, yHead + 10);

        // 3. DRAW HISTORY (From the list in RequestList)
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 0; i < gp.reqList.history.size(); i++) {
            Request r = gp.reqList.history.get(i);
            int rowY = yHead + 40 + (i * 30) - (scrollOffset / 5); // Basic scroll logic
            
            // Only draw if within table bounds
            if (rowY > yHead + 20 && rowY < gp.screenHeight - 150) {
                g2.drawString(r.id, gp.tileSize + 20, rowY);
                g2.drawString(r.status, gp.tileSize + 100, rowY);
                g2.drawString(r.outcome, gp.tileSize + 250, rowY);
            }
        }

        // 4. BACK BUTTON
        g2.setColor(new Color(0, 0, 102));
        g2.fill(backBtn);
        g2.setColor(Color.WHITE);
        g2.drawString("BACK", backBtn.x + 35, backBtn.y + 25);
    }
}