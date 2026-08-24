package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private GamePanel gp;
    
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        
        
        //Login Screen Typing
        if (gp.gameState == gp.loginState) {
            //subState 0 = Username, subState 1 = Password
        	if (gp.loginM.activeField == 0 || gp.loginM.activeField == 1) {
        	    if (c == KeyEvent.VK_BACK_SPACE) {
        	        if (gp.loginM.activeField == 0 && gp.loginM.userText.length() > 0) {
        	            gp.loginM.userText = gp.loginM.userText.substring(0, gp.loginM.userText.length() - 1);
        	        } else if (gp.loginM.activeField == 1 && gp.loginM.passText.length() > 0) {
        	            gp.loginM.passText = gp.loginM.passText.substring(0, gp.loginM.passText.length() - 1);
        	        }
        	    } else if (c >= 32 && c <= 126) {
        	        if (gp.loginM.activeField == 0 && gp.loginM.userText.length() < 16) {
        	            gp.loginM.userText += c;
        	        } else if (gp.loginM.activeField == 1 && gp.loginM.passText.length() < 16) {
        	            gp.loginM.passText += c;
        	        }
        	    }
        	}
        }
        
      //Admin Login Screen Typing
        else if (gp.gameState == gp.adminLoginState) {

            //USERNAME FIELD
            if (gp.adminLogin.activeField == 0) {

                //Backspace
                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.adminLogin.username.length() > 0) {

                        gp.adminLogin.username =
                                gp.adminLogin.username.substring(
                                        0,
                                        gp.adminLogin.username.length() - 1);
                    }
                }

                //Normal characters
                else if (c >= 32 && c <= 126) {

                    if (gp.adminLogin.username.length() < 16) {
                        gp.adminLogin.username += c;
                    }
                }
            }

            //PASSWORD FIELD
            else if (gp.adminLogin.activeField == 1) {

                //Backspace
                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.adminLogin.password.length() > 0) {

                        gp.adminLogin.password =
                                gp.adminLogin.password.substring(
                                        0,
                                        gp.adminLogin.password.length() - 1);
                    }
                }

                //Normal characters
                else if (c >= 32 && c <= 126) {

                    if (gp.adminLogin.password.length() < 16) {
                        gp.adminLogin.password += c;
                    }
                }
            }
        }
        
      //Manage Users - Add User Form Typing
      //Manage Users - Add/Edit User Form Typing
        else if (gp.gameState == gp.adminUserState
                && (gp.userManager.addingUser
                || gp.userManager.editingUser)) {

            //USERNAME FIELD
            if (gp.userManager.activeField == 0) {

                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.userManager.newUsername.length() > 0) {

                        gp.userManager.newUsername =
                                gp.userManager.newUsername.substring(
                                        0,
                                        gp.userManager.newUsername.length() - 1);
                    }

                } else if (c >= 32 && c <= 126) {

                    if (gp.userManager.newUsername.length() < 20) {
                        gp.userManager.newUsername += c;
                    }
                }
            }

            //PASSWORD FIELD
            else if (gp.userManager.activeField == 1) {

                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.userManager.newPassword.length() > 0) {

                        gp.userManager.newPassword =
                                gp.userManager.newPassword.substring(
                                        0,
                                        gp.userManager.newPassword.length() - 1);
                    }

                } else if (c >= 32 && c <= 126) {

                    if (gp.userManager.newPassword.length() < 20) {
                        gp.userManager.newPassword += c;
                    }
                }
            }
        }
        
      //MANAGE REQUESTS - ADD REQUEST FORM TYPING
        else if (gp.gameState == gp.adminRequestState
                && gp.requestManager.addingRequest 
                || gp.requestManager.editingRequest) {

            //DESCRIPTION
            if (gp.requestManager.activeField == 0) {

                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.requestManager.newDescription.length() > 0) {

                        gp.requestManager.newDescription =
                                gp.requestManager.newDescription.substring(
                                        0,
                                        gp.requestManager.newDescription.length() - 1);
                    }

                } else if (c >= 32 && c <= 126) {

                    if (gp.requestManager.newDescription.length() < 60) {
                        gp.requestManager.newDescription += c;
                    }
                }
            }

            //CATEGORY
            else if (gp.requestManager.activeField == 1) {

                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.requestManager.newCategory.length() > 0) {

                        gp.requestManager.newCategory =
                                gp.requestManager.newCategory.substring(
                                        0,
                                        gp.requestManager.newCategory.length() - 1);
                    }

                } else if (c >= 32 && c <= 126) {

                    if (gp.requestManager.newCategory.length() < 20) {
                        gp.requestManager.newCategory += c;
                    }
                }
            }

            //COST
            else if (gp.requestManager.activeField == 2) {

                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.requestManager.newCost.length() > 0) {

                        gp.requestManager.newCost =
                                gp.requestManager.newCost.substring(
                                        0,
                                        gp.requestManager.newCost.length() - 1);
                    }

                } else if (Character.isDigit(c)) {

                    if (gp.requestManager.newCost.length() < 8) {
                        gp.requestManager.newCost += c;
                    }
                }
            }

            //APPROVAL IMPACT
            else if (gp.requestManager.activeField == 3) {

                if (c == KeyEvent.VK_BACK_SPACE) {

                    if (gp.requestManager.newImpact.length() > 0) {

                        gp.requestManager.newImpact =
                                gp.requestManager.newImpact.substring(
                                        0,
                                        gp.requestManager.newImpact.length() - 1);
                    }

                } else if (Character.isDigit(c)
                        || (c == '-'
                        && gp.requestManager.newImpact.length() == 0)) {

                    if (gp.requestManager.newImpact.length() < 4) {
                        gp.requestManager.newImpact += c;
                    }
                }
            }
        }
        
        //PRESIDENT SETUP TYPING
        else if (gp.gameState == gp.setupState && gp.pSetup.nameBoxSelected) {
            if (c == KeyEvent.VK_BACK_SPACE) {
                if (gp.pSetup.presidentName.length() > 0) {
                    gp.pSetup.presidentName = gp.pSetup.presidentName.substring(0, gp.pSetup.presidentName.length() - 1);
                }
            } else if (c >= 32 && c <= 126) {
                if (gp.pSetup.presidentName.length() < 20) {
                    gp.pSetup.presidentName += c;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_1) gp.gameState = gp.requestState;
            if (code == KeyEvent.VK_2) gp.gameState = gp.historyState; // New state
        }
        else if (gp.gameState == gp.requestState || gp.gameState == gp.historyState) {
            if (code == KeyEvent.VK_ESCAPE) gp.gameState = gp.playState;
        }
    
       
        
        // Quick Enter for Login
        if (code == KeyEvent.VK_ENTER && gp.gameState == gp.loginState) {
            gp.loginM.activeField = -1; // deselect field
            gp.loginM.subState = 2;     // focus login button
        }
        }

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    

  
}