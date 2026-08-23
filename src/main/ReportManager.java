package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

//Handles the creation and saving of end-of-term reports
public class ReportManager {

    //Reference to the main GamePanel
    private GamePanel gp;
    
    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    /**
     * Receives the GamePanel used by the ReportManager.
     */
    public ReportManager(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Receives the final approval rating, final budget and
     * the list of requests handled during the term.
     * Generates report.txt containing the final term results
     * and the decision history.
     */
    public void generateFinalReport(
            int finalApproval,
            int finalBudget,
            ArrayList<Request> history) {

        lastError = "";

        try (PrintWriter writer =
                new PrintWriter(
                        new FileWriter("report.txt"))) {

            writer.println("OFFICIAL PRESIDENTIAL REPORT");
            writer.println("============================");

            writer.println(
                    "Total Decisions Made: "
                    + history.size());

            writer.println(
                    "Final Approval Rating: "
                    + finalApproval + "%");

            writer.println(
                    "Remaining Budget: R"
                    + finalBudget);

            String outcome =
                    (finalApproval >= 50
                    && finalBudget >= 0)
                    ? "SUCCESSFUL"
                    : "UNSUCCESSFUL";

            writer.println(
                    "Term Outcome: "
                    + outcome);

            writer.println();
            writer.println("--- Decision History ---");

            for (Request r : history) {

                writer.println(
                        "ID: "
                        + r.getId()
                        + " | Status: "
                        + r.getStatus());
            }

            System.out.println(
                    "Report generated successfully in report.txt");

        } catch (IOException e) {

            lastError =
                    "Could not save final report.";

            e.printStackTrace();
        }
    }

    /**
     * Receives the president name, final budget and final approval.
     * Appends a summary of the completed term to game_history.txt.
     */
    public boolean saveGameToHistory(
            String name,
            int budget,
            int approval) {

        lastError = "";

        try (BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter(
                                "game_history.txt",
                                true))) {

            String status;

            if (approval <= 0
                    || budget <= 0) {

                status = "FAILED";

            } else {

                status = "COMPLETED";
            }

            bw.write(
                    name
                    + "|"
                    + budget
                    + "|"
                    + approval
                    + "|"
                    + status);

            bw.newLine();

            return true;

        } catch (IOException e) {

            lastError =
                    "Could not save game history.";

            e.printStackTrace();

            return false;
        }
    }
    
    /**
     * Loads previous game records from game_history.txt.
     * Returns each record as a String array.
     */
    public ArrayList<String[]> loadGameHistory() {

        ArrayList<String[]> gameHistory = new ArrayList<>();

        try (BufferedReader br =
                new BufferedReader(
                        new FileReader("game_history.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|");

                if (data.length >= 4) {
                    gameHistory.add(data);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "No game history file found yet.");
        }

        return gameHistory;
    }
}