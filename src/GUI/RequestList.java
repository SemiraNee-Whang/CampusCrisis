package GUI;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import main.GamePanel;
import main.Request;

public class RequestList {
    GamePanel gp;
    
    // VISIBILITY: These must be public for MouseHandler to see them
    public Request currentRequest;
    public ArrayList<Request> allRequests = new ArrayList<>();
    public ArrayList<Request> history = new ArrayList<>(); 
    
    public boolean showButtons = false;
    
    public Rectangle approveBtn, declineBtn, postponeBtn;
    private Random random = new Random();

    public RequestList(GamePanel gp) {
        this.gp = gp;
        
        // 1. Load the data from your text file
        loadRequests();
        
        // 2. Initialise the first random request
        getNextRandomRequest();

        // 3. Define Button hitboxes
        approveBtn = new Rectangle(gp.tileSize * 3, gp.screenHeight - 120, 140, 45);
        declineBtn = new Rectangle(gp.tileSize * 6, gp.screenHeight - 120, 140, 45);
        postponeBtn = new Rectangle(gp.tileSize * 9, gp.screenHeight - 120, 140, 45);
    }

    private void loadRequests() {
        try {
            InputStream is = getClass().getResourceAsStream("/requests.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    allRequests.add(new Request(
                        parts[0], parts[1], parts[2], 
                        Integer.parseInt(parts[3]), 
                        Integer.parseInt(parts[4])
                    ));
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error loading requests: " + e.getMessage());
        }
    }

    // VISIBILITY: Public so MouseHandler can call it after a decision
    public void getNextRandomRequest() {
        if (!allRequests.isEmpty()) {
            int index = random.nextInt(allRequests.size());
            currentRequest = allRequests.get(index);
        } else {
            currentRequest = null;
        }
    }

    public void draw(Graphics2D g2) {
        // --- DRAWING THE UI ---
        // White Background Box
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 4, 15, 15);
        
        // Border for the request row
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(gp.tileSize * 2 + 10, gp.tileSize * 2 + 10, gp.screenWidth - gp.tileSize * 4 - 20, gp.tileSize * 3, 10, 10);

        if (currentRequest != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString(currentRequest.description, gp.tileSize * 2 + 30, gp.tileSize * 3 + 10);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.GRAY);
            g2.drawString("Category: " + currentRequest.category, gp.tileSize * 2 + 30, gp.tileSize * 3 + 35);
        }

        // BUTTON LOGIC
        if (showButtons && currentRequest != null) {
            // Updated to Yellow buttons
            Color btnYellow = new Color(255, 215, 0); // Gold-ish Yellow
            drawStyledButton(g2, approveBtn, "APPROVE", btnYellow);
            drawStyledButton(g2, declineBtn, "DECLINE", btnYellow);
            drawStyledButton(g2, postponeBtn, "POSTPONE", btnYellow);
        } else {
            g2.setFont(new Font("Arial", Font.ITALIC, 14));
            g2.setColor(Color.GRAY);
            g2.drawString("Click the request area to reveal actions...", gp.tileSize * 2 + 30, gp.screenHeight - 100);
        }
    }

    private void drawStyledButton(Graphics2D g2, Rectangle r, String text, Color bgColor) {
        // Shadow effect for a "Decision Button" feel
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRect(r.x + 3, r.y + 3, r.width, r.height);

        // Main Button Body
        g2.setColor(bgColor); 
        g2.fill(r);
        
        // Button Border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(r);

        // Text (Centred)
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textX = r.x + (r.width - fm.stringWidth(text)) / 2;
        int textY = r.y + (r.height + fm.getAscent()) / 2 - 2;
        g2.drawString(text, textX, textY);
    }
}