package it.polimi.ingsw.constants;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;

public class Constants {
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_RED = "\033[31m";
    public static final String ANSI_GREEN = "\033[32m";
    public static final String ANSI_YELLOW = "\033[93m";
    public static final String ANSI_BLUE = "\033[34m";
    public static final String ANSI_PINK = "\033[95m";
    public static final String ANSI_WHITE = "\033[37m";
    public static final String ANSI_BLACK = "\033[96m"; //TODO ciano per adesso
    public static final String ANSI_GREY = "\033[90m";
    public static final String ERIANTYS =
                    " ███████╗ ██████╗  ██╗  █████╗  ██╗   ██╗ ████████╗ ██╗  ██╗ ███████╗\n" +
                    " ██╔════╝ ██╔══██╗ ██║ ██╔══██╗ ███╗  ██║ ╚══██╔══╝ ╚██╗██╔╝ ██╔════╝\n" +
                    " ███████╗ ██████╔╝ ██║ ███████║ ██╔██╗██║    ██║     ╚═██╔╝  ███████╗\n" +
                    " ██╔════╝ ██╔══██╗ ██║ ██╔══██║ ██║╚═███║    ██║      ██╔╝   ╚════██║\n" +
                    " ███████╗ ██║  ██║ ██║ ██║  ██║ ██║  ╚██║    ██║     ██╔╝    ███████║\n" +
                    " ╚══════╝ ╚═╝  ╚═╝ ╚═╝ ╚═╝  ╚═╝ ╚═╝   ╚═╝    ╚═╝     ╚═╝     ╚══════╝\n";

    public static String getAnsi(CharacterColor characterColor) {
        if (characterColor == CharacterColor.RED) return ANSI_RED;
        else if (characterColor == CharacterColor.GREEN) return ANSI_GREEN;
        else if (characterColor == CharacterColor.YELLOW) return ANSI_YELLOW;
        else if (characterColor == CharacterColor.BLUE) return ANSI_BLUE;
        else if (characterColor == CharacterColor.PINK) return ANSI_PINK;
        else return null;
    }
    public static String getAnsi(PlayerColor playerColor) {
        if (playerColor == PlayerColor.WHITE) return ANSI_WHITE;
        else if (playerColor == PlayerColor.BLACK) return ANSI_BLACK;
        else if (playerColor == PlayerColor.GREY) return ANSI_GREY;
        else return null;
    }

    public static String moveCursor(int y, int x){
        return "\033["+y+";"+x+"H";
    }

    public static String cursorUp(int y){
        return "\033["+y+"A";
    }

    public static String cursorRight(int x){
        return "\033["+x+"C";
    }
}
