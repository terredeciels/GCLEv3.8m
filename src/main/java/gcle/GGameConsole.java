package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import gui.ChessGui;
import gui.GameView;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;
import static position.ICodage.*;

public class GGameConsole extends GGame implements ICodage {

    /*
    @TODO tests console
     */
    private boolean end_of_game;
    private int human_color;
    private String fen;
    private CommandLine cmd;
    private  Scanner console;
    private Options options;
    private CommandLineParser parser;

    public GGameConsole() throws IOException, NodeNotFoundException, IllegalMoveException {
        end_of_game = false;
        fen = ICodage.FEN_INITIALE;
        gp = FenToGPosition.toGPosition(fen);
        console = new Scanner(System.in);
        System.out.println("Enter command : ");
        while (console.hasNextLine()) {
            String[] input = new String[]{""};
            String cons = console.next();
            input[0] = cons;
            parse(input);
        }
    }
     public GGameConsole(GPosition gp) {
//        end_of_game = false;
        this.gp=gp;
//        fen = ICodage.FEN_INITIALE;
//        gp = FenToGPosition.toGPosition(fen);
//        console = new Scanner(System.in);
//        System.out.println("Enter command : ");
//        while (console.hasNextLine()) {
//            String[] input = new String[]{""};
//            String cons = console.next();
//            input[0] = cons;
//            parse(input);
//        }
    }

    private void parse(String[] in) throws IOException, NodeNotFoundException, IllegalMoveException {

        options = new Options();

        options.addOption("q", false, "quit gcle");
        options.addOption("gui", false, "launch gui");
        options.addOption("g", false, "launch auto-play");
        options.addOption("gw", false, "launch new game with white");
        options.addOption("gb", false, "launch new game with black");

        options.addOption("fen", true, "get fen position");

        parser = new DefaultParser();

        try {
            cmd = parser.parse(options, in);
            if (cmd.hasOption("q")) {
                exit();
            } else if (cmd.hasOption("gui")) {
                gui();
            } else if (cmd.hasOption("gw")) {//humain a blancs
                human_color = BLANC;
                play();
            } else if (cmd.hasOption("gb")) {//humain a noirs
                human_color = NOIR;
                play();
            } else if (cmd.hasOption("g")) {//ordi a blancs et noirs
                //auto-play
            } else if (cmd.hasOption("fen")) { //recuperer second arg (fen)
                String f = cmd.getOptionValue("fen");
                if (f != null) {
                    fen = f;
                    gp = FenToGPosition.toGPosition(fen);
                }
            }

        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }

    public void play() throws IOException, NodeNotFoundException, IllegalMoveException, ParseException {

        while (!end_of_game) {
            if (gp.getTrait() == human_color) {
                System.out.println("Enter move : ");
                String coupsS = console.next(); // consume the valid token
                String[] input = new String[]{""};
                input[0] = coupsS;
                cmd = parser.parse(options, input);
                if (!cmd.hasOption("q")) {
                    GCoups coups = toCase(coupsS);
                    if (isInputValidMove(coups)) {
                        playHuman();
                    }
                } else {
//                    end_of_game = true;
                    exit();
                }
            } else {
                playEngine();
                System.out.println(gcoups_curr);
            }
        }
        /*
       @TODO end_of_game
         */
    }

    private boolean isInputValidMove(GCoups coups) {

        Iterator<GCoups> it = gp.getCoupsValides().iterator();
        while (it.hasNext()) {
            GCoups c = it.next();
            if (c.getCaseO() == coups.getCaseO() && c.getCaseX() == coups.getCaseX()) {
                this.gcoups = c;
                return true;
            }
        }

        return false;
        /*
        @TODO traiter coups console invalide
         */

    }

    public GCoups toCase(String coupsS) throws IllegalMoveException {
        String[] coupsT = coupsS.split("");
        int colO = STRING_COL.indexOf(coupsS.charAt(0));
        int rgO = Integer.parseInt(coupsT[1]);
        int colX = STRING_COL.indexOf(coupsS.charAt(2));
        int rgX = Integer.parseInt(coupsT[3]);
        int caseO = CASES117[colO + 8 * (rgO - 1)];
        int caseX = CASES117[colX + 8 * (rgX - 1)];

        return new GCoups(caseO, caseX);
    }

    private void exit() {
        System.out.println("bye !");
        System.exit(0);
    }

    private void gui() {
        gp = FenToGPosition.toGPosition(fen);
        GameView gameview = new GameView(gp);
        ChessGui gui = new ChessGui(gameview);
        gui.setGuiPosition();
    }
}
