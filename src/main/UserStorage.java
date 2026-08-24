package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class UserStorage.
 */
//Handles reading, writing and managing user data in secondary storage
public class UserStorage {

    /** The user data. */
    //Stores all users loaded from Log in & Sign Up.txt
    private ArrayList<String[]> userData = new ArrayList<>();
    
    /** The last error. */
    private String lastError = "";

    /**
     * Gets the last error.
     *
     * @return the last error
     */
    public String getLastError() {
        return lastError;
    }

    /**
     * Loads all valid user records from Log in & Sign Up.txt.
     * Returns the ArrayList containing the loaded users.
     *
     * @return the array list
     */
    public ArrayList<String[]> loadUsers() {

        userData.clear();
        lastError = "";


        try (BufferedReader br =
                new BufferedReader(
                        new FileReader("Log in & Sign Up.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (!line.isEmpty()) {

                    String[] parts = line.split(",", 2);

                    if (parts.length == 2) {

                        String username = parts[0].trim();
                        String password = parts[1].trim();

                        //Only stores valid records
                        if (Validation.isValidString(username)
                                && Validation.isValidString(password)) {

                            userData.add(new String[] {
                                    username,
                                    password
                            });
                        }
                    }
                }
            }

        } catch (Exception e) {

            lastError =
                    "Could not read user data.";

            e.printStackTrace();
        }

        return userData;
    }

    /**
     * Returns true if the supplied username already exists.
     *
     * @param username the username
     * @return true, if successful
     */
    public boolean usernameExists(String username) {

        if (!Validation.isValidString(username)) {
            return false;
        }

        for (String[] user : userData) {

            if (user.length >= 2
                    && user[0].trim().equalsIgnoreCase(username.trim())) {

                return true;
            }
        }

        return false;
    }

    /**
     * Receives a username and password.
     * Validates the data and adds the new user to secondary storage.
     * Returns true if the user was successfully added.
     *
     * @param username the username
     * @param password the password
     * @return true, if successful
     */
    public boolean addUser(String username, String password) {

        if (!Validation.isValidString(username)
                || !Validation.isValidString(password)) {

            return false;
        }

        if (usernameExists(username)) {
            return false;
        }

        userData.add(new String[] {
                username.trim(),
                password.trim()
        });

        return saveAllUsers();
    }

    /**
     * Edits an existing user.
     * Returns true if the user is found and successfully updated.
     *
     * @param oldUsername the old username
     * @param newUsername the new username
     * @param newPassword the new password
     * @return true, if successful
     */
    public boolean editUser(
            String oldUsername,
            String newUsername,
            String newPassword) {

        if (!Validation.isValidString(oldUsername)
                || !Validation.isValidString(newUsername)
                || !Validation.isValidString(newPassword)) {

            return false;
        }

        for (int i = 0; i < userData.size(); i++) {

            String[] user = userData.get(i);

            if (user[0].trim()
                    .equalsIgnoreCase(oldUsername.trim())) {

                //Checks if new username belongs to another user
                for (int j = 0; j < userData.size(); j++) {

                    if (j != i
                            && userData.get(j)[0].trim()
                            .equalsIgnoreCase(newUsername.trim())) {

                        return false;
                    }
                }

                userData.set(
                        i,
                        new String[] {
                                newUsername.trim(),
                                newPassword.trim()
                        });

                return saveAllUsers();
            }
        }

        return false;
    }

    /**
     * Deletes a user using their username.
     * Returns true if the user is found and removed.
     *
     * @param username the username
     * @return true, if successful
     */
    public boolean deleteUser(String username) {

        if (!Validation.isValidString(username)) {
            return false;
        }

        for (int i = 0; i < userData.size(); i++) {

            if (userData.get(i)[0].trim()
                    .equalsIgnoreCase(username.trim())) {

                userData.remove(i);

                return saveAllUsers();
            }
        }

        return false;
    }

    /**
     * Rewrites Log in & Sign Up.txt using the current userData.
     * Returns true if the file is successfully saved.
     *
     * @return true, if successful
     */
    private boolean saveAllUsers() {

        try (BufferedWriter bw =
                new BufferedWriter(
                        new FileWriter("Log in & Sign Up.txt"))) {

            for (String[] user : userData) {

                if (user.length >= 2) {

                    bw.write(
                            user[0].trim()
                            + ","
                            + user[1].trim());

                    bw.newLine();
                }
            }

            return true;
        } catch (Exception e) {

            lastError = "Could not save user data.";
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Returns the current user data.
     *
     * @return the user data
     */
    public ArrayList<String[]> getUserData() {
        return userData;
    }
    
    
    /**
     * Validates a username and password against stored users.
     * Returns true if a matching account is found.
     *
     * @param username the username
     * @param password the password
     * @return true, if successful
     */
    public boolean validateLogin(String username, String password) {

        if (!Validation.isValidString(username)
                || !Validation.isValidString(password)) {
            return false;
        }

        //Make sure latest data is loaded
        loadUsers();

        for (String[] user : userData) {

            if (user.length >= 2
                    && user[0].trim().equals(username.trim())
                    && user[1].trim().equals(password.trim())) {

                return true;
            }
        }

        return false;
    }


    /**
     * Returns true if a username exists in storage.
     *
     * @param username the username
     * @return true, if successful
     */
    public boolean userExists(String username) {

        if (!Validation.isValidString(username)) {
            return false;
        }

        loadUsers();

        for (String[] user : userData) {

            if (user.length >= 2
                    && user[0].trim()
                    .equalsIgnoreCase(username.trim())) {

                return true;
            }
        }

        return false;
    }
}

