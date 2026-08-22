package Admin;

import java.awt.*;
import main.GamePanel;

//Handles the main Admin Menu after successful login
public class AdminMenu {

    GamePanel gp;

    //Buttons used on the Admin Menu
    public Rectangle manageRequestsBtn;
    public Rectangle manageUsersBtn;
    public Rectangle backBtn;

    public AdminMenu(GamePanel gp) {
        this.gp = gp;

        //Button used to open Request Management
        manageRequestsBtn = new Rectangle(
                gp.screenWidth / 2 - 150,
                220,
                300,
                50);

        //Button used to open User Management
        manageUsersBtn = new Rectangle(
                gp.screenWidth / 2 - 150,
                310,
                300,
                50);

        //Back button returns to the Main Menu
        backBtn = new Rectangle(
                20,
                20,
                100,
                40);
    }

    //Draws the Admin Menu screen
    public void draw(Graphics2D g2) {

        //Background
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 32));

        String title = "ADMIN PANEL";

        int titleX = (gp.screenWidth
                - g2.getFontMetrics().stringWidth(title)) / 2;

        g2.drawString(title, titleX, 120);

        //Manage Requests Button
        drawButton(
                g2,
                manageRequestsBtn,
                "MANAGE REQUESTS");

        //Manage Users Button
        drawButton(
                g2,
                manageUsersBtn,
                "MANAGE USERS");

        //Back Button
        g2.setColor(Color.GRAY);
        g2.fill(backBtn);

        g2.setColor(Color.WHITE);
        g2.draw(backBtn);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString(
                "BACK",
                backBtn.x + 25,
                backBtn.y + 25);
    }

    //Draws a reusable Admin Menu button
    private void drawButton(
            Graphics2D g2,
            Rectangle button,
            String text) {

        g2.setColor(Color.YELLOW);
        g2.fill(button);

        g2.setColor(Color.BLACK);
        g2.draw(button);

        g2.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fm = g2.getFontMetrics();

        int textX = button.x
                + (button.width - fm.stringWidth(text)) / 2;

        int textY = button.y
                + (button.height + fm.getAscent()) / 2 - 3;

        g2.drawString(text, textX, textY);
    }
}