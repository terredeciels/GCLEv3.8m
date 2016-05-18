package gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import position.FenToGPosition;
import position.GPosition;
import position.ICodage;

public class GameView implements ICodage {

    private final static Logger LOG = Logger.getLogger(GameView.class.getName());
    GPosition gp;

    public GameView(GPosition gp) {
        this.gp = gp;
    }

    void setFen(ChessGui gui, String f) {// from GUI
        try {
            gp = FenToGPosition.toGPosition(f);
            gui.setGuiPosition();
            CoupsValidesToGui a = new CoupsValidesToGui(f);
            a.start();
            gui.analysisJTextArea.setText(a.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            LOG.log(Level.WARNING, "fen error, return initiale position");
            gui.fenJTextField.setText(ICodage.FEN_INITIALE);
            gp = FenToGPosition.toGPosition(ICodage.FEN_INITIALE);

        }

    }

    void reset() {
    }

    void flip(ChessGui gui) {
    }

    int couleurPiece(int _case) {//copie AbstractGenerateur
        return (gp.getEtats()[_case] < 0) ? BLANC : NOIR;
    }

    int typeDePiece(int _case) {//copie AbstractGenerateur
        return (gp.getEtats()[_case] < 0) ? -gp.getEtats()[_case] : gp.getEtats()[_case];
    }
}
