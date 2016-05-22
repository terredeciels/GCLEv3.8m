package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import position.GCoups;
import position.GPosition;

public interface ISearch {

    void start();

    void stop();

    boolean isStopped();

    GCoups getMeilleurCoups() throws NodeNotFoundException;// ??

}
