package gcle.main;

import com.googlecode.jctree.NodeNotFoundException;
import gcle.GGameConsole;
import gcle.IllegalMoveException;
import java.io.IOException;
import position.ICodage;

public class GcleConsole {

    public static boolean DEBUG=true;

    /*
    @TODO exceptions 
     */
 /*
    @TODO tests 
     */
    public static void main(String[] args) throws IOException, NodeNotFoundException, IllegalMoveException {
      new GGameConsole();
    }

}
