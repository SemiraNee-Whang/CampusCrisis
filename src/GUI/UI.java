package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import main.GamePanel;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    public int commandNum = 0; 

    public UI(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
    }

    public void drawTitleScreen() {
        // BACKGROUND
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        String text = "CAMPUS CRISIS";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        // JESSIE PREVIEW
        x = gp.screenWidth / 2 - (gp.tileSize);
        y += gp.tileSize * 1.5;
        g2.drawImage(gp.player.downNeutral, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // MENU OPTIONS
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) g2.drawString(">", x - gp.tileSize, y);

        text = "EXIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) g2.drawString(">", x - gp.tileSize, y);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}