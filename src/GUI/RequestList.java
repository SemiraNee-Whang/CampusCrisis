package GUI;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import main.GamePanel;
import main.Request;

public class RequestList {
    GamePanel gp;
    public Request currentRequest;
    public ArrayList<Request> allRequests = new ArrayList<>();
    public ArrayList<Request> history = new ArrayList<>(); 
    public boolean showButtons = false;
    public Rectangle approveBtn, declineBtn, postponeBtn;

    public RequestList(GamePanel gp) {
        this.gp = gp;
        loadRequests(); 
        
        if (!allRequests.isEmpty()) {
            currentRequest = allRequests.get(0);
        }

        // Button positioning (matching your screenshot layout)
        int btnWidth = 140;
        int btnHeight = 45;
        int yPos = gp.screenHeight - 120;
        
        approveBtn = new Rectangle(gp.tileSize * 3, yPos, btnWidth, btnHeight);
        declineBtn = new Rectangle(gp.tileSize * 6, yPos, btnWidth, btnHeight);
        postponeBtn = new Rectangle(gp.tileSize * 9, yPos, btnWidth, btnHeight);
    }

    // --- LOGIC FOR BUTTON CLICKS ---
    public void handleInput(int mouseX, int mouseY) {
        if (!showButtons || currentRequest == null) {
            // If buttons aren't showing, clicking the area reveals them
            showButtons = true;
            return;
        }

        if (approveBtn.contains(mouseX, mouseY)) {
            processRequest("APPROVED");
        } else if (declineBtn.contains(mouseX, mouseY)) {
            processRequest("DECLINED");
        } else if (postponeBtn.contains(mouseX, mouseY)) {
            postponeRequest();
        }
    }

    private void processRequest(String status) {
        System.out.println("Request " + status + ": " + currentRequest.requestName);
        
        // 1. Move current to history
        history.add(currentRequest);
        
        // 2. Remove from the pending list
        allRequests.remove(currentRequest);
        
        // 3. Load the next one or clear if empty
        nextRequest();
    }

    private void postponeRequest() {
        System.out.println("Request POSTPONED: " + currentRequest.requestName);
        
        // Move to the back of the queue
        allRequests.remove(currentRequest);
        allRequests.add(currentRequest);
        
        nextRequest();
    }

    private void nextRequest() {
        if (!allRequests.isEmpty()) {
            currentRequest = allRequests.get(0);
        } else {
            currentRequest = null;
        }
        showButtons = false; // Hide buttons until the next click
    }

    // --- LOADING & DRAWING ---
    private void loadRequests() {
        try (InputStream is = getClass().getResourceAsStream("/requests.txt")) {
            if (is == null) return;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        try {
                            allRequests.add(new Request(parts[0].trim(), parts[1].trim(), parts[2].trim(), 
                                           Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim())));
                        } catch (Exception e) { /* Skip bad lines */ }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void draw(Graphics2D g2) {
        // UI Box
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 5, 15, 15);
        
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(gp.tileSize * 2 + 10, gp.tileSize * 2 + 10, gp.screenWidth - gp.tileSize * 4 - 20, gp.tileSize * 5 - 20, 10, 10);

        if (currentRequest != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            String text = currentRequest.description != null ? currentRequest.description : currentRequest.requestName;
            drawWrappedText(g2, text, gp.tileSize * 2 + 30, gp.tileSize * 2 + 60, gp.screenWidth - gp.tileSize * 5);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            g2.setColor(Color.GRAY);
            g2.drawString("Category: " + currentRequest.category, gp.tileSize * 2 + 30, gp.tileSize * 2 + 110);
        }

        if (showButtons && currentRequest != null) {
            Color btnYellow = new Color(255, 215, 0); 
            drawStyledButton(g2, approveBtn, "APPROVE", btnYellow);
            drawStyledButton(g2, declineBtn, "DECLINE", btnYellow);
            drawStyledButton(g2, postponeBtn, "POSTPONE", btnYellow);
        }
    }

    private void drawWrappedText(Graphics2D g2, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentY = y;
        for (String word : words) {
            if (fm.stringWidth(line + word) < maxWidth) {
                line.append(word).append(" ");
            } else {
                g2.drawString(line.toString(), x, currentY);
                line = new StringBuilder(word + " ");
                currentY += fm.getHeight();
            }
        }
        g2.drawString(line.toString(), x, currentY);
    }

    private void drawStyledButton(Graphics2D g2, Rectangle r, String text, Color bgColor) {
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRect(r.x + 3, r.y + 3, r.width, r.height);
        g2.setColor(bgColor); 
        g2.fill(r);
        g2.setColor(Color.BLACK);
        g2.draw(r);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, r.x + (r.width - fm.stringWidth(text)) / 2, r.y + (r.height + fm.getAscent()) / 2 - 2);
    }
}