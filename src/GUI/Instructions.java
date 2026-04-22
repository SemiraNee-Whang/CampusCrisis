package GUI;

import java.awt.*;
import main.GamePanel;

public class Instructions {
    GamePanel gp;
    public int subState = 0; 
    public Rectangle nextBtn, backBtn;

    public Instructions(GamePanel gp) {
        this.gp = gp;
        backBtn = new Rectangle(gp.tileSize * 2, gp.screenHeight - 100, 120, 45);
        nextBtn = new Rectangle(gp.screenWidth - (gp.tileSize * 2) - 120, gp.screenHeight - 100, 120, 45);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(211, 211, 211)); 
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.BLACK);

        //Words for each page of the instructions
        switch (subState) {
            case 0: 
            	drawCenteredPage(g2, "Campus Crisis - Player Guide", "Welcome to the presidency! Let's learn the ropes."); 
            	break;
            case 1: 
            	drawCenteredPage(g2, "Welcome to Campus Crisis!", "You are the Student President, and it is your job to keep students happy while managing the school budget. "
            			+ "\n\nMake smart choices - every decision matters!");
            	break;
            case 2: drawLeftAlignedPage(g2, "Getting Started", "• Log in using your username and password.\n• Enter your President name."
            		+ "\n• You will be taken to the main dashboard where your term begins."); 
            	break;
            case 3: 
            	drawLeftAlignedPage(g2, "Dashboard", "On the dashboard you can see:\n\n• President Name - Your name."
            			+ "\n• Time - How much time you have left.\n• Budget - Your remaining money.\n• Approval - How happy the students are."
            			+ "\n\nControls:\nPress [1] - View requests | Press [2] - History | Press [ESC] - Back"); 
            	break;
            case 4: 
            	drawLeftAlignedPage(g2, "Student Requests", "Students will send requests such as:"
            			+ "\n• School dances | Sports funding\n• Cultural events | Campus improvements\n\nEach request affects Budget and Approval rating. "
            			+ "\nYou must choose to Approve or Decline!"); 
            	break;
            case 5: 
            	drawLeftAlignedPage(g2, "Approving Requests", "When you approve a request:\n• Your budget may decrease."
            			+ "\n• Your approval may increase.\n• A task is created and saved.\n\nBe careful - do not run out of money!"); 
            	break;
            case 6: 
            	drawLeftAlignedPage(g2, "Declining Requests", "When you decline:\n• You save money.\n• Approval usually drops."
            			+ "\n\nSometimes saying no is necessary - but too many declines can lose student support!"); 
            	break;
            case 7: 
            	drawLeftAlignedPage(g2, "Decision History", "View all past decisions including:\n• Request ID"
            			+ "\n• Status (Approved/Declined)\n• Budget and approval changes\n\nUse this to track your performance!"); 
            	break;
            case 8: 
            	drawCenteredPage(g2, "Winning the Game", "You succeed by:\n• Keeping approval above 0%\n• Managing your budget wisely"
            			+ "\n• Surviving until the timer ends\n\nAt the end, you will receive a final report!"); 
            	break;
        }

        //Logic and Design for buttons
        Color btnYellow = new Color(255, 215, 0);
        if (subState > 0) drawStyledButton(g2, backBtn, "BACK", btnYellow);
        String nextText = (subState == 8) ? "FINISH" : "NEXT";
        drawStyledButton(g2, nextBtn, nextText, btnYellow);
    }

    //Centre the font and for design 
    private void drawCenteredPage(Graphics2D g2, String title, String body) {
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        int titleX = getCenteredX(g2, title);
        g2.drawString(title, titleX, 120); 

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] lines = body.split("\n");
        int y = 220; 
        for (String line : lines) {
            int bodyX = getCenteredX(g2, line);
            g2.drawString(line, bodyX, y);
            y += 35;
        }
    }

    //Positioning and Design
    private void drawLeftAlignedPage(Graphics2D g2, String title, String body) {
        
        g2.setFont(new Font("Arial", Font.BOLD, 26));
        g2.drawString(title, 100, 100);

        
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        int y = 170;
        for (String line : body.split("\n")) {
            g2.drawString(line, 100, y);
            y += 40;
        }
    }

    //Button Design
    private void drawStyledButton(Graphics2D g2, Rectangle r, String text, Color bg) {
        g2.setColor(bg);
        g2.fill(r);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(r);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        int textX = r.x + (r.width - g2.getFontMetrics().stringWidth(text)) / 2;
        int textY = r.y + (r.height + g2.getFontMetrics().getAscent()) / 2 - 4;
        g2.drawString(text, textX, textY);
    }

    private int getCenteredX(Graphics2D g2, String text) {
        return gp.screenWidth / 2 - (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() / 2;
    }
}