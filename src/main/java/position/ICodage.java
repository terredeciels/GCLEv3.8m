package position;

public interface ICodage {

    int INFINI = Integer.MAX_VALUE;
    int PAS_DE_CASE = -1;    // e.p.
    String FEN_INITIALE = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    int BLANC = -1,
            NOIR = 1;
    int a1 = 26,
            h1 = 33,
            a8 = 110,
            h8 = 117;
    int e1 = 30,
            f1 = 31,
            g1 = 32,
            d1 = 29,
            c1 = 28,
            b1 = 27;
    int e8 = 114, f8 = 115, g8 = 116, d8 = 113, c8 = 112, b8 = 111;
    int d7 = 101, e7 = 102;
    int d2 = 41, e2 = 42; // verif
    int a2 = 38, h2 = 45, a7 = 98, h7 = 105;//verif
    int d4 = 65, e4 = 66;
    int e6 = 90, f6 = 91;
    int d5 = 77;
    int f3 = 55;
    int c2 = 40, c4 = 64;
    int g7 = 104, g6 = 92;
    int c3 = 52;
    int e3 = 54, a6 = 86, c7 = 100, c5 = 76, b7 = 99, b6 = 87, f7 = 103;
    int b4=63,e5=78,f5=79;
    int[] CASES117 = {
        26, 27, 28, 29, 30, 31, 32, 33,
        38, 39, 40, 41, 42, 43, 44, 45,
        50, 51, 52, 53, 54, 55, 56, 57,
        62, 63, 64, 65, 66, 67, 68, 69,
        74, 75, 76, 77, 78, 79, 80, 81,
        86, 87, 88, 89, 90, 91, 92, 93,
        98, 99, 100, 101, 102, 103, 104, 105,
        110, 111, 112, 113, 114, 115, 116, 117
    };
    int[] CASES64 = {
        0, 1, 2, 3, 4, 5, 6, 7,
        8, 9, 10, 11, 12, 13, 14, 15,
        16, 17, 18, 19, 20, 21, 22, 23,
        24, 25, 26, 27, 28, 29, 30, 31,
        32, 33, 34, 35, 36, 37, 38, 39,
        40, 41, 42, 43, 44, 45, 46, 47,
        48, 49, 50, 51, 52, 53, 54, 55,
        56, 57, 58, 59, 60, 61, 62, 63};

    int[] INDICECASES = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, -1, -1,
        -1, -1, 8, 9, 10, 11, 12, 13, 14, 15, -1, -1,
        -1, -1, 16, 17, 18, 19, 20, 21, 22, 23, -1, -1,
        -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1,
        -1, -1, 32, 33, 34, 35, 36, 37, 38, 39, -1, -1,
        -1, -1, 40, 41, 42, 43, 44, 45, 46, 47, -1, -1,
        -1, -1, 48, 49, 50, 51, 52, 53, 54, 55, -1, -1,
        -1, -1, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

    int[] INDICECASES_GUI = {
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1,
        -1, -1, 48, 49, 50, 51, 52, 53, 54, 55, -1, -1,
        -1, -1, 40, 41, 42, 43, 44, 45, 46, 47, -1, -1,
        -1, -1, 32, 33, 34, 35, 36, 37, 38, 39, -1, -1,
        -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1,
        -1, -1, 16, 17, 18, 19, 20, 21, 22, 23, -1, -1,
        -1, -1, 8, 9, 10, 11, 12, 13, 14, 15, -1, -1,
        -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };
    String[] STRING_CASES = {
        "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
        "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
        "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
        "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
        "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
        "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
        "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
        "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"
    };
    String STRING_COL = "abcdefgh";

    String[] STRING_PIECE = {"", "N", "B", "R", "Q"};
    // Types pièces codés comme dans CP
    int ROI = 6,
            CAVALIER = 1,
            TOUR = 3,
            FOU = 2,
            DAME = 4,
            PION = 5;
    int[] PIECE_VALUES = {0, 300, 350, 500, 900, 100, 2000};
    //
    int NB_CASES = 64,
            NB_CELLULES = 144;
    int VIDE = 0,
            OUT = 9;
    int nord = +12,
            est = -1,
            sud = -12,
            ouest = +1;
    int nordest = nord + est;
    int nordouest = nord + ouest;
    int sudest = sud + est;
    int sudouest = sud + ouest;
    int[] DIR_CAVALIER = {
        2 * nord + est, 2 * nord + ouest, 2 * est + nord, 2 * est + sud, 2 * sud + est, 2 * sud + ouest,
        2 * ouest + nord, 2 * ouest + sud
    };
    int[] DIR_DAME = {
        nord, nordest, est, sudest, sud, sudouest, ouest, nordouest
    };
    int[] DIR_FOU = {nordest, sudest, sudouest, nordouest};
    int[] DIR_ROI = {
        nord, nordest, est, sudest, sud, sudouest, ouest, nordouest
    };
    int[] DIR_TOUR = {nord, est, sud, ouest};

    enum TYPE_DE_COUPS {

        Roque, EnPassant, Promotion, Deplacement, Prise, Attaque;
    }

    enum PIECE {

    }

    class Roque {

        static boolean[] roques;
        static int trait;

        Roque() {
            roques = new boolean[4];
        }

        /*
        
        K petit roque blanc
        Q grand roque blanc
        k petit roque noir
        q grand roque noir
         */
        static void unsetRoque() {
            if (trait == BLANC) {
                unsetKQ();
            } else if (trait == NOIR) {
                unsetkq();
            }
        }

        static void unsetKQ() {
            roques[0] = false;
            roques[1] = false;
        }

        static void unsetkq() {
            roques[2] = false;
            roques[3] = false;
        }

        static void unsetK(int color) {
            if (color == BLANC) {
                unsetK();
            } else if (color == NOIR) {
                unsetk();
            }

        }

        static void unsetQ(int color) {
            if (color == BLANC) {
                unsetQ();
            } else if (color == NOIR) {
                unsetq();
            }
        }

        static void unsetK() {
            roques[0] = false;
        }

        static void unsetQ() {
            roques[1] = false;
        }

        static void unsetk() {
            roques[2] = false;
        }

        static void unsetq() {
            roques[3] = false;
        }

        static int caseTourH() {
            return trait == BLANC ? h1 : h8;
        }

        static int caseTourA() {
            return trait == BLANC ? a1 : a8;
        }

        static int caseRoi() {
            return trait == BLANC ? e1 : e8;
        }
    }
}
