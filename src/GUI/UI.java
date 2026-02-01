package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

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
        
        // Starting height for the menu 
        int y = gp.tileSize * 6;

        //START NEW TERM
        int x = getXforCenteredText("START NEW TERM");
        drawOption("START NEW TERM", x, y, 0);

        //INSTRUCTIONS
        y += gp.tileSize;
        x = getXforCenteredText("INSTRUCTIONS");
        drawOption("INSTRUCTIONS", x, y, 1);

        //VIEW REPORTS
        y += gp.tileSize;
        x = getXforCenteredText("VIEW REPORTS");
        drawOption("VIEW REPORTS", x, y, 2);

        // EXIT
        y += gp.tileSize;
        x = getXforCenteredText("EXIT");
        drawOption("EXIT", x, y, 3);
    }

    public void drawExitConfirmation() {
        if (titleBg != null) g2.drawImage(titleBg, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        String text = "Are you sure you want to exit?";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 - gp.tileSize;

        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 3, y + 3);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        y += gp.tileSize * 2;
        drawOption("YES", x + gp.tileSize * 2, y, 0);
        drawOption("NO", x + gp.tileSize * 6, y, 1);
    }

    private void drawOption(String text, int x, int y, int index) {
        // Shadow
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 3, y + 3);
        
        // Hover Color Change
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