package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import org.apache.commons.collections.iterators.ArrayIterator;
import position.GCoups;
import position.GPosition;
import position.UndoGCoups;

public class GGame {

    /*
    @TODO Thread
     */
    GCoups gcoups;
    GCoups gcoups_curr;
    GPosition gp;

    GCoups[] listecoupspartie;
    GPosition[] listepositionspartie;
    int halfmove;

    public GGame() {
        listecoupspartie = new GCoups[100];
        listepositionspartie = new GPosition[100];
        halfmove = 1;
    }

    void playHuman() {
        listepositionspartie[halfmove] = gp;
        listecoupspartie[halfmove] = gcoups;
        UndoGCoups ug = new UndoGCoups();
        gp.exec(gcoups, ug);
        listepositionspartie[halfmove++] = gp;
    }

    void playEngine() throws NodeNotFoundException {
        // new Thread ?
        // search.run();// ??
        // search.start(); //??
        Search search = new Search(this);
        gcoups_curr = search.getMeilleurCoups();
        listecoupspartie[halfmove] = gcoups_curr;
        UndoGCoups ug = new UndoGCoups();
        gp.exec(gcoups_curr, ug);
        listepositionspartie[halfmove++] = gp;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        ArrayIterator it = new ArrayIterator(listecoupspartie);
        while (it.hasNext()) {
            buff.append(GCoups.getString((GCoups) it.next()));
            buff.append(";");
        }
        return buff.toString();
    }

    public GPosition getGPosition() {
        return gp;
    }
    
}
