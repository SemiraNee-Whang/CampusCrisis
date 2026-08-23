package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//Handles admin credential validation from secondary storage
public class AdminStorage {

    /**
     * Validates the entered admin username and password.
     * Returns true if a matching admin account is found.
     */
    public boolean validateAdmin(
            String username,
            String password) {

        if (!Validation.isValidString(username)
                || !Validation.isValidString(password)) {

            return false;
        }

        try (BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                getClass()
                                .getResourceAsStream(
                                        "/admin.txt")))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts =
                        line.split("\\|");

                if (parts.length >= 2) {

                    String fileUsername =
                            parts[0].trim();

                    String filePassword =
                            parts[1].trim();

                    if (username.equals(fileUsername)
                            && password.equals(filePassword)) {

                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}