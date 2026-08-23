package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//Handles the processing and storage of request decisions
public class DecisionManager {

    private GamePanel gp;

    /**
     * Receives the GamePanel used by the DecisionManager.
     */
    public DecisionManager(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Receives a Request and the decision selected by the player.
     * Updates the budget, approval, request status and request outcome.
     * Returns true if the request is completed and should be removed
     * from the pending request list.
     */
    public boolean processDecision(Request r, String decision) {

        if (r == null) {
            return false;
        }

        //Handles approval of a request
        if (decision.equals("Approve")) {

            gp.dashboard.budget -= r.getCost();
            gp.dashboard.approval += r.getImpact();

            r.setStatus("Approved");

            r.setOutcome(
                    "Budget -"
                    + r.getCost()
                    + ", Approval +"
                    + r.getImpact());

            return true;
        }

        //Handles declining a request
        else if (decision.equals("Decline")) {

            gp.dashboard.approval -= 8;

            r.setStatus("Declined");
            r.setOutcome("Approval -8");

            return true;
        }

        //Handles postponing a request
        else if (decision.equals("Postpone")) {

            r.setStatus("Postponed");
            r.setOutcome("No change (Deferred)");

            return false;
        }

        return false;
    }

    /**
     * Receives a completed Request.
     * Appends the request decision information to decisions.txt.
     */
    public void saveDecisionToFile(Request r) {

        try (BufferedWriter writer =
                new BufferedWriter(
                        new FileWriter(
                                "decisions.txt",
                                true))) {

            writer.write(
                    r.getId()
                    + " | "
                    + r.getStatus()
                    + " | "
                    + r.getOutcome()
                    + " | "
                    + r.getRequestName());

            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}