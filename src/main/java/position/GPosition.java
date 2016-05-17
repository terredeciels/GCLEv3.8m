package position;

import java.util.ArrayList;
import java.util.Collections;
import main.Main;
import org.chesspresso.position.Position;
import static position.ICodage.*;

public class GPosition implements ICodage {

    Position position;
    String fen;
    int trait;
    boolean droitPetitRoqueNoir;
    boolean droitGrandRoqueNoir;
    boolean droitPetitRoqueBlanc;
    boolean droitGrandRoqueBlanc;
    int caseEP;
    int[] etats;
    ArrayList<String> cp_coupsvalides_lan;
    ArrayList<GCoups> coupsvalides;

    private ArrayList<String> coupsvalides_lan;

    public GPosition() {
        etats = new int[NB_CELLULES];
    }

    public ArrayList<GCoups> getCoupsValides() {
        coupsvalides = new Generateur(this).getCoups();
//        assert (!coupsvalides.isEmpty());
        coupsvalides_lan = new ArrayList<>();
        for (GCoups c : coupsvalides) {
            coupsvalides_lan.add(GCoups.getString(c));
        }
        Collections.sort(coupsvalides_lan);
        return coupsvalides;
    }

    public boolean exec(GCoups gcoups, UndoGCoups ui) {
        System.arraycopy(etats, 0, ui.etats, 0, NB_CELLULES);
        ui.droitPetitRoqueNoir = droitPetitRoqueNoir;
        ui.droitGrandRoqueNoir = droitGrandRoqueNoir;
        ui.droitPetitRoqueBlanc = droitPetitRoqueBlanc;
        ui.droitGrandRoqueBlanc = droitGrandRoqueBlanc;
        ui.caseEP = caseEP;

        int caseO = gcoups.getCaseO();
        int caseX = gcoups.getCaseX();

        caseEP = -1;
        if (gcoups.getPiece() == PION && Math.abs(caseX - caseO) == 24) {// avance de 2 cases
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
                etats[caseX] = etats[caseO];//ROI
                etats[caseO] = VIDE;
                etats[gcoups.getCaseXTour()] = etats[gcoups.getCaseOTour()];//TOUR
                etats[gcoups.getCaseOTour()] = VIDE;
                if (trait == BLANC) {
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

    private void valideDroitRoque(GCoups gcoups) {
        int caseO = gcoups.getCaseO();
        int piece = gcoups.getPiece();
        //piece deplacee = tour ou roi
        if (trait == BLANC) {
            switch (piece) {
                case ROI:
                    droitPetitRoqueBlanc = false;
                    droitGrandRoqueBlanc = false;
                    break;
                case TOUR:
                    droitPetitRoqueBlanc = caseO == h1 ? false : droitPetitRoqueBlanc;
                    droitGrandRoqueBlanc = caseO == a1 ? false : droitGrandRoqueBlanc;
                    break;
                default:
                    break;
            }
            kingAndRookWhiteNotMove(e1, a1, h1);// roi et tour à leurs places
        } else if (trait == NOIR) {
            switch (piece) {
                case ROI:
                    droitPetitRoqueNoir = false;
                    droitGrandRoqueNoir = false;
                    break;
                case TOUR:
                    droitPetitRoqueNoir = caseO == h8 ? false : droitPetitRoqueNoir;
                    droitGrandRoqueNoir = caseO == a8 ? false : droitGrandRoqueNoir;
                    break;
                default:
                    break;
            }
            kingAndRookBlackNotMove(e8, a8, h8);
        }

    }

    private void kingAndRookBlackNotMove(int R, int Ta, int Th) {
        if (etats[Ta] != TOUR || etats[R] != ROI) {
            droitGrandRoqueNoir = false;
        }
        if (etats[Th] != TOUR || etats[R] != ROI) {
            droitPetitRoqueNoir = false;
        }
    }

    private void kingAndRookWhiteNotMove(int R, int Ta, int Th) {
        if (etats[Ta] != -TOUR || etats[R] != -ROI) {
            droitGrandRoqueBlanc = false;
        }
//            droitGrandRoqueBlanc = etats[a1] != -TOUR || etats[e1] != -ROI ? false:droitGrandRoqueBlanc;
        if (etats[Th] != -TOUR || etats[R] != -ROI) {
            droitPetitRoqueBlanc = false;
        }
    }

    public void unexec(UndoGCoups ui) {
        System.arraycopy(ui.etats, 0, etats, 0, NB_CELLULES);
        droitPetitRoqueNoir = ui.droitPetitRoqueNoir;
        droitGrandRoqueNoir = ui.droitGrandRoqueNoir;
        droitPetitRoqueBlanc = ui.droitPetitRoqueBlanc;
        droitGrandRoqueBlanc = ui.droitGrandRoqueBlanc;
        caseEP = ui.caseEP;
        trait = -trait;
    }

    public ArrayList<String> getCoupsvalides_lan() {
        return coupsvalides_lan;
    }

    public String print() {
        String str = "";
        String e_str;
        String Clr_str;
        int rg = 0;
        for (int e : etats) {
            int piecetype = Math.abs(e);
            Clr_str = piecetype == e ? "N" : "B";
            switch (piecetype) {
                case PION:
                    e_str = "P";
                    break;
                case ROI:
                    e_str = "K";
                    break;
                case VIDE:
                    e_str = " ";
                    break;
                case OUT:
                    e_str = "X";
                    break;
                default:
                    e_str = STRING_PIECE[piecetype];
                    break;
            }
            Clr_str = "X".equals(e_str) ? "X" : Clr_str;
            Clr_str = " ".equals(e_str) ? "_" : Clr_str;
            str += e_str + Clr_str + " ";
            rg++;
            if (rg == 12) {
                str += '\n';
                rg = 0;
            }
        }
        return str;
    }

    public int[] getEtats() {
        return etats;
    }

    public String getFen() {
        return fen;
    }

    public int getTrait() {
        return trait;
    }

    @Override
    public String toString() {
        return Main.DEBUG ? "CP_CoupsValides : " + '\n'
                + cp_coupsvalides_lan + '\n'
                + "G_CoupsValides : " + '\n'
                + coupsvalides_lan
                : "G_CoupsValides : " + '\n' + coupsvalides_lan;
//        return coupsvalides_lan.toString();
    }

}
