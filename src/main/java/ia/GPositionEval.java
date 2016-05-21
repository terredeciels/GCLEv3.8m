package ia;

import position.GPosition;
import static position.ICodage.*;
import static org.joor.Reflect.on;

class GPositionEval {

    GPosition gp;

    public GPositionEval(GPosition gp) {
        this.gp = gp;
    }

    int evaluate(String typeEval) {
        return on(this).call(typeEval).get();// JOOR lib
    }

    private int evaluate1() {
        int eval;
        GPositionEvaluator gPositionEvaluator = new GPositionEvaluator(gp);
        eval = gPositionEvaluator.evaluateComplete(gp.getTrait());

        return eval;
    }

    //Le Matériel
    int evaluate0() {
        int Mb = 0, Mn = 0;
        for (int caseO : CASES117) {
            int C = couleurPiece(gp, caseO);
            int etat = gp.getEtats()[caseO];
            if (etat != OUT && etat != VIDE) {
                if (C == BLANC) {
                    Mb += PIECE_VALUES[Math.abs(etat)];
                } else {
                    Mn += PIECE_VALUES[Math.abs(etat)];
                }

            }
        }
        return gp.getTrait() == BLANC ? Mb - Mn : Mn - Mb;
    }

    private int couleurPiece(GPosition P, int caseO) {// copy abstractgenerateur
        return (P.getEtats()[caseO] < 0) ? BLANC : NOIR;
    }

}
