package GUI;

import java.awt.*;
import java.util.ArrayList;
import main.GamePanel;
import main.Request;

//Handles loading, displaying and managing student requests during gameplay
public class RequestList {

    //Reference to the main GamePanel
    private GamePanel gp;

    //Stores the request currently being displayed
    public Request currentRequest;

    //Stores all requests that are still waiting to be handled
    public ArrayList<Request> allRequests = new ArrayList<>();

    //Stores requests that have already been handled
    public ArrayList<Request> history = new ArrayList<>();

    //Controls whether the Approve, Decline and Postpone buttons are visible
    public boolean showButtons = false;

    //Stores the clickable button areas
    public Rectangle approveBtn, declineBtn, postponeBtn;

    /**
     * Receives the GamePanel used by the RequestList.
     * Loads all requests from requests.txt and sets the first request
     * as the current request if requests are available.
     */
    public RequestList(GamePanel gp) {
        this.gp = gp;

        //Loads all requests from the text file
        loadRequests();

        //Sets the first request as the current request
        if (!allRequests.isEmpty()) {
            currentRequest = allRequests.get(0);
        }

        //Button positioning
        int btnWidth = 140;
        int btnHeight = 45;
        int yPos = gp.screenHeight - 120;

        approveBtn = new Rectangle(
                gp.tileSize * 3,
                yPos,
                btnWidth,
                btnHeight);

        declineBtn = new Rectangle(
                gp.tileSize * 6,
                yPos,
                btnWidth,
                btnHeight);

        postponeBtn = new Rectangle(
                gp.tileSize * 9,
                yPos,
                btnWidth,
                btnHeight);
    }


   

    /**
     * Reads request data from requests.txt.
     * Each valid line is converted into a Request object and stored
     * in the allRequests ArrayList.
     */
    /**
     * Loads request objects from the backend RequestStorage class.
     */
    private void loadRequests() {

        allRequests.clear();

        allRequests.addAll(
                gp.requestStorage.loadRequestObjects()
        );
    }

    /**
     * Receives the Graphics2D object used to draw the request screen.
     * Displays the current request and the request action buttons.
     */
    public void draw(Graphics2D g2) {

        //Request display box
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(
                gp.tileSize * 2,
                gp.tileSize * 2,
                gp.screenWidth - gp.tileSize * 4,
                gp.tileSize * 5,
                15,
                15);

        //Request box border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(
                gp.tileSize * 2 + 10,
                gp.tileSize * 2 + 10,
                gp.screenWidth - gp.tileSize * 4 - 20,
                gp.tileSize * 5 - 20,
                10,
                10);

        //Displays the current request
        if (currentRequest != null) {

            g2.setFont(new Font(
                    "Arial",
                    Font.BOLD,
                    22));

            String text =
                    currentRequest.getDescription() != null
                    ? currentRequest.getDescription()
                    : currentRequest.getRequestName();

            //Displays long descriptions over multiple lines
            drawWrappedText(
                    g2,
                    text,
                    gp.tileSize * 2 + 30,
                    gp.tileSize * 2 + 60,
                    gp.screenWidth - gp.tileSize * 5);

            //Displays request category
            g2.setFont(new Font(
                    "Arial",
                    Font.PLAIN,
                    16));

            g2.setColor(Color.GRAY);

            g2.drawString(
                    "Category: "
                    + currentRequest.getCategory(),
                    gp.tileSize * 2 + 30,
                    gp.tileSize * 2 + 110);
        }

        //Displays the request action buttons
        if (showButtons && currentRequest != null) {

            Color btnYellow =
                    new Color(255, 215, 0);

            drawStyledButton(
                    g2,
                    approveBtn,
                    "APPROVE",
                    btnYellow);

            drawStyledButton(
                    g2,
                    declineBtn,
                    "DECLINE",
                    btnYellow);

            drawStyledButton(
                    g2,
                    postponeBtn,
                    "POSTPONE",
                    btnYellow);
        }
    }

    /**
     * Receives text and its drawing position.
     * Splits long text into multiple lines so that it fits
     * inside the available width.
     */
    private void drawWrappedText(
            Graphics2D g2,
            String text,
            int x,
            int y,
            int maxWidth) {

        FontMetrics fm = g2.getFontMetrics();

        String[] words =
                text.split(" ");

        StringBuilder line =
                new StringBuilder();

        int currentY = y;

        //Builds each line until the maximum width is reached
        for (String word : words) {

            if (fm.stringWidth(
                    line + word) < maxWidth) {

                line.append(word).append(" ");

            } else {

                g2.drawString(
                        line.toString(),
                        x,
                        currentY);

                line =
                        new StringBuilder(
                                word + " ");

                currentY += fm.getHeight();
            }
        }

        //Draws the final line
        g2.drawString(
                line.toString(),
                x,
                currentY);
    }

    /**
     * Receives a button area, button text and background colour.
     * Draws a reusable styled button on the request screen.
     */
    private void drawStyledButton(
            Graphics2D g2,
            Rectangle r,
            String text,
            Color bgColor) {

        //Button shadow
        g2.setColor(
                new Color(0, 0, 0, 80));

        g2.fillRect(
                r.x + 3,
                r.y + 3,
                r.width,
                r.height);

        //Button background
        g2.setColor(bgColor);
        g2.fill(r);

        //Button border
        g2.setColor(Color.BLACK);
        g2.draw(r);

        //Centres button text
        FontMetrics fm =
                g2.getFontMetrics();

        g2.drawString(
                text,
                r.x
                + (r.width
                - fm.stringWidth(text)) / 2,
                r.y
                + (r.height
                + fm.getAscent()) / 2
                - 2);
    }
    
    /**
     * Loads the next request in the pending request list.
     * Sets currentRequest to null if no requests remain.
     */
    public void loadNextRequest() {

        if (!allRequests.isEmpty()) {
            currentRequest = allRequests.get(0);
        } else {
            currentRequest = null;
        }

        showButtons = false;
    }
    
    /**
     * Reloads all requests from secondary storage
     * and prepares the first request for gameplay.
     */
    public void reloadRequests() {

        allRequests.clear();
        history.clear();

        loadRequests();

        if (!allRequests.isEmpty()) {
            currentRequest = allRequests.get(0);
        } else {
            currentRequest = null;
        }

        showButtons = false;
    }
}