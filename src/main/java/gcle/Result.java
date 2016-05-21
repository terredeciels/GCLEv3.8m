package gcle;

class Result {

    public static final int NOMOVE = -8;
    public static final int NOVALUE = -9;
    public static final int INFINITY = 200000;
    public int bestMove = NOMOVE;
    public int ponderMove = NOMOVE;
    public int value = NOVALUE;
    public int resultValue = -INFINITY;
    public long time = -1;
    public int moveNumber = 0;
    public int depth = 0;

    /**
     * Creates a new Result.
     */
    public Result() {
    }
}
