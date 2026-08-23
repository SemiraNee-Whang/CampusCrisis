package main;

//Provides reusable validation methods for user input
public class Validation {

    /**
     * Receives text entered by the user.
     * Returns true if the text contains a value.
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Receives text that should contain an integer.
     * Returns true if the value can be converted to an integer.
     */
    public static boolean isInteger(String value) {

        if (!isValidString(value)) {
            return false;
        }

        try {
            Integer.parseInt(value.trim());
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Receives text that should contain a positive integer.
     * Returns true if the value is an integer greater than or equal to zero.
     */
    public static boolean isPositiveInteger(String value) {

        if (!isInteger(value)) {
            return false;
        }

        return Integer.parseInt(value.trim()) > 0;
    }

    /**
     * Receives a percentage entered as text.
     * Returns true if it is an integer between 0 and 100.
     */
    public static boolean isValidPercentage(String value) {

        if (!isInteger(value)) {
            return false;
        }

        int number = Integer.parseInt(value.trim());

        return number >= 0 && number <= 100;
    }
    
    /**
     * Receives an approval impact entered as text.
     * Returns true if it is an integer between -100 and 100.
     */
    public static boolean isValidImpact(String value) {

        if (!isInteger(value)) {
            return false;
        }

        int impact =
                Integer.parseInt(value.trim());

        return impact >= -100
                && impact <= 100;
    }
}