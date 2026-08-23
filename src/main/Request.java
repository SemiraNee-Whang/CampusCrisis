package main;

//Handles Request (Main Class For it)
public class Request {

    //Stores request details
    private String id;
    private String description;
    private String category;

    //Stores request cost and approval impact
    private int cost;
    private int impact;

    //Stores the result of the decision
    private String status = "";
    private String outcome = "";

    //Default Constructor
    public Request() {

    }

    //Creates a Request object with all required request information
    public Request(String id, String description, String category, int cost, int impact) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.impact = impact;
    }

    //Returns the Request ID
    public String getId() {
        return id;
    }

    //Returns the Request description
    public String getDescription() {
        return description;
    }

    //Returns the Request category
    public String getCategory() {
        return category;
    }


    //Returns the cost of the Request
    public int getCost() {
        return cost;
    }

    //Returns the approval impact of the Request
    public int getImpact() {
        return impact;
    }

    //Returns the current Request status
    public String getStatus() {
        return status;
    }

    //Returns the outcome of the Request
    public String getOutcome() {
        return outcome;
    }

 

    //Changes the Request status
    public void setStatus(String status) {

        if (Validation.isValidString(status)) {
            this.status = status;
        }
    }

    //Changes the Request outcome
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}