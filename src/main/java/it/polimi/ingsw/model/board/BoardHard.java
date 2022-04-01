package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.SpecialCard;
import it.polimi.ingsw.model.SpecialCardwithProhibitions;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.enums.SpecialCardName;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class BoardHard extends Board {
    int coins;
    Map<String, Integer> playerCoins;
    SpecialCard[] specialCards;

    public void createBoardHard(int playersNumber) {
        createBoard(playersNumber);
        coins = 20;
        specialCards = createThreeRandomSpecialCards();
        playerCoins = new HashMap<>();
    }

    public void setPlayerCoins(String player){
        playerCoins.put(player,1);
        coins--;
    }

    public int getCoins() {
        return coins;
    }

    public SpecialCard[] getSpecialCards() {
        return specialCards;
    }
    public void moveCoin(String player, SpecialCard card){
        playerCoins.replace(player,playerCoins.get(player)-1);
        coins+=(card.getCost())-1;
        card.updateCost();
    }
    public void moveCoin(String player){
        playerCoins.replace(player,playerCoins.get(player)+1);
        coins--;
    }

    public void removeLock(int islandPosition){
        getIslands().get(islandPosition).setLock(false);
    }

    protected SpecialCard[] createThreeRandomSpecialCards(){
        SpecialCard[] cards= new SpecialCard[3];
        SpecialCardName[] values= SpecialCardName.values();
        Random r=new Random();
        for(int i=0;i<3;i++)
        {
            switch (values[r.nextInt(values.length)]){
                case PRIEST:
                    cards[i]=new SpecialCardwithStudents(SpecialCardName.PRIEST,1,
                            "Prendi uno studente dalla carta e piazzalo su un'isola a tua scelta." +
                                    "Poi,pesca 1 studente dal sacchetto e mettilo su questa carta",removeRandomStudent(4));
                    break;
                case QUEEN:
                    cards[i]=new SpecialCardwithStudents(SpecialCardName.QUEEN,2,
                            "Prendi uno studente da questa carta e piazzalo nella tua Sala." +
                                    "Poi pesca un nuovo studente dal sacchetto e posizionalo su questa carta",removeRandomStudent(4));
                    break;
                case THIEF:
                    cards[i]=new SpecialCard(SpecialCardName.THIEF,3,
                            "Scegli un colore di Studente." +
                                    "Ogni giocatore (incluso te) deve rimettere nel sacchetto 3 Studenti di quel colore presenti nella sua Sala" +
                                    "Chi avesse meno di 3 Studenti di quel colore, rimetterà tutti quelli che ha.");
                    break;
                case KNIGHT:
                    cards[i]=new SpecialCard(SpecialCardName.KNIGHT,2,
                            "Durante questo turno, nel calcolo dell'influenza hai 2 punti in più di influenza addizionali");
                    break;
                case CLOWN:
                    cards[i]=new SpecialCardwithStudents(SpecialCardName.CLOWN,1,
                            "Puoi prendere fino a 3 Studenti da questa carta e scambiarli con altrettanti Studenti presenti nel tuo ingresso",
                            removeRandomStudent(6));
                    break;
                case DIPLOMAT:
                    cards[i]=new SpecialCard(SpecialCardName.DIPLOMAT,3,
                            "Scegli un'isola e calcola la maggioranza come se Madre Natura avesse terminato il suo movimento lì." +
                                    "In questo turno Madre Natura si muoverà come di consueto e nell'isola dove terminerà il suo movimento" +
                                    "la maggioranza verrà normalmente calcolata.");
                    break;
                case POSTMAN:
                    cards[i]=new SpecialCard(SpecialCardName.QUEEN,1,
                            "Puoi muovere Madre Natura fino a 2 isole addizionali rispetto a quanto indicato " +
                                    "sulla Carta Assistente che hai giocato");
                    break;
                case HERBOLARIA:
                    cards[i]=new SpecialCardwithProhibitions(SpecialCardName.HERBOLARIA,2, "Piazza unua tessera divieto" +
                            "su un'isola a tua scelta. La prima volta che Madre Natura termina il suo movimento lì" +
                            "rimettete la tessera Divieto sulla carta SENZA calcolare l'influenza su quell'isola nè piazzare torri.",4);

                    break;
                case CENTAUR:
                    cards[i]=new SpecialCard(SpecialCardName.CENTAUR,3,
                            "Durante il conteggio dell'influenza su un'isola (o su un gruppo di isole), le Torri presenti non vengono calcolate.");
                    break;
                case LUMBERJACK:
                    cards[i]=new SpecialCard(SpecialCardName.LUMBERJACK,3,
                            "Scegli un colore di Studente; in questo turno, durante il calcolo dell'influenza quel colorre non fornisce influenza");
                    break;
                case DINER:
                    cards[i]=new SpecialCard(SpecialCardName.DINER,2,
                            "Durante questo turno, prendi il controllo dei Professori anche se nella tua Sala hai lo stesso numero " +
                                    "di Studenti del giocatore che li controlla al momento.");
                    break;
                case PERFORMER:
                    cards[i]=new SpecialCard(SpecialCardName.PERFORMER,1,
                            "Puoi scambiare tra loro fino a 2 Studenti presenti nella tua Sala e nel tuo Ingresso");
                    break;
            }
        }
        return cards;
    }
}