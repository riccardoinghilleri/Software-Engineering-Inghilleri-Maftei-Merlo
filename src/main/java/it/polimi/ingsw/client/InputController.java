package it.polimi.ingsw.client;

import it.polimi.ingsw.constants.Constants;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class InputController {
    private static Scanner reader;
    private static PrintStream printer;

    public static void setScanner(Scanner scanner) {
        reader = scanner;
    }

    public static void setPrinter(PrintStream printStream) {
        printer = printStream;
    }

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

    public static String checkYNInput() {
        String response = reader.nextLine();
        while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
            printer.println(">Invalid input.Please try again.");
            //printer.println(">Are you sure about your choice? [y/n]: ");
            response = reader.nextLine();
            if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n"))
                Constants.clearRowBelow(2);
        }
        return response;
    }

    public static int checkInt(List<Integer> values) {
        int data = InputController.checkParseInt();
        while (!values.contains(data)) {
            printer.println(">Invalid input. Please try again.");
            data = InputController.checkParseInt();
            if(!values.contains(data))
                Constants.clearRowBelow(2);
        }
        return data;
    }

    public static String checkString(List<String> values){
        String parameter = reader.nextLine().toUpperCase();
        while (!values.contains(parameter)) {
            printer.println(">Invalid input. Please try again");
            parameter = reader.nextLine().toUpperCase();
            if(!values.contains(parameter))
                Constants.clearRowBelow(2);
        }
        return parameter;
    }


    public static int checkRange(int min,int max) {
        int data = InputController.checkParseInt();
        while (data < min || data > max) {
            printer.println(">Invalid input. Please try again.");
            data = checkParseInt();
            if(data < min || data > max)
                Constants.clearRowBelow(2);
        }
        return data;
    }
}
