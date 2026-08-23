package main;

import java.awt.Rectangle;
import java.awt.event.*;




	public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {
    private GamePanel gp;
    public int mouseX, mouseY;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    
    public void mousePressed(MouseEvent e) {
    	gp.requestFocusInWindow();
        int x = e.getX();
        int y = e.getY();

        //Request Pop-Up Interaction
        if (gp.gameState == gp.requestState) {
            handleRequestPopUp(x, y);
            return; // Lock inputs while in request screen
        }

        //Other Game States
        if (gp.gameState == gp.titleState) {
            handleTitleClick();
        } else if (gp.gameState == gp.loginState) {
            handleLoginClick();
        } else if (gp.gameState == gp.adminLoginState) {
            handleAdminLoginClick(x, y);
        }else if (gp.gameState == gp.adminState) {
            handleAdminMenuClick(x, y);
        }else if (gp.gameState == gp.adminUserState) {
            handleUserManagerClick(x, y);
        }else if (gp.gameState == gp.adminRequestState) {
                handleRequestManagerClick(x, y);
        } else if (gp.gameState == gp.setupState) {
            handleSetupClick(x, y);
        } else if (gp.gameState == gp.instructionState) {
            handleInstructionClick(x, y);
        } else if (gp.gameState == gp.historyState) {
            if (gp.historyView.backBtn.contains(x, y)) {
                gp.gameState = gp.playState;
            }
           
        } else if (gp.gameState == gp.reportState) {
            if (gp.reportView.backBtn.contains(e.getPoint())) {
                resetGame();
                gp.gameState = gp.titleState;
            } 
       
           
        }
    }

    //Handles clicks within the request pop-up, including "Postpone"
    private void handleRequestPopUp(int x, int y) {
        Rectangle requestBox = new Rectangle(gp.tileSize * 2, gp.tileSize * 2, gp.screenWidth - gp.tileSize * 4, gp.tileSize * 4);
        
        // Click the box to reveal actions
        if (requestBox.contains(x, y) && !gp.reqList.showButtons) {
            gp.reqList.showButtons = true; 
        } 
        //Approval button
        else if (gp.reqList.showButtons) {
            if (gp.reqList.approveBtn.contains(x, y)) {
                processDecision("Approve");
                gp.reqList.showButtons = false;
            }
            //Decline button
            else if (gp.reqList.declineBtn.contains(x, y)) {
                processDecision("Decline");
                gp.reqList.showButtons = false;
            }
            //Postpone button
            else if (gp.reqList.postponeBtn.contains(x, y)) {
                processDecision("Postpone"); // Call decision logic for Postpone
                gp.reqList.showButtons = false;
                            }
        }
    }

    
    /**
     * Receives the decision selected by the player.
     * Sends the decision to DecisionManager and updates the request queue.
     */
    private void processDecision(String decision) {

        Request r = gp.reqList.currentRequest;

        if (r == null) {
            return;
        }

        boolean completed =
                gp.decisionManager.handleDecisionResult(
                        r,
                        decision,
                        gp.reqList.allRequests,
                        gp.reqList.history,
                        gp.history);

        if (completed) {
            gp.incrementRequestsHandled();
        }

        //Backend corrects approval range
        gp.dashboard.approval =
                gp.decisionManager.clampApproval(
                        gp.dashboard.approval);

        gp.checkGameOver();

        if (gp.gameState == gp.reportState) {
            return;
        }

        gp.reqList.loadNextRequest();
    }

    //Handles restarting stats for a fresh game session.
    public void resetGame() {
        gp.dashboard.budget = gp.pSetup.STARTING_BUDGET;
        gp.dashboard.approval = gp.pSetup.STARTING_APPROVAL;
        gp.dashboard.minutes = 5;
        gp.dashboard.seconds = 0;
        gp.history.clear();
        gp.reqList.history.clear();
        gp.player.setDefaultValues();
    }

    // Main menu button and Exit screen logic.
    private void handleTitleClick() {
        if (!gp.ui.confirmExitState) {
            if (gp.ui.commandNum == 0) gp.gameState = gp.loginState;
            if (gp.ui.commandNum == 1) {
                gp.gameState = gp.instructionState; 
                gp.instructions.subState = 0;
            }
            if (gp.ui.commandNum == 2) {
            	gp.reportView.loadGameHistory(); 
            	gp.gameState = gp.reportState;
            }
            	
            if (gp.ui.commandNum == 3) {
                gp.ui.confirmExitState = true;
                gp.ui.commandNum = -1;
            } 
            if (gp.ui.commandNum == 6) {
                gp.gameState = gp.adminLoginState;
                gp.ui.commandNum = -1;
            }
        } else {
            // Use indices from UI.java
            if (gp.ui.commandNum == 4) { // YES
                System.exit(0);
            }
            if (gp.ui.commandNum == 5) { // NO
                gp.ui.confirmExitState = false;
                gp.ui.commandNum = -1;
            }
        }
    }

    private void handleInstructionClick(int x, int y) {
        if (gp.instructions.nextBtn.contains(x, y)) {
            if (gp.instructions.subState < 8) gp.instructions.subState++;
            else {
                gp.gameState = gp.titleState;
                gp.instructions.subState = 0;
            }
        }
        else if (gp.instructions.backBtn.contains(x, y)) {
            if (gp.instructions.subState > 0) gp.instructions.subState--;
        }
    }

   
    private void handleLoginClick() {

        int x = mouseX;
        int y = mouseY;

        // Back button
        if (x >= 10 && x <= 100 && y >= 10 && y <= 50) {

            gp.loginM.activeField = -1;
            gp.gameState = gp.titleState;
            gp.ui.commandNum = -1;

        // Username field click
        } else if (y >= gp.tileSize * 4 - 40 && y <= gp.tileSize * 4 + 10) {

            gp.loginM.activeField = 0;

        // Password field click
        } else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) {

            gp.loginM.activeField = 1;

        // Login/Create button click
        } else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) {

            gp.loginM.activeField = -1;

            if (gp.loginM.isSignUp) {
                gp.loginM.registerUser();
            } else if (gp.loginM.validateLogin()) {
                gp.gameState = gp.setupState;
            }

        // Switch link click
        } else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) {

            gp.loginM.activeField = -1;
            gp.loginM.isSignUp = !gp.loginM.isSignUp;
        }
    }
	

    public void handleSetupClick(int x, int y) {
        int boxX = gp.tileSize * 6;
        int boxY = gp.tileSize * 5 - 30;
        gp.pSetup.nameBoxSelected = (x >= boxX && x <= boxX + (gp.tileSize * 6) && y >= boxY && y <= boxY + 40);
        
        if (gp.pSetup.subState == 1
                && gp.pSetup.validatePresidentName()) {

            gp.gameState = gp.playState;
        }
        
        else if (gp.pSetup.subState == 2) gp.gameState = gp.loginState;
    }

 

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX(); mouseY = e.getY();
        if (gp.gameState == gp.titleState) handleTitleHover(mouseX, mouseY);
        else if (gp.gameState == gp.loginState) handleLoginHover(mouseX, mouseY);
        else if (gp.gameState == gp.setupState) handleSetupHover(mouseX, mouseY);
    }

    public void handleTitleHover(int x, int y) {

        if (!gp.ui.confirmExitState) {

            if (x >= gp.tileSize * 3 && x <= gp.tileSize * 12) {
                //Start New Term
                if (y >= gp.tileSize * 5 - 40 && y <= gp.tileSize * 5 + 10) {
                    gp.ui.commandNum = 0;
                }

                //Instructions
                else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) {
                    gp.ui.commandNum = 1;
                }

                //View Reports
                else if (y >= gp.tileSize * 7 - 40 && y <= gp.tileSize * 7 + 10) {
                    gp.ui.commandNum = 2;
                }

                //Admin
                else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) {
                    gp.ui.commandNum = 6;
                }

                //Exit
                else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) {
                    gp.ui.commandNum = 3;
                }

                else {
                    gp.ui.commandNum = -1;
                }

            } else {
                gp.ui.commandNum = -1;
            }

        } else {

            int centerY = (gp.screenHeight / 2) + (gp.tileSize * 2);

            if (y >= centerY - 40 && y <= centerY + 10) {

                if (x > gp.screenWidth / 2 - 150 && x < gp.screenWidth / 2 - 20) {
                    gp.ui.commandNum = 4;
                }

                else if (x > gp.screenWidth / 2 + 20 && x < gp.screenWidth / 2 + 150) {
                    gp.ui.commandNum = 5;
                }

                else {
                    gp.ui.commandNum = -1;
                }

            } else {
                gp.ui.commandNum = -1;
            }
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {

        //Scrolls through the Decision History screen
        if (gp.gameState == gp.historyState) {

            gp.historyView.scrollOffset +=
                    e.getWheelRotation() * 20;

            if (gp.historyView.scrollOffset < 0) {
                gp.historyView.scrollOffset = 0;
            }
        }

        //Scrolls through the Admin Request Manager
        else if (gp.gameState == gp.adminRequestState) {

            gp.requestManager.scrollOffset +=
                    e.getWheelRotation() * 30;

            //Stops scrolling above the first request
            if (gp.requestManager.scrollOffset < 0) {
                gp.requestManager.scrollOffset = 0;
            }

            //Gets accurate maximum scroll amount
            int maxScroll =
                    gp.requestManager.getMaxScroll();

            //Stops scrolling past the last request
            if (gp.requestManager.scrollOffset > maxScroll) {
                gp.requestManager.scrollOffset = maxScroll;
            }
        }

        //Scrolls through the Admin User Manager
        else if (gp.gameState == gp.adminUserState) {

            gp.userManager.scrollOffset +=
                    e.getWheelRotation() * 30;

            //Stops scrolling above the first user
            if (gp.userManager.scrollOffset < 0) {
                gp.userManager.scrollOffset = 0;
            }

            //Gets the correct maximum scroll amount
            int maxScroll =
                    gp.userManager.getMaxScroll();

            //Stops scrolling past the last user
            if (gp.userManager.scrollOffset > maxScroll) {
                gp.userManager.scrollOffset = maxScroll;
            }
        }
    }
    
    public void mouseDragged(MouseEvent e) 
    { 
    	mouseX = e.getX(); mouseY = e.getY(); 
    }
    public void mouseClicked(MouseEvent e) 
    {

    }
    public void mouseReleased(MouseEvent e) {

        //Removes glow when mouse is released
        gp.adminLogin.loginPressed = false;
        
    }
    public void mouseEntered(MouseEvent e) 
    {
    	
    }
    public void mouseExited(MouseEvent e) 
    {
    	
    }

    public void handleLoginHover(int x, int y) {
        if (x >= 10 && x <= 100 && y >= 10 && y <= 50) 
        	gp.loginM.subState = 4;
        else if (y >= gp.tileSize * 4 - 40 && y <= gp.tileSize * 4 + 10) 
        	gp.loginM.subState = 0;
        else if (y >= gp.tileSize * 6 - 40 && y <= gp.tileSize * 6 + 10) 
        	gp.loginM.subState = 1;
        else if (y >= gp.tileSize * 8 - 40 && y <= gp.tileSize * 8 + 10) 
        	gp.loginM.subState = 2;
        else if (y >= gp.tileSize * 9 - 40 && y <= gp.tileSize * 9 + 10) 
        	gp.loginM.subState = 3;
        else gp.loginM.subState = -1;
    }

    public void handleSetupHover(int x, int y) {
        if (x >= 10 && x <= 100 && y >= 10 && y <= 50) 
        	gp.pSetup.subState = 2;
        else if (y >= gp.tileSize * 5 - 30 && y <= gp.tileSize * 6 - 30) 
        	gp.pSetup.subState = 0;
        else if (y >= gp.tileSize * 7 && y <= gp.tileSize * 8) 
        	gp.pSetup.subState = 1;
        else gp.pSetup.subState = -1;
    }
    
  //Handles clicks on the Admin Login screen
    private void handleAdminLoginClick(int x, int y) {

        //Back button
        if (gp.adminLogin.backBtn.contains(x, y)) {

            gp.adminLogin.activeField = -1;

            //Clears login details when leaving the screen
            gp.adminLogin.username = "";
            gp.adminLogin.password = "";
            gp.adminLogin.errorMessage = "";

            gp.gameState = gp.titleState;
        }

        //Username textbox
        else if (gp.adminLogin.usernameBox.contains(x, y)) {

            gp.adminLogin.activeField = 0;
            gp.adminLogin.errorMessage = "";
        }

        //Password textbox
        else if (gp.adminLogin.passwordBox.contains(x, y)) {

            gp.adminLogin.activeField = 1;
            gp.adminLogin.errorMessage = "";
        }

        //Login button
        else if (gp.adminLogin.loginBtn.contains(x, y)) {

            gp.adminLogin.loginPressed = true;
            gp.adminLogin.activeField = -1;

            if (gp.adminLogin.validateLogin()) {
                gp.gameState = gp.adminState;
            }
        }
    }
    
    //Handles clicks on the main Admin Menu
    private void handleAdminMenuClick(int x, int y) {

        //Manage Requests Button
        if (gp.adminMenu.manageRequestsBtn.contains(x, y)) {
        	
        	//Reloads requests before opening the screen
        	gp.requestManager.loadRequests();

        	//Starts the table at the top
        	gp.requestManager.scrollOffset = 0;

        	//Opens the Request Management screen
        	gp.gameState = gp.adminRequestState;


        }

        //Manage Users Button
        else if (gp.adminMenu.manageUsersBtn.contains(x, y)) {

        	//Reloads users before opening the screen
        	gp.userManager.loadUsers();

        	//Starts the table at the top
        	gp.userManager.scrollOffset = 0;

        	//Opens the User Management screen
        	gp.gameState = gp.adminUserState;
        }

        //Back Button
        else if (gp.adminMenu.backBtn.contains(x, y)) {

            //Clears admin login information when logging out
            gp.adminLogin.username = "";
            gp.adminLogin.password = "";
            gp.adminLogin.errorMessage = "";
            gp.adminLogin.activeField = -1;

            gp.gameState = gp.titleState;
        }
    }
    //Handles clicks on the Request Management screen
    private void handleRequestManagerClick(int x, int y) {

    	//DELETE CONFIRMATION IS OPEN
    	if (gp.requestManager.deletingRequest) {

    	    //YES
    	    if (gp.requestManager.confirmDeleteBtn.contains(x, y)) {

    	        gp.requestManager.confirmDeleteRequest();
    	        return;
    	    }

    	    //NO
    	    if (gp.requestManager.cancelDeleteBtn.contains(x, y)) {

    	        gp.requestManager.deletingRequest = false;
    	        gp.requestManager.message = "";
    	        return;
    	    }

    	    //Prevents clicks going through to screen behind
    	    return;
    	}
     
        // ADD/EDIT FORM
        if (gp.requestManager.addingRequest
                || gp.requestManager.editingRequest) {

            //Description
            if (gp.requestManager.descriptionBox.contains(x, y)) {

                gp.requestManager.activeField = 0;
                gp.requestManager.message = "";
                gp.requestFocusInWindow();
                return;
            }

            //Category
            if (gp.requestManager.categoryBox.contains(x, y)) {

                gp.requestManager.activeField = 1;
                gp.requestManager.message = "";
                gp.requestFocusInWindow();
                return;
            }

            //Cost
            if (gp.requestManager.costBox.contains(x, y)) {

                gp.requestManager.activeField = 2;
                gp.requestManager.message = "";
                gp.requestFocusInWindow();
                return;
            }

            //Approval Impact
            if (gp.requestManager.impactBox.contains(x, y)) {

                gp.requestManager.activeField = 3;
                gp.requestManager.message = "";
                gp.requestFocusInWindow();
                return;
            }

            //Save
            if (gp.requestManager.saveBtn.contains(x, y)) {

                if (gp.requestManager.editingRequest) {

                    gp.requestManager.saveEditedRequest();

                } else {

                    gp.requestManager.saveNewRequest();
                }

                return;
            }

            //Cancel
            if (gp.requestManager.cancelBtn.contains(x, y)) {

                gp.requestManager.addingRequest = false;
                gp.requestManager.editingRequest = false;

                gp.requestManager.newDescription = "";
                gp.requestManager.newCategory = "";
                gp.requestManager.newCost = "";
                gp.requestManager.newImpact = "";

                gp.requestManager.originalRequestID = "";

                gp.requestManager.message = "";
                gp.requestManager.activeField = -1;

                return;
            }

            return;
        }


        // TABLE SELECTION
        if (x >= 40
                && x <= gp.screenWidth - 40
                && y >= 100
                && y <= gp.screenHeight - 120) {

            gp.requestManager.selectRequestAt(y);
            return;
        }


        // ADD
        if (gp.requestManager.addBtn.contains(x, y)) {

            gp.requestManager.addingRequest = true;
            gp.requestManager.editingRequest = false;

            gp.requestManager.newDescription = "";
            gp.requestManager.newCategory = "";
            gp.requestManager.newCost = "";
            gp.requestManager.newImpact = "";

            gp.requestManager.activeField = 0;
            gp.requestManager.message = "";

            gp.requestFocusInWindow();
        }

        // EDIT
        else if (gp.requestManager.editBtn.contains(x, y)) {

            if (!Validation.isValidString(
                    gp.requestManager.selectedRequestID)) {

                gp.requestManager.message =
                        "Please select a request first.";

                return;
            }

            gp.requestManager.loadRequestForEdit(
                    gp.requestManager.selectedRequestID);

            gp.requestFocusInWindow();
        }


      //DELETE REQUEST
        else if (gp.requestManager.deleteBtn.contains(x, y)) {

            if (!Validation.isValidString(
                    gp.requestManager.selectedRequestID)) {

                gp.requestManager.message =
                        "Please select a request first.";

                return;
            }

            gp.requestManager.deletingRequest = true;
            gp.requestManager.message = "";
        }


        // BACK
        else if (gp.requestManager.backBtn.contains(x, y)) {

            gp.requestManager.selectedRequestID = "";
            gp.gameState = gp.adminState;
        }
    }
    
  //Handles clicks on the User Management screen
    private void handleUserManagerClick(int x, int y) {

    	//DELETE CONFIRMATION IS OPEN
    	if (gp.userManager.deletingUser) {

    	    //Yes
    	    if (gp.userManager.confirmDeleteBtn.contains(x, y)) {

    	        gp.userManager.confirmDeleteUser();
    	        return;
    	    }

    	    //No
    	    if (gp.userManager.cancelDeleteBtn.contains(x, y)) {

    	        gp.userManager.deletingUser = false;
    	        gp.userManager.message = "";
    	        return;
    	    }

    	    return;
    	}
    	
        //ADD OR EDIT FORM IS OPEN
        if (gp.userManager.addingUser
                || gp.userManager.editingUser) {

            //Username textbox
            if (gp.userManager.usernameBox.contains(x, y)) {

                gp.userManager.activeField = 0;
                gp.userManager.message = "";
                gp.requestFocusInWindow();
                return;
            }

            //Password textbox
            if (gp.userManager.passwordBox.contains(x, y)) {

                gp.userManager.activeField = 1;
                gp.userManager.message = "";
                gp.requestFocusInWindow();
                return;
            }

            //SAVE button
            if (gp.userManager.saveBtn.contains(x, y)) {

                if (gp.userManager.editingUser) {

                    gp.userManager.saveEditedUser();

                } else {

                    gp.userManager.saveNewUser();
                }

                return;
            }

            //CANCEL button
            if (gp.userManager.cancelBtn.contains(x, y)) {

                gp.userManager.addingUser = false;
                gp.userManager.editingUser = false;

                gp.userManager.newUsername = "";
                gp.userManager.newPassword = "";
                gp.userManager.originalUsername = "";

                gp.userManager.message = "";
                gp.userManager.activeField = -1;

                return;
            }

            return;
        }


        //SELECT A USER FROM THE TABLE
        if (x >= 120
                && x <= gp.screenWidth - 120
                && y >= 110
                && y <= gp.screenHeight - 120) {

            gp.userManager.selectUserAt(y);
            return;
        }


        //ADD USER
        if (gp.userManager.addBtn.contains(x, y)) {

            gp.userManager.addingUser = true;
            gp.userManager.editingUser = false;

            gp.userManager.newUsername = "";
            gp.userManager.newPassword = "";

            gp.userManager.message = "";
            gp.userManager.activeField = 0;

            gp.requestFocusInWindow();
        }


        //EDIT USER
        else if (gp.userManager.editBtn.contains(x, y)) {

            //Checks that a user has been selected
            if (!Validation.isValidString(
                    gp.userManager.selectedUsername)) {

                gp.userManager.message =
                        "Please select a user first.";

                return;
            }

            //Loads the selected user's information
            gp.userManager.loadUserForEdit(
                    gp.userManager.selectedUsername);

            gp.requestFocusInWindow();
        }


        //DELETE USER
        else if (gp.userManager.deleteBtn.contains(x, y)) {

            if (!Validation.isValidString(
                    gp.userManager.selectedUsername)) {

                gp.userManager.message =
                        "Please select a user first.";

                return;
            }

            gp.userManager.deletingUser = true;
            gp.userManager.message = "";
        }


        //BACK
        else if (gp.userManager.backBtn.contains(x, y)) {

            gp.userManager.selectedUsername = "";
            gp.gameState = gp.adminState;
        }
    }
    
    
}