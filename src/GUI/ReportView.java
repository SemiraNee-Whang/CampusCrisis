package GUI;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import main.GamePanel;

public class ReportView {
    GamePanel gp;
    public Rectangle backBtn;
    ArrayList<String[]> gameHistory = new ArrayList<>();

    public ReportView(GamePanel gp) {
        this.gp = gp;
        //Positioned at the bottom centre
        this.backBtn = new Rectangle(gp.screenWidth/2 - 50, gp.screenHeight - 80, 100, 40);
    }

  //Reads the history file and stores it in an ArrayList for display
    public void loadGameHistory() {
        gameHistory.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("game_history.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split by the pipe character
                gameHistory.add(line.split("\\|"));
            }
        } catch (Exception e) {
            System.out.println("No history file found yet.");
        }
    }

    public void draw(Graphics2D g2) {
        //Background
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //Title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        g2.setColor(Color.WHITE);
        g2.drawString("PREVIOUS TERMS RECORDS", gp.tileSize, gp.tileSize);

        //Table Headers
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
        int y = gp.tileSize * 2;
        int startX = gp.tileSize;
        
        g2.setColor(Color.YELLOW);
        g2.drawString("PRESIDENT", startX, y);
        g2.drawString("FINAL BUDGET", startX + 200, y);
        g2.drawString("APPROVAL", startX + 400, y);
        g2.drawString("STATUS", startX + 550, y);

        g2.drawLine(startX, y + 10, gp.screenWidth - gp.tileSize, y + 10);

        //Draw Table Rows
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16F));
        g2.setColor(Color.WHITE);
        
        int rowHeight = 35;
        y += rowHeight + 10;

        for (int i = 0; i < gameHistory.size(); i++) {
            String[] data = gameHistory.get(i);
            if (data.length >= 4) {
                g2.drawString(data[0], startX, y);        // Name
                g2.drawString("R" + data[1], startX + 200, y); // Budget
                g2.drawString(data[2] + "%", startX + 400, y); // Approval
                g2.drawString(data[3], startX + 550, y);  // Outcome (e.g. Completed/Failed)
                
                y += rowHeight;
            }
            //Limit display to fit screen
            if (y > gp.screenHeight - 100) break;
        }

        //Calls Back Button
        drawBackButton(g2);
    }

    private void drawBackButton(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fill(backBtn);
        g2.setColor(Color.WHITE);
        g2.draw(backBtn);
        g2.drawString("BACK", backBtn.x + 25, backBtn.y + 25);
    }
}