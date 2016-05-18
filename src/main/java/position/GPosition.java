package position;

import java.util.ArrayList;
import java.util.Collections;
import main.Main;
import org.chesspresso.position.Position;
import static position.ICodage.*;

public class GPosition extends GPositionMove implements ICodage {

    Position position;
    String fen;
    ArrayList<String> cp_coupsvalides_lan;
    ArrayList<GCoups> coupsvalides;

    private ArrayList<String> coupsvalides_lan;


    public GPosition() {
        etats = new int[NB_CELLULES];
        R =new Roque();
        roques = R.roques;
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

    public ArrayList<String> getCoupsvalides_lan() {
        return coupsvalides_lan;
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
