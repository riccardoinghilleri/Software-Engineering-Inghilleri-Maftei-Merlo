package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterCardName;

import java.io.Serializable;
import java.util.*;

/**
 * This class is the second version of the Board when the Game mode is 'expert',
 * adding the management of the CharacterCards and therefore coins.
 */

public class BoardExpert extends Board{
    int boardCoins;
    int [] playerCoins;
    CharacterCard[] characterCards;

    /**
     * The constructor of the boardExpert: it creates the coins, chooses randomly 3 characterCard and gives to each player a coin.
     * @param players a list of the total players involved in the game
     * @param gameModel it needs the gameModel such as the basic Board
     */
    public BoardExpert(List<Player> players, GameModel gameModel) {
        super(players, gameModel);
        //--CREO LE MONETE--
        boardCoins = 20;
        //--ESTRAGGO 3 CARTE PERSONAGGIO CASUALI--
        characterCards = createThreeRandomCharacterCards();
        //--ASSEGNO AD OGNI PLAYER UNA MONETA--
        playerCoins = new int[players.size()];
        for(Player p: players){
            playerCoins[p.getClientID()]=1;
            boardCoins--;
        }
    }

    /**
     * Getter of the coins of each player
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
     * @return a list of the all the CharacterCard randomly chosen
     */
    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    /**
     * This method is able to return the CharacterCard whose name is passed as parameter of the method
     * @param name of the CharacterCard requested
     * @return a CharacterCard
     */
    public CharacterCard getCharacterCardbyName(String name) {
        //TODO forse bisogna lanciare eccezione se non si trova la carta
        CharacterCard characterCard = null;
        for(CharacterCard card : characterCards ) {
            if(name.equals(card.getName().toString()))
                characterCard = card;
        }
        return characterCard;
    }

    /**
     * After a player uses a CharacterCard this method removes the coins from the player and put the cost at -1
     * It adds a coin to the CharacterCard
     * @param clientId the player who uses the Card
     * @param card: the card chosen
     */
    public void moveCoin(int clientId, CharacterCard card){
        playerCoins[clientId]-=card.getCost();
        boardCoins+=(card.getCost())-1;
        card.updateCost();
    }

    /**
     * It adds a coin to a specified player removing them from the Board
     * @param clientId the client to who coins are added.
     */
    //Aggiunge una moneta al player
    public void addCointoPlayer(int clientId){
        playerCoins[clientId]+=1;
        boardCoins--;
    }

    public void removeNoEntryTiles(int islandPosition){
        getIslands().get(islandPosition).setNoEntryTile(false);
    }

    /**
     * This method creates randomly 3 CharacterCards,
     * calling the constructors of the 3 different types of cards
     * @return a list of 3 Cards
     */
    protected CharacterCard[] createThreeRandomCharacterCards(){
        CharacterCard[] cards= new CharacterCard[3];
        CharacterCardName[] values= CharacterCardName.values();
        List<CharacterCardName> chosedNames= new ArrayList<>();
        Random r=new Random();
        while(chosedNames.size()<3)
        {
            CharacterCardName name=values[r.nextInt(values.length)];
            if(!chosedNames.contains(name))
                chosedNames.add(name);
        }
        for(int i=0;i<3;i++)
        {
            switch (chosedNames.get(i)){
                case PRIEST:
                    cards[i]=new CharacterCardwithStudents(CharacterCardName.PRIEST,1,
                            "Prendi uno studente dalla carta e piazzalo su un'isola a tua scelta." +
                                    "Poi,pesca 1 studente dal sacchetto e mettilo su questa carta",removeRandomStudents(4));
                    break;
                case QUEEN:
                    cards[i]=new CharacterCardwithStudents(CharacterCardName.QUEEN,2,
                            "Prendi uno studente da questa carta e piazzalo nella tua Sala." +
                                    "Poi pesca un nuovo studente dal sacchetto e posizionalo su questa carta",removeRandomStudents(4));
                    break;
                case THIEF:
                    cards[i]=new CharacterCard(CharacterCardName.THIEF,3,
                            "Choose a type of Student: every player" +
                                    "(including yourself) must return 3 students of that type from their Dining Room to the bag. " +
                                    "If any player has fewer than 3 students of that type, return as many students as they have.");
                    break;
                case KNIGHT:
                    cards[i]=new CharacterCard(CharacterCardName.KNIGHT,2,
                            "Durante questo turno, nel calcolo dell'influenza hai 2 punti in più di influenza addizionali");
                    break;
                case CLOWN:
                    cards[i]=new CharacterCardwithStudents(CharacterCardName.CLOWN,1,
                            "Puoi prendere fino a 3 Studenti da questa carta e scambiarli con altrettanti Studenti presenti nel tuo ingresso",
                            removeRandomStudents(6));
                    break;
                case DIPLOMAT:
                    cards[i]=new CharacterCard(CharacterCardName.DIPLOMAT,3,
                            "Scegli un'isola e calcola la maggioranza come se Madre Natura avesse terminato il suo movimento lì." +
                                    "In questo turno Madre Natura si muoverà come di consueto e nell'isola dove terminerà il suo movimento" +
                                    "la maggioranza verrà normalmente calcolata.");
                    break;
                case POSTMAN:
                    cards[i]=new CharacterCard(CharacterCardName.POSTMAN,1,
                            "Puoi muovere Madre Natura fino a 2 isole addizionali rispetto a quanto indicato " +
                                    "sulla Carta Assistente che hai giocato");
                    break;
                case HERBOLARIA:
                    cards[i]=new CharacterCardwithProhibitions(CharacterCardName.HERBOLARIA,2, "Piazza unua tessera divieto" +
                            "su un'isola a tua scelta. La prima volta che Madre Natura termina il suo movimento lì" +
                            "rimettete la tessera Divieto sulla carta SENZA calcolare l'influenza su quell'isola nè piazzare torri.",4);

                    break;
                case CENTAUR:
                    cards[i]=new CharacterCard(CharacterCardName.CENTAUR,3,
                            "Durante il conteggio dell'influenza su un'isola (o su un gruppo di isole), le Torri presenti non vengono calcolate.");
                    break;
                case LUMBERJACK:
                    cards[i]=new CharacterCard(CharacterCardName.LUMBERJACK,3,
                            "Scegli un colore di Studente; in questo turno, durante il calcolo dell'influenza quel colorre non fornisce influenza");
                    break;
                case DINER:
                    cards[i]=new CharacterCard(CharacterCardName.DINER,2,
                            "Durante questo turno, prendi il controllo dei Professori anche se nella tua Sala hai lo stesso numero " +
                                    "di Studenti del giocatore che li controlla al momento.");
                    break;
                case PERFORMER:
                    cards[i]=new CharacterCard(CharacterCardName.PERFORMER,1,
                            "Puoi scambiare tra loro fino a 2 Studenti presenti nella tua Sala e nel tuo Ingresso");
                    break;
            }
        }
        return cards;
    }

    public StringBuilder draw(int x, int y) {
        StringBuilder boardExpert = new StringBuilder(super.draw(x,y));
        boardExpert.append(Constants.cursorUp(32));
        //165+x
        for(CharacterCard characterCard: characterCards){
            boardExpert.append(characterCard.draw(165+x, 0));
            boardExpert.append(Constants.cursorDown(1));
        }
        boardExpert.append(Constants.cursorDown(13));

        return boardExpert;
    }
}