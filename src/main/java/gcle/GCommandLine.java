package gcle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class GCommandLine {

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("Enter move : ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            String[] input = new String[1];
            input[0] = s;
            parse(input);
        }
    }

    static void parse(String[] args) {
        // create Options object
        Options options = new Options();

        // add t option
        options.addOption("q", false, "quit gcle");
        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("q")) {
                System.out.println("bye !");
                System.exit(0);
            }

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }
}
