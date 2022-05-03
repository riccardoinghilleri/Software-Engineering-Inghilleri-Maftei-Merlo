package it.polimi.ingsw.client;
import java.util.Random;

public class CloudDisplay implements CharFigure {
    private final Stream stream;
    private static final int width = 15;
    private static final int height = 16;
    private  int numPlayers;
    private static final BackColor cloudColor = BackColor.ANSI_BRIGHT_BG_BLUE;
    private static final ForeColor studentColor = ForeColor.values()[new Random().nextInt(ForeColor.values().length)];;

    private CloudDisplay(Stream stream, int numPlayers){
       this.stream=stream;
       this.numPlayers= numPlayers;
    }
    @Override
    public void draw(int dim1, int dim2) {
        for (int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                if (i == 0 && j == 0) stream.addChar('╭', i + dim1, j + dim2);
                else if (i == 0 && j == height) stream.addChar('╰', i + dim1, j + dim2);
                else if (i == width && j == 0) stream.addChar('╮', i + dim1, j + dim2);
                else if (i == width && j == height) stream.addChar('╯', i + dim1, j + dim2);
                else stream.addColor(i + dim1, j + dim2, BackColor.ANSI_BRIGHT_BG_BLUE);
            }
        }
        int j=0;
        for( int i=0; i<numPlayers;i++)
        {
            stream.addChar('X', dim1 + 1 + j ,dim2+height/2, studentColor, cloudColor);
            j=j+3;
        }


    }

}
