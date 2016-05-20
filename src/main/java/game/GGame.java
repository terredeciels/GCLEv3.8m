package game;

import com.googlecode.jctree.NodeNotFoundException;
import ia.IA;
import ouverture.Ouverture;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;

public class GGame implements ICodage {

    private final GPosition gp;
    private final IA ia;
    private final int depth = 1;
    private boolean end_opening;

    public GGame(String f) {
        gp = FenToGPosition.toGPosition(f);
        ia = new IA(gp, depth);
    }

    public GCoups getMeilleurCoups() throws NodeNotFoundException {
        //ouvertures
        Ouverture ouv = new Ouverture();
        GCoups gc = null;
        if (!end_opening) {
            GCoups next_coups = ouv.searchNextMoveInTree(gc);
            if (next_coups != null) {
                return next_coups;
            } else {
                end_opening = true;
            }
        }
        ia.search();
        return ia.getMeilleurCoups();

    }

    public GPosition getGPosition() {
        return gp;
    }

}
