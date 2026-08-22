package Admin;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import main.GamePanel;

//Handles viewing and managing users from the login and sign up text file
public class UserManager {

    GamePanel gp;

    //Stores user data read from the login text file
    public ArrayList<String[]> userData = new ArrayList<>();

    //Buttons on the User Management screen
    public Rectangle addBtn;
    public Rectangle editBtn;
    public Rectangle deleteBtn;
    public Rectangle backBtn;

    //Used to scroll through users
    public int scrollOffset = 0;

    public UserManager(GamePanel gp) {
        this.gp = gp;

        //Add User button
        addBtn = new Rectangle(
                120,
                gp.screenHeight - 80,
                120,
                40);

        //Edit User button
        editBtn = new Rectangle(
                270,
                gp.screenHeight - 80,
                120,
                40);

        //Delete User button
        deleteBtn = new Rectangle(
                420,
                gp.screenHeight - 80,
                120,
                40);

        //Back button
        backBtn = new Rectangle(
                570,
                gp.screenHeight - 80,
                120,
                40);

        //Loads users when the screen is created
        loadUsers();
    }

  //Reads all users from the login text file
    public void loadUsers() {

        //Clears old data before reloading
        userData.clear();

        try (BufferedReader br =
                new BufferedReader(new FileReader("Log in & Sign Up.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                //Format:
                //username,password
                String[] parts = line.split(",");

                if (parts.length >= 2) {
                    userData.add(parts);
                }
            }

        } catch (Exception e) {

            System.out.println("Log in & Sign Up.txt could not be found.");
            e.printStackTrace();
        }
    }

    //Draws the User Management screen
    public void draw(Graphics2D g2) {

        //Background
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(
                0,
                0,
                gp.screenWidth,
                gp.screenHeight);

        //Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                30));

        String title = "MANAGE USERS";

        int titleX =
                (gp.screenWidth
                        - g2.getFontMetrics()
                        .stringWidth(title)) / 2;

        g2.drawString(
                title,
                titleX,
                70);

        //Table Background
        int tableX = 120;
        int tableY = 110;
        int tableWidth = gp.screenWidth - 240;
        int tableHeight = gp.screenHeight - 230;

        g2.setColor(Color.WHITE);
        g2.fillRect(
                tableX,
                tableY,
                tableWidth,
                tableHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(
                tableX,
                tableY,
                tableWidth,
                tableHeight);

        //Table Headers
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                16));

        int headerY = tableY + 35;

        g2.drawString(
                "USERNAME",
                tableX + 50,
                headerY);

        g2.drawString(
                "PASSWORD",
                tableX + 400,
                headerY);

        //Header Line
        g2.drawLine(
                tableX + 20,
                headerY + 10,
                tableX + tableWidth - 20,
                headerY + 10);

        //User Rows
        g2.setFont(new Font(
                "Arial",
                Font.PLAIN,
                15));

        int rowHeight = 35;

        for (int i = 0; i < userData.size(); i++) {

            String[] data = userData.get(i);

            int rowY =
                    headerY
                    + 45
                    + (i * rowHeight)
                    - scrollOffset;

            //Only draws users inside the table
            if (rowY > headerY + 20
                    && rowY < tableY + tableHeight - 10) {

                g2.drawString(
                        data[0].trim(),
                        tableX + 50,
                        rowY);

                g2.drawString(
                        data[1].trim(),
                        tableX + 400,
                        rowY);
            }
        }

        //Draws the Admin buttons
        drawButton(g2, addBtn, "ADD");
        drawButton(g2, editBtn, "EDIT");
        drawButton(g2, deleteBtn, "DELETE");
        drawButton(g2, backBtn, "BACK");
    }

    //Draws a reusable button
    private void drawButton(
            Graphics2D g2,
            Rectangle button,
            String text) {

        g2.setColor(Color.YELLOW);
        g2.fill(button);

        g2.setColor(Color.BLACK);
        g2.draw(button);

        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                14));

        FontMetrics fm = g2.getFontMetrics();

        int textX =
                button.x
                + (button.width
                - fm.stringWidth(text)) / 2;

        int textY =
                button.y
                + (button.height
                + fm.getAscent()) / 2
                - 2;

        g2.drawString(
                text,
                textX,
                textY);
    }
}