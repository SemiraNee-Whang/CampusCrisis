package GUI;

import java.awt.*;
import main.GamePanel;

public class RequestList {
    GamePanel gp;
    
    // We'll use an index to track which request is being hovered or selected
    public int slotNum = 0;
    
    // Placeholder data - later this will be loaded from requests.txt
    String[][] requests = {
        {"Fix Broken Desks", "Infrastructure"},
        {"New Science Kits", "Education"},
        {"Sports Day Water", "Student Life"},
        {"Library Books", "Education"}
    };

    public RequestList(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        // 1. BACKGROUND (Consistent with your UI design)
        g2.setColor(new Color(20, 20, 30, 240)); // Slightly transparent
        g2.fillRect(gp.tileSize, gp.tileSize, gp.screenWidth - (gp.tileSize*2), gp.screenHeight - (gp.tileSize*2));

        // 2. BORDER
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(gp.tileSize, gp.tileSize, gp.screenWidth - (gp.tileSize*2), gp.screenHeight - (gp.tileSize*2));

        // 3. TITLE
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));
        g2.setColor(Color.WHITE);
        g2.drawString("STUDENT REQUESTS", gp.tileSize * 2, gp.tileSize * 2);

        // 4. LIST HEADINGS
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("DESCRIPTION", gp.tileSize * 2, gp.tileSize * 3);
        g2.drawString("CATEGORY", gp.tileSize * 9, gp.tileSize * 3);
        g2.drawLine(gp.tileSize * 2, gp.tileSize * 3 + 5, gp.screenWidth - (gp.tileSize * 2), gp.tileSize * 3 + 5);

        // 5. DRAW REQUESTS
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        for (int i = 0; i < requests.length; i++) {
            int y = gp.tileSize * 4 + (i * 40);

            // Highlight the selected/hovered slot
            if (i == slotNum) {
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillRect(gp.tileSize * 2 - 10, y - 25, gp.screenWidth - (gp.tileSize * 4), 35);
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }

            // Display data from specs
            g2.drawString(requests[i][0], gp.tileSize * 2, y); // Short Description
            g2.drawString(requests[i][1].toUpperCase(), gp.tileSize * 9, y); // Category
        }

        // 6. NAVIGATION HINT
        g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("Select a request to view details and vote.", gp.tileSize * 2, gp.screenHeight - gp.tileSize - 20);
        g2.drawString("[ESC] Close", gp.screenWidth - (gp.tileSize * 4), gp.screenHeight - gp.tileSize - 20);
    }
}