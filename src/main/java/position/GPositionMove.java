package position;

public class GPositionMove {

    int caseEP;
    boolean droitGrandRoqueBlanc;
    boolean droitGrandRoqueNoir;
    boolean droitPetitRoqueBlanc;
    boolean droitPetitRoqueNoir;
    int[] etats;
    int trait;

    public boolean exec(GCoups gcoups, UndoGCoups ui) {
        System.arraycopy(etats, 0, ui.etats, 0, ICodage.NB_CELLULES);
        ui.droitPetitRoqueNoir = droitPetitRoqueNoir;
        ui.droitGrandRoqueNoir = droitGrandRoqueNoir;
        ui.droitPetitRoqueBlanc = droitPetitRoqueBlanc;
        ui.droitGrandRoqueBlanc = droitGrandRoqueBlanc;
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
                if (trait == ICodage.BLANC) {
                    droitPetitRoqueBlanc = false;
                    droitGrandRoqueBlanc = false;
                } else {
                    droitPetitRoqueNoir = false;
                    droitGrandRoqueNoir = false;
                }
                break;
            default:
                break;
        }
        trait = -trait;
        return true;
    }

    protected void kingAndRookBlackNotMove(int R, int Ta, int Th) {
        if (etats[Ta] != ICodage.TOUR || etats[R] != ICodage.ROI) {
            droitGrandRoqueNoir = false;
        }
        if (etats[Th] != ICodage.TOUR || etats[R] != ICodage.ROI) {
            droitPetitRoqueNoir = false;
        }
    }

    protected void kingAndRookWhiteNotMove(int R, int Ta, int Th) {
        if (etats[Ta] != -ICodage.TOUR || etats[R] != -ICodage.ROI) {
            droitGrandRoqueBlanc = false;
        }
        //            droitGrandRoqueBlanc = etats[a1] != -TOUR || etats[e1] != -ROI ? false:droitGrandRoqueBlanc;
        if (etats[Th] != -ICodage.TOUR || etats[R] != -ICodage.ROI) {
            droitPetitRoqueBlanc = false;
        }
    }

    public void unexec(UndoGCoups ui) {
        System.arraycopy(ui.etats, 0, etats, 0, ICodage.NB_CELLULES);
        droitPetitRoqueNoir = ui.droitPetitRoqueNoir;
        droitGrandRoqueNoir = ui.droitGrandRoqueNoir;
        droitPetitRoqueBlanc = ui.droitPetitRoqueBlanc;
        droitGrandRoqueBlanc = ui.droitGrandRoqueBlanc;
        caseEP = ui.caseEP;
        trait = -trait;
    }

    protected void valideDroitRoque(GCoups gcoups) {
        int caseO = gcoups.getCaseO();
        int piece = gcoups.getPiece();
        //piece deplacee = tour ou roi
        if (trait == ICodage.BLANC) {
            switch (piece) {
                case ICodage.ROI:
                    droitPetitRoqueBlanc = false;
                    droitGrandRoqueBlanc = false;
                    break;
                case ICodage.TOUR:
                    droitPetitRoqueBlanc = caseO == ICodage.h1 ? false : droitPetitRoqueBlanc;
                    droitGrandRoqueBlanc = caseO == ICodage.a1 ? false : droitGrandRoqueBlanc;
                    break;
                default:
                    break;
            }
            kingAndRookWhiteNotMove(ICodage.e1, ICodage.a1, ICodage.h1); // roi et tour à leurs places
        } else if (trait == ICodage.NOIR) {
            switch (piece) {
                case ICodage.ROI:
                    droitPetitRoqueNoir = false;
                    droitGrandRoqueNoir = false;
                    break;
                case ICodage.TOUR:
                    droitPetitRoqueNoir = caseO == ICodage.h8 ? false : droitPetitRoqueNoir;
                    droitGrandRoqueNoir = caseO == ICodage.a8 ? false : droitGrandRoqueNoir;
                    break;
                default:
                    break;
            }
            kingAndRookBlackNotMove(ICodage.e8, ICodage.a8, ICodage.h8);
        }
    }

}
