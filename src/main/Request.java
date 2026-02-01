package main;

public class Request {
    public String id, description, category;
    public int cost, impact;
    
    public String status = ""; 
    public String outcome = ""; 

    public Request(String id, String description, String category, int cost, int impact) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.impact = impact;
    }
}