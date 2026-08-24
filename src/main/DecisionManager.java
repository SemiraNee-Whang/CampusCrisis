package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The Class DecisionManager.
 */
//Handles the processing and storage of request decisions
public class DecisionManager {

    private GamePanel gp;

    /**
     * Creates the DecisionManager and stores a reference to GamePanel
     * so that budget and approval values can be updated.
     *
     * @param gp the main GamePanel containing the current game data
     */
    public DecisionManager(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Receives a Request and the player's selected decision.
     * Updates the budget, approval rating, request status and outcome
     * according to whether the request is approved, declined or postponed.
     *
     * @param r the Request currently being processed
     * @param decision the decision selected by the player
     * @return true if the request is completed and should be removed
     *         from the pending queue, or false if it is postponed
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
     * Receives a completed Request and appends its ID, status and outcome
     * to decisions.txt.
     *
     * @param r the completed Request whose decision must be stored
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
                    + r.getOutcome());

            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    
    /**
     * Processes a player decision and updates all related request collections.
     * Postponed requests are moved to the back of the pending queue.
     * Completed requests are removed from the pending queue, added to the
     * current request history and game history, and saved to decisions.txt.
     *
     * @param r the Request being processed
     * @param decision the player's selected decision
     * @param pendingRequests the list of requests still waiting to be handled
     * @param requestHistory the current term's completed request history
     * @param gameHistory the overall in-memory game decision history
     * @return true if the request was completed, or false if it was postponed
     */
    public boolean handleDecisionResult(
            Request r,
            String decision,
            java.util.ArrayList<Request> pendingRequests,
            java.util.ArrayList<Request> requestHistory,
            java.util.ArrayList<Request> gameHistory) {

        boolean completed =
                processDecision(r, decision);

        //Postpone moves request to back of queue
        if (!completed) {

            pendingRequests.remove(r);
            pendingRequests.add(r);

            return false;
        }

        //Approve/Decline removes request from pending list
        pendingRequests.remove(r);

        //Adds request to histories
        requestHistory.add(r);
        gameHistory.add(r);

        //Stores completed decision
        saveDecisionToFile(r);

        return true;
    }
    
    /**
     * Receives an approval rating and keeps it within the valid
     * range of 0 to 100.
     *
     * @param approval the approval rating to check
     * @return 100 if the value is above 100, 0 if it is below 0,
     *         otherwise the original approval value
     */
    public int clampApproval(int approval) {

        if (approval > 100) {
            return 100;
        }

        if (approval < 0) {
            return 0;
        }

        return approval;
    }
}