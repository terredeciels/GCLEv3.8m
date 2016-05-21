package gcle;

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

public class GCommandLine implements ICodage {

    public static void main(String[] args) throws IOException {
        while (true) {
            String[] input = new String[]{""};
            System.out.println("Enter command : ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            input[0] = s;
            parse(input);
        }
    }

    private static void parse(String[] in) throws IOException {
        // create Options object
        Options options = new Options();
        // add q option
        options.addOption("q", false, "quit gcle");
        options.addOption("gui", false, "launch gui");
        options.addOption("gw", false, "launch new game with white");
        options.addOption("gb", false, "launch new game with black");
        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse(options, in);
            if (cmd.hasOption("q")) {
                System.out.println("bye !");
                System.exit(0);
            } else if (cmd.hasOption("gui")) {
                String fen = ICodage.FEN_INITIALE;
                GPosition gp = FenToGPosition.toGPosition(fen);
                GameView gameview = new GameView(gp);
                ChessGui gui = new ChessGui(gameview);
                gui.setGuiPosition();
            } else if (cmd.hasOption("gw")) {
                System.out.println("Enter move : ");
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                String coupsS = bufferRead.readLine();
                try {
                    GCoups gcoups = toCase(coupsS);
                    System.out.println(GCoups.getString(gcoups));
                    //verifier si coups valide
                    //ajouter propriétés du coups

                } catch (IllegalMoveException ex) {
                }

            } else if (cmd.hasOption("gb")) {
                Gcle.main(null);
            }

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }

    private static GCoups toCase(String coupsS) throws IllegalMoveException {
        String[] coupsT = coupsS.split("");
        int colO = STRING_COL.indexOf(coupsS.charAt(0));
        int rgO = Integer.parseInt(coupsT[1]);
        int colX = STRING_COL.indexOf(coupsS.charAt(2));
        int rgX =  Integer.parseInt(coupsT[3]);
        int caseO = CASES117[colO + 8 * (rgO - 1)];
        int caseX = CASES117[colX + 8 * (rgX - 1)];

        return new GCoups(caseO, caseX);
    }
}
