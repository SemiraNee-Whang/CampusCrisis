package GUI;

import java.awt.*;
import main.GamePanel;

public class PresidentSetup {
    GamePanel gp;
    public String presidentName = "";
    public int subState = 0; // 0: Name Input, 1: Confirm Button, 2: Back Button
    
    // Initializing stats based on your specs
    public final int STARTING_BUDGET = 50000;
    public final int STARTING_APPROVAL = 50; // 50%
    
    public PresidentSetup(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        // 1. Background (Light Grey)
        g2.setColor(new Color(211, 211, 211));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // 2. Heading (Dark Blue, 16pt Bold Sans Serif)
        g2.setFont(new Font("SansSerif", Font.BOLD, 32)); // Scaled for screen
        g2.setColor(new Color(0, 0, 139));
        String title = "PRESIDENT SETUP";
        int x = getXforCenteredText(title, g2);
        g2.drawString(title, x, gp.tileSize * 2);

        // 3. Label (12pt Sans Serif)
        g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
        g2.setColor(Color.BLACK);
        String label = "Enter President's Name:";
        g2.drawString(label, getXforCenteredText(label, g2), gp.tileSize * 4);

        // 4. Input Field (White box)
        g2.setColor(Color.WHITE);
        int fieldW = gp.tileSize * 8;
        int fieldH = gp.tileSize;
        int fieldX = gp.screenWidth/2 - fieldW/2;
        int fieldY = gp.tileSize * 5 - 30;
        g2.fillRect(fieldX, fieldY, fieldW, fieldH);
        g2.setColor(subState == 0 ? Color.YELLOW : Color.BLUE); // Highlight if active
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(fieldX, fieldY, fieldW, fieldH);

        // Draw the typed name
        g2.setColor(Color.BLACK);
        g2.drawString(presidentName + (subState == 0 ? "|" : ""), fieldX + 10, fieldY + 40);

        // 5. Confirmation Button (Dark Blue with White Text)
        int btnX = gp.screenWidth/2 - (gp.tileSize * 3)/2;
        int btnY = gp.tileSize * 7;
        g2.setColor(new Color(0, 0, 139));
        g2.fillRect(btnX, btnY, gp.tileSize * 3, gp.tileSize);
        
        g2.setColor(subState == 1 ? Color.YELLOW : Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("CONFIRM", btnX + 35, btnY + 45);
        
        // 6. Back Button (consistent with Login Screen)
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.setColor(subState == 2 ? Color.YELLOW : new Color(0, 0, 139));
        g2.drawString("< BACK", 20, 40);
    }

    private int getXforCenteredText(String text, Graphics2D g2) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}