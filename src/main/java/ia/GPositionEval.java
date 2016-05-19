package ia;

import static org.joor.Reflect.on;
import position.GPosition;
import static position.ICodage.*;

class GPositionEval {

    GPosition gp;

    public GPositionEval(GPosition gp) {
        this.gp = gp;
    }

    int evaluate(String typeEval) {
        return on(this).call(typeEval).get();// JOOR lib
    }

    private int evaluate1() {
        int eval = 1;

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
                    Mb += VALEUR_PIECE[Math.abs(etat)];
                } else {
                    Mn += VALEUR_PIECE[Math.abs(etat)];
                }

            }
        }
        return gp.getTrait() == BLANC ? Mb - Mn : Mn - Mb;
    }

    private int couleurPiece(GPosition P, int caseO) {// copy abstractgenerateur
        return (P.getEtats()[caseO] < 0) ? BLANC : NOIR;
    }

}
