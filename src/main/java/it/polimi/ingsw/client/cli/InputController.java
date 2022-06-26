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
     * This method set the scanner, class that simply can parse
     * @param scanner instance of Scanner class
     */
    public static void setScanner(Scanner scanner) {
        reader = scanner;
    }
    /**
     * This method set the printer, that simply format the data to the output stream
     * @param printStream instance of PrintStream class
     */
    public static void setPrinter(PrintStream printStream) {
        printer = printStream;
    }

    /**
     * This method check , parsing the input, that the insert input is a number.
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
     *
     * @return
     */
    public static String checkYNInput() {
        String response = reader.nextLine();
        while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
            printer.println(">Invalid input.Please try again.");
            //printer.println(">Are you sure about your choice? [y/n]: ");
            response = reader.nextLine();
            /*if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n"))
                Constants.clearRowBelow(2);*/
        }
        return response;
    }

    /**
     *
     * @param values
     * @return
     */
    public static int checkInt(List<Integer> values) {
        int data = InputController.checkParseInt();
        while (!values.contains(data)) {
            printer.println(">Invalid input. Please try again.");
            data = InputController.checkParseInt();
            /*if(!values.contains(data))
                Constants.clearRowBelow(2);*/
        }
        return data;
    }

    public static String checkString(List<String> values) {
        String parameter = reader.nextLine().toUpperCase();
        while (!values.contains(parameter)) {
            printer.println(">Invalid input. Please try again");
            parameter = reader.nextLine().toUpperCase();
            /*if(!values.contains(parameter))
                Constants.clearRowBelow(2);*/
        }
        return parameter;
    }
    /**
     * This method checks if the number chosen by the player is in the range.
     * @param min minimum value allowed
     * @param max maximum value allowed
     */
    public static int checkRange(int min, int max) {
        int data = InputController.checkParseInt();
        while (data < min || data > max) {
            printer.println(">Invalid input. Please try again.");
            data = checkParseInt();
            /*if(data < min || data > max)
                Constants.clearRowBelow(2);*/
        }
        return data;
    }
}
