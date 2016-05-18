package ia;

import java.util.ArrayList;
import position.GCoups;
import position.GPosition;
import position.UndoGCoups;

public class IA extends GPositionEval {

    private ArrayList<GCoups> allGCoups;
    private UndoGCoups ug;
    private int index;

    private int eval;
    private GCoups gcoups;

    public IA(GPosition gp) {
        super(gp);
        index = 0;
    }

    public int alphaBeta(int depth, int alpha, int beta) {
        if (depth == 0) {
            return evaluate();
        }
        genLegalMoves();
        while (hasNextMove()) {
            makeNextMove();
            int val = -alphaBeta(depth - 1, -beta, -alpha);
//            gcoups.setCoupsEval(val);
            undoMove();
            if (val >= beta) {
                return beta;
            }
            if (val > alpha) {
                alpha = val;
            }
        }
        return alpha;
    }

    private void genLegalMoves() {
        allGCoups = gp.getCoupsValides();
    }

    private boolean hasNextMove() {
        return index < allGCoups.size();
    }

    private void makeNextMove() {
        ug = new UndoGCoups();
        gcoups = allGCoups.get(index);
        gp.exec(gcoups, ug);
        index++;
    }

    private void undoMove() {
        gp.unexec(ug);
    }

}
