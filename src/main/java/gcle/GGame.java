package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import position.GCoups;
import position.GPosition;
import position.UndoGCoups;

class GGame {// une partie

    int ordi_color;
    int human_color;
    GPosition this_gp;
    GCoups this_gcoups;
    List<GCoups> listecoupspartie;
    List<GPosition> listepositionspartie;
    int halfmove;
//    int nummove;

    public GGame() {
        this.listecoupspartie = new ArrayList<>();
        this.listepositionspartie = new ArrayList<>();
        this.halfmove = 1;
//        this.nummove=1;
    }

    void playHuman(int human_color, GCoups gcoups) {
        this.listepositionspartie.add(halfmove, this_gp);
        this.listecoupspartie.add(halfmove, gcoups);
        UndoGCoups ug = new UndoGCoups();
        this_gp.exec(gcoups, ug);
        this.listepositionspartie.add(halfmove++, this_gp);
    }

    void playEngine(int ordi_color) {
        // new Thread ?  
        // search.run();// ??
        // search.start(); //??
        Search search = new Search(this_gp);
        try {
            this_gcoups = search.getMeilleurCoups();
            this.listecoupspartie.add(halfmove, this_gcoups);
            UndoGCoups ug = new UndoGCoups();
            this_gp.exec(this_gcoups, ug);
            this.listepositionspartie.add(halfmove++, this_gp);
        } catch (NodeNotFoundException ex) {

        }
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        Iterator<GCoups> it = listecoupspartie.iterator();
        while (it.hasNext()) {
            buff.append(GCoups.getString(it.next()));
            buff.append(";");
        }
        return buff.toString();
    }

}
