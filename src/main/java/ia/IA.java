package ia;

import java.util.logging.Level;
import java.util.logging.Logger;
import position.GCoups;
import position.GPosition;
import position.ICodage;
import position.UndoGCoups;

public class IA extends GPositionEval implements ICodage {

    private final int depth;
    /*
    TODO end_of_game
    */
    private final boolean end_of_game = false;
    private GCoups meilleur_coup;

    public IA(GPosition gp, int depth) {
        super(gp);
        this.depth = depth;
    }

    public int search() {
        return alphabeta(depth, -INFINI, INFINI);
    }

    private int alphabeta(int profondeur, int alpha, int beta) {
        if (end_of_game || profondeur <= 0) {
            return evaluate("evaluate1");
        }
        for (GCoups gcoups : gp.getCoupsValides()) {
            UndoGCoups ug = new UndoGCoups();
            gp.exec(gcoups, ug);
            int score = -alphabeta(profondeur - 1, -beta, -alpha);
            gp.unexec(ug);
            if (score >= alpha) {
                alpha = score;
                meilleur_coup = gcoups;
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return alpha;
    }

    public GCoups getMeilleurCoups() {
        return meilleur_coup;
    }
}
