package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import position.GCoups;

public interface ISearch {

    void start();

    void stop();

//    void ponderhit();
    boolean isStopped();
    
    GCoups getMeilleurCoups() throws NodeNotFoundException;// ??

//    public abstract void setSearchDepth(int searchDepth);
//
//    public abstract void setSearchNodes(long searchNodes);
//
//    public abstract void setSearchTime(long searchTime);
//
//    public abstract void setSearchClock(int side, long timeLeft);
//
//    public abstract void setSearchClockIncrement(int side, long timeIncrement);
//
//    public abstract void setSearchMovesToGo(int searchMovesToGo);
//
//    public abstract void setSearchInfinite();
//
//    public abstract void setSearchPonder();
//
//    public abstract void setSearchMoveList(List<GenericMove> moveList);

    //    public Search(GPositionEvaluator evaluation, IProtocol protocol) {
    //        this.protocol = protocol;
    //    }
    
}
