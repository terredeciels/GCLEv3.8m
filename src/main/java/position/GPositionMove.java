package position;

import static position.ICodage.*;

public class GPositionMove {

    int caseEP;
    Roque R;
    boolean[] roques;

    int[] etats;
    int trait;

    public boolean exec(GCoups gcoups, UndoGCoups ui) {
        System.arraycopy(etats, 0, ui.etats, 0, ICodage.NB_CELLULES);
        ui.setKQkq(roques);
        ui.caseEP = caseEP;
        int caseO = gcoups.getCaseO();
        int caseX = gcoups.getCaseX();
        caseEP = -1;
        if (gcoups.getPiece() == ICodage.PION && Math.abs(caseX - caseO) == 24) {
            // avance de 2 cases
            caseEP = trait == ICodage.NOIR ? caseX + 12 : caseX - 12;
        }
        switch (gcoups.getTypeDeCoups()) {
            case Deplacement:
                etats[caseX] = etats[caseO];
                etats[caseO] = ICodage.VIDE;
                valideDroitRoque(gcoups);
                break;
            case Prise:
                etats[caseX] = etats[caseO];
                etats[caseO] = ICodage.VIDE;
                //piece prise = tour
                valideDroitRoque(gcoups);
                break;
            case EnPassant:
                // caseX == caseEP
                etats[caseX] = etats[caseO];
                etats[caseO] = ICodage.VIDE;
                if (trait == ICodage.BLANC) {
                    etats[caseX + ICodage.sud] = ICodage.VIDE;
                } else if (trait == ICodage.NOIR) {
                    etats[caseX + ICodage.nord] = ICodage.VIDE;
                }
                break;
            case Promotion:
                etats[caseX] = gcoups.getPiecePromotion();
                etats[caseO] = ICodage.VIDE;
                break;
            case Roque:
                etats[caseX] = etats[caseO]; //ROI
                etats[caseO] = ICodage.VIDE;
                etats[gcoups.getCaseXTour()] = etats[gcoups.getCaseOTour()]; //TOUR
                etats[gcoups.getCaseOTour()] = ICodage.VIDE;

                R.unsetRoque(trait);

                break;
            default:
                break;
        }
        trait = -trait;
        return true;
    }

    public void unexec(UndoGCoups ui) {
        System.arraycopy(ui.etats, 0, etats, 0, ICodage.NB_CELLULES);
        setKQkq(ui.roques);
        caseEP = ui.caseEP;
        trait = -trait;
    }

    protected void valideDroitRoque(GCoups gcoups) {
        int caseO = gcoups.getCaseO();
        int piece = gcoups.getPiece();
        //piece deplacee = tour ou roi
        if (trait == BLANC) {
            switch (piece) {
                case ROI:
                    R.unsetRoque(trait);
                    break;
                case TOUR:
                    if (caseO == h1) {
                        R.unsetK(BLANC);
                    }
                    if (caseO == a1) {
                        R.unsetQ(BLANC);
                    }
                    break;
                default:
                    break;
            }
            kingOrRookHaveMove(e1, a1, h1, BLANC); // roi et tour à leurs places
        } else if (trait == NOIR) {
            switch (piece) {
                case ROI:
                    R.unsetRoque(trait);
                    break;
                case TOUR:
                    if (caseO == h1) {
                        R.unsetK(NOIR);
                    }
                    if (caseO == a1) {
                        R.unsetQ(NOIR);
                    }
                    break;
                default:
                    break;
            }
            kingOrRookHaveMove(e8, a8, h8, NOIR);
        }
    }

    protected void kingOrRookHaveMove(int caseRoi, int Ta, int Th, int color) {
        if (etats[Ta] != color * TOUR || etats[caseRoi] != color * ROI) {
            R.unsetQ(color);
        }

        if (etats[Th] != color * TOUR || etats[caseRoi] != color * ROI) {
            R.unsetK(color);
        }
    }

//    protected void kingAndRookBlackNotMove(int caseRoi, int Ta, int Th, int color) {
//        if (etats[Ta] != color * TOUR || etats[caseRoi] != color * ROI) {
//            R.unsetQ(color);
//        }
//        if (etats[Th] != color * TOUR || etats[caseRoi] != color * ROI) {
//            R.unsetK(color);
//        }
//    }

    void setKQkq(boolean[] roq) {
        roques[0] = roq[0];
        roques[1] = roq[1];
        roques[2] = roq[2];
        roques[3] = roq[3];
    }
}
