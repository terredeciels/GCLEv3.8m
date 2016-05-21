package gcle;

import com.fluxchess.jcpi.commands.IProtocol;
import com.fluxchess.jcpi.commands.ProtocolInformationCommand;

class ChessLogger {
// Singleton Pattern

    private static final ChessLogger INSTANCE = new ChessLogger();
    private static final IProtocol PROTOCOL = null;
    private static final boolean DEBUG = false;

    private ChessLogger() {
    }

    public static ChessLogger getLogger() {
        return INSTANCE;
    }

    public void debug(String information) {
        if (DEBUG && PROTOCOL != null) {
            ProtocolInformationCommand command = new ProtocolInformationCommand();
            command.setString(information);
            PROTOCOL.send(command);
        }
    }
}
