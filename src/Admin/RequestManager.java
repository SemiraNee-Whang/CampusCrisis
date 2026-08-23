package Admin;

import java.awt.*;
import main.Validation;
import java.util.ArrayList;
import main.GamePanel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JOptionPane;

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
                40);

        categoryBox = new Rectangle(
                360,
                280,
                380,
                40);

        costBox = new Rectangle(
                360,
                340,
                200,
                40);

        impactBox = new Rectangle(
                360,
                400,
                200,
                40);

        saveBtn = new Rectangle(
                gp.screenWidth / 2 - 130,
                470,
                110,
                40);

        cancelBtn = new Rectangle(
                gp.screenWidth / 2 + 20,
                470,
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
        
        if (addingRequest) {
            drawAddRequestForm(g2);
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
     * Allows the admin to add a new request.
     * Validates all entered data before saving the request.
     */
    public void addRequest() {

        String id = generateRequestID();

        //Request Description
        String description = JOptionPane.showInputDialog(
                null,
                "Enter Request Description:");

        //Stops if Cancel is pressed
        if (description == null) {
            return;
        }

        //Validates description
        if (!Validation.isValidString(description)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a request description.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }


        //Request Category
        String category = JOptionPane.showInputDialog(
                null,
                "Enter Request Category:");

        if (category == null) {
            return;
        }

        //Validates category
        if (!Validation.isValidString(category)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a request category.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }


        //Request Cost
        String costText = JOptionPane.showInputDialog(
                null,
                "Enter Request Cost:");

        if (costText == null) {
            return;
        }

        //Validates cost
        if (!Validation.isPositiveInteger(costText)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Cost must be a positive whole number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }


        //Approval Impact
        String impactText = JOptionPane.showInputDialog(
                null,
                "Enter Approval Impact:");

        if (impactText == null) {
            return;
        }

        //Validates approval impact
        if (!Validation.isInteger(impactText)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Approval Impact must be a whole number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }


        //Converts validated Strings to integers
        int cost = Integer.parseInt(costText.trim());
        int impact = Integer.parseInt(impactText.trim());


        try (BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter(
                                "res/requests.txt",
                                true))) {

            //Format:
            //requestID|description|category|cost|approvalImpact
            bw.write(
                    id.trim()
                    + "|"
                    + description.trim()
                    + "|"
                    + category.trim()
                    + "|"
                    + cost
                    + "|"
                    + impact);

            bw.newLine();

            JOptionPane.showMessageDialog(
                    null,
                    "Request added successfully.\nRequest ID: "
                    + id);

            //Reload table
            loadRequests();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Could not add request.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
        }
    }
  //Rewrites requests.txt using the current ArrayList
    private void saveAllRequests() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("res/requests.txt"))) {

            for (String[] data : requestData) {

                if (data.length >= 5) {

                    //Format:
                    //requestID|description|category|cost|approvalImpact
                    bw.write(
                            data[0].trim() + "|"
                            + data[1].trim() + "|"
                            + data[2].trim() + "|"
                            + data[3].trim() + "|"
                            + data[4].trim());

                    bw.newLine();
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Could not save requests.txt.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();
        }
    }
    
  //Edits an existing request in requests.txt
    public void editRequest() {

        //Asks the admin which Request ID must be edited
        String searchID = JOptionPane.showInputDialog(
                null,
                "Enter the Request ID you want to edit:");

        if (searchID == null) {
            return;
        }

        //Validates Request ID
        if (!Validation.isValidString(searchID)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a Request ID.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        boolean found = false;

        //Searches for the matching request
        for (int i = 0; i < requestData.size(); i++) {

            String[] data = requestData.get(i);

            if (data.length >= 5
                    && data[0].trim().equalsIgnoreCase(searchID.trim())) {

                found = true;

                //Stores the old values
                String oldDescription = data[1].trim();
                String oldCategory = data[2].trim();
                String oldCost = data[3].trim();
                String oldImpact = data[4].trim();

                //Asks for the new description
                String newDescription = JOptionPane.showInputDialog(
                        null,
                        "Enter new Description:",
                        oldDescription);

                if (newDescription == null) {
                    return;
                }

                //Validates description
                if (!Validation.isValidString(newDescription)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a request description.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                //Asks for the new category
                String newCategory = JOptionPane.showInputDialog(
                        null,
                        "Enter new Category:",
                        oldCategory);

                if (newCategory == null) {
                    return;
                }

                //Validates category
                if (!Validation.isValidString(newCategory)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a request category.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                //Asks for the new cost
                String newCostText = JOptionPane.showInputDialog(
                        null,
                        "Enter new Cost:",
                        oldCost);

                if (newCostText == null) {
                    return;
                }

                //Validates cost
                if (!Validation.isPositiveInteger(newCostText)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Cost must be a positive whole number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                //Asks for the new approval impact
                String newImpactText = JOptionPane.showInputDialog(
                        null,
                        "Enter new Approval Impact:",
                        oldImpact);

                if (newImpactText == null) {
                    return;
                }

                //Validates approval impact
                if (!Validation.isInteger(newImpactText)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Approval Impact must be a whole number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                //Converts validated Strings
                int newCost =
                        Integer.parseInt(newCostText.trim());

                int newImpact =
                        Integer.parseInt(newImpactText.trim());

                //Updates the request inside the ArrayList
                requestData.set(
                        i,
                        new String[] {
                            data[0].trim(),
                            newDescription.trim(),
                            newCategory.trim(),
                            String.valueOf(newCost),
                            String.valueOf(newImpact)
                        });

                //Rewrites requests.txt
                saveAllRequests();

                //Reloads the table
                loadRequests();

                JOptionPane.showMessageDialog(
                        null,
                        "Request updated successfully.");

                //Stops searching once the request has been found
                break;
            }
        }

        //Shows an error if the Request ID cannot be found
        if (!found) {

            JOptionPane.showMessageDialog(
                    null,
                    "Request ID not found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
  //Deletes an existing request from requests.txt
    public void deleteRequest() {

        //Asks the admin which Request ID must be deleted
        String searchID = JOptionPane.showInputDialog(
                null,
                "Enter the Request ID you want to delete:");

        //Stops if the admin presses Cancel or enters nothing
        if (searchID == null || searchID.trim().isEmpty()) {
            return;
        }

        boolean found = false;

        //Searches through all requests
        for (int i = 0; i < requestData.size(); i++) {

            String[] data = requestData.get(i);

            //Checks if the Request ID matches
            if (data.length >= 5
                    && data[0].trim().equalsIgnoreCase(searchID.trim())) {

                found = true;

                //Asks the admin to confirm the deletion
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete "
                        + data[0].trim() + "?\n\n"
                        + data[1].trim(),
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                //Only deletes if YES is selected
                if (choice == JOptionPane.YES_OPTION) {

                    //Removes the request from the ArrayList
                    requestData.remove(i);

                    //Updates requests.txt
                    saveAllRequests();

                    //Reloads the requests displayed on screen
                    loadRequests();

                    JOptionPane.showMessageDialog(
                            null,
                            "Request deleted successfully.");
                }

                break;
            }
        }

        //Shows an error if the Request ID does not exist
        if (!found) {

            JOptionPane.showMessageDialog(
                    null,
                    "Request ID not found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Draws the Add Request form directly on the Manage Requests screen.
     */
    private void drawAddRequestForm(Graphics2D g2) {

        int formX = 180;
        int formY = 150;
        int formWidth = 600;
        int formHeight = 390;

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

        g2.drawString(
                "ADD REQUEST",
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

        g2.drawString(
                newDescription
                + (activeField == 0 ? "|" : ""),
                descriptionBox.x + 10,
                descriptionBox.y + 27);


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
        if (!Validation.isInteger(newImpact)) {
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

            message = "Could not add request.";
        }
    }
    
    
}