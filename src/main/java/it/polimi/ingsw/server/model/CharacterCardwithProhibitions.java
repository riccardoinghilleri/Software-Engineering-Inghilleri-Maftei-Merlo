package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;

public class CharacterCardwithProhibitions extends CharacterCard {
    private int prohibitionsNumber;

    public CharacterCardwithProhibitions(CharacterCardName name, int cost, String description, int prohibitionsNumber) {
        super(name,cost,description);
        this.prohibitionsNumber=prohibitionsNumber;
    }

    public int getProhibitionsNumber() {
        return prohibitionsNumber;
    }

    public void subProhibitionCard() {
        //TODO lanciare un'eccezione se sono finite le tessere divieto e il client vuole usarne una
        if(prohibitionsNumber>0) prohibitionsNumber--;
    }
    public void restockProhibitionsNumber() {
        if(prohibitionsNumber<4) prohibitionsNumber++;
    }

    @Override
    public StringBuilder draw() {
        StringBuilder card = new StringBuilder();
        String top_wall = "╒════════════╕\n";//14
        String middle_wall = "├────────────┤\n";
        String bottom_wall = "╘════════════╛\n";
        String vertical = "│";
        String cost;
        if (super.getCost() < 10)
            cost = vertical + "  COST: " + super.getCost() + "   " + vertical+"\n";
        else cost = vertical + "  COST: " + super.getCost() + "  " + vertical+"\n";
        int name_index = 0;
        int noEntryTiles_index=0;
        card.append(top_wall);
        for (int j = 0; j < 14; j++) {
            if (j == 0 || j == 13) card.append(vertical);
            else if (j > (12-super.getName().toString().length())/2 && name_index < super.getName().toString().length()) {
                card.append(super.getName().toString().charAt(name_index));
                name_index++;
            } else card.append(" ");
        }
        card.append("\n");
        card.append(middle_wall);
        card.append(cost);
        card.append(middle_wall);
        for(int j=0;j<14;j++){
            if (j == 0 || j == 13) card.append(vertical);
            else if (((j%2!=0 && ((12-prohibitionsNumber*2-1)/2)%2==0) || (j%2==0 && ((12-prohibitionsNumber*2-1)/2)%2!=0))
                    && j>(12-prohibitionsNumber*2-1)/2 && noEntryTiles_index<prohibitionsNumber) {
                card.append("X");
                noEntryTiles_index++;
            }
            else card.append(" ");
        }
        card.append("\n");
        card.append(bottom_wall);
        return card;
    }

    @Override
    public String toString() {
        return super.getName() + ": " + super.getDescription() + "\nCost: " + super.getCost() + "\nNo Entry Tiles: " + prohibitionsNumber;
    }
}
