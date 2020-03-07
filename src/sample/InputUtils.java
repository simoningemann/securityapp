package sample;

import java.util.Scanner;

public class InputUtils {

    public static char[] prompt(String message) {

        // setup scanner
        Scanner scan = new Scanner(System.in);

        // ask for input
        System.out.println(message);
        char[] password = scan.next().toCharArray();

        return password;
    }

    public static char[] requireStrongPassword() {
        int minLength = 10;
        char [] password = prompt("Choose a password of at least " + minLength + " characters, which contains at least one capital letter, one lower case letter, one number and one symbol.");

        while(isPasswordStrong(password, minLength))
            password = prompt("Choose a password of at least " + minLength + " characters, which contains at least one capital letter, one lower case letter, one number and one special character.");
        
        return password;

    }

    public static boolean isPasswordStrong(char[] password, int minLength) {
        return password.length < minLength || !containsUpper(password) || !containsLower(password) || !containsDigit(password) || !containsSpecialCharacter(password);
    }

    public static boolean containsUpper (char[] string) {

        for (char ch : string)
            if (Character.isUpperCase(ch)) return true;

        return false;

    }

    public static boolean containsLower (char[] string) {

        for (char ch : string)
            if (Character.isLowerCase(ch)) return true;

        return false;

    }

    public static boolean containsDigit (char[] string) {

        for (char ch : string)
            if (Character.isDigit(ch)) return true;

        return false;

    }

    public static boolean containsSpecialCharacter (char[] string) {

        for (char ch : string)
            if (!Character.isLetterOrDigit(ch)) return true;

        return false;

    }

}
