/*
 * 
 */
package Admin;

import java.awt.*;
import main.Validation;
import java.util.ArrayList;
import main.GamePanel;


//Handles viewing and managing requests from requests.txt
public class RequestManager {

    private GamePanel gp;

    //Stores the request data read from requests.txt
    public ArrayList<String[]> requestData = new ArrayList<>();

    //Buttons on the Request Management screen
    public Rectangle addBtn;
    public Rectangle editBtn;
    public Rectangle deleteBtn;
    public Rectangle backBtn;

    //Used to scroll through requests
    public int scrollOffset = 0;
    
  //Controls whether the Add Request form is visible
    public boolean addingRequest = false;

    //Stores form input
    public String newDescription = "";
    public String newCategory = "";
    public String newCost = "";
    public String newImpact = "";

    //Tracks selected input field
    //0 = Description, 1 = Category, 2 = Cost, 3 = Impact
    public int activeField = -1;

    //Displays validation/success messages
    public String message = "";

    //Form input boxes
    public Rectangle descriptionBox;
    public Rectangle categoryBox;
    public Rectangle costBox;
    public Rectangle impactBox;

    //Form buttons
    public Rectangle saveBtn;
    public Rectangle cancelBtn;
    
    public boolean editingRequest = false;
    public String originalRequestID = "";
    public String selectedRequestID = "";

  //Controls whether delete confirmation is visible
    public boolean deletingRequest = false;

    //Delete confirmation buttons
    public Rectangle confirmDeleteBtn;
    public Rectangle cancelDeleteBtn;
    
    public RequestManager(GamePanel gp) {
        this.gp = gp;

        //Add Request button
        addBtn = new Rectangle(
                120,
                gp.screenHeight - 80,
                120,
                40);

        //Edit Request button
        editBtn = new Rectangle(
                270,
                gp.screenHeight - 80,
                120,
                40);

        //Delete Request button
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
        
        descriptionBox = new Rectangle(
                360,
                220,
                380,
                60);

        categoryBox = new Rectangle(
                360,
                300,
                380,
                40);

        costBox = new Rectangle(
                360,
                360,
                200,
                40);

        impactBox = new Rectangle(
                360,
                420,
                200,
                40);

        saveBtn = new Rectangle(
                gp.screenWidth / 2 - 130,
                500,
                110,
                40);

        cancelBtn = new Rectangle(
                gp.screenWidth / 2 + 20,
                500,
                110,
                40);
        
        confirmDeleteBtn = new Rectangle(
                gp.screenWidth / 2 - 130,
                gp.screenHeight / 2 + 40,
                110,
                40);

        cancelDeleteBtn = new Rectangle(
                gp.screenWidth / 2 + 20,
                gp.screenHeight / 2 + 40,
                110,
                40);

        //Loads requests when the screen is created
        loadRequests();
    }

    /**
     * Loads request records from the backend RequestStorage class.
     */
    public void loadRequests() {

        requestData.clear();

        requestData.addAll(
                gp.requestStorage.loadRequests()
        );
    }

    //Draws the Request Management screen
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

        String title = "MANAGE REQUESTS";

        int titleX =
                (gp.screenWidth
                        - g2.getFontMetrics()
                        .stringWidth(title)) / 2;

        g2.drawString(
                title,
                titleX,
                70);

        //Table Background
        int tableX = 40;
        int tableY = 100;
        int tableWidth = gp.screenWidth - 80;
        int tableHeight = gp.screenHeight - 220;

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
                14));

        int headerY = tableY + 30;

        g2.drawString(
                "ID",
                tableX + 20,
                headerY);

        g2.drawString(
                "DESCRIPTION",
                tableX + 100,
                headerY);

        g2.drawString(
                "CATEGORY",
                tableX + 450,
                headerY);

        g2.drawString(
                "COST",
                tableX + 600,
                headerY);

        g2.drawString(
                "IMPACT",
                tableX + 710,
                headerY);

        //Header Line
        g2.drawLine(
                tableX + 10,
                headerY + 10,
                tableX + tableWidth - 10,
                headerY + 10);

        //Request Rows
        g2.setFont(new Font(
                "Arial",
                Font.PLAIN,
                13));

        int rowHeight = 35;

        for (int i = 0; i < requestData.size(); i++) {

            String[] data = requestData.get(i);

            int rowY =
                    headerY
                    + 45
                    + (i * rowHeight)
                    - scrollOffset;

            //Only draws rows inside the table
            if (rowY > headerY + 20
                    && rowY < tableY + tableHeight - 10) {

            	if (data[0].trim()
            	        .equalsIgnoreCase(selectedRequestID)) {

            	    g2.setColor(new Color(230, 230, 230));

            	    g2.fillRect(
            	            tableX + 10,
            	            rowY - 22,
            	            tableWidth - 20,
            	            28);
            	}

            	g2.setColor(Color.BLACK);
                g2.drawString(
                        data[0].trim(),
                        tableX + 20,
                        rowY);

                //Limits description length so it fits
                String description =
                        data[1].trim();

                if (description.length() > 40) {
                    description =
                            description.substring(
                                    0,
                                    40)
                            + "...";
                }

                g2.drawString(
                        description,
                        tableX + 100,
                        rowY);

                g2.drawString(
                        data[2].trim(),
                        tableX + 450,
                        rowY);

                g2.drawString(
                        "R" + data[3].trim(),
                        tableX + 600,
                        rowY);

                g2.drawString(
                        data[4].trim(),
                        tableX + 710,
                        rowY);
            }
        }
        
        if (addingRequest || editingRequest) {
            drawAddRequestForm(g2);
        }
        
        if (deletingRequest) {
            drawDeleteConfirmation(g2);
        }

        //Draws the Admin buttons
        drawButton(
                g2,
                addBtn,
                "ADD");

        drawButton(
                g2,
                editBtn,
                "EDIT");

        drawButton(
                g2,
                deleteBtn,
                "DELETE");

        drawButton(
                g2,
                backBtn,
                "BACK");
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

        FontMetrics fm =
                g2.getFontMetrics();

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
  //Automatically generates the next Request ID
    public String generateRequestID() {

        int highestID = 0;

        //Checks all existing requests
        for (String[] request : requestData) {

            if (request.length > 0) {

                String id = request[0].trim();

                try {

                    //Removes REQ from the ID
                    //Example: REQ005 becomes 005
                    String numberPart = id.replace("REQ", "");

                    int number = Integer.parseInt(numberPart);

                    //Keeps track of the highest ID found
                    if (number > highestID) {
                        highestID = number;
                    }

                } catch (NumberFormatException e) {
                    //Ignores IDs that are not in the correct format
                }
            }
        }

        //Adds 1 to the highest existing ID
        int nextID = highestID + 1;

        //Creates the new ID
        //Example: 6 becomes REQ006
        return String.format("REQ%03d", nextID);
    }
   
 
    /**
     * Draws the Add Request form directly on the Manage Requests screen.
     */
    private void drawAddRequestForm(Graphics2D g2) {

        int formX = 180;
        int formY = 150;
        int formWidth = 600;
        int formHeight = 400;

        //Form background
        g2.setColor(new Color(30, 30, 40));
        g2.fillRoundRect(
                formX,
                formY,
                formWidth,
                formHeight,
                15,
                15);

        //Form border
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(
                formX,
                formY,
                formWidth,
                formHeight,
                15,
                15);

        //Title
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                22));

        g2.setColor(Color.WHITE);

        String formTitle =
                editingRequest ? "EDIT REQUEST" : "ADD REQUEST";

        g2.drawString(
                formTitle,
                formX + 215,
                formY + 40);


        //Labels
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                16));

        g2.drawString(
                "Description:",
                formX + 40,
                formY + 100);

        g2.drawString(
                "Category:",
                formX + 40,
                formY + 160);

        g2.drawString(
                "Cost:",
                formX + 40,
                formY + 220);

        g2.drawString(
                "Approval Impact:",
                formX + 40,
                formY + 280);


        //Description box
        g2.setColor(
                activeField == 0
                ? Color.YELLOW
                : Color.WHITE);

        g2.draw(descriptionBox);

        g2.setColor(Color.WHITE);

        drawWrappedText(
                g2,
                newDescription + (activeField == 0 ? "|" : ""),
                descriptionBox.x + 10,
                descriptionBox.y + 22,
                descriptionBox.width - 20,
                3);
        


        //Category box
        g2.setColor(
                activeField == 1
                ? Color.YELLOW
                : Color.WHITE);

        g2.draw(categoryBox);

        g2.setColor(Color.WHITE);

        g2.drawString(
                newCategory
                + (activeField == 1 ? "|" : ""),
                categoryBox.x + 10,
                categoryBox.y + 27);


        //Cost box
        g2.setColor(
                activeField == 2
                ? Color.YELLOW
                : Color.WHITE);

        g2.draw(costBox);

        g2.setColor(Color.WHITE);

        g2.drawString(
                newCost
                + (activeField == 2 ? "|" : ""),
                costBox.x + 10,
                costBox.y + 27);


        //Impact box
        g2.setColor(
                activeField == 3
                ? Color.YELLOW
                : Color.WHITE);

        g2.draw(impactBox);

        g2.setColor(Color.WHITE);

        g2.drawString(
                newImpact
                + (activeField == 3 ? "|" : ""),
                impactBox.x + 10,
                impactBox.y + 27);


        //Validation / success message
        if (!message.equals("")) {

            g2.setColor(Color.ORANGE);

            g2.drawString(
                    message,
                    formX + 40,
                    formY + 330);
        }


        //Save / Cancel
        drawButton(
                g2,
                saveBtn,
                "SAVE");

        drawButton(
                g2,
                cancelBtn,
                "CANCEL");
    }
    
    /**
     * Validates the entered request information
     * and sends it to RequestStorage to be saved.
     */
    public void saveNewRequest() {

        //Validate description
        if (!Validation.isValidString(newDescription)) {
            message = "Please enter a request description.";
            return;
        }

        //Validate category
        if (!Validation.isValidString(newCategory)) {
            message = "Please enter a request category.";
            return;
        }

        //Validate cost
        if (!Validation.isPositiveInteger(newCost)) {
            message = "Cost must be a positive whole number.";
            return;
        }

        //Validate approval impact
        if (!Validation.isValidImpact(newImpact)) {
            message = "Approval Impact must be a whole number.";
            return;
        }

        //Send validated data to backend storage class
        boolean added =
                gp.requestStorage.addRequest(
                        newDescription,
                        newCategory,
                        newCost,
                        newImpact);

        if (added) {

            //Reload table from secondary storage
            loadRequests();

            //Return table to top
            scrollOffset = 0;

            message = "Request added successfully.";

            //Clear form
            newDescription = "";
            newCategory = "";
            newCost = "";
            newImpact = "";

            activeField = -1;
            addingRequest = false;

        } else {

        	if (!gp.requestStorage.getLastError().isEmpty()) {

        	    message =
        	            gp.requestStorage.getLastError();

        	} else {

        	    message =
        	            "Could not add request.";
        	}
        }
    }
    
    /**
     * Selects the request row that was clicked.
     */
    public void selectRequestAt(int mouseY) {

        int tableY = 100;
        int headerY = tableY + 30;
        int rowHeight = 35;

        for (int i = 0; i < requestData.size(); i++) {

            int rowY =
                    headerY
                    + 45
                    + (i * rowHeight)
                    - scrollOffset;

            if (mouseY >= rowY - 22
                    && mouseY <= rowY + 8) {

                selectedRequestID =
                        requestData.get(i)[0].trim();

                message = "";
                return;
            }
        }
    }
    
    /**
     * Loads the selected request into the Edit Request form.
     */
    public boolean loadRequestForEdit(String requestID) {

        if (!Validation.isValidString(requestID)) {
            message = "Please select a request first.";
            return false;
        }

        for (String[] request : requestData) {

            if (request[0].trim()
                    .equalsIgnoreCase(requestID.trim())) {

                originalRequestID = request[0].trim();

                newDescription = request[1].trim();
                newCategory = request[2].trim();
                newCost = request[3].trim();
                newImpact = request[4].trim();

                editingRequest = true;
                addingRequest = false;

                activeField = 0;
                message = "";

                return true;
            }
        }

        message = "Request not found.";
        return false;
    }
    
    /**
     * Validates and saves changes to an existing request.
     */
    public void saveEditedRequest() {

        if (!Validation.isValidString(newDescription)) {
            message = "Please enter a request description.";
            return;
        }

        if (!Validation.isValidString(newCategory)) {
            message = "Please enter a request category.";
            return;
        }

        if (!Validation.isPositiveInteger(newCost)) {
            message = "Cost must be a positive whole number.";
            return;
        }

        if (!Validation.isInteger(newImpact)) {
            message = "Approval Impact must be a whole number.";
            return;
        }

        boolean updated =
                gp.requestStorage.editRequest(
                        originalRequestID,
                        newDescription,
                        newCategory,
                        newCost,
                        newImpact);

        if (updated) {

            loadRequests();

            message = "Request updated successfully.";

            originalRequestID = "";
            newDescription = "";
            newCategory = "";
            newCost = "";
            newImpact = "";

            editingRequest = false;
            activeField = -1;

        } else {

            message = "Could not update request.";
        }
    }
    
    private void drawWrappedText(
            Graphics2D g2,
            String text,
            int x,
            int y,
            int maxWidth,
            int maxLines) {

        FontMetrics fm = g2.getFontMetrics();

        String[] words = text.split(" ");

        StringBuilder line = new StringBuilder();

        int currentY = y;
        int lineCount = 0;

        for (String word : words) {

            String testLine =
                    line.length() == 0
                    ? word
                    : line + " " + word;

            if (fm.stringWidth(testLine) <= maxWidth) {

                line = new StringBuilder(testLine);

            } else {

                g2.drawString(
                        line.toString(),
                        x,
                        currentY);

                lineCount++;

                if (lineCount >= maxLines) {
                    return;
                }

                currentY += fm.getHeight();

                line = new StringBuilder(word);
            }
        }

        if (line.length() > 0
                && lineCount < maxLines) {

            g2.drawString(
                    line.toString(),
                    x,
                    currentY);
        }
    }
    
    /**
     * Deletes the selected request using RequestStorage.
     * Displays whether the deletion was successful.
     */
    public void confirmDeleteRequest() {

        if (!Validation.isValidString(selectedRequestID)) {
            message = "Please select a request first.";
            return;
        }

        boolean deleted =
                gp.requestStorage.deleteRequest(selectedRequestID);

        if (deleted) {

            //Reload requests after deletion
            loadRequests();

            message = "Request deleted successfully.";

            selectedRequestID = "";
            deletingRequest = false;

        } else {

            message = "Could not delete request.";
        }
    }
    
    /**
     * Draws a confirmation box before deleting a request.
     */
    private void drawDeleteConfirmation(Graphics2D g2) {

        int formX = 250;
        int formY = 200;
        int formWidth = 460;
        int formHeight = 220;

        //Background
        g2.setColor(new Color(30, 30, 40));
        g2.fillRoundRect(
                formX,
                formY,
                formWidth,
                formHeight,
                15,
                15);

        //Border
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(
                formX,
                formY,
                formWidth,
                formHeight,
                15,
                15);

        //Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                22));

        g2.drawString(
                "DELETE REQUEST",
                formX + 135,
                formY + 45);

        //Message
        g2.setFont(new Font(
                "Arial",
                Font.PLAIN,
                16));

        g2.drawString(
                "Are you sure you want to delete:",
                formX + 70,
                formY + 95);

        //Selected Request ID
        g2.setFont(new Font(
                "Arial",
                Font.BOLD,
                17));

        g2.drawString(
                selectedRequestID,
                formX + 190,
                formY + 125);

        //Buttons
        drawButton(
                g2,
                confirmDeleteBtn,
                "YES");

        drawButton(
                g2,
                cancelDeleteBtn,
                "NO");
    }
    
    /**
     * Calculates the maximum amount the request table can scroll.
     */
    public int getMaxScroll() {

        int rowHeight = 35;

        //Must match the values used in draw()
        int tableY = 100;
        int tableHeight = gp.screenHeight - 220;
        int headerY = tableY + 30;

        //Available space underneath the table header
        int visibleHeight =
                (tableY + tableHeight - 10)
                - (headerY + 45);

        //Total vertical space needed for all requests
        int totalHeight =
                requestData.size() * rowHeight;

        return Math.max(
                0,
                totalHeight - visibleHeight);
    }
}