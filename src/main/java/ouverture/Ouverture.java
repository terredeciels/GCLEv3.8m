package ouverture;

import com.googlecode.jctree.ArrayListTree;
import com.googlecode.jctree.NodeNotFoundException;
import com.googlecode.jctree.Tree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import position.GCoups;
import position.ICodage;
import static position.ICodage.TYPE_DE_COUPS.*;

public class Ouverture implements ICodage {

    /*
    @TODO distinguer ouvertures noirs et blancs
     */
    private final Tree<GCoups> O;
    private GCoups _root;

    public Ouverture() throws NodeNotFoundException {
        O = new ArrayListTree<>();

        setOpeningTree();
    }

    // choix aleatoire d'un coups enfant du coups en cours (gc)
    public GCoups searchNextMoveInTree(int halfmove, GCoups gc) throws NodeNotFoundException {
        if (halfmove == 0) {
            Collection<GCoups> child = O.children(_root);
            ArrayList<GCoups> arrayList = new ArrayList<>(child);
            GCoups choix = new Aleatoire().aleaChoice(arrayList);
            return choix;
        } else {
            Iterator<GCoups> ito = O.iterator();
            while (ito.hasNext()) {
                GCoups coups = ito.next();
                if (coups.equals(gc)) { // equals ?
                    Collection<GCoups> child = O.children(coups);
                    ArrayList<GCoups> arrayList = new ArrayList<>(child);
                    GCoups choix = new Aleatoire().aleaChoice(arrayList);
                    return choix;
                }
            }
        }

        return null;
    }

    private void setOpeningTree() throws NodeNotFoundException {
        _root = new GCoups(0, 0, 0, 0, null);
        O.add(_root);
        GCoups _e4 = new GCoups(PION, e2, e4, 0, Deplacement);
        GCoups _d4 = new GCoups(PION, d2, d4, 0, Deplacement);
        GCoups _e6 = new GCoups(PION, e7, e6, 0, Deplacement);
        GCoups _Cf6 = new GCoups(CAVALIER, g8, f6, 0, Deplacement);
        GCoups _d5 = new GCoups(PION, d7, d5, 0, Deplacement);
        GCoups _e4xd5 = new GCoups(PION, e4, d5, 0, Prise);
        GCoups _Cf3 = new GCoups(CAVALIER, g1, f3, 0, Deplacement);
        GCoups _c4 = new GCoups(PION, c2, c4, 0, Deplacement);
        GCoups _g6 = new GCoups(PION, g7, g6, 0, Deplacement);
        GCoups _Cc3 = new GCoups(CAVALIER, b1, c3, 0, Deplacement);
        O.add(_root, _e4);
        O.add(_root, _d4);

        O.add(_e4, _e6);
        O.add(_d4, _Cf6);
        O.add(_e6, _d4);

        O.add(_d4, _d5);
        O.add(_d5, _e4xd5);
        O.add(_d5, _Cf3);
        O.add(_Cf6, _c4);
        O.add(_c4, _g6);
        O.add(_g6, _Cc3);
        O.add(_g6, _e4);

    }

    private class Aleatoire {

        private final Random randomGenerator;

        public Aleatoire() {
            randomGenerator = new Random();
        }

        public GCoups aleaChoice(ArrayList<GCoups> arrayList) {
//            assert(!arrayList.isEmpty());
            int randomInt = randomGenerator.nextInt(arrayList.size());
            GCoups gcoups = arrayList.get(randomInt);

            return gcoups;
        }

    }
}
