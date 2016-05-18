package position;

public class UndoGCoups {

    final int[] etats;
    boolean[] roques;
//    boolean droitPetitRoqueNoir;
//    boolean droitGrandRoqueNoir;
//    boolean droitPetitRoqueBlanc;
//    boolean droitGrandRoqueBlanc;

    int caseEP;

    public UndoGCoups() {

        etats = new int[ICodage.NB_CELLULES];
        roques = new boolean[4];
    }

    void setKQkq(boolean[] roq) {
        roques[0] = roq[0];
        roques[1] = roq[1];
        roques[2] = roq[2];
        roques[3] = roq[3];
    }

}
