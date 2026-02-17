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
    public Request currentRequest;
    public ArrayList<Request> allRequests = new ArrayList<>();
    public ArrayList<Request> history = new ArrayList<>(); 
    public boolean showButtons = false;
    public Rectangle approveBtn, declineBtn, postponeBtn;
    private Random random = new Random();

    public RequestList(GamePanel gp) {
        this.gp = gp;
        loadRequests(); // Loads normal requests
        getNextRandomRequest();

        approveBtn = new Rectangle(gp.tileSize * 3, gp.screenHeight - 120, 140, 45);
        declineBtn = new Rectangle(gp.tileSize * 6, gp.screenHeight - 120, 140, 45);
        postponeBtn = new Rectangle(gp.tileSize * 9, gp.screenHeight - 120, 140, 45);
    }

    private void loadRequests() {
        try {
            // Looking for requests.txt in the res folder
            InputStream is = getClass().getResourceAsStream("/requests.txt");
            if (is == null) {
                System.out.println("Could not find requests.txt in res folder!");
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    // Normal requests have: ID|Description|Category|Cost|Impact
                    allRequests.add(new Request(
                        parts[0].trim(), 
                        parts[1].trim(), 
                        parts[2].trim(), 
                        Integer.parseInt(parts[3].trim()), 
                        Integer.parseInt(parts[4].trim())
                    ));
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error loading requests: " + e.getMessage());
        }
    }

    public void getNextRandomRequest() {
        // Logic to alternate between normal requests and random events
        // For now, let's keep it pulling from the normal list
        if (!allRequests.isEmpty()) {
            int index = random.nextInt(allRequests.size());
            currentRequest = allRequests.get(index);
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 4, 15, 15);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(gp.tileSize * 2 + 10, gp.tileSize * 2 + 10, gp.screenWidth - gp.tileSize * 4 - 20, gp.tileSize * 3, 10, 10);

        if (currentRequest != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString(currentRequest.description != null ? currentRequest.description : currentRequest.requestName, 
                         gp.tileSize * 2 + 30, gp.tileSize * 3 + 10);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.GRAY);
            g2.drawString("Category: " + currentRequest.category, gp.tileSize * 2 + 30, gp.tileSize * 3 + 35);
        }

        if (showButtons && currentRequest != null) {
            Color btnYellow = new Color(255, 215, 0); 
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
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRect(r.x + 3, r.y + 3, r.width, r.height);
        g2.setColor(bgColor); 
        g2.fill(r);
        g2.setColor(Color.BLACK);
        g2.draw(r);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textX = r.x + (r.width - fm.stringWidth(text)) / 2;
        int textY = r.y + (r.height + fm.getAscent()) / 2 - 2;
        g2.drawString(text, textX, textY);
    }
}