package gcle;

import java.io.IOException;
import position.FenToGPosition;
import position.GCoups;
import position.ICodage;

class GGameConsole extends GGame implements ICodage {

    private final String fen;
    private final boolean end_of_game;

    public GGameConsole(int color, String fen) {
        this.end_of_game = false;
        this.human_color = color;
        this.ordi_color = -color;

        this.fen = fen;

    }

    void play() throws IOException {
        this_gp = FenToGPosition.toGPosition(fen);
        int trait = this_gp.getTrait();
        while (!end_of_game) {
            if ((human_color == BLANC && trait == BLANC)
                    || human_color == NOIR && trait == NOIR) {
                String traitS = trait == BLANC ? "white" : "black";
                System.out.println("Enter " + traitS + " move : ");
                String coupsS = GcleConsole.bufferRead.readLine();
                try {
                    GCoups coups = GcleConsole.toCase(coupsS);
                    //verifier si coups valide
                    if (isInputValidMove(coups)) {
                        playHuman(human_color, this_gcoups);
                    }
                    playEngine(ordi_color);
                } catch (IllegalMoveException ex) {
                }
            } else {
                super.playEngine(ordi_color);

            }
        }
        /*
       @TODO end_of_game
         */
    }

    private boolean isInputValidMove(GCoups coups) {

        int index = this_gp.getCoupsvalides_lan().indexOf(GCoups.getString(coups));
        /*
        @TODO traiter coups console invalide
         */
        assert (index != -1);
        this_gcoups = this_gp.getCoupsValides().get(index);
        return (index != -1);
    }
}
