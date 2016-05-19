package ia;

import position.GPosition;
import static position.ICodage.*;

class GPositionEval {

    GPosition gp;

    public GPositionEval(GPosition gp) {
        this.gp = gp;
    }

    //Le Matériel
    int evaluate() {
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

    private int couleurPiece(GPosition P, int caseO) {
        return (P.getEtats()[caseO] < 0) ? BLANC : NOIR;
    }

}
