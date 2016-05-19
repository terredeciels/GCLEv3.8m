package main;

import game.GGame;
import position.GCoups;
import position.ICodage;

public class Main {

    public final static boolean DEBUG = true;

    public static void main(String[] args) {
//        GStart gStart = new GStart();
        String f = "r3k2r/8/8/8/8/8/8/4K3 w kq - 0 1";
//        f = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
        f = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8"; 
//        f=ICodage.FEN_INITIALE;
//        f="rnb1kb1r/ppp1pppp/8/8/3QP3/8/PP2P1PP/RNB1KBNR b KQkq - 0 6";
      
        GGame g = new GGame(f);
        System.out.println(GCoups.getString(g.getMeilleurCoups()));
//        System.out.println("MAX_VALUE = " + Integer.MAX_VALUE);
    }

}
