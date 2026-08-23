package Admin;

import java.awt.*;
import main.GamePanel;
import main.Validation;

//Handles the Admin Login Screen
public class AdminLogin {

    private GamePanel gp;

    //Stores admin username and password entered by the user
    public String username = "";
    public String password = "";

    //Tracks which textbox is selected
    //0 = Username, 1 = Password
    public int activeField = 0;

    //Displays an error message if login details are incorrect
    public String errorMessage = "";

    //Buttons and textboxes
    public Rectangle usernameBox;
    public Rectangle passwordBox;
    public Rectangle loginBtn;
    public Rectangle backBtn;
    
    public boolean loginPressed = false;

    public AdminLogin(GamePanel gp) {
        this.gp = gp;

        //Username textbox
        usernameBox = new Rectangle(
                gp.screenWidth / 2 - 150,
                180,
                300,
                45);

        //Password textbox
        passwordBox = new Rectangle(
                gp.screenWidth / 2 - 150,
                280,
                300,
                45);

        //Login button
        loginBtn = new Rectangle(
                gp.screenWidth / 2 - 75,
                370,
                150,
                45);

        //Back button
        backBtn = new Rectangle(
                20,
                20,
                100,
                40);
    }

    //Draws the Admin Login screen
    public void draw(Graphics2D g2) {

        //Background
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //Title
        g2.setColor(Color.WHITE	);
        g2.setFont(new Font("Arial", Font.BOLD, 32));

        String title = "ADMIN LOGIN";
        int titleX = (gp.screenWidth - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, 100);

        //Username label
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Username", gp.screenWidth / 2 - 150, 165);

        //Username textbox
        drawTextBox(g2, usernameBox,username, activeField == 0);
        
        //Password label
        g2.drawString("Password", gp.screenWidth / 2 - 150, 265);

        //Hide password using asterisks
        String hiddenPassword = "";
        for (int i = 0; i < password.length(); i++) {
            hiddenPassword += "*";
        }

        //Password textbox
        drawTextBox(g2, passwordBox, hiddenPassword,activeField == 1);
        //Login button
        g2.setColor(Color.YELLOW);
        g2.fill(loginBtn);

        g2.setColor(Color.BLACK);
        g2.draw(loginBtn);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("LOGIN", loginBtn.x + 45, loginBtn.y + 28);

        //Shows error message if login is incorrect
        if (!errorMessage.equals("")) {
            g2.setColor(Color.RED);
            g2.drawString(errorMessage,
                    gp.screenWidth / 2 - 120,
                    450);
        }
        
      //Glow effect when Login button is pressed
        if (loginPressed) {
            g2.setColor(new Color(255, 215, 0, 80));
            g2.fillRoundRect(
                    loginBtn.x - 6,
                    loginBtn.y - 6,
                    loginBtn.width + 12,
                    loginBtn.height + 12,
                    12,
                    12);
        }

        //Back button
        g2.setColor(Color.GRAY);
        g2.fill(backBtn);

        g2.setColor(Color.WHITE);
        g2.draw(backBtn);

        g2.drawString("BACK", backBtn.x + 25, backBtn.y + 25);
    }

  //Draws a reusable text box
  //Draws a reusable text box
    private void drawTextBox(Graphics2D g2, Rectangle box, String text, boolean active) {

        //Glow around selected textbox
        if (active) {
            g2.setColor(new Color(255, 215, 0, 80));
            g2.fillRoundRect(
                    box.x - 6,
                    box.y - 6,
                    box.width + 12,
                    box.height + 12,
                    12,
                    12);
        }

        //Text box background
        g2.setColor(new Color(40, 40, 50));
        g2.fill(box);

        //Text box border
        if (active) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));
        } else {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1));
        }

        g2.draw(box);

        //Text inside textbox
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString(text, box.x + 10, box.y + 28);
    }
    
    /**
     * Validates the entered admin login details
     * using the backend AdminStorage class.
     */
    public boolean validateLogin() {

        if (!Validation.isValidString(username)) {
            errorMessage = "Please enter a username.";
            return false;
        }

        if (!Validation.isValidString(password)) {
            errorMessage = "Please enter a password.";
            return false;
        }

        boolean valid =
                gp.adminStorage.validateAdmin(
                        username,
                        password);

        if (valid) {

            errorMessage = "";
            return true;

        } else {

            errorMessage =
                    "Incorrect username or password.";

            return false;
        }
    }
}