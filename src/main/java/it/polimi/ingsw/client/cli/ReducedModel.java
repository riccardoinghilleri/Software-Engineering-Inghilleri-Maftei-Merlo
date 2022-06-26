package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.server.model.*;

public class ReducedModel {

    /**
     * This method is used to display all the board on the screen
     */
    public static StringBuilder draw(Board board, int x, int y) {
        StringBuilder card = new StringBuilder();
        int high;
        if (!board.getGameModel().isExpertGame()) high = 28;
        else if (board.getPlayersNumber() < 4) high = 34;
        else high = 35;
        int distance;
        int movement_players = 0; //serve per stampare il terzo e il quarto player
        int coin;
        int count = 0; //Si potrebbe togliere, ma l'ultimo for diventa illeggibile
        //Stampo cornice
        card.append(Constants.boardFrame(x, y, board.getGameModel().isExpertGame(), board.getPlayersNumber()));
        card.append(Constants.cursorUp(high));
        //Stampo players
        if (board.getPlayersNumber() != 4) {
            for (Player player : board.getGameModel().getPlayers()) {
                coin = board.getGameModel().isExpertGame() ? ((BoardExpert) board.getGameModel().getBoard()).getPlayerCoins(player.getClientID()) : -1;
                if (board.getGameModel().getCurrentPlayer().getClientID() == player.getClientID())
                    card.append(draw(player, 2 + x, 0, coin, true));
                else
                    card.append(draw(player, 2 + x, 0, coin, false));
                card.append(Constants.cursorDown(1));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (i == 2) {
                    if (board.getGameModel().isExpertGame())
                        card.append(Constants.cursorUp(2 * 11));
                    else card.append(Constants.cursorUp(2 * 9));
                    movement_players = 163;
                }
                coin = board.getGameModel().isExpertGame()
                        ? ((BoardExpert) board.getGameModel().getBoard()).getPlayerCoins(board.getGameModel().getPlayers().get(i).getClientID())
                        : -1;
                if (board.getGameModel().getCurrentPlayer().getClientID() == board.getGameModel().getPlayers().get(i).getClientID())
                    card.append(draw(board.getGameModel().getPlayers().get(i), movement_players + 2 + x, 0, coin, true));
                else
                    card.append(draw(board.getGameModel().getPlayers().get(i), movement_players + 2 + x, 0, coin, false));
                card.append(Constants.cursorDown(1));
            }
        }
        if (board.getGameModel().isExpertGame() && board.getPlayersNumber() < 4)
            card.append(Constants.cursorUp(board.getPlayersNumber() * 11 - 2));
        else if (board.getGameModel().isExpertGame() && board.getPlayersNumber() == 4)
            card.append(Constants.cursorUp(2 * 11));
        else if (!board.getGameModel().isExpertGame() && board.getPlayersNumber() == 4)
            card.append(Constants.cursorUp(2 * 9));
        else card.append(Constants.cursorUp(board.getPlayersNumber() * 9));
        //card.append(Constants.cursorRight(34));
        //111
        distance = (111 - (int) Math.ceil(((float) board.getIslands().size() - 2.0) / 2.0) * 21) / (1 + (int) Math.ceil(((float) board.getIslands().size() - 2.0) / 2.0)); //TODO potrebbe non essere divisibile e avere un resto
        //stampa prima fila di isole
        for (int i = 0; i < Math.ceil(((float) board.getIslands().size() - 2.0) / 2.0); i++) {
            card.append(draw(board.getIslands().get(i + 1), x + 36 + distance * (i + 1) + i * 21, 0, i + 2));
            card.append(Constants.cursorUp(5));
        }
        card.append(Constants.cursorDown(6));
        card.append(draw(board.getIslands().get(0), x + 19, 0, 1)); //stampo isola 0
        card.append(Constants.cursorUp(4));
        //103 larghezza nuvole
        distance = ((103 - board.getPlayersNumber() * 11) / (board.getPlayersNumber() + 1));
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            card.append(draw(board.getClouds()[i], x + 40 + distance * (i + 1) + i * 11, 0, i + 1));
            card.append(Constants.cursorUp(3));
        }
        card.append(Constants.cursorUp(1));
        card.append(draw(board.getIslands().get((int) Math.ceil(((float) board.getIslands().size() - 2.0) / 2.0) + 1), 143 + x, 0, (int) Math.ceil(((float) board.getIslands().size() - 2.0) / 2.0) + 2));
        card.append(Constants.cursorDown(1));
        distance = (111 - (int) Math.floor(((float) board.getIslands().size() - 2.0) / 2.0) * 21)
                / (1 + (int) Math.floor(((float) board.getIslands().size() - 2.0) / 2.0));
        //stampo ultima fila di isole
        for (int i = board.getIslands().size() - 1; i >= Math.ceil(((float) board.getIslands().size() - 2.0) / 2.0) + 2; i--) {
            card.append(draw(board.getIslands().get(i), x + 37 + distance * (count + 1) + count * 21, 0, i + 1));
            card.append(Constants.cursorUp(5));
            count++;
        }
        if (board.getGameModel().isExpertGame() && board.getPlayersNumber() < 4) {
            card.append(Constants.cursorDown(8));
        } else if (board.getGameModel().isExpertGame() && board.getPlayersNumber() == 4)
            card.append(Constants.cursorDown(6));
        else card.append(Constants.cursorDown(6));
        if (board.getPlayersNumber() == 2) {
            distance = 15;
            card.append(draw(board.getSchools()[0], 53 + x, 0));
            card.append(Constants.cursorUp(8));
            card.append(draw(board.getSchools()[1], x + 53 + distance + 31, 0));
        } else if (board.getPlayersNumber() == 3) {
            distance = 3;
            card.append(draw(board.getSchools()[0], 42 + x, 0));
            card.append(Constants.cursorUp(8));
            card.append(draw(board.getSchools()[1], 42 + x + distance + 31, 0));
            card.append(Constants.cursorUp(8));
            card.append(draw(board.getSchools()[2], 42 + x + distance * 2 + 31 * 2, 0));
        } else {
            distance = 2;
            card.append(draw(board.getSchools()[0], 25 + x, 0));
            card.append(Constants.cursorUp(8));
            card.append(draw(board.getSchools()[1], 25 + x + distance + 31, 0));
            card.append(Constants.cursorUp(8));
            card.append(draw(board.getSchools()[2], 25 + x + distance * 2 + 31 * 2, 0));
            card.append(Constants.cursorUp(8));
            card.append(draw(board.getSchools()[3], 25 + x + distance * 3 + 31 * 3, 0));
        }
        card.append(Constants.cursorDown(3));
        return card;
    }

    public static StringBuilder drawExpert(BoardExpert boardExpert, int x, int y) {
        StringBuilder card = new StringBuilder(draw(boardExpert, x, y));
        int distance;
        if (boardExpert.getPlayersNumber() != 4) {
            card.append(Constants.cursorUp(32));
            distance = 165 + x;
            for (CharacterCard characterCard : boardExpert.getCharacterCards()) {
                card.append(draw(characterCard, distance, 0));
                card.append(Constants.cursorDown(1));
            }
            card.append(Constants.cursorDown(12));
        } else {
            //165+x
            card.append(Constants.cursorUp(2));
            distance = 1;
            for (CharacterCard characterCard : boardExpert.getCharacterCards()) {
                card.append(draw(characterCard, 49 + x + 3 * distance + 14 * distance, 0));
                card.append(Constants.cursorUp(6));
                distance++;
            }
            card.append(Constants.cursorDown(8));
        }
        return card;
    }

    public static StringBuilder draw(AssistantCard assistantCard, int x, int y) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        String top_wall = "╒════════╕\n";
        String bottom_wall = "╘════════╛\n";
        String vertical = "│";
        Constants.moveObject(card, x, top_wall);
        String line = vertical + "PRIORITY" + vertical + "\n";
        Constants.moveObject(card, x, line);
        if (assistantCard.getPriority() == 10) {
            line = vertical + "   " + assistantCard.getPriority() + "   " + vertical + "\n";
        } else
            line = vertical + "   " + assistantCard.getPriority() + "    " + vertical + "\n";
        Constants.moveObject(card, x, line);
        line = vertical + " STEPS: " + vertical + "\n";
        Constants.moveObject(card, x, line);
        line = vertical + "   " + assistantCard.getMotherNatureSteps() + "    " + vertical + "\n";
        Constants.moveObject(card, x, line);
        Constants.moveObject(card, x, bottom_wall);
        return card;
    }

    private static StringBuilder draw(CharacterCard characterCard, int x, int y) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        String top_wall = "╒════════════╕\n";//14
        String middle_wall = "├────────────┤\n";
        String bottom_wall = "╘════════════╛\n";
        String vertical = "│";
        String cost;
        String empty = vertical + "            " + vertical;
        if (characterCard.getCost() < 10)
            cost = vertical + "  COST: " + characterCard.getCost() + "   " + vertical + "\n";
        else
            cost = vertical + "  COST: " + characterCard.getCost() + "  " + vertical + "\n";
        int name_index = 0;
        Constants.moveObject(card, x, top_wall);
        card.append(Constants.cursorRight(x));
        for (int j = 0; j < 14; j++) {
            if (j == 0 || j == 13) card.append(vertical);
            else if (j > (12 - characterCard.getName().toString().length()) / 2 && name_index < characterCard.getName().toString().length()) {
                card.append(characterCard.getName().toString().charAt(name_index));
                name_index++;
            } else card.append(" ");
        }
        card.append("\n");
        Constants.moveObject(card, x, middle_wall);
        Constants.moveObject(card, x, cost);
        Constants.moveObject(card, x, middle_wall);
        if (characterCard instanceof CharacterCardwithProhibitions) {
            int noEntryTiles_index = 0;
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13) card.append(vertical);
                else if (((j % 2 != 0 && ((12 - ((CharacterCardwithProhibitions) characterCard).getProhibitionsNumber() * 2 - 1) / 2) % 2 == 0) || (j % 2 == 0 && ((12 - ((CharacterCardwithProhibitions) characterCard).getProhibitionsNumber() * 2 - 1) / 2) % 2 != 0))
                        && j > (12 - ((CharacterCardwithProhibitions) characterCard).getProhibitionsNumber() * 2 - 1) / 2 && noEntryTiles_index < ((CharacterCardwithProhibitions) characterCard).getProhibitionsNumber()) {
                    card.append("X");
                    noEntryTiles_index++;
                } else card.append(" ");
            }
        } else if (characterCard instanceof CharacterCardwithStudents) {
            int students_index = 0;
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13) card.append(vertical);
                else if (((j % 2 != 0 && ((12 - ((CharacterCardwithStudents) characterCard).getStudents().size() * 2 - 1) / 2) % 2 == 0) || (j % 2 == 0 && ((12 - ((CharacterCardwithStudents) characterCard).getStudents().size() * 2 - 1) / 2) % 2 != 0))
                        && j > (12 - ((CharacterCardwithStudents) characterCard).getStudents().size() * 2 - 1) / 2 && students_index < ((CharacterCardwithStudents) characterCard).getStudents().size()) {
                    card.append(((CharacterCardwithStudents) characterCard).getStudents().get(students_index));
                    students_index++;
                } else card.append(" ");
            }
        } else {
            Constants.moveObject(card, x, empty);
        }
        card.append("\n");
        Constants.moveObject(card, x, bottom_wall);
        return card;
    }

    /*
        private static StringBuilder draw(CharacterCardwithProhibitions characterCardwithProhibitions, int x, int y) {
            StringBuilder card = new StringBuilder();
            card.append(Constants.cursorUp(y));
            String top_wall = "╒════════════╕\n";//14
            String middle_wall = "├────────────┤\n";
            String bottom_wall = "╘════════════╛\n";
            String vertical = "│";
            String cost;
            if (characterCardwithProhibitions.getCost() < 10)
                cost = vertical + "  COST: " + characterCardwithProhibitions.getCost() + "   " + vertical + "\n";
            else
                cost = vertical + "  COST: " + characterCardwithProhibitions.getCost() + "  " + vertical + "\n";
            int name_index = 0;
            int noEntryTiles_index = 0;
            Constants.moveObject(card, x, top_wall);
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13) card.append(vertical);
                else if (j > (12 - characterCardwithProhibitions.getName().toString().length()) / 2 && name_index < characterCardwithProhibitions.getName().toString().length()) {
                    card.append(characterCardwithProhibitions.getName().toString().charAt(name_index));
                    name_index++;
                } else card.append(" ");
            }
            card.append("\n");
            Constants.moveObject(card, x, middle_wall);
            Constants.moveObject(card, x, cost);
            Constants.moveObject(card, x, middle_wall);
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13) card.append(vertical);
                else if (((j % 2 != 0 && ((12 - characterCardwithProhibitions.getProhibitionsNumber() * 2 - 1) / 2) % 2 == 0) || (j % 2 == 0 && ((12 - characterCardwithProhibitions.getProhibitionsNumber() * 2 - 1) / 2) % 2 != 0))
                        && j > (12 - characterCardwithProhibitions.getProhibitionsNumber() * 2 - 1) / 2 && noEntryTiles_index < characterCardwithProhibitions.getProhibitionsNumber()) {
                    card.append("X");
                    noEntryTiles_index++;
                } else card.append(" ");
            }
            card.append("\n");
            Constants.moveObject(card, x, bottom_wall);
            return card;
        }

        private static StringBuilder draw(CharacterCardwithStudents characterCardwithStudents, int x, int y) {
            StringBuilder card = new StringBuilder();
            card.append(Constants.cursorUp(y));
            String top_wall = "╒════════════╕\n";//14
            String middle_wall = "├────────────┤\n";
            String bottom_wall = "╘════════════╛\n";
            String vertical = "│";
            String cost;
            if (characterCardwithStudents.getCost() < 10)
                cost = vertical + "  COST: " + characterCardwithStudents.getCost() + "   " + vertical + "\n";
            else
                cost = vertical + "  COST: " + characterCardwithStudents.getCost() + "  " + vertical + "\n";
            int name_index = 0;
            int students_index = 0;
            Constants.moveObject(card, x, top_wall);
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13) card.append(vertical);
                else if (j > (12 - characterCardwithStudents.getName().toString().length()) / 2 && name_index < characterCardwithStudents.getName().toString().length()) {
                    card.append(characterCardwithStudents.getName().toString().charAt(name_index));
                    name_index++;
                } else card.append(" ");
            }
            card.append("\n");
            Constants.moveObject(card, x, middle_wall);
            Constants.moveObject(card, x, cost);
            Constants.moveObject(card, x, middle_wall);
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 14; j++) {
                if (j == 0 || j == 13) card.append(vertical);
                else if (((j % 2 != 0 && ((12 - characterCardwithStudents.getStudents().size() * 2 - 1) / 2) % 2 == 0) || (j % 2 == 0 && ((12 - characterCardwithStudents.getStudents().size() * 2 - 1) / 2) % 2 != 0))
                        && j > (12 - characterCardwithStudents.getStudents().size() * 2 - 1) / 2 && students_index < characterCardwithStudents.getStudents().size()) {
                    card.append(characterCardwithStudents.getStudents().get(students_index));
                    students_index++;
                } else card.append(" ");
            }
            card.append("\n");
            Constants.moveObject(card, x, bottom_wall);
            return card;
        }
    */
    private static StringBuilder draw(Cloud cloud, int x, int y, int pos) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        String top = "#" + pos + "• • • •  \n";
        String bottom = "  • • • •  \n";
        Constants.moveObject(card, x, top);
        int students_index = 0;
        for (int i = 0; i < 2; i++) {
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 11; j++) {
                if (!cloud.getStudents().isEmpty()) {
                    if (j == 0 || j == 10) card.append("•");
                    else if (i == 0 && (j == 3 || j == 7)) {
                        card.append(cloud.getStudents().get(students_index));
                        students_index++;
                    } else if (i == 1 && cloud.getStudents().size() % 2 == 0 && (j == 3 || j == 7)) {
                        card.append(cloud.getStudents().get(students_index));
                        students_index++;
                    } else if (i == 1 && cloud.getStudents().size() % 2 != 0 && j == 5) {
                        card.append(cloud.getStudents().get(students_index));
                        students_index++;
                    } else card.append(" ");
                } else {
                    if (j == 0 || j == 10) card.append("•");
                    else card.append(" ");
                }
            }
            card.append("\n");
        }
        Constants.moveObject(card, x, bottom);
        return card;
    }

    private static StringBuilder draw(Island island, int x, int y, int pos) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        int towers_index = 0;
        String horizontal_wall = "═══════════════";
        String to_right_wall = "/";
        String to_left_wall = "\\";
        for (int i = 0; i < 6; i++) {
            card.append(Constants.cursorRight(x));
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 5) {
                    if (j == 3) {
                        card.append(horizontal_wall);
                        j = 17;
                    } else card.append(" ");
                } else if ((i == 1 && j == 1) || (i == 2 && j == 0) || (i == 3 && j == 20) || (i == 4 && j == 19)) {
                    card.append(to_right_wall);
                } else if ((i == 1 && j == 19) || (i == 2 && j == 20) || (i == 3 && j == 0) || (i == 4 && j == 1)) {
                    card.append(to_left_wall);
                } else if (i == 1 && j == 3) {
                    card.append("#");
                    if (pos < 10) {
                        card.append(pos);
                        card.append(" ");
                    } else {
                        card.append(pos / 10);
                        card.append(pos % 10);
                    }
                    j = 5;
                } else if (i == 1) {
                    if (j == 8) {
                        card.append(Constants.getAnsi(CharacterColor.BLUE));
                        card.append(island.getStudents().get(CharacterColor.BLUE).size());
                        card.append(Constants.ANSI_RESET);
                    } else if (j == 12) {
                        card.append(Constants.getAnsi(CharacterColor.PINK));
                        card.append(island.getStudents().get(CharacterColor.PINK).size());
                        card.append(Constants.ANSI_RESET);
                    } else card.append(" ");
                } else if (i == 2) {
                    if (j == 6) {
                        card.append(Constants.getAnsi(CharacterColor.GREEN));
                        card.append(island.getStudents().get(CharacterColor.GREEN).size());
                        card.append(Constants.ANSI_RESET);
                    } else if (j == 10) {
                        card.append(Constants.getAnsi(CharacterColor.YELLOW));
                        card.append(island.getStudents().get(CharacterColor.YELLOW).size());
                        card.append(Constants.ANSI_RESET);
                    } else if (j == 14) {
                        card.append(Constants.getAnsi(CharacterColor.RED));
                        card.append(island.getStudents().get(CharacterColor.RED).size());
                        card.append(Constants.ANSI_RESET);
                    } else card.append(" ");
                } else if (i == 3) {
                    if (j > 4 && ((j % 2 != 0 && ((21 - island.getTowers().size() * 2 - 1) / 2) % 2 == 0)
                            || (j % 2 == 0 && ((21 - island.getTowers().size() * 2 - 1) / 2) % 2 != 0))
                            && j > (21 - island.getTowers().size() * 2 - 1) / 2 && towers_index < island.getTowers().size()) {
                        card.append(island.getTowers().get(towers_index));
                        towers_index++;
                    } else card.append(" ");
                } else if (i == 4) {
                    if (j == 8 && island.hasMotherNature()) card.append("M");
                    else if (j == 11 && island.hasNoEntryTile()) {
                        card.append(island.getNoEntryTile() + "X");
                        j = 13;
                    } else card.append(" ");
                }
            }
            card.append("\n");
        }
        return card;
    }

    private static StringBuilder draw(Player player, int x, int y, int coins, boolean currentPlayer) {
        StringBuilder box = new StringBuilder();
        box.append(Constants.cursorUp(y));
        String top_wall = "╔══════════════╗\n";
        String middle_wall = "╠══════════════╣\n";
        String bottom_wall = "╚══════════════╝\n";
        String line;
        Constants.moveObject(box, x, top_wall);
        if (currentPlayer)
            line = "║*" + player.getNickname(); //TODO mettere una stellina invece dell'asterisco
        else line = "║ " + player.getNickname();
        Constants.moveObject(box, x, line);
        box.append(" ".repeat(Math.max(0, (15 - 2 - player.getNickname().length()))));
        box.append("║\n");
        Constants.moveObject(box, x, middle_wall);
        if (player.getColor() == PlayerColor.GREY)
            line = "║ COLOR: " + player.getColor() + "  ║\n";
        else
            line = "║ COLOR: " + player.getColor() + " ║\n";
        Constants.moveObject(box, x, line);
        Constants.moveObject(box, x, middle_wall);
        if (player.getChosenAssistantCard() == null) {
            line = "║ PRIORITY: X  ║\n";
            Constants.moveObject(box, x, line);
            Constants.moveObject(box, x, middle_wall);
            line = "║ STEPS: X     ║\n";
            Constants.moveObject(box, x, line);
        } else {
            if (player.getChosenAssistantCard().getPriority() < 10)
                line = "║ PRIORITY: " + player.getChosenAssistantCard().getPriority() + "  ║\n";
            else
                line = "║ PRIORITY: " + player.getChosenAssistantCard().getPriority() + " ║\n";
            Constants.moveObject(box, x, line);
            Constants.moveObject(box, x, middle_wall);
            line = "║ STEPS: " + player.getChosenAssistantCard().getMotherNatureSteps() + "     ║\n";
            Constants.moveObject(box, x, line);
        }
        if (coins != -1) {
            Constants.moveObject(box, x, middle_wall);
            if (coins < 10)
                line = "║ COINS: " + coins + "     ║\n";
            else
                line = "║ COINS: " + coins + "    ║\n";
            Constants.moveObject(box, x, line);
        }
        Constants.moveObject(box, x, bottom_wall);
        return box;
    }

    private static StringBuilder draw(School school, int x, int y) {
        StringBuilder box = new StringBuilder();
        box.append(Constants.cursorUp(y));
        String top_wall = "╔═════════════════════════════╗\n";
        String middle_wall = "╠═══╦═══════════════════╦═╦═══╣\n";
        String bottom_wall = "╚═══╩═══════════════════╩═╩═══╝\n";
        String vertical_wall = "║";
        Constants.moveObject(box, x, top_wall);
        int entrance_index = 0;
        int diningRoom_index;
        int towers_index = 0;
        int owner_index = 0;
        box.append(Constants.cursorRight(x));
        for (int j = 0; j < 31; j++) {
            if (j == 0 || j == 30) box.append(vertical_wall);
            else if (j > (29 - school.getOwner().getNickname().length()) / 2 && owner_index < school.getOwner().getNickname().length()) {
                box.append(school.getOwner().getNickname().charAt(owner_index));
                owner_index++;
            } else box.append(" ");
        }
        box.append("\n");
        Constants.moveObject(box, x, middle_wall);
        for (int i = 1; i < 6; i++) {
            diningRoom_index = 0;
            box.append(Constants.cursorRight(x));
            for (int j = 0; j < 31; j++) {
                if (j == 0 || j == 4 || j == 24 || j == 26 || j == 30)
                    box.append(vertical_wall);
                else if ((j == 1 || j == 3) && entrance_index < school.getEntrance().size()) {
                    box.append(school.getEntrance().get(entrance_index));
                    entrance_index++;
                } else if ((j > 4 && j < 24 && j % 2 != 0)
                        && diningRoom_index < school.getDiningRoom().get(CharacterColor.values()[i - 1]).size()) {
                    box.append(school.getDiningRoom().get(CharacterColor.values()[i - 1]).get(diningRoom_index));
                    diningRoom_index++;
                } else if (j == 25 && school.getProfessorByColor(CharacterColor.values()[i - 1]) != null) {
                    box.append(school.getProfessorByColor(CharacterColor.values()[i - 1]));
                } else if (j > 26 && j % 2 != 0 && towers_index < school.getTowers().size()) {
                    box.append(school.getTowers().get(towers_index));
                    towers_index++;
                } else box.append(" ");
            }
            box.append("\n");
        }
        Constants.moveObject(box, x, bottom_wall);
        return box;
    }


}
