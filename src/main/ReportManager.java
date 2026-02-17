package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ReportManager {
    GamePanel gp;

    public ReportManager(GamePanel gp) {
        this.gp = gp;
    }

    public void generateFinalReport(int finalApproval, int finalBudget, ArrayList<Request> history) {
        // Automatically created when the term ends
        try (PrintWriter writer = new PrintWriter(new FileWriter("/report.txt"))) {
            
            writer.println("OFFICIAL PRESIDENTIAL REPORT");
            writer.println("============================");
            writer.println("Total Decisions Made: " + history.size());
            writer.println("Final Approval Rating: " + finalApproval + "%");
            writer.println("Remaining Budget: £" + finalBudget);
            
            // Logic for Term Outcome
            String outcome = (finalApproval >= 50 && finalBudget >= 0) ? "SUCCESSFUL" : "UNSUCCESSFUL";
            writer.println("Term Outcome: " + outcome);
            
            writer.println("\n--- Decision History ---");
            for (Request r : history) {
                // Listing ID and the choice made
                writer.println("ID: " + r.id + " | Status: " + r.status);
            }
            
            System.out.println("Report generated successfully in res/report.txt");
        } catch (IOException e) {
            System.err.println("Critical Error: Could not save report. " + e.getMessage());
        }
    }
}