package main;

public class Event extends Request {
    
    public Event() {
        super();
    }

    public static Event createRandomEvent(String id, String name, int impact, int cost) {
        Event e = new Event();
        e.id = id;
        e.requestName = name;
        e.description = name; 
        e.impact = impact;
        e.cost = cost;
        e.category = "Random Event";
        return e;
    }
}