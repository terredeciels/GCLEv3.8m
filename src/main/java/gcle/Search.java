package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import ia.IA;
import java.util.concurrent.Semaphore;
import ouverture.Ouverture;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;
import static position.ICodage.TYPE_DE_COUPS.Deplacement;

public class Search implements ICodage, ISearch, Runnable {

    private final Thread thread = new Thread(this);
    private final Semaphore semaphore = new Semaphore(0);
    private final GPosition gp;
    private final IA ia;
    private final int depth = 1;
    private boolean end_opening;
    private boolean stopped;

    public Search(GPosition gp) {
        this.gp = gp;
        this.ia = new IA(gp, depth);
    }

    public Search(String f) {

        this.gp = FenToGPosition.toGPosition(f);
        this.ia = new IA(gp, depth);
    }

    @Override
    public final GCoups getMeilleurCoups() throws NodeNotFoundException {
        //ouvertures
        Ouverture ouv = new Ouverture();
        /*
        @TODO count moves
         */
        GCoups lastmove = new GCoups(PION, e2, e4, 0, Deplacement);
        if (!end_opening) {
            GCoups next_coups = ouv.searchNextMoveInTree(lastmove);
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
        this.stopped = false;

        // Go...
        this.semaphore.release();
//        Result moveResult = getBestMove();

    }

    @Override
    public void start() {
        this.thread.start();
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
            this.thread.join();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public boolean isStopped() {
        return !this.thread.isAlive();
    }

}
