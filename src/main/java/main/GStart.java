package main;

import gui.ChessGui;
import gui.GameView;
import position.FenToGPosition;
import position.GPosition;
import position.ICodage;

class GStart {

    public GStart() {
        String fen = ICodage.FEN_INITIALE;
        GPosition gp = FenToGPosition.toGPosition(fen);
        GameView gameview = new GameView(gp);
        ChessGui gui = new ChessGui(gameview);
        gui.setGuiPosition();
    }

}
