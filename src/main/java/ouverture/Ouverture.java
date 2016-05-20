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

  private final  Tree<GCoups> O;

    public Ouverture() throws NodeNotFoundException {
        O = new ArrayListTree<>();
        setOpeningTree();
    }

    // choix aleatoire d'un coups enfant du coups en cours (gc)
    public GCoups searchNextMoveInTree(GCoups gc) throws NodeNotFoundException {
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
        return null;
    }

    private void setOpeningTree() throws NodeNotFoundException {
        GCoups _root = new GCoups(0, 0, 0, 0, null);
        O.add(_root);
        GCoups _e4 = new GCoups(PION, e2, e4, 0, Deplacement);
        GCoups _d4 = new GCoups(PION, d2, d4, 0, Deplacement);
        O.add(_root, _e4);
        O.add(_root, _d4);
        GCoups _e6 = new GCoups(PION, e7, e6, 0, Deplacement);
        GCoups _Cf6 = new GCoups(CAVALIER, g8, f6, 0, Deplacement);
        O.add(_e4, _e6);
        O.add(_d4, _Cf6);
        GCoups _d5 = new GCoups(PION, d7, d5, 0, Deplacement);
        O.add(_e6, _d4);
        O.add(_d4, _d5);
        GCoups _e4xd5 = new GCoups(PION, e4, d5, 0, Prise);
        GCoups _Cf3 = new GCoups(CAVALIER, g1, f3, 0, Deplacement);
        O.add(_d5, _e4xd5);
        O.add(_d5, _Cf3);
        GCoups _c4 = new GCoups(PION, c2, c4, 0, Deplacement);
        O.add(_Cf6, _c4);
        GCoups _g6 = new GCoups(PION, g7, g6, 0, Deplacement);
        O.add(_c4, _g6);
        GCoups _Cc3 = new GCoups(CAVALIER, b1, c3, 0, Deplacement);
        O.add(_g6, _Cc3);
        O.add(_g6, _e4);

    }

//    public class GCoupsFind {
//
//        private GCoups choix;
//
//        public GCoupsFind() {
//
//        }
//
//        private GCoupsFind(GCoups choix) {
//            this.choix = choix;
//        }
//
//        public GCoups getChoix() {
//            return choix;
//        }
//
//    }

    private class Aleatoire {

        private final Random randomGenerator;

        public Aleatoire() {
            randomGenerator = new Random();
        }

        public GCoups aleaChoice(ArrayList<GCoups> arrayList) {

            int randomInt = randomGenerator.nextInt(arrayList.size());
            GCoups gcoups = arrayList.get(randomInt);

            return gcoups;
        }

    }
}
