package gcle;

import com.googlecode.jctree.NodeNotFoundException;
import gui.ChessGui;
import gui.GameView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import position.FenToGPosition;
import position.GCoups;
import position.ICodage;
import static position.ICodage.BLANC;
import static position.ICodage.CASES117;
import static position.ICodage.NOIR;
import static position.ICodage.STRING_COL;

public class GGameConsole extends GGame implements ICodage {

    private boolean end_of_game;
    private int human_color;
    private String fen;

    private CommandLine cmd;
//    private BufferedReader bufferRead;
    private final Scanner console;
    private Options options;
    private CommandLineParser parser;

    public GGameConsole() throws IOException, NodeNotFoundException, IllegalMoveException {
        end_of_game = false;
        fen = ICodage.FEN_INITIALE;
        gp = FenToGPosition.toGPosition(fen);
        console = new Scanner(System.in);
        System.out.println("Enter command : ");
//        Scanner lineTokenizer;
        while (console.hasNextLine()) {

//            lineTokenizer = new Scanner(console.nextLine());
            String[] input = new String[]{""};

            String cons;
//            if (lineTokenizer.hasNext()) {
            cons = console.next(); // consume the valid token
//            } 
//            else {
//                System.out.printf("syntax error ...");
//                continue;  // proceed to the next line of input
//            }
//            bufferRead = new BufferedReader(new InputStreamReader(System.in));
//            String cons = bufferRead.readLine();
            input[0] = cons;
            parse(input);
//            lineTokenizer.close();
        }
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
//                String coupsS = bufferRead.readLine();
                String coupsS;
//            if (console.hasNext()) {
                coupsS = console.next(); // consume the valid token
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

//            } 
//        else {
//                System.out.printf("syntax error ...");
//            }
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
