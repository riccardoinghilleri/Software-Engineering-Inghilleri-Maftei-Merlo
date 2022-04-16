package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;

import java.util.*;

public class BoardExpert extends Board {
    int boardCoins;
    Map<String, Integer> playerCoins;
    CharacterCard[] characterCards;

    public BoardExpert(List<Player> players, GameModel gameModel) {
        super(players, gameModel);
        //--CREO LE MONETE--
        boardCoins = 20;
        //--ESTRAGGO 3 CARTE PERSONAGGIO CASUALI--
        characterCards = createThreeRandomCharacterCards();
        //--ASSEGNO AD OGNI PLAYER UNA MONETA--
        playerCoins = new HashMap<>();
        for(Player p: players){
            playerCoins.put(p.getNickname(),1);
            boardCoins--;
        }
    }

    public Integer getPlayerCoins(String playerNickname) {
        return playerCoins.get(playerNickname);
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
        for(CharacterCard card : characterCards ) {
            if(name.equals(card.getName().toString()))
                characterCard = card;
        }
        return characterCard;
    }

    //Rimuove le monete dal player e ne mette il costo - 1 nella board, aggiunge una moneta alla carta personaggio
    public void moveCoin(String player, CharacterCard card){
        playerCoins.replace(player,playerCoins.get(player)-card.getCost());
        boardCoins+=(card.getCost())-1;
        card.updateCost();
    }

    //Aggiunge una moneta al player
    public void addCointoPlayer(String player){
        playerCoins.replace(player,playerCoins.get(player)+1);
        boardCoins--;
    }

    public void removeNoEntryTiles(int islandPosition){
        getIslands().get(islandPosition).setNoEntryTile(false);
    }

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
                            "Scegli un colore di Studente." +
                                    "Ogni giocatore (incluso te) deve rimettere nel sacchetto 3 Studenti di quel colore presenti nella sua Sala." +
                                    "Chi avesse meno di 3 Studenti di quel colore, rimetterà tutti quelli che ha.");
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
                    cards[i]=new CharacterCard(CharacterCardName.QUEEN,1,
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
}