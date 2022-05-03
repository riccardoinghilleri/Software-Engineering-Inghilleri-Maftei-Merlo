package it.polimi.ingsw.client;

public class IslandDisplay {
    private final Stream stream;
    private static final int width = 25;
    private static final int height = 25;
    private static  String name;
    private static final BackColor IslandColor = BackColor.ANSI_BRIGHT_BG_GREEN;

    private IslandDisplay(Stream stream){
        this.stream= stream;
    }
    public void draw(int dim1, int dim2) {
        for (int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                if (i == 0 && j == 0) stream.addChar('╭', i + dim1, j + dim2);
                else if (i == 0 && j == height) stream.addChar('╰', i + dim1, j + dim2);
                else if (i == width && j == 0) stream.addChar('╮', i + dim1, j + dim2);
                else if (i == width && j == height) stream.addChar('╯', i + dim1, j + dim2);
                else stream.addColor(i + dim1, j + dim2, BackColor.ANSI_BRIGHT_BG_GREEN);
            }
        }
 //TODO da finire, bisogna capire come far apparire su terminale le 12 isole in fila.
    }

}
