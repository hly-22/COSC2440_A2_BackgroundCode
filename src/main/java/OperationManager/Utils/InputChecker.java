package OperationManager.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputChecker {

    // check for valid CID
    public static boolean isValidCIDFormat(String cID) {
        String regex = "^c-[0-9]{7}$"; // c- followed by exactly 7 digits
        return cID.matches(regex);
    }
    // check for valid PID
    public static boolean isValidPIDFormat(String pID) {
        String regex = "^p-[0-9]{7}$"; // p- followed by exactly 7 digits
        return pID.matches(regex);
    }
    // check for valid FID
    public static boolean isValidFIDFormat(String fID) {
        String regex = "^f-[0-9]{7}$"; // f- followed by exactly 7 digits
        return fID.matches(regex);
    }
    // check for valid phone number
    public static boolean isValidPhoneNumber(String phone) {
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        int length = digitsOnly.length();
        return length >= 9 && length <= 11;
    }
    // check for valid email
    public static boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    // check for valid insurance card format
    public static boolean isValidCardFormat(String cardNumber) {
        String regex = "^[0-9]{10}$";
        return cardNumber.matches(regex);
    }
}
