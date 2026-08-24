package GUI;

import java.awt.*;
import main.GamePanel;
import main.Validation;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginManager.
 */
public class LoginManager {
    
    /** The gp. */
    private GamePanel gp;
    
    /** The user text. */
    public String userText = "";
    
    /** The pass text. */
    public String passText = "";
    
    /** The sub state. */
    public int subState = -1; // -1: None, 0: User, 1: Pass, 2: Action, 3: Switch
    
    /** The is sign up. */
    public boolean isSignUp = false;
    
    /** The message. */
    public String message = "";
    
    /** The active field. */
    public int activeField = -1;

    /**
     * Instantiates a new login manager.
     *
     * @param gp the gp
     */
    public LoginManager(GamePanel gp) {
        this.gp = gp;
    }
    
    /**
     * Draw.
     *
     * @param g2 the g 2
     */
    public void draw(Graphics2D g2) {
        //Fills the background with a dark theme colour
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //Back Button
        // Sets the font size for the navigation button
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        
        // Changes the text colour to yellow if the mouse is hovering over the back button 
        if (subState == 4) {
            g2.setColor(Color.YELLOW);
        } else {
            // Uses a standard white color to keep it visible against the dark background
            g2.setColor(Color.WHITE);
        }
        // Renders the back button at the top-left of the screen
        g2.drawString("< BACK", 20, 40);

        //Title
        // Sets the font and determines if the screen should display "SIGN UP" or "LOG IN"
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        g2.setColor(Color.WHITE);
        String title = isSignUp ? "SIGN UP" : "LOG IN";
        g2.drawString(title, gp.tileSize * 6, gp.tileSize * 2);

        //Input Fields
        // Draws the username and password fields using the drawField helper method
        g2.setFont(g2.getFont().deriveFont(24F));
        drawField(g2, "Username:", userText, gp.tileSize * 4, activeField == 0);
        drawField(g2, "Password:", "*".repeat(passText.length()), gp.tileSize * 6, activeField == 1);

        //Action Button
        // Displays the main button for logging in or creating an account
        String actionBtn = isSignUp ? "CREATE ACCOUNT" : "LOGIN";
        // Highlights the button in yellow if the mouse is hovering over it (subState 2)
        g2.setColor(subState == 2 ? Color.YELLOW : Color.WHITE);
        g2.drawString(actionBtn, gp.tileSize * 6, gp.tileSize * 8);

        //Switch Link
        // Displays a link to toggle between the Login and Sign-Up states
        String switchLink = isSignUp ? "Go to Login" : "No account? Sign Up";
        g2.setFont(g2.getFont().deriveFont(18F));
        // Highlights the link in yellow if the mouse is hovering over it (subState 3)
        g2.setColor(subState == 3 ? Color.YELLOW : Color.GRAY);
        g2.drawString(switchLink, gp.tileSize * 6, gp.tileSize * 9);

        //Error/Success Message
        //Displays feedback messages (like "Account Created" or "Invalid Login") in orange
        g2.setColor(Color.ORANGE);
        g2.drawString(message, gp.tileSize * 5, gp.tileSize * 3);
    }

    /**
     * Draw field.
     *
     * @param g2 the g 2
     * @param label the label
     * @param text the text
     * @param y the y
     * @param active the active
     */
    private void drawField(Graphics2D g2, String label, String text, int y, boolean active) {
        g2.setColor(Color.WHITE);
        g2.drawString(label, gp.tileSize * 2, y);
        g2.setColor(active ? Color.YELLOW : Color.WHITE);
        g2.drawRect(gp.tileSize * 5, y - 30, gp.tileSize * 8, 40);
        g2.drawString(text + (active ? "_" : ""), gp.tileSize * 5 + 10, y);
    }

    /**
     * Validate login.
     *
     * @return true, if successful
     */
    public boolean validateLogin() {

        if (!Validation.isValidString(userText)) {
            message = "Please enter your username.";
            return false;
        }

        if (!Validation.isValidString(passText)) {
            message = "Please enter your password.";
            return false;
        }

        boolean valid =
                gp.userStorage.validateLogin(
                        userText,
                        passText);

        if (valid) {
            message = "";
            return true;
        }

        //Gives a more useful error
        if (!gp.userStorage.userExists(userText)) {
            message = "Incorrect username.";
        } else {
            message = "Incorrect password.";
        }

        return false;
    }

    /**
     * Register user.
     */
    public void registerUser() {

        if (!Validation.isValidString(userText)) {
            message = "Please enter a username.";
            return;
        }

        if (!Validation.isValidString(passText)) {
            message = "Please enter a password.";
            return;
        }

        if (gp.userStorage.userExists(userText)) {
            message = "Username already exists.";
            return;
        }

        boolean added =
                gp.userStorage.addUser(
                        userText,
                        passText);

        if (added) {

            message = "Success! Please Login.";

            isSignUp = false;
            userText = "";
            passText = "";

        } else {

            message = "Could not create account.";
        }
    }
}