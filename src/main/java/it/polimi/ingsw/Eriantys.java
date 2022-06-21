package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.server.Server;

import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Class Eriantys is the main class of this project.
 *
 * @author Riccardo Inghilleri, Daniela Maftei, Manuela Merlo
 */
public class Eriantys {

    /**
     * Method main selects Cli, Gui or Server based on the arguments provided.
     *
     * @param args of type String[]
     */
    public static void main(String[] args) {
        Constants.clearScreen();
        System.out.println(Constants.ERIANTYS);
        System.out.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela\n");
        System.out.println("Welcome to Eriantys!\nWhat do you want to launch?");
        System.out.println("0. SERVER\n1. CLI\n2. GUI");
        System.out.println("\n>Type the number of the desired option: ");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }
        switch (input) {
            case 0:
                Server.main(null);
                break;
            case 1:
                Cli.main(null);
                break;
            case 2:
                Gui.main(null);
                break;
            default:
                System.err.println("Invalid argument, please run the executable again!");
        }
    }
}
