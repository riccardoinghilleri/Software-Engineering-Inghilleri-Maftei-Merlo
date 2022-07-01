package it.polimi.ingsw.client.cli;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * InputController class manages all the data received in input from the client.
 */
public class InputController {
    private static Scanner reader;
    private static PrintStream printer;

    /**
     * This method sets the scanner.
     * @param scanner instance of Scanner class
     */
    public static void setScanner(Scanner scanner) {
        reader = scanner;
    }

    /**
     * This method sets the printer.
     * @param printStream instance of PrintStream class
     */
    public static void setPrinter(PrintStream printStream) {
        printer = printStream;
    }

    /**
     * This method checks , parsing the input, that the insert input is a number.
     * If no, it asks the player to try again and insert a number.
     */
    public static int checkParseInt() {
        int result = -1;
        boolean error;
        do {
            try {
                result = Integer.parseInt(reader.nextLine());
                error = false;
            } catch (NumberFormatException e) {
                System.out.println(">Invalid input: you have to insert a number. Please try again.");
                error = true;
            }
        } while (error);
        return result;
    }

    /**
     * This method checks the input when the client has to answer [y/n]
     * @return the answer given by the client
     */
    public static String checkYNInput() {
        String response = reader.nextLine();
        while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
            printer.println(">Invalid input.Please try again.");
            response = reader.nextLine();
        }
        return response;
    }

    /**
     *This method check if the input inserted by the client, corresponds to one
     * of the values of the list
     * @param values available numbers
     * @return the chosen number
     */
    public static int checkInt(List<Integer> values) {
        int data = InputController.checkParseInt();
        while (!values.contains(data)) {
            printer.println(">Invalid input. Please try again.");
            data = InputController.checkParseInt();
        }
        return data;
    }

    /**
     * This method check if the input inserted by the client, corresponds to one
     * of the values of the list
     * @param values available string that can be passed in input
     * @return the chosen parameter
     */
    public static String checkString(List<String> values) {
        String parameter = reader.nextLine().toUpperCase();
        while (!values.contains(parameter)) {
            printer.println(">Invalid input. Please try again");
            parameter = reader.nextLine().toUpperCase();
        }
        return parameter;
    }
    /**
     * This method checks if the number chosen by the player is in the available range.
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    public static int checkRange(int min, int max) {
        int data = InputController.checkParseInt();
        while (data < min || data > max) {
            printer.println(">Invalid input. Please try again.");
            data = checkParseInt();
        }
        return data;
    }
}
