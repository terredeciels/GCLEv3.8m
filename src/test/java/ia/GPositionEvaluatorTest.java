package ia;

import gcle.Search;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import position.GPosition;
import position.ICodage;

public class GPositionEvaluatorTest implements ICodage {

    private final GPosition gp;

    public GPositionEvaluatorTest() {
        String f = "r3k2r/8/8/8/8/8/8/4K3 w kq - 0 1";
//        f = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
        f = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";
//        f=ICodage.FEN_INITIALE;
//        f="rnb1kb1r/ppp1pppp/8/8/3QP3/8/PP2P1PP/RNB1KBNR b KQkq - 0 6";
        f = "rnq4r/pp2bkpp/2p5/8/8/8/PPP1NKPP/RNBQ3R b - - 0 10";

        Search g = new Search(f);
        gp = g.getGPosition();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEvaluateQuick() {
        System.out.println("evaluateQuick");
        int trait = gp.getTrait();
        int noirsOuBlancs = trait;
        GPositionEvaluator instance = new GPositionEvaluator(gp);

        int eval = instance.evaluateQuick(noirsOuBlancs);
        System.out.println("eval =  " + eval);
        int eval_mat_blanc = instance.MaterialValue[instance.SIDE_WHITE];
        int eval_mat_noir = instance.MaterialValue[instance.SIDE_BLACK];
        System.out.println("eval mat blanc  " + eval_mat_blanc);
        System.out.println("eval mat noir  " + eval_mat_noir);
//        assert (eval != 0);

    }

//    @Test
//    public void testEvaluateComplete() {
//        System.out.println("evaluateComplete");
//        int noirsOuBlancs = 0;
//        GPositionEvaluator instance = null;
//        int expResult = 0;
//        int result = instance.evaluateComplete(noirsOuBlancs);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
