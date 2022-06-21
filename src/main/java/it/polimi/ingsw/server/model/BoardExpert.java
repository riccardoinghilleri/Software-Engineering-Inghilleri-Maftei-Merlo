package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterCardName;

import java.util.*;

public class BoardExpert extends Board {
    private int boardCoins;
    private int[] playerCoins;
    private CharacterCard[] characterCards;

    public BoardExpert(List<Player> players, GameModel gameModel) {
        super(players, gameModel);
        //--CREO LE MONETE--
        boardCoins = 20;
        //--ESTRAGGO 3 CARTE PERSONAGGIO CASUALI--
        characterCards = createThreeRandomCharacterCards();
        //--ASSEGNO AD OGNI PLAYER UNA MONETA--
        playerCoins = new int[players.size()];
        for (Player p : players) {
            playerCoins[p.getClientID()] = 1;
            boardCoins--;
        }
    }

    public int[] getCoins() {
        return playerCoins;
    }

    public Integer getPlayerCoins(int clientId) {
        return playerCoins[clientId];
    }

    public int getBoardCoins() {
        return boardCoins;
    }

    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    public CharacterCard getCharacterCardbyName(String name) {
        //TODO forse bisogna lanciare eccezione se non si trova la carta
        CharacterCard characterCard = null;
        for (CharacterCard card : characterCards) {
            if (name.equals(card.getName().toString()))
                characterCard = card;
        }
        return characterCard;
    }

    //Rimuove le monete dal player e ne mette il costo - 1 nella board, aggiunge una moneta alla carta personaggio
    public void moveCoin(int clientId, CharacterCard card) {
        playerCoins[clientId] -= card.getCost();
        boardCoins += (card.getCost()) - 1;
        card.updateCost();
    }

    //Aggiunge una moneta al player
    public void addCointoPlayer(int clientId) {
        playerCoins[clientId] += 1;
        boardCoins--;
    }

    public void removeNoEntryTiles(int islandPosition) {
        getIslands().get(islandPosition).setNoEntryTile(false);
    }

    protected CharacterCard[] createThreeRandomCharacterCards() {
        CharacterCard[] cards = new CharacterCard[3];
        CharacterCardName[] values = CharacterCardName.values();
        List<CharacterCardName> chosenNames = new ArrayList<>();
        Random r = new Random();
        while (chosenNames.size() < 3) {
            CharacterCardName name = values[r.nextInt(values.length)];
            if (!chosenNames.contains(name))
                chosenNames.add(name);
        }
        //chosenNames.add(CharacterCardName.PERFORMER);
        //chosenNames.add(CharacterCardName.PRIEST);
        //chosenNames.add(CharacterCardName.HERBOLARIA);
        for (int i = 0; i < 3; i++) {
            switch (chosenNames.get(i)) {
                case PRIEST:
                    cards[i] = new CharacterCardwithStudents(CharacterCardName.PRIEST, 1,
                            "Take 1 Student from this card and place it on an Island of your choice. " +
                                    "Then, draw a new Student from the Bag and place it on this card.", removeRandomStudents(4));
                    break;
                case QUEEN:
                    cards[i] = new CharacterCardwithStudents(CharacterCardName.QUEEN, 2,
                            "Take 1 Student from this card and place it in your Dining Room. " +
                                    "Then, draw a new Student from the Bag and place it on this card.", removeRandomStudents(4));
                    break;
                case THIEF:
                    cards[i] = new CharacterCard(CharacterCardName.THIEF, 3,
                            "Choose a type of Student: every player" +
                                    "(including yourself) must return 3 students of that type from their Dining Room to the bag. " +
                                    "If any player has fewer than 3 students of that type, return as many students as they have.");
                    break;
                case KNIGHT:
                    cards[i] = new CharacterCard(CharacterCardName.KNIGHT, 2,
                            "During the influence calculation this turn, you count as having 2 more influence.");
                    break;
                case CLOWN:
                    cards[i] = new CharacterCardwithStudents(CharacterCardName.CLOWN, 1,
                            "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.",
                            removeRandomStudents(6));
                    break;
                case DIPLOMAT:
                    cards[i] = new CharacterCard(CharacterCardName.DIPLOMAT, 3,
                            "Choose an Island and resolve the Island as if Mother Nature had ended her movements here. " +
                                    "Mother Nature will still move and the Island where she ends her movements will also be resolved.");
                    break;
                case POSTMAN:
                    cards[i] = new CharacterCard(CharacterCardName.POSTMAN, 1,
                            "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.");
                    break;
                case HERBOLARIA:
                    cards[i] = new CharacterCardwithProhibitions(CharacterCardName.HERBOLARIA, 2, "Place a No Entry Tile " +
                            "on an Island of your choice. The first time Mother Nature ends her movements there, put the No Entry Tile back " +
                            "onto this card DO NOT calculate influence on that island, or place any Towers.", 4);
                    break;
                case CENTAUR:
                    cards[i] = new CharacterCard(CharacterCardName.CENTAUR, 3,
                            "When resolving a Conquering on an Island, Towers do not count towards influence.");
                    break;
                case LUMBERJACK:
                    cards[i] = new CharacterCard(CharacterCardName.LUMBERJACK, 3,
                            "Choose a color of Student: during the influence calculation this turn, that color adds no influence.");
                    break;
                case DINER:
                    cards[i] = new CharacterCard(CharacterCardName.DINER, 2,
                            "During this turn, you take control of any number of Professors even if you have the same number " +
                                    "of Students as the Player who currently controls them.");
                    break;
                case PERFORMER:
                    cards[i] = new CharacterCard(CharacterCardName.PERFORMER, 1,
                            "You may exchange up to 2 Students between your Entrance and your Dining Room.");
                    break;
            }
        }
        return cards;
    }

    public StringBuilder draw(int x, int y) {
        StringBuilder boardExpert = new StringBuilder(super.draw(x, y));
        int distance;
        if (getPlayersNumber() != 4) {
            boardExpert.append(Constants.cursorUp(32));
            distance = 165 + x;
            for (CharacterCard characterCard : characterCards) {
                boardExpert.append(characterCard.draw(distance, 0));
                boardExpert.append(Constants.cursorDown(1));
            }
            boardExpert.append(Constants.cursorDown(12));
        } else {
            //165+x
            boardExpert.append(Constants.cursorUp(1));
            distance = 1;
            for (CharacterCard characterCard : characterCards) {
                boardExpert.append(characterCard.draw(49 + x + 3 * distance + 14 * distance, 0));
                boardExpert.append(Constants.cursorUp(6));
                distance++;
            }
            boardExpert.append(Constants.cursorDown(8));
        }
        return boardExpert;
    }
}