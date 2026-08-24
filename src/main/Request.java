package main;

// TODO: Auto-generated Javadoc
/**
 * The Class Request.
 */
//Handles Request (Main Class For it)
public class Request {

    /** The id. */
    //Stores request details
    private String id;
    
    /** The description. */
    private String description;
    
    /** The category. */
    private String category;

    /** The cost. */
    //Stores request cost and approval impact
    private int cost;
    
    /** The impact. */
    private int impact;

    /** The status. */
    //Stores the result of the decision
    private String status = "";
    
    /** The outcome. */
    private String outcome = "";

    /**
     * Instantiates a new request.
     */
    //Default Constructor
    public Request() {

    }

    /**
     * Instantiates a new request.
     *
     * @param id the id
     * @param description the description
     * @param category the category
     * @param cost the cost
     * @param impact the impact
     */
    //Creates a Request object with all required request information
    public Request(String id, String description, String category, int cost, int impact) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.impact = impact;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    //Returns the Request ID
    public String getId() {
        return id;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    //Returns the Request description
    public String getDescription() {
        return description;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    //Returns the Request category
    public String getCategory() {
        return category;
    }


    /**
     * Gets the cost.
     *
     * @return the cost
     */
    //Returns the cost of the Request
    public int getCost() {
        return cost;
    }

    /**
     * Gets the impact.
     *
     * @return the impact
     */
    //Returns the approval impact of the Request
    public int getImpact() {
        return impact;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    //Returns the current Request status
    public String getStatus() {
        return status;
    }

    /**
     * Gets the outcome.
     *
     * @return the outcome
     */
    //Returns the outcome of the Request
    public String getOutcome() {
        return outcome;
    }

 

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    //Changes the Request status
    public void setStatus(String status) {

        if (Validation.isValidString(status)) {
            this.status = status;
        }
    }

    /**
     * Sets the outcome.
     *
     * @param outcome the new outcome
     */
    //Changes the Request outcome
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}