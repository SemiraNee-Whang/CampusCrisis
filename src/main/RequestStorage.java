package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * The Class RequestStorage.
 */
//Handles reading, writing and managing request data in secondary storage
public class RequestStorage {

	//Stores request records loaded from requests.txt
    private ArrayList<String[]> requestData = new ArrayList<>();

    
    /** The last error. */
    private String lastError = "";

    	/**
    	 * Returns the most recent error message produced while
    	 * reading from or writing to secondary storage.
    	 *
    	 * @return the latest storage error message
    	 */
    public String getLastError() {
        return lastError;
    }
    
    /**
     * Loads all request records from requests.txt.
     * Each record is separated into request ID, description,
     * category, cost and approval impact.
     * Invalid records are ignored.
     *
     * @return an ArrayList containing all valid request records
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
                            && Validation.isValidImpact(impact)) {

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

            lastError =
                    "Could not read request data.";

            e.printStackTrace();
        }

        return requestData;
    }

    /**
     * Searches the current request records to find the highest
     * Request ID and generates the next available ID.
     *
     * @return the next Request ID in the format REQ001
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
     * Receives the details of a new request, validates them,
     * generates a new Request ID and adds the record to secondary storage.
     *
     * @param description the description of the request
     * @param category the category of the request
     * @param costText the request cost entered as text
     * @param impactText the approval impact entered as text
     * @return true if the request is valid and successfully saved,
     *         otherwise false
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
                || !Validation.isValidImpact(impactText)) {

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
     * Receives the ID of an existing request and its updated details.
     * Searches for the matching request, replaces its data and rewrites
     * requests.txt.
     *
     * @param id the ID of the request being edited
     * @param newDescription the updated request description
     * @param newCategory the updated request category
     * @param newCost the updated request cost
     * @param newImpact the updated approval impact
     * @return true if the request is found and successfully updated,
     *         otherwise false
     */
    public boolean editRequest(
            String id,
            String newDescription,
            String newCategory,
            String newCost,
            String newImpact) {

    	//Validates all updated request details
        if (!Validation.isValidString(id)
                || !Validation.isValidString(newDescription)
                || !Validation.isValidString(newCategory)
                || !Validation.isPositiveInteger(newCost)
                || !Validation.isValidImpact(newImpact)) {

            return false;
        }
        
      //Searches for the request with the matching ID
        for (int i = 0; i < requestData.size(); i++) {

            String[] request =
                    requestData.get(i);

            if (request[0].trim()
                    .equalsIgnoreCase(id.trim())) {
            		
            	//Replaces the old record with the updated information
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
     * Receives a Request ID, searches for the matching record,
     * removes it from the request list and updates requests.txt.
     *
     * @param id the ID of the request to delete
     * @return true if the request is found and successfully deleted,
     *         otherwise false
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
     * Rewrites requests.txt using the current requestData ArrayList.
     * Each request is written as a pipe-separated record.
     *
     * @return true if all request records are successfully saved,
     *         otherwise false
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
     *
     * @return the request data
     */
    public ArrayList<String[]> getRequestData() {
        return requestData;
    }
    
    /**
     * Loads the stored request records and converts each valid record
     * into a Request object for use during gameplay.
     *
     * @return an ArrayList of Request objects
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