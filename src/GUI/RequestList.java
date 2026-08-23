package GUI;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
     * Receives the mouse x and y coordinates.
     * Determines which request action button was selected.
     */
    public void handleInput(int mouseX, int mouseY) {

        //If the action buttons are hidden, display them
        if (!showButtons || currentRequest == null) {
            showButtons = true;
            return;
        }

        //Checks which action button was clicked
        if (approveBtn.contains(mouseX, mouseY)) {
            processRequest("APPROVED");

        } else if (declineBtn.contains(mouseX, mouseY)) {
            processRequest("DECLINED");

        } else if (postponeBtn.contains(mouseX, mouseY)) {
            postponeRequest();
        }
    }

    /**
     * Receives the status selected for the current request.
     * Adds the request to the history, removes it from the pending
     * request list and moves to the next request.
     */
    private void processRequest(String status) {

        System.out.println(
                "Request " + status + ": "
                + currentRequest.getRequestName());

        //Adds the current request to the completed request history
        history.add(currentRequest);

        //Removes the request from the pending request list
        allRequests.remove(currentRequest);

        //Loads the next available request
        nextRequest();
    }

    /**
     * Moves the current request to the back of the request queue
     * without removing it from the game.
     */
    private void postponeRequest() {

        System.out.println(
                "Request POSTPONED: "
                + currentRequest.getRequestName());

        //Removes the request from its current position
        allRequests.remove(currentRequest);

        //Adds the request to the back of the queue
        allRequests.add(currentRequest);

        //Loads the next available request
        nextRequest();
    }

    /**
     * Sets the next request in the ArrayList as the current request.
     * If there are no requests left, currentRequest is set to null.
     */
    private void nextRequest() {

        if (!allRequests.isEmpty()) {
            currentRequest = allRequests.get(0);

        } else {
            currentRequest = null;
        }

        //Hides the buttons until the next request is opened
        showButtons = false;
    }

    /**
     * Reads request data from requests.txt.
     * Each valid line is converted into a Request object and stored
     * in the allRequests ArrayList.
     */
    private void loadRequests() {

        try (InputStream is =
                getClass().getResourceAsStream("/requests.txt")) {

            //Stops if requests.txt cannot be found
            if (is == null) {
                return;
            }

            try (BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(is))) {

                String line;

                while ((line = br.readLine()) != null) {

                    //Format:
                    //requestID|description|category|cost|approvalImpact
                    String[] parts = line.split("\\|");

                    if (parts.length >= 5) {

                        try {

                            //Creates a Request object using the stored data
                            Request request = new Request(
                                    parts[0].trim(),
                                    parts[1].trim(),
                                    parts[2].trim(),
                                    Integer.parseInt(parts[3].trim()),
                                    Integer.parseInt(parts[4].trim()));

                            //Adds the Request object to the ArrayList
                            allRequests.add(request);

                        } catch (Exception e) {

                            //Skips incorrectly formatted request records
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}