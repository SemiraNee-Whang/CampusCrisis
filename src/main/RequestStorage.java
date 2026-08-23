package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

//Handles reading, writing and managing request data in secondary storage
public class RequestStorage {

    //Stores request records loaded from requests.txt
    private ArrayList<String[]> requestData = new ArrayList<>();

    
    private String lastError = "";

    public String getLastError() {
        return lastError;
    }
    
    /**
     * Loads all valid request records from requests.txt.
     * Returns the ArrayList containing the loaded requests.
     */
    public ArrayList<String[]> loadRequests() {

        requestData.clear();

        try (BufferedReader br =
                new BufferedReader(
                        new FileReader("res/requests.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");

                //Correct request format contains 5 fields
                if (parts.length >= 5) {

                    String id = parts[0].trim();
                    String description = parts[1].trim();
                    String category = parts[2].trim();
                    String cost = parts[3].trim();
                    String impact = parts[4].trim();

                    //Only loads valid request records
                    if (Validation.isValidString(id)
                            && Validation.isValidString(description)
                            && Validation.isValidString(category)
                            && Validation.isPositiveInteger(cost)
                            && Validation.isInteger(impact)) {

                        requestData.add(new String[] {
                                id,
                                description,
                                category,
                                cost,
                                impact
                        });
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return requestData;
    }

    /**
     * Generates the next available Request ID.
     * Returns the ID in the format REQ001.
     */
    public String generateRequestID() {

        int highestID = 0;

        for (String[] request : requestData) {

            if (request.length >= 1) {

                String id = request[0].trim();

                try {

                    String numberPart =
                            id.replace("REQ", "");

                    int number =
                            Integer.parseInt(numberPart);

                    if (number > highestID) {
                        highestID = number;
                    }

                } catch (NumberFormatException e) {

                    //Ignores incorrectly formatted IDs
                }
            }
        }

        int nextID = highestID + 1;

        return String.format(
                "REQ%03d",
                nextID);
    }

    /**
     * Receives request details and adds a new request.
     * Returns true if the request is successfully saved.
     */
    public boolean addRequest(
            String description,
            String category,
            String costText,
            String impactText) {

        //Validates request information
        if (!Validation.isValidString(description)
                || !Validation.isValidString(category)
                || !Validation.isPositiveInteger(costText)
                || !Validation.isInteger(impactText)) {

            return false;
        }

        String id = generateRequestID();

        requestData.add(new String[] {
                id,
                description.trim(),
                category.trim(),
                costText.trim(),
                impactText.trim()
        });

        return saveAllRequests();
    }

    /**
     * Receives the Request ID and new request details.
     * Returns true if the matching request is successfully updated.
     */
    public boolean editRequest(
            String id,
            String newDescription,
            String newCategory,
            String newCost,
            String newImpact) {

        if (!Validation.isValidString(id)
                || !Validation.isValidString(newDescription)
                || !Validation.isValidString(newCategory)
                || !Validation.isPositiveInteger(newCost)
                || !Validation.isInteger(newImpact)) {

            return false;
        }

        for (int i = 0; i < requestData.size(); i++) {

            String[] request =
                    requestData.get(i);

            if (request[0].trim()
                    .equalsIgnoreCase(id.trim())) {

                requestData.set(
                        i,
                        new String[] {
                                request[0].trim(),
                                newDescription.trim(),
                                newCategory.trim(),
                                newCost.trim(),
                                newImpact.trim()
                        });

                return saveAllRequests();
            }
        }

        return false;
    }

    /**
     * Deletes a request using its Request ID.
     * Returns true if the request is found and removed.
     */
    public boolean deleteRequest(String id) {

        if (!Validation.isValidString(id)) {
            return false;
        }

        for (int i = 0; i < requestData.size(); i++) {

            if (requestData.get(i)[0]
                    .trim()
                    .equalsIgnoreCase(id.trim())) {

                requestData.remove(i);

                return saveAllRequests();
            }
        }

        return false;
    }

    /**
     * Rewrites requests.txt using the current request data.
     * Returns true if the file is successfully saved.
     */
    private boolean saveAllRequests() {

        lastError = "";

        try (BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter(
                                "res/requests.txt"))) {

            for (String[] request : requestData) {

                if (request.length >= 5) {

                    bw.write(
                            request[0].trim()
                            + "|"
                            + request[1].trim()
                            + "|"
                            + request[2].trim()
                            + "|"
                            + request[3].trim()
                            + "|"
                            + request[4].trim());

                    bw.newLine();
                }
            }

            return true;

        } catch (Exception e) {

            lastError =
                    "Could not save request data.";

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the current request data.
     */
    public ArrayList<String[]> getRequestData() {
        return requestData;
    }
    
    /**
     * Loads request records from secondary storage
     * and returns them as Request objects for gameplay.
     */
    public ArrayList<Request> loadRequestObjects() {

        ArrayList<Request> requests = new ArrayList<>();

        //Reuse the existing validated file-reading method
        loadRequests();

        for (String[] data : requestData) {

            if (data.length >= 5) {

                String id = data[0].trim();
                String description = data[1].trim();
                String category = data[2].trim();

                int cost =
                        Integer.parseInt(data[3].trim());

                int impact =
                        Integer.parseInt(data[4].trim());

                requests.add(
                        new Request(
                                id,
                                description,
                                category,
                                cost,
                                impact));
            }
        }

        return requests;
    }
}