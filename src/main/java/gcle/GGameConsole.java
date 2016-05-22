package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import position.FenToGPosition;
import position.GCoups;
import position.ICodage;

class GGameConsole extends GGame implements ICodage {

    private final boolean end_of_game;
    private final int human_color;

    public GGameConsole(int color, String fen) {
        this.end_of_game = false;
        this.human_color = color;
        gp = FenToGPosition.toGPosition(fen);
    }

    void play() throws IOException, NodeNotFoundException, IllegalMoveException {

        while (!end_of_game) {
            if (gp.getTrait() == human_color) {
//                String traitS = trait == BLANC ? "white" : "black";
//                System.out.println("Enter " + traitS + " move : ");
                System.out.println("Enter move : ");
                String coupsS = GcleConsole.bufferRead.readLine();
                GCoups coups = GcleConsole.toCase(coupsS);
                if (isInputValidMove(coups)) {
                    playHuman();
                }
            } else {
                playEngine();
                System.out.println(gcoups_curr);
            }
        }
        /*
       @TODO end_of_game
         */
    }

    private boolean isInputValidMove(GCoups coups) {

        Iterator<GCoups> it = gp.getCoupsValides().iterator();
        while (it.hasNext()) {
            GCoups c = it.next();
            if (c.getCaseO() == coups.getCaseO() && c.getCaseX() == coups.getCaseX()) {
                this.gcoups = c;
                return true;
            }
        }

        return false;
        /*
        @TODO traiter coups console invalide
         */

    }

}
