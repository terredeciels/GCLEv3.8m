package ia;

import position.GPosition;
import static position.ICodage.*;

class GPositionEval {

    GPosition gp;

    public GPositionEval(GPosition gp) {
        this.gp = gp;
    }

     int evaluate(GPosition gp) {
        // simpliscime
        // cas des echecs , pat, mat
        //Evaluation de la position / du coups
        //valeurs des pièces sommées
        //parcours des pièces blanches (IA) et noires de P
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

        return Mb - Mn; // IA a les Blancs
    }

    private int couleurPiece(GPosition P, int caseO) {
        return (P.getEtats()[caseO] < 0) ? BLANC : NOIR;
    }

}
