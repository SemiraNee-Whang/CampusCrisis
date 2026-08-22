package Admin;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import main.GamePanel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JOptionPane;

//Handles viewing and managing users from the login and sign up text file
public class UserManager {

    private GamePanel gp;

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
                new BufferedReader(
                        new FileReader("Log in & Sign Up.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                //Removes unnecessary spaces
                line = line.trim();

                //Ignores empty lines
                if (!line.isEmpty()) {

                    //Format:
                    //username,password
                    String[] parts = line.split(",", 2);

                    if (parts.length == 2) {

                        userData.add(new String[]{
                            parts[0].trim(),
                            parts[1].trim()
                        });
                    }
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "Log in & Sign Up.txt could not be loaded.");

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
    
  //Adds a new user to Log in & Sign Up.txt
    public void addUser() {

        //Asks the admin to enter a username
        String username = JOptionPane.showInputDialog(
                null,
                "Enter Username:");

        //Stops if Cancel is clicked or nothing is entered
        if (username == null || username.trim().isEmpty()) {
            return;
        }

        //Checks that the username does not already exist
        for (String[] user : userData) {

            if (user.length >= 2
                    && user[0].trim().equalsIgnoreCase(username.trim())) {

                JOptionPane.showMessageDialog(
                        null,
                        "That username already exists.",
                        "Duplicate Username",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }
        }

        //Asks the admin to enter a password
        String password = JOptionPane.showInputDialog(
                null,
                "Enter Password:");

        //Stops if Cancel is clicked or nothing is entered
        if (password == null || password.trim().isEmpty()) {
            return;
        }

        try (BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter(
                                "Log in & Sign Up.txt",
                                true))) {

            //Format:
            //username,password
            bw.write(
                    username.trim()
                    + ","
                    + password.trim());

            bw.newLine();
            bw.flush();


            //Reloads users so the new user appears immediately
            loadUsers();

            scrollOffset = getMaxScroll();

          //Automatically scrolls to the newest user
            JOptionPane.showMessageDialog(
                    null,
                    "User added successfully.");
            
        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Could not add user.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
        }
    }
    
  //Edits an existing user in Log in & Sign Up.txt
    public void editUser() {

        //Asks the admin which username must be edited
        String searchUsername = JOptionPane.showInputDialog(
                null,
                "Enter the Username you want to edit:");

        //Stops if Cancel is clicked or nothing is entered
        if (searchUsername == null || searchUsername.trim().isEmpty()) {
            return;
        }

        boolean found = false;

        //Searches through all users
        for (int i = 0; i < userData.size(); i++) {

            String[] user = userData.get(i);

            if (user.length >= 2
                    && user[0].trim().equalsIgnoreCase(searchUsername.trim())) {

                found = true;

                //Stores the old values
                String oldUsername = user[0].trim();
                String oldPassword = user[1].trim();

                //Asks for the new username
                String newUsername = JOptionPane.showInputDialog(
                        null,
                        "Enter new Username:",
                        oldUsername);

                if (newUsername == null || newUsername.trim().isEmpty()) {
                    return;
                }

                //Checks that the new username does not belong to another user
                for (int j = 0; j < userData.size(); j++) {

                    if (j != i) {

                        String[] otherUser = userData.get(j);

                        if (otherUser.length >= 2
                                && otherUser[0].trim()
                                .equalsIgnoreCase(newUsername.trim())) {

                            JOptionPane.showMessageDialog(
                                    null,
                                    "That username already exists.",
                                    "Duplicate Username",
                                    JOptionPane.ERROR_MESSAGE);

                            return;
                        }
                    }
                }

                //Asks for the new password
                String newPassword = JOptionPane.showInputDialog(
                        null,
                        "Enter new Password:",
                        oldPassword);

                if (newPassword == null || newPassword.trim().isEmpty()) {
                    return;
                }

                //Updates the user inside the ArrayList
                userData.set(i, new String[]{
                    newUsername.trim(),
                    newPassword.trim()
                });

                //Updates the text file
                saveAllUsers();

                //Reloads users so the table updates
                loadUsers();

                JOptionPane.showMessageDialog(
                        null,
                        "User updated successfully.");

                break;
            }
        }

        //Shows an error if the username cannot be found
        if (!found) {

            JOptionPane.showMessageDialog(
                    null,
                    "Username not found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
  //Rewrites Log in & Sign Up.txt using the current ArrayList
    private void saveAllUsers() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("Log in & Sign Up.txt"))) {

            for (String[] user : userData) {

                if (user.length >= 2) {

                    //Format:
                    //username,password
                    bw.write(
                            user[0].trim()
                            + ","
                            + user[1].trim());

                    bw.newLine();
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Could not save user details.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
        }
    }	
    
  //Deletes an existing user from Log in & Sign Up.txt
    public void deleteUser() {

        //Asks the admin which username must be deleted
        String searchUsername = JOptionPane.showInputDialog(
                null,
                "Enter the Username you want to delete:");

        //Stops if Cancel is clicked or nothing is entered
        if (searchUsername == null || searchUsername.trim().isEmpty()) {
            return;
        }

        boolean found = false;

        //Searches through all users
        for (int i = 0; i < userData.size(); i++) {

            String[] user = userData.get(i);

            //Checks if the username matches
            if (user.length >= 2
                    && user[0].trim().equalsIgnoreCase(searchUsername.trim())) {

                found = true;

                //Asks the admin to confirm the deletion
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete user:\n"
                        + user[0].trim() + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                //Only deletes if YES is selected
                if (choice == JOptionPane.YES_OPTION) {

                    //Removes the user from the ArrayList
                    userData.remove(i);

                    //Updates Log in & Sign Up.txt
                    saveAllUsers();

                    //Reloads users so the table updates
                    loadUsers();

                    JOptionPane.showMessageDialog(
                            null,
                            "User deleted successfully.");
                }

                break;
            }
        }

        //Shows an error if the username cannot be found
        if (!found) {

            JOptionPane.showMessageDialog(
                    null,
                    "Username not found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
  //Calculates how far the User Manager table can scroll
    public int getMaxScroll() {

        int rowHeight = 35;

        //Matches the table dimensions used inside draw()
        int tableY = 110;
        int tableHeight = gp.screenHeight - 230;
        int headerY = tableY + 35;

        //Space available underneath the table heading
        int visibleHeight =
                (tableY + tableHeight - 10)
                - (headerY + 45);

        //Total height needed for all users
        int totalHeight =
                userData.size() * rowHeight;

        //Prevents negative scrolling
        return Math.max(0, totalHeight - visibleHeight);
    }
}