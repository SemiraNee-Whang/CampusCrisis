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
        backBtn = new Rectangle(gp.tileSize + 20, gp.screenHeight - 140, 100, 35);	    }

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

        // 3. DRAW HISTORY
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 0; i < gp.reqList.history.size(); i++) {
            main.Request r = gp.reqList.history.get(i);
            int rowY = yHead + 40 + (i * 30) - (scrollOffset); 
            
            if (rowY > yHead + 20 && rowY < gp.screenHeight - 150) {
                // Status-based colouring (Optional but looks great)
                if(r.status.equals("Approved")) g2.setColor(new Color(0, 150, 0)); // Dark Green
                else g2.setColor(new Color(200, 0, 0)); // Dark Red
                
                g2.drawString(r.id, gp.tileSize + 20, rowY);
                g2.drawString(r.status, gp.tileSize + 100, rowY);
                g2.setColor(Color.BLACK); // Reset for description
                g2.drawString(r.outcome, gp.tileSize + 250, rowY);
            }
        }

        // 4. YELLOW BACK BUTTON (Matching Request buttons)
        Color btnYellow = new Color(255, 215, 0);
        drawStyledButton(g2, backBtn, "BACK", btnYellow);
    }

    // Helper method for the yellow button style
    private void drawStyledButton(Graphics2D g2, Rectangle r, String text, Color bgColor) {
        g2.setColor(bgColor);
        g2.fill(r);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(r);

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        int textX = r.x + (r.width - fm.stringWidth(text)) / 2;
        int textY = r.y + (r.height + fm.getAscent()) / 2 - 2;
        g2.drawString(text, textX, textY);
    }
}