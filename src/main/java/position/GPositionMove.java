package position;

import static java.lang.System.*;
import static position.ICodage.*;
import static position.ICodage.Roque.*;

public class GPositionMove {

    int caseEP;
    Roque R;
    boolean[] roques;

    int[] etats;
    int trait;

    public boolean exec(GCoups gcoups, UndoGCoups ug) {
        arraycopy(etats, 0, ug.etats, 0, NB_CELLULES);
        ug.setKQkq(roques);
        ug.caseEP = caseEP;
        int caseO = gcoups.getCaseO();
        int caseX = gcoups.getCaseX();
        caseEP = -1;
        Roque.trait = trait;
        if (Math.abs(gcoups.getPiece()) == PION && Math.abs(caseX - caseO) == 24) {
            // avance de 2 cases
            caseEP = trait == NOIR ? caseX + 12 : caseX - 12;
        }
        switch (gcoups.getTypeDeCoups()) {
            case Deplacement:
                etats[caseX] = etats[caseO];
                etats[caseO] = VIDE;
                valideDroitRoque(gcoups);
                break;
            case Prise:
                etats[caseX] = etats[caseO];
                etats[caseO] = VIDE;
                //piece prise = tour
                valideDroitRoque(gcoups);
                break;
            case EnPassant:
                // caseX == caseEP
                etats[caseX] = etats[caseO];
                etats[caseO] = VIDE;
                if (trait == BLANC) {
                    etats[caseX + sud] = VIDE;
                } else if (trait == NOIR) {
                    etats[caseX + nord] = VIDE;
                }
                break;
            case Promotion:
                etats[caseX] = gcoups.getPiecePromotion();
                etats[caseO] = VIDE;
                break;
            case Roque:
                etats[caseX] = etats[caseO]; //ROI
                etats[caseO] = VIDE;
                etats[gcoups.getCaseXTour()] = etats[gcoups.getCaseOTour()]; //TOUR
                etats[gcoups.getCaseOTour()] = VIDE;
                unsetRoque();
                break;
            default:
                break;
        }
        trait = -trait;
        return true;
    }

    public void unexec(UndoGCoups ug) {
        arraycopy(ug.etats, 0, etats, 0, NB_CELLULES);
        arraycopy(ug.roques, 0, roques, 0, 4);
        caseEP = ug.caseEP;
        trait = -trait;
    }

    protected void valideDroitRoque(GCoups gcoups) {
        int caseO = gcoups.getCaseO();

        switch (gcoups.getPiece()) {
            //piece deplacee = tour ou roi
            case ROI:
                unsetRoque();
                break;
            case TOUR:
                if (caseO == caseTourH()) {
                    unsetK(trait);
                }
                if (caseO == caseTourA()) {
                    unsetQ(trait);
                }
                break;
            default:
                break;
        }
        // roi et tour à leurs places
        if (etats[caseTourA()] != trait * TOUR || etats[caseRoi()] != trait * ROI) {
            unsetQ(trait);
        }

        if (etats[caseTourH()] != trait * TOUR || etats[caseRoi()] != trait * ROI) {
            unsetK(trait);
        }
    }

}
