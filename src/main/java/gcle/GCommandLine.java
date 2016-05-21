package gcle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import position.GCoups;
import position.ICodage;

public class GCommandLine implements ICodage {

    public static void main(String[] args) throws IOException {
        while (true) {
            String[] input = new String[]{""};
            System.out.println("Enter move : ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            input[0] = s;
            parse(input);
        }
    }

    private static void parse(String[] in) {
        // create Options object
        Options options = new Options();
        // add q option
        options.addOption("q", false, "quit gcle");
        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments

            CommandLine cmd = parser.parse(options, in);
            if (cmd.hasOption("q")) {
                System.out.println("bye !");
                System.exit(0);
            }

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

        try {
            GCoups gcoups = toCase(in);
               //verifier si coups valide
            //ajouter propriétés du coups
            
         

        } catch (IllegalMoveException ex) {

        }

    }

    private static GCoups toCase(String[] in) throws IllegalMoveException {
        int colO = STRING_COL.indexOf(in[0]);
        int rgO = Integer.valueOf(in[1]);
        int colX = STRING_COL.indexOf(in[2]);
        int rgX = Integer.valueOf(in[3]);
        int caseO = CASES117[colO + 8 * (rgO - 1)];
        int caseX = CASES117[colX + 8 * (rgX - 1)];

        return new GCoups(caseO, caseX);
    }
}
