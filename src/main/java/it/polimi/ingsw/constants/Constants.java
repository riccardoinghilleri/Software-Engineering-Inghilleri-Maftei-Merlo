package it.polimi.ingsw.constants;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;

/**
 * This class contains the colors string and the writing to be shown on the screen when a client start playing in CLI mode.
 */
public class Constants {
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_BLACK = "\033[30;47m";
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_GREEN = "\033[32m";
    public static final String ANSI_YELLOW = "\033[33m";
    public static final String ANSI_BLUE = "\033[34m";
    public static final String ANSI_PINK = "\033[95m";
    public static final String ANSI_WHITE = "\033[37m";
    public static final String ANSI_GREY = "\033[90m";
    public static final String ERIANTYS =
                    " ███████╗ ██████╗  ██╗  █████╗  ██╗   ██╗ ████████╗ ██╗  ██╗ ███████╗\n" +
                    " ██╔════╝ ██╔══██╗ ██║ ██╔══██╗ ███╗  ██║ ╚══██╔══╝ ╚██╗██╔╝ ██╔════╝\n" +
                    " ███████╗ ██████╔╝ ██║ ███████║ ██╔██╗██║    ██║     ╚═██╔╝  ███████╗\n" +
                    " ██╔════╝ ██╔══██╗ ██║ ██╔══██║ ██║╚═███║    ██║      ██╔╝   ╚════██║\n" +
                    " ███████╗ ██║  ██║ ██║ ██║  ██║ ██║  ╚██║    ██║     ██╔╝    ███████║\n" +
                    " ╚══════╝ ╚═╝  ╚═╝ ╚═╝ ╚═╝  ╚═╝ ╚═╝   ╚═╝    ╚═╝     ╚═╝     ╚══════╝\n";

    public static final String SERVER_STATUS =
                    " ███████╗ ███████╗ ██████╗  ██╗     ██╗ ███████╗ ██████╗        ███████╗ ████████╗  █████╗  ████████╗ ██╗  ██╗ ███████╗\n" +
                    " ██╔════╝ ██╔════╝ ██╔══██╗ ╚██╗   ██╔╝ ██╔════╝ ██╔══██╗       ██╔════╝ ╚══██╔══╝ ██╔══██╗ ╚══██╔══╝ ██║  ██║ ██╔════╝\n" +
                    " ███████╗ ███████╗ ██████╔╝  ╚██╗ ██╔╝  ███████╗ ██████╔╝       ███████╗    ██║    ███████║    ██║    ██║  ██║ ███████╗\n" +
                    " ╚════██║ ██╔════╝ ██╔══██╗   ╚████╔╝   ██╔════╝ ██╔══██╗       ╚════██║    ██║    ██╔══██║    ██║    ██║  ██║ ╚════██║\n" +
                    " ███████║ ███████╗ ██║  ██║    ╚██╔╝    ███████╗ ██║  ██║       ███████║    ██║    ██║  ██║    ██║    ╚█████╔╝ ███████║\n" +
                    " ╚══════╝ ╚══════╝ ╚═╝  ╚═╝     ╚═╝     ╚══════╝ ╚═╝  ╚═╝       ╚══════╝    ╚═╝    ╚═╝  ╚═╝    ╚═╝     ╚════╝  ╚══════╝\n";

    public static final String CONNECTION =
                    " ███████╗  ██████╗  ██╗   ██╗ ██╗   ██╗ ███████╗ ███████╗ ████████╗ ██╗  ██████╗  ██╗   ██╗\n" +
                    " ██╔════╝ ██╔═══██╗ ███╗  ██║ ███╗  ██║ ██╔════╝ ██╔════╝ ╚══██╔══╝ ██║ ██╔═══██╗ ███╗  ██║\n" +
                    " ██║      ██║   ██║ ██╔██╗██║ ██╔██╗██║ ███████╗ ██║         ██║    ██║ ██║   ██║ ██╔██╗██║\n" +
                    " ██║      ██║   ██║ ██║╚═███║ ██║╚═███║ ██╔════╝ ██║         ██║    ██║ ██║   ██║ ██║╚═███║\n" +
                    " ███████╗ ╚██████╔╝ ██║  ╚██║ ██║  ╚██║ ███████╗ ███████╗    ██║    ██║ ╚██████╔╝ ██║  ╚██║\n" +
                    " ╚══════╝  ╚═════╝  ╚═╝   ╚═╝ ╚═╝   ╚═╝ ╚══════╝ ╚══════╝    ╚═╝    ╚═╝  ╚═════╝  ╚═╝   ╚═╝\n";
    public static final String SETUP_GAME =
                    " ███████╗ ███████╗ ████████╗ ██╗  ██╗ ███████╗       ███████╗  █████╗  ██╗   ██╗ ███████╗\n" +
                    " ██╔════╝ ██╔════╝ ╚══██╔══╝ ██║  ██║ ██╔══██║       ██╔════╝ ██╔══██╗ ███╗ ███║ ██╔════╝\n" +
                    " ███████╗ ███████╗    ██║    ██║  ██║ ███████║       ██║████╗ ███████║ ██╔██╔██║ ███████╗\n" +
                    " ╚════██║ ██╔════╝    ██║    ██║  ██║ ██╔════╝       ██║╚═██║ ██╔══██║ ██║╚═╝██║ ██╔════╝\n" +
                    " ███████║ ███████╗    ██║    ╚█████╔╝ ██║            ███████║ ██║  ██║ ██║   ██║ ███████╗\n" +
                    " ╚══════╝ ╚══════╝    ╚═╝     ╚════╝  ╚═╝            ╚══════╝ ╚═╝  ╚═╝ ╚═╝   ╚═╝ ╚══════╝\n";

    public static final String WAITING =
                    " ██╗ ██╗ ██╗ ██╗  █████╗  ██╗ ████████╗ ██╗ ██╗   ██╗ ███████╗\n" +
                    " ╚██╗╚████╔╝██╔╝ ██╔══██╗ ██║ ╚══██╔══╝ ██║ ███╗  ██║ ██╔════╝\n" +
                    "  ╚██╗╚██╔╝██╔╝  ███████║ ██║    ██║    ██║ ██╔██╗██║ ██║████╗\n" +
                    "   ╚████████╔╝   ██╔══██║ ██║    ██║    ██║ ██║ ╚═██║ ██║╚═██║\n" +
                    "    ╚██╔═██╔╝    ██║  ██║ ██║    ██║    ██║ ██║  ╚██║ ███████║ ██╗ ██╗ ██╗\n" +
                    "     ╚═╝ ╚═╝     ╚═╝  ╚═╝ ╚═╝    ╚═╝    ╚═╝ ╚═╝   ╚═╝ ╚══════╝ ╚═╝ ╚═╝ ╚═╝\n";

    /**
     * This method returns the Ansi_code which corresponds to the specified color
     * @param characterColor the colors of students
     * @return the string of the color corresponding to the ansi_code
     */
    public static String getAnsi(CharacterColor characterColor) {
        if (characterColor == CharacterColor.RED) return ANSI_RED;
        else if (characterColor == CharacterColor.GREEN) return ANSI_GREEN;
        else if (characterColor == CharacterColor.YELLOW) return ANSI_YELLOW;
        else if (characterColor == CharacterColor.BLUE) return ANSI_BLUE;
        else if (characterColor == CharacterColor.PINK) return ANSI_PINK;
        else return null;
    }
    /**
     * This method returns the Ansi_code which corresponds to the specified color
     * @param playerColor the colors of students
     * @return the string of the color corresponding to the ansi_code
     */
    public static String getAnsi(PlayerColor playerColor) {
        if (playerColor == PlayerColor.WHITE) return ANSI_WHITE;
        else if (playerColor == PlayerColor.BLACK) return ANSI_BLACK;
        else if (playerColor == PlayerColor.GREY) return ANSI_GREY;
        else return null;
    }

    public static String moveCursor(int y, int x) {
        return "\033[" + y + ";" + x + "H";
    }

    //quando si sale utilizzare una posizione in meno
    public static String cursorUp(int y) {
        return "\033[" + y + "A";
    }

    //usare una posizione in piu quando si scende
    public static String cursorDown(int y) {
        return "\033[" + y + "B";
    }

    public static String cursorRight(int x) {
        return "\033[" + x + "C";
    }

    public static String cursorLeft(int x) {
        return "\033[" + x + "D";
    }

    public static void moveObject(StringBuilder stringBuilder, int x, String string) {
        stringBuilder.append(cursorRight(x));
        stringBuilder.append(string);
    }

    public static void clearScreen() {
        try {
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder boardFrame(int x, int y, boolean expertMode, int playersNumber) {
        StringBuilder frame = new StringBuilder();
        frame.append(Constants.cursorDown(y));
        int four_x=0, four_y=0;
        if (!expertMode) {
            if(playersNumber==4) four_x=17;
            for (int i = 0; i < 29; i++) {
                frame.append(Constants.cursorRight(x));
                for (int j = 0; j < 166+four_x; j++) {
                    if (i == 0 || i == 28 || j == 0 || j == 165+four_x) {
                        frame.append("█");
                    } else frame.append(" ");
                }
                frame.append("\n");
            }
        } else {
            if(playersNumber==4){
                four_x=2;
                four_y=1;
            }
            for (int i = 0; i < 35+four_y; i++) {
                frame.append(Constants.cursorRight(x));
                for (int j = 0; j < 181+four_x; j++) {
                    if (i == 0 || i == 34+four_y || j == 0 || j == 180+four_x) {
                        frame.append("█");
                    } else frame.append(" ");
                }
                frame.append("\n");
            }
        }
        frame.append(Constants.ANSI_GREEN+" PRESS CTRL+C AT ANY TIME TO END THE GAME.\n"+Constants.ANSI_RESET);
        return frame;
    }

    public static void clearRowBelow(int y) {
        System.out.println(cursorUp(y));
        System.out.print("\033[H\033[0J");
    }
}
