package main;

import java.io.*;
import java.util.ArrayList;

public class EventManager {
    GamePanel gp;
    public ArrayList<Request> randomEvents = new ArrayList<>();

    public EventManager(GamePanel gp) {
        this.gp = gp;
        loadRandomEvents();
    }

    public void loadRandomEvents() {
        try {
            // Using getResourceAsStream for project portability
            InputStream is = getClass().getResourceAsStream("/events.txt");
            if (is == null) {
                System.out.println("Could not find events.txt in res folder!");
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
             // Inside your while loop in EventManager.java
                if (parts.length == 4) {
                    // CHANGE: Now creating a unique Event object
                    Event env = new Event(); 
                    
                    env.id = parts[0].replace("EVT", "").trim();
                    env.requestName = parts[1].trim();
                    env.description = parts[1].trim(); 
                    env.impact = Integer.parseInt(parts[2].trim());
                    env.cost = Integer.parseInt(parts[3].trim());
                    
                    randomEvents.add(env);
                
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error loading random events: " + e.getMessage());
        }
    }
}