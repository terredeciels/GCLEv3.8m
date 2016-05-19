package ia;

import java.util.ArrayList;
import position.GCoups;
import position.GPosition;
import position.ICodage;
import position.UndoGCoups;

public class IA extends GPositionEval implements ICodage {

    private final int depth;
    private ArrayList<GPosition> positionSuivante;

    public IA(GPosition gp, int depth) {
        super(gp);
        this.depth = depth;
    }

    public int search() {
        return alphaBeta(gp, 0, -INFINI, INFINI);
    }

    // Ne marche que pour les niveau pairs ?
    private int alphaBeta(GPosition gp, int niveau, int alpha, int beta) {
        int val, best, i, N;

        if (niveau == depth) {
            return evaluate(gp);
        }
        N = trouveCoupsValides(gp);
        if (odd(niveau)) {
            best = INFINI;
        } else {
            best = -INFINI;
        }
        for (i = 0; i < N; i++) {
            GPosition pos = positionSuivante.get(i);
            System.out.println(pos.print());
            val = alphaBeta(pos, niveau + 1, alpha, beta);
            System.out.println(val);

            if (odd(niveau)) { // on minimise
                if (val < best) {
                    best = val;
                    if (best < beta) {
                        beta = best;
                        if (alpha > beta) {
                            return best; // coupure alpha
                        }
                    }
                } else if (val > best) { // on maximise
                    best = val;
                    if (best > alpha) {
                        alpha = best;
                        if (alpha > beta) {
                            return best; // coupure beta
                        }
                    }
                }
            }

        }
        return best;
    }

    private int trouveCoupsValides(GPosition gp) {
        positionSuivante = new ArrayList<>();
        ArrayList<GCoups> allMoves = gp.getCoupsValides();
        for (GCoups gcoups : allMoves) {
            UndoGCoups ug = new UndoGCoups();
            gp.exec(gcoups, ug);
            positionSuivante.add(gp);
            gp.unexec(ug);
        }
        return positionSuivante.size();

    }

    boolean odd(int a) {
        return a % 2 != 0;
    }
}
