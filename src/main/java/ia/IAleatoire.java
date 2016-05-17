package ia;

import java.util.ArrayList;
import java.util.Random;
import position.GCoups;

public class IAleatoire {

    private final Random randomGenerator;

    public IAleatoire() {
        randomGenerator = new Random();
    }

    public GCoups evaluate(ArrayList<GCoups> coupsvalides) {

        int randomInt = randomGenerator.nextInt(coupsvalides.size());
        GCoups gcoups = coupsvalides.get(randomInt);

        return gcoups;
    }

    public short evaluate(short[] allMoves) {
        int randomInt = randomGenerator.nextInt(allMoves.length);
        short move = allMoves[randomInt];
        return move;
    }

}
