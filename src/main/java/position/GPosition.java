package position;

import gcle.uci.GcleUCI;
import java.util.ArrayList;
import java.util.Collections;
import org.chesspresso.position.Position;
import static position.ICodage.*;

public class GPosition extends GPositionMove implements ICodage {

    Position position;
    String fen;
    ArrayList<String> cp_coupsvalides_lan;
    ArrayList<GCoups> coupsvalides;

    private ArrayList<String> coupsvalides_lan;
    private boolean estEnEchec;

    public GPosition() {
        etats = new int[NB_CELLULES];
        R = new Roque();
        roques = Roque.roques;
    }

    public ArrayList<GCoups> getCoupsValides() {
        Generateur generateur = new Generateur(this);
        coupsvalides = generateur.getCoups();
      estEnEchec =  generateur.estEnEchec();
//        assert (!coupsvalides.isEmpty());
        coupsvalides_lan = new ArrayList<>();
        for (GCoups c : coupsvalides) {
            coupsvalides_lan.add(GCoups.getString(c));
        }
        Collections.sort(coupsvalides_lan);
        return coupsvalides;
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

    public void setTrait(int trait) {
        this.trait = trait;
    }

    public int getCaseEP() {
        return caseEP;
    }

    public boolean estEnEchec() {
        return estEnEchec;
    }

    public boolean hasRoques(int color) {
        int c = color == BLANC ? 0 : 2;
        return roques[0 + c] || roques[1 + c];
    }

    public boolean isPetitRoque(int color) {
        int c = color == BLANC ? 0 : 2;
        return roques[0 + c];
    }

    public boolean isGrandRoque(int color) {
        int c = color == BLANC ? 0 : 2;
        return roques[1 + c];
    }

    public ArrayList<String> getCoupsvalides_lan() {
        return coupsvalides_lan;
    }

    @Override
    public String toString() {
        return GcleUCI.DEBUG ? "CP_CoupsValides : " + '\n'
                + cp_coupsvalides_lan + '\n'
                + "G_CoupsValides : " + '\n'
                + coupsvalides_lan
                : "G_CoupsValides : " + '\n' + coupsvalides_lan;
//        return coupsvalides_lan.toString();
    }

}
