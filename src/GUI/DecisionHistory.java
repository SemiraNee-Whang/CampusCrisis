package GUI;

import java.awt.*;
import main.GamePanel;
import main.Request;

public class DecisionHistory {
    GamePanel gp;
    public int scrollOffset = 0; 
    public Rectangle backBtn;

    public DecisionHistory(GamePanel gp) {
        this.gp = gp;
        // Positioned at the bottom-left of the white table area
        backBtn = new Rectangle(gp.tileSize + 20, gp.screenHeight - 110, 100, 35);
    }

    public void draw(Graphics2D g2) {
        // 1. DIM THE BACKGROUND (To make the table pop)
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // 2. WHITE TABLE BACKGROUND
        int tableX = gp.tileSize;
        int tableY = gp.tileSize;
        int tableW = gp.screenWidth - (gp.tileSize * 2);
        int tableH = gp.screenHeight - (gp.tileSize * 3);
        
        g2.setColor(Color.WHITE);
        g2.fillRect(tableX, tableY, tableW, tableH);
        g2.setColor(Color.BLACK);
        g2.drawRect(tableX, tableY, tableW, tableH);

        // 3. COLUMN LABELS
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        int yHead = tableY + 40;
        
        g2.drawString("ID", tableX + 30, yHead);
        g2.drawString("STATUS", tableX + 130, yHead);
        g2.drawString("OUTCOME SUMMARY", tableX + 300, yHead);
        
        // Header Underline
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(tableX + 20, yHead + 10, tableX + tableW - 20, yHead + 10);

        // 4. DRAW HISTORY FROM GamePanel ArrayList
        g2.setFont(new Font("Consolas", Font.PLAIN, 15)); // Monospaced looks better for logs
        
        // IMPORTANT: Changed from gp.reqList.history to gp.history to match MouseHandler
        for (int i = 0; i < gp.history.size(); i++) {
            Request r = gp.history.get(i);
            
            // Calculate Y position based on scroll
            int rowY = yHead + 50 + (i * 35) - scrollOffset; 
            
            // Clipping: Only draw if inside the white table area
            if (rowY > yHead + 20 && rowY < tableY + tableH - 20) {
                
                // Color coding based on status
                if ("Approved".equals(r.status)) {
                    g2.setColor(new Color(0, 120, 0)); // Dark Green
                } else if ("Declined".equals(r.status)) {
                    g2.setColor(new Color(180, 0, 0)); // Dark Red
                } else {
                    g2.setColor(Color.GRAY);
                }
                
                g2.drawString(r.id, tableX + 30, rowY);
                g2.drawString(r.status, tableX + 130, rowY);
                
                g2.setColor(Color.BLACK); // Reset for description
                g2.drawString(r.outcome, tableX + 300, rowY);
            }
        }

        // 5. BACK BUTTON
        drawStyledButton(g2, backBtn, "BACK", new Color(255, 215, 0));
    }

    private void drawStyledButton(Graphics2D g2, Rectangle r, String text, Color bgColor) {
        g2.setColor(bgColor);
        g2.fill(r);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(r);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textX = r.x + (r.width - fm.stringWidth(text)) / 2;
        int textY = r.y + (r.height + fm.getAscent()) / 2 - 2;
        g2.drawString(text, textX, textY);
    }
}