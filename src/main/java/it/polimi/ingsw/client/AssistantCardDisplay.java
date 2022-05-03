package it.polimi.ingsw.client;


public class AssistantCardDisplay implements CharFigure {
    private final String cardName;
    private final Stream stream;
    private final String possibleMotherNatureSteps;
    private final String turnOrder;

    private static final int width = 35;
    private static final int height = 20;
    private static final BackColor cardsColor = BackColor.ANSI_BRIGHT_BG_WHITE;
    private static final ForeColor descriptionColor = ForeColor.ANSI_BLACK;

    public AssistantCardDisplay(Stream stream, String cardName, String possibleMotherNatureSteps, String turnOrder) {
        this.cardName = cardName;
        this.possibleMotherNatureSteps = possibleMotherNatureSteps;
        this.turnOrder = turnOrder;
        this.stream = stream;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() { return height; }


    @Override
    public void draw(int dim1, int dim2) {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                if (i == 0 && j == 0) stream.addChar('╭', i + dim1, j + dim2);
                else if (i == 0 && j == height) stream.addChar('╰', i + dim1, j + dim2);
                else if (i == width && j == 0) stream.addChar('╮', i + dim1, j + dim2);
                else if (i == width && j == height) stream.addChar('╯', i + dim1, j + dim2);
                else stream.addColor(i + dim1, j + dim2, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
        }
        int midPointCard = (int) (dim1 + Math.ceil(width * 1.0 / 2.0));
        int midPointName = cardName.length() / 2;
        stream.addString(midPointCard - midPointName, dim2, cardName.substring(0, midPointName + 1), descriptionColor, cardsColor);
        stream.addString(midPointCard, dim2, cardName.substring(midPointName), descriptionColor, cardsColor);
        stream.addString(dim1 + 5, dim2 + 5, turnOrder, descriptionColor, cardsColor);
        stream.addString(dim1 + 30, dim2 +5 , possibleMotherNatureSteps, descriptionColor, cardsColor);

    }
}
