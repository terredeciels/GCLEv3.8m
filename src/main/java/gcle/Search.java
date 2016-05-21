package gcle;

import com.fluxchess.jcpi.commands.IProtocol;
import com.fluxchess.jcpi.commands.ProtocolBestMoveCommand;
import com.fluxchess.jcpi.models.GenericMove;
import com.googlecode.jctree.NodeNotFoundException;
import ia.IA;
import java.util.concurrent.Semaphore;
import ouverture.Ouverture;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;

public class Search implements ISearch, Runnable {

    private boolean canStop;
    private boolean stopped;
    private Result bestResult = null;
    private final Thread thread = new Thread(this);
    private final Semaphore semaphore = new Semaphore(0);
    private final ChessLogger logger = ChessLogger.getLogger();
    private final IProtocol protocol;
    private final GPosition gp;
    private final IA ia;
    private final int depth = 1;
    private boolean end_opening;

    public Search(String f) {
        protocol = null;//
        this.gp = FenToGPosition.toGPosition(f);
        this.ia = new IA(gp, depth);
    }

    public Search(String f, IProtocol protocol) {
        gp = FenToGPosition.toGPosition(f);
        ia = new IA(gp, depth);
        this.protocol = protocol;
    }

//    public Search(GPositionEvaluator evaluation, IProtocol protocol) {
//        this.protocol = protocol;
//    }
    public GCoups getMeilleurCoups() throws NodeNotFoundException {
        //ouvertures
        Ouverture ouv = new Ouverture();
        GCoups gc = null;
        if (!end_opening) {
            GCoups next_coups = ouv.searchNextMoveInTree(gc);
            if (next_coups != null) {
                return next_coups;
            } else {
                end_opening = true;
            }
        }
        ia.search();
        return ia.getMeilleurCoups();

    }

    public GPosition getGPosition() {
        return gp;
    }

    @Override
    public void run() {
//        this.logger.debug("Analyzing fen " + board.getBoard().toString());
        this.stopped = false;
        this.canStop = false;
        this.bestResult = new Result();

        // Go...
        this.semaphore.release();
        Result moveResult = getBestMove();

        // Send the result
        if (moveResult.bestMove != Result.NOMOVE) {
            GenericMove bestMove = null;
            this.protocol.send(new ProtocolBestMoveCommand(bestMove, null));
        } else {
            this.protocol.send(new ProtocolBestMoveCommand(null, null));
        }

        // Cleanup manually
//        this.transpositionTable = null;
//        this.info = null;
//        this.evaluation = null;
    }

    public static GenericMove toCommandMove(int move) {
        return null;
    }

    private Result getBestMove() {
        return null;
    }

    @Override
    public void start() {
        this.thread.start();
        try {
            // Wait for initialization
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            this.logger.debug(e.getMessage());
            // Do nothing
        }
    }

    @Override
    public void stop() {
        this.stopped = true;
        this.canStop = true;
        try {
            // Wait for the thread to die
            this.thread.join();
        } catch (InterruptedException e) {
            this.logger.debug(e.getMessage());
            // Do nothing
        }
    }

//    @Override
//    public void ponderhit() {
//        // 
//    }
    @Override
    public boolean isStopped() {
        return !this.thread.isAlive();
    }

}
