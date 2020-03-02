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

}
