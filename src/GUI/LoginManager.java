package GUI;

import java.awt.*;
import java.io.*;
import java.util.Scanner;
import main.GamePanel;

public class LoginManager {
    GamePanel gp;
    public String userText = "";
    public String passText = "";
    public int subState = -1; // -1: None, 0: User, 1: Pass, 2: Action, 3: Switch
    public boolean isSignUp = false;
    public String message = "";

    public LoginManager(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(20, 20, 30));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
        g2.setColor(Color.WHITE);
        String title = isSignUp ? "SIGN UP" : "LOG IN";
        g2.drawString(title, gp.tileSize * 6, gp.tileSize * 2);

        g2.setFont(g2.getFont().deriveFont(24F));
        drawField(g2, "Username:", userText, gp.tileSize * 4, subState == 0);
        drawField(g2, "Password:", "*".repeat(passText.length()), gp.tileSize * 6, subState == 1);

        String actionBtn = isSignUp ? "CREATE ACCOUNT" : "LOGIN";
        g2.setColor(subState == 2 ? Color.YELLOW : Color.WHITE);
        g2.drawString(actionBtn, gp.tileSize * 6, gp.tileSize * 8);

        String switchLink = isSignUp ? "Go to Login" : "No account? Sign Up";
        g2.setFont(g2.getFont().deriveFont(18F));
        g2.setColor(subState == 3 ? Color.YELLOW : Color.GRAY);
        g2.drawString(switchLink, gp.tileSize * 6, gp.tileSize * 9);

        g2.setColor(Color.ORANGE);
        g2.drawString(message, gp.tileSize * 5, gp.tileSize * 3);
    }

    private void drawField(Graphics2D g2, String label, String text, int y, boolean active) {
        g2.setColor(Color.WHITE);
        g2.drawString(label, gp.tileSize * 2, y);
        g2.setColor(active ? Color.YELLOW : Color.WHITE);
        g2.drawRect(gp.tileSize * 5, y - 30, gp.tileSize * 8, 40);
        g2.drawString(text + (active ? "_" : ""), gp.tileSize * 5 + 10, y);
    }

    public boolean validateLogin() {
        boolean userFound = false;
        File file = new File("Log in & Sign Up.txt");
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String[] data = s.nextLine().split(",");
                if (data.length == 2) {
                    if (data[0].equals(userText)) {
                        userFound = true;
                        if (data[1].equals(passText)) return true;
                    }
                }
            }
            message = userFound ? "Incorrect password" : "Incorrect username";
        } catch (FileNotFoundException e) {
            message = "Log in & Sign Up.txt not found!";
        }
        return false;
    }

    public void registerUser() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Log in & Sign Up.txt", true))) {
            if (userText.isEmpty() || passText.isEmpty()) { message = "Fields empty!"; return; }
            bw.write(userText + "," + passText);
            bw.newLine();
            message = "Success! Please Login.";
            isSignUp = false; userText = ""; passText = "";
        } catch (IOException e) { message = "Error writing file!"; }
    }
}