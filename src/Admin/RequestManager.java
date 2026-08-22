package Admin;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        //Loads requests when the screen is created
        loadRequests();
    }

    //Reads all requests from requests.txt
    public void loadRequests() {

        //Clears old data before reloading
        requestData.clear();

        try (InputStream is =
                     getClass().getResourceAsStream("/requests.txt")) {

            if (is == null) {
                System.out.println("requests.txt could not be found.");
                return;
            }

            try (BufferedReader br =
                         new BufferedReader(new InputStreamReader(is))) {

                String line;

                while ((line = br.readLine()) != null) {

                    //Format:
                    //requestID|description|category|cost|approvalImpact
                    String[] parts = line.split("\\|");

                    if (parts.length >= 5) {
                        requestData.add(parts);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
  //Adds a new request to requests.txt
    public void addRequest() {

        
    	String id = generateRequestID();

        //Asks the admin to enter the Request Description
        String description = JOptionPane.showInputDialog(
                null,
                "Enter Request Description:");

        if (description == null || description.trim().isEmpty()) {
            return;
        }

        //Asks the admin to enter the Request Category
        String category = JOptionPane.showInputDialog(
                null,
                "Enter Request Category:");

        if (category == null || category.trim().isEmpty()) {
            return;
        }

        try {

            //Asks the admin to enter the cost
            int cost = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            null,
                            "Enter Request Cost:"));

            //Asks the admin to enter the approval impact
            int impact = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            null,
                            "Enter Approval Impact:"));

            //Adds the new request to requests.txt
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
            }

            //Shows confirmation message
            JOptionPane.showMessageDialog(
                    null,
                    "Request added successfully.\nRequest ID: " + id);
            
            //Reloads the table so the new request appears
            loadRequests();

        } catch (NumberFormatException e) {

            //Shows an error if cost or approval impact is not a number
            JOptionPane.showMessageDialog(
                    null,
                    "Cost and Approval Impact must be numbers.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {

            //Shows an error if the file cannot be updated
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

        if (searchID == null || searchID.trim().isEmpty()) {
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

                if (newDescription == null || newDescription.trim().isEmpty()) {
                    return;
                }

                //Asks for the new category
                String newCategory = JOptionPane.showInputDialog(
                        null,
                        "Enter new Category:",
                        oldCategory);

                if (newCategory == null || newCategory.trim().isEmpty()) {
                    return;
                }

                try {

                    //Asks for the new cost
                    int newCost = Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    null,
                                    "Enter new Cost:",
                                    oldCost));

                    //Asks for the new approval impact
                    int newImpact = Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    null,
                                    "Enter new Approval Impact:",
                                    oldImpact));

                    //Updates the request inside the ArrayList
                    requestData.set(i, new String[]{
                        data[0].trim(),
                        newDescription.trim(),
                        newCategory.trim(),
                        String.valueOf(newCost),
                        String.valueOf(newImpact)
                    });

                    //Rewrites requests.txt with the updated data
                    saveAllRequests();

                    //Reloads the table
                    loadRequests();

                    JOptionPane.showMessageDialog(
                            null,
                            "Request updated successfully.");

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Cost and Approval Impact must be numbers.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }

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
    
    
}