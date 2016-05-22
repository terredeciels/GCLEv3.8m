package gcle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import position.GCoups;
import position.ICodage;

class GGame implements ICodage {

    private final String fen;

    private final int ordi_color;
    private final int human_color;
    private final boolean end_of_game;
    private final BufferedReader bufferRead;

    public GGame(BufferedReader bufferRead,int ordi_color, String fen) {
        this.bufferRead = bufferRead;
        this.end_of_game = false;
        this.ordi_color = ordi_color;
        this.human_color = -ordi_color;
        this.fen = fen;

    }

    void play() throws IOException {

        while (!end_of_game) {
            if (human_color == BLANC) {
                System.out.println("Enter move : ");
                String coupsS = bufferRead.readLine();
                try {
                    GCoups gcoups = GConsole.toCase(coupsS);
                    System.out.println(GCoups.getString(gcoups));
                    //verifier si coups valide

                    //ajouter propriétés du coups
                    
                } catch (IllegalMoveException ex) {
                }
            } else {
                // new Thread ?
                Search search = new Search(fen);
               // search.run();// ??
               // search.start(); //??
            }
        }
    }
}
