package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import ia.IA;
import java.util.concurrent.Semaphore;
import ouverture.Ouverture;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;

public class Search implements ICodage, ISearch, Runnable {

    /*
    TODO Thread
     */
    private final Thread thread = new Thread(this);
    private final Semaphore semaphore = new Semaphore(0);
    
    private final GPosition gp;
    private final IA ia;
    private final int depth = 1;
    private boolean end_opening;
    private boolean stopped;
    private GCoups lastmove;
    private int halfmove;

    public Search(GGame game) {
        halfmove = game.halfmove - 1;
        lastmove = game.listecoupspartie[halfmove];
        gp = game.gp;
        ia = new IA(gp, depth);
    }

    public Search(String f) {

        gp = FenToGPosition.toGPosition(f);
        ia = new IA(gp, depth);
    }

    @Override
    public final GCoups getMeilleurCoups() throws NodeNotFoundException {
        Ouverture ouv = new Ouverture();
        if (!end_opening) {
            GCoups next_coups = ouv.searchNextMoveInTree(halfmove, lastmove);
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
        stopped = false;

        // Go...
        this.semaphore.release();
//        Result moveResult = getBestMove();

    }

    @Override
    public void start() {
        thread.start();
        try {
            this.semaphore.acquire(); // ??
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void stop() {
        stopped = true;
        try {
            // Wait for the thread to die
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public boolean isStopped() {
        return !thread.isAlive();
    }

}
