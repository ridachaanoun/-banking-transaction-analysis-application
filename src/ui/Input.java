package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public final class Input {
    private static final Scanner SC = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Input() {}

    public static String prompt(String label) {
        System.out.print(label);
        return SC.nextLine().trim();
    }

    public static int promptInt(String label) {
        while (true) {
            System.out.print(label);
            String s = SC.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    public static double promptDouble(String label) {
        while (true) {
            System.out.print(label);
            String s = SC.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public static LocalDate promptDate(String label, LocalDate defaultValue) {
        while (true) {
            System.out.print(label + " (yyyy-MM-dd" + (defaultValue != null ? ", Enter to use " + defaultValue + ")" : ")") + ": ");
            String s = SC.nextLine().trim();
            if (s.isEmpty() && defaultValue != null) return defaultValue;
            try {
                return LocalDate.parse(s, DATE_FMT);
            } catch (Exception e) {
                System.out.println("Invalid date. Try again.");
            }
        }
    }

    public static boolean promptYesNo(String label, boolean defaultYes) {
        while (true) {
            System.out.print(label + (defaultYes ? " [Y/n]: " : " [y/N]: "));
            String s = SC.nextLine().trim().toLowerCase();
            if (s.isEmpty()) return defaultYes;
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Please answer y or n.");
        }
    }
}