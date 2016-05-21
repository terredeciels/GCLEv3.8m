package gcle;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineDebugCommand;
import com.fluxchess.jcpi.commands.EngineInitializeRequestCommand;
import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.fluxchess.jcpi.commands.EnginePonderHitCommand;
import com.fluxchess.jcpi.commands.EngineReadyRequestCommand;
import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.commands.EngineStopCalculatingCommand;
import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.commands.ProtocolInitializeAnswerCommand;
import com.fluxchess.jcpi.commands.ProtocolReadyAnswerCommand;
import com.fluxchess.jcpi.protocols.IProtocolHandler;
import com.googlecode.jctree.NodeNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import position.GCoups;
import position.GPosition;
import position.ICodage;

public class Gcle extends AbstractEngine {

    public final static boolean DEBUG = true;
    private ISearch search;
    private GPosition gp;

    public Gcle() {
        // Set the protocol
//        ChessLogger.setProtocol(getProtocol());
        initialize();
    }

    public Gcle(IProtocolHandler handler) {
        super(handler);
        // Set the protocol
//        ChessLogger.setProtocol(handler);
        initialize();
    }

    public static void main(String[] args) {
//        String fen = ICodage.FEN_INITIALE;
//        GPosition gp = FenToGPosition.toGPosition(fen);
//        GameView gameview = new GameView(gp);
//        ChessGui gui = new ChessGui(gameview);
//        gui.setGuiPosition();

        new Gcle().run();
    }

    private void initialize() {
        // Create a new search
        String f = "r3k2r/8/8/8/8/8/8/4K3 w kq - 0 1";
        f = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";
        f = ICodage.FEN_INITIALE;
        f = "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1";
        f = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";

        // new GCommandLine();
        search = new Search(f, getProtocol());
        try {
            GCoups coups = search.getMeilleurCoups();
            System.out.println(GCoups.getString(coups));
        } catch (NodeNotFoundException ex) {

        }
    }

    @Override
    protected void quit() {
        // Stop calculating
        new EngineStopCalculatingCommand().accept(this);
    }

    @Override
    public void receive(EngineInitializeRequestCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }

        // Stop calculating
        new EngineStopCalculatingCommand().accept(this);

        initialize();

        // Send the initialization commands
        ProtocolInitializeAnswerCommand initializeCommand
                = new ProtocolInitializeAnswerCommand("GCLE", "Auteur inconnu");
//        for (AbstractOption option : Configuration.options) {
//            initializeCommand.addOption(option);
//        }
        getProtocol().send(initializeCommand);
    }

    @Override
    public void receive(EngineSetOptionCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        //
    }

    @Override
    public void receive(EngineDebugCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        ProtocolInformationCommand infoCommand = new ProtocolInformationCommand();
        if (command.debug) {
            infoCommand.setString("Turning on debugging mode");
        } else {
            infoCommand.setString("Turning off debugging mode");
        }
        getProtocol().send(infoCommand);
    }

    @Override
    public void receive(EngineReadyRequestCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        // Send a pong back
        getProtocol().send(new ProtocolReadyAnswerCommand(command.token));
    }

    @Override
    public void receive(EngineNewGameCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        // Stop calculating
        new EngineStopCalculatingCommand().accept(this);
    }

    @Override
    public void receive(EngineAnalyzeCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        if (!this.search.isStopped()) {
            this.search.stop();
        }

        // Create a new board
//        this.board = new Hex88Board(command.board);
        // Make all moves
//        List<GenericMove> moveList = command.moves;
//        for (GenericMove move : moveList) {
//            int newMove = IntMove.convertMove(move, this.board);
//            this.board.makeMove(newMove);
//        }
    }

    @Override
    public void receive(EngineStartCalculatingCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        // + parametres de search
        this.search.start();
    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        if (!this.search.isStopped()) {
            // Stop the search
            this.search.stop();
        }
//          else {
//            ChessLogger.getLogger().debug("There is no search active.");
//        }
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
//        if (!search.isStopped()) {
//            search.ponderhit();
//        }
//         else {
//            ChessLogger.getLogger().debug("There is no search active.");
//        }
    }

}
