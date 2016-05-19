package game;

import ia.IA;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;

public class GGame implements ICodage {

    private final GPosition gp;
    private final IA ia;
    private final int depth = 1;

    public GGame(String f) {
        gp = FenToGPosition.toGPosition(f);
        ia = new IA(gp, depth);
    }

    public GCoups getMeilleurCoups() {
        ia.search();
        return ia.getMeilleurCoups();
    }

}
