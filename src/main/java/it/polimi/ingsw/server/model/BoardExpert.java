package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;

import java.util.*;

/**
 * This class is the second version of the Board when the Game mode is 'expert',
 * adding the management of the CharacterCards and therefore coins.
 */
public class BoardExpert extends Board {
    private int boardCoins;
    private final int[] playerCoins;
    private final CharacterCard[] characterCards;

    /**
     * The constructor of the boardExpert: it creates the coins, chooses randomly 3 characterCard and gives to each player a coin.
     *
     * @param gameModel it needs the gameModel as the basic Board.
     */
    public BoardExpert(GameModel gameModel) {
        super(gameModel);

        boardCoins = 20;

        characterCards = createThreeRandomCharacterCards();

        playerCoins = new int[gameModel.getPlayersNumber()];
        for (Player p : gameModel.getPlayers()) {
            playerCoins[p.getClientID()] = 1;
            boardCoins--;
        }
    }

    /**
     * @return and array with the coins of each player
     */
    public int[] getCoins() {
        return playerCoins;
    }

    /**
     * Getter of the coins of each player
     *
     * @param clientId the id of the player considered
     * @return the number of coins of the specified player
     */
    public Integer getPlayerCoins(int clientId) {
        return playerCoins[clientId];
    }

    /**
     * @return the total number of coins left on the board
     */
    public int getBoardCoins() {
        return boardCoins;
    }

    /**
     * @return a list of all the randomly chosen CharacterCards
     */
    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    /**
     * This method return the CharacterCard whose name is passed as a parameter of the method
     *
     * @param name of the CharacterCard requested
     * @return a CharacterCard
     */
    public CharacterCard getCharacterCardbyName(String name) {
        CharacterCard characterCard = null;
        for (CharacterCard card : characterCards) {
            if (name.equals(card.getName().toString()))
                characterCard = card;
        }
        return characterCard;
    }

    /**
     * After a player uses a CharacterCard, this method removes the coins from the player and put them subtract 1 into the board.
     * It updates the cost of the CharacterCard.
     *
     * @param clientId the player who uses the Card
     * @param card:    the chosen card
     */
    public void moveCoin(int clientId, CharacterCard card) {
        //Rimuove le monete dal player e ne mette il costo - 1 nella board, aggiunge una moneta alla carta personaggio
        playerCoins[clientId] -= card.getCost();
        boardCoins += (card.getCost()) - 1;
        card.updateCost();
    }

    /**
     * It adds a coin to a specified player removing them from the Board
     *
     * @param clientId client id of the player to which the coin is added
     */
    public void addCointoPlayer(int clientId) {
        playerCoins[clientId] += 1;
        boardCoins--;
    }

    public void removeNoEntryTiles(int islandPosition) {
        getIslands().get(islandPosition).setNoEntryTile(false);
    }

    /**
     * This method creates randomly 3 CharacterCards,
     * calling the constructors of the 3 different types of cards, specifying the name, the coins and what the card can do.
     *
     * @return an array of 3 Cards
     */
    private CharacterCard[] createThreeRandomCharacterCards() {
        CharacterCard[] cards = new CharacterCard[3];
        CharacterCardName[] values = CharacterCardName.values();
        List<CharacterCardName> chosenNames = new ArrayList<>();
        Random r = new Random();
        while (chosenNames.size() < 3) {
            CharacterCardName name = values[r.nextInt(values.length)];
            if (!chosenNames.contains(name))
                chosenNames.add(name);
        }
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

    /**
     * This method is used only for tests.
     * It overrides the 3 randomly chosen characterCards, whit 3 chosen CharacterCards.
     */
    public void createThreeCharacterCards(int index) {
        CharacterCardName[] values = CharacterCardName.values();
        List<CharacterCardName> chosenNames = new ArrayList<>(Arrays.asList(values).subList(3 * (index - 1), 3 * index));
        for (int i = 0; i < 3; i++) {
            switch (chosenNames.get(i)) {
                case PRIEST:
                    characterCards[i] = new CharacterCardwithStudents(CharacterCardName.PRIEST, 1,
                            "Take 1 Student from this card and place it on an Island of your choice. " +
                                    "Then, draw a new Student from the Bag and place it on this card.", removeRandomStudents(4));
                    break;
                case QUEEN:
                    characterCards[i] = new CharacterCardwithStudents(CharacterCardName.QUEEN, 2,
                            "Take 1 Student from this card and place it in your Dining Room. " +
                                    "Then, draw a new Student from the Bag and place it on this card.", removeRandomStudents(4));
                    break;
                case THIEF:
                    characterCards[i] = new CharacterCard(CharacterCardName.THIEF, 3,
                            "Choose a type of Student: every player" +
                                    "(including yourself) must return 3 students of that type from their Dining Room to the bag. " +
                                    "If any player has fewer than 3 students of that type, return as many students as they have.");
                    break;
                case KNIGHT:
                    characterCards[i] = new CharacterCard(CharacterCardName.KNIGHT, 2,
                            "During the influence calculation this turn, you count as having 2 more influence.");
                    break;
                case CLOWN:
                    characterCards[i] = new CharacterCardwithStudents(CharacterCardName.CLOWN, 1,
                            "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.",
                            removeRandomStudents(6));
                    break;
                case DIPLOMAT:
                    characterCards[i] = new CharacterCard(CharacterCardName.DIPLOMAT, 3,
                            "Choose an Island and resolve the Island as if Mother Nature had ended her movements here. " +
                                    "Mother Nature will still move and the Island where she ends her movements will also be resolved.");
                    break;
                case POSTMAN:
                    characterCards[i] = new CharacterCard(CharacterCardName.POSTMAN, 1,
                            "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.");
                    break;
                case HERBOLARIA:
                    characterCards[i] = new CharacterCardwithProhibitions(CharacterCardName.HERBOLARIA, 2, "Place a No Entry Tile " +
                            "on an Island of your choice. The first time Mother Nature ends her movements there, put the No Entry Tile back " +
                            "onto this card DO NOT calculate influence on that island, or place any Towers.", 4);
                    break;
                case CENTAUR:
                    characterCards[i] = new CharacterCard(CharacterCardName.CENTAUR, 3,
                            "When resolving a Conquering on an Island, Towers do not count towards influence.");
                    break;
                case LUMBERJACK:
                    characterCards[i] = new CharacterCard(CharacterCardName.LUMBERJACK, 3,
                            "Choose a color of Student: during the influence calculation this turn, that color adds no influence.");
                    break;
                case DINER:
                    characterCards[i] = new CharacterCard(CharacterCardName.DINER, 2,
                            "During this turn, you take control of any number of Professors even if you have the same number " +
                                    "of Students as the Player who currently controls them.");
                    break;
                case PERFORMER:
                    characterCards[i] = new CharacterCard(CharacterCardName.PERFORMER, 1,
                            "You may exchange up to 2 Students between your Entrance and your Dining Room.");
                    break;
            }
        }
    }
}