package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import gui.ChessGui;
import gui.GameView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;

public class GcleConsole implements ICodage {

    static BufferedReader bufferRead;

    public static void main(String[] args) throws IOException, NodeNotFoundException, IllegalMoveException {
        String[] input = new String[]{""};
        System.out.println("Enter command : ");
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String cons = bufferRead.readLine();
        input[0] = cons;
        parse(input);
    }

    private static void parse(String[] in) throws IOException, NodeNotFoundException, IllegalMoveException {

        Options options = new Options();

        options.addOption("q", false, "quit gcle");
        options.addOption("gui", false, "launch gui");
        options.addOption("g", false, "launch auto-play");
        options.addOption("gw", false, "launch new game with white");
        options.addOption("gb", false, "launch new game with black");

        options.addOption("fen", true, "get fen position");

        CommandLineParser parser = new DefaultParser();
        String fen = ICodage.FEN_INITIALE;
        try {
            CommandLine cmd = parser.parse(options, in);
            if (cmd.hasOption("q")) {
                System.out.println("bye !");
                System.exit(0);
            } else if (cmd.hasOption("gui")) {
                GPosition gp = FenToGPosition.toGPosition(fen);
                GameView gameview = new GameView(gp);
                ChessGui gui = new ChessGui(gameview);
                gui.setGuiPosition();
            } else if (cmd.hasOption("gw")) {//humain a blancs
                new GGameConsole(BLANC, fen).play();
            } else if (cmd.hasOption("gb")) {//humain a noirs
                new GGameConsole(NOIR, fen).play();
            } else if (cmd.hasOption("g")) {//ordi a blancs et noirs
                //auto-play
            } else if (cmd.hasOption("fen")) {//recuperer second arg (fen)
            }

        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }

    static GCoups toCase(String coupsS) throws IllegalMoveException {
        String[] coupsT = coupsS.split("");
        int colO = STRING_COL.indexOf(coupsS.charAt(0));
        int rgO = Integer.parseInt(coupsT[1]);
        int colX = STRING_COL.indexOf(coupsS.charAt(2));
        int rgX = Integer.parseInt(coupsT[3]);
        int caseO = CASES117[colO + 8 * (rgO - 1)];
        int caseX = CASES117[colX + 8 * (rgX - 1)];

        return new GCoups(caseO, caseX);
    }

}
