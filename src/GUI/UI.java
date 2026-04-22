package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

//Handles the UI
public class UI {

    GamePanel gp;
    Graphics2D g2;
    // -1 means no hover
    public int commandNum = -1; 
    public boolean confirmExitState = false;
    BufferedImage titleBg;

    public UI(GamePanel gp) {
        this.gp = gp;
        
        try {
            titleBg = ImageIO.read(getClass().getResourceAsStream("/TitleScreen/PIC.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        if (gp.gameState == gp.titleState) {
            if (!confirmExitState) {
                drawTitleScreen();
            } else {
                drawExitConfirmation();
            }
        }
    }

    public void drawTitleScreen() {
        if (titleBg != null) g2.drawImage(titleBg, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        
        //Starting height for the menu 
        int y = gp.tileSize * 6;

        //Start New Term
        int x = getXforCenteredText("START NEW TERM");
        drawOption("START NEW TERM", x, y, 0);

        //Instructions
        y += gp.tileSize;
        x = getXforCenteredText("INSTRUCTIONS");
        drawOption("INSTRUCTIONS", x, y, 1);

        //View Reports
        y += gp.tileSize;
        x = getXforCenteredText("VIEW REPORTS");
        drawOption("VIEW REPORTS", x, y, 2);

        //Exit
        y += gp.tileSize;
        x = getXforCenteredText("EXIT");
        drawOption("EXIT", x, y, 3);
    }

    public void drawExitConfirmation() {
    	
        //Background and Overlay
        if (titleBg != null) 
        	g2.drawImage(titleBg, 0, 0, gp.screenWidth, gp.screenHeight, null);
        
        
        //Adding a dark semi-transparent tint to make the white text readable
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //Main Question
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        String text = "Are you sure you want to exit?";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2; // Position text in the middle of the screen

        //Draw shadow for depth
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 3, y + 3);
        // Draw main text
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        //Interactive Buttons (YES/NO)
        // Shift y down to place buttons below the question
        y += gp.tileSize * 2;
        int centerX = gp.screenWidth / 2;

        // Use indices 4 and 5 to distinguish from the 0-3 indices used in the main menu
        // YES is drawn to the left of centre, NO to the right
        drawOption("YES", centerX - 120, y, 4); 
        drawOption("NO", centerX + 60, y, 5);
    }
    
    private void drawOption(String text, int x, int y, int index) {
        //Shadow
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 3, y + 3);
        
        //Hover Colour Change
        if (commandNum == index) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}