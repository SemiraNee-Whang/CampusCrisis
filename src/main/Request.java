package main;

//Handles Request (Main CLass For it)
public class Request {
    public String id, description, category,requestName;
    public int cost, impact;
    
    public String status = ""; 
    public String outcome = ""; 
    
    public Request() {
    
    }

    public Request(String id, String description, String category, int cost, int impact) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.cost = cost;
        this.impact = impact;
    }
}