package ia;

public class GPositionEvaluator {

    public static final int SIDE_BLACK = 1;
    public static final int SIDE_WHITE = 0;
    public static final int PAWN = 0;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 4;
    public static final int ROOK = 6;
    public static final int QUEEN = 8;
    public static final int KING = 10;
    public static final int WHITE_PAWN = PAWN + SIDE_WHITE;
    public static final int WHITE_KNIGHT = KNIGHT + SIDE_WHITE;
    public static final int WHITE_BISHOP = BISHOP + SIDE_WHITE;
    public static final int WHITE_ROOK = ROOK + SIDE_WHITE;
    public static final int WHITE_QUEEN = QUEEN + SIDE_WHITE;
    public static final int WHITE_KING = KING + SIDE_WHITE;
    public static final int BLACK_PAWN = PAWN + SIDE_BLACK;
    public static final int BLACK_KNIGHT = KNIGHT + SIDE_BLACK;
    public static final int BLACK_BISHOP = BISHOP + SIDE_BLACK;
    public static final int BLACK_ROOK = ROOK + SIDE_BLACK;
    public static final int BLACK_QUEEN = QUEEN + SIDE_BLACK;
    public static final int BLACK_KING = KING + SIDE_BLACK;
    public static final int EMPTY_SQUARE = 12;
    public static final int ALL_PIECES = 12;
    public static final int ALL_BITBOARDS = 14;
    public static final int CASTLE_KINGSIDE = 0;
    public static final int CASTLE_QUEENSIDE = 2;
    int MaxPawnFileBins[];
    int MaxPawnColorBins[];
    int MaxTotalPawns;
    int PawnRams;
    int MaxMostAdvanced[];
    int MaxPassedPawns[];
    int MinPawnFileBins[];
    int MinMostBackward[];
    private long SquareBits[];
    private static final int[] PIECE_VALUES;
    private final int MaterialValue[];
    private final int NumPawns[];
    private final long BitBoards[];
    private final boolean HasCastled[];
    private final boolean CastlingStatus[];

    private static final int GRAIN = 3;

    static {
        PIECE_VALUES = new int[ALL_PIECES];
        PIECE_VALUES[WHITE_PAWN] = 100;
        PIECE_VALUES[BLACK_PAWN] = 100;
        PIECE_VALUES[WHITE_KNIGHT] = 300;
        PIECE_VALUES[BLACK_KNIGHT] = 300;
        PIECE_VALUES[WHITE_BISHOP] = 350;
        PIECE_VALUES[BLACK_BISHOP] = 350;
        PIECE_VALUES[WHITE_ROOK] = 500;
        PIECE_VALUES[BLACK_ROOK] = 500;
        PIECE_VALUES[BLACK_QUEEN] = 900;
        PIECE_VALUES[WHITE_QUEEN] = 900;
        PIECE_VALUES[WHITE_KING] = 2000;
        PIECE_VALUES[BLACK_KING] = 2000;
    }

    public GPositionEvaluator() {
        MaxPawnFileBins = new int[8];
        MaxPawnColorBins = new int[2];
        MaxMostAdvanced = new int[8];
        MaxPassedPawns = new int[8];
        MinPawnFileBins = new int[8];
        MinMostBackward = new int[8];
        BitBoards = new long[ALL_BITBOARDS];
        CastlingStatus = new boolean[4];
        HasCastled = new boolean[2];

        NumPawns = new int[2];
        MaterialValue = new int[2];
    }

    public int EvaluateQuickie(int fromWhosePerspective) {
        return ((EvalMaterial(fromWhosePerspective) >> GRAIN) << GRAIN);
    }

    public int EvaluateComplete(int fromWhosePerspective) {
        AnalyzePawnStructure(fromWhosePerspective);

        return (((EvalMaterial(fromWhosePerspective)
                + EvalPawnStructure(fromWhosePerspective)
                + EvalBadBishops(fromWhosePerspective)
                + EvalDevelopment(fromWhosePerspective)
                + EvalRookBonus(fromWhosePerspective)
                + EvalKingTropism(fromWhosePerspective)) >> GRAIN) << GRAIN);
    }

    private int EvalKingTropism(int fromWhosePerspective) {
        int score = 0;

        // Square coordinates
        int kingRank = 0, kingFile = 0;
        int pieceRank = 0, pieceFile = 0;

        if (fromWhosePerspective == SIDE_WHITE) {
            // Look for enemy king first!
            for (int i = 0; i < 64; i++) {
                if (FindBlackPiece(i) == BLACK_KING) {
                    kingRank = i >> 8;
                    kingFile = i % 8;
                    break;
                }
            }

            // Now, look for pieces which need to be evaluated
            for (int i = 0; i < 64; i++) {
                pieceRank = i >> 8;
                pieceFile = i % 8;

                switch (FindWhitePiece(i)) {

                    case WHITE_ROOK:
                        score -= (Math.min(Math.abs(kingRank - pieceRank),
                                Math.abs(kingFile - pieceFile)) << 1);
                        break;
                    case WHITE_KNIGHT:
                        score += 5 - Math.abs(kingRank - pieceRank)
                                - Math.abs(kingFile - pieceFile);
                        break;
                    case WHITE_QUEEN:
                        score -= Math.min(Math.abs(kingRank - pieceRank),
                                Math.abs(kingFile - pieceFile));
                        break;
                    default:
                        break;
                }
            }
        } else {
            // Look for enemy king first!
            for (int i = 0; i < 64; i++) {
                if (FindWhitePiece(i) == WHITE_KING) {
                    kingRank = i >> 8;
                    kingFile = i % 8;
                    break;
                }
            }

            // Now, look for pieces which need to be evaluated
            for (int i = 0; i < 64; i++) {
                pieceRank = i >> 8;
                pieceFile = i % 8;
                switch (FindBlackPiece(i)) {
                    case BLACK_ROOK:
                        score -= (Math.min(Math.abs(kingRank - pieceRank),
                                Math.abs(kingFile - pieceFile)) << 1);
                        break;
                    case BLACK_KNIGHT:
                        score += 5 - Math.abs(kingRank - pieceRank)
                                - Math.abs(kingFile - pieceFile);
                        break;
                    case BLACK_QUEEN:
                        score -= Math.min(Math.abs(kingRank - pieceRank),
                                Math.abs(kingFile - pieceFile));
                        break;
                    default:
                        break;
                }
            }
        }
        return score;
    }

    private int EvalRookBonus(int fromWhosePerspective) {
        long rookboard = GetBitBoard(ROOK + fromWhosePerspective);
        if (rookboard == 0) {
            return 0;
        }

        int score = 0;
        for (int square = 0; square < 64; square++) {
            // Find a rook
            if ((rookboard & SquareBits[square]) != 0) {
                // Is this rook on the seventh rank?
                int rank = (square >> 3);
                int file = (square % 8);
                if ((fromWhosePerspective == SIDE_WHITE)
                        && (rank == 1)) {
                    score += 22;
                }
                if ((fromWhosePerspective == SIDE_BLACK)
                        && (rank == 7)) {
                    score += 22;
                }

                // Is this rook on a semi- or completely open file?
                if (MaxPawnFileBins[file] == 0) {
                    if (MinPawnFileBins[file] == 0) {
                        score += 10;
                    } else {
                        score += 4;
                    }
                }

                // Is this rook behind a passed pawn?
                if ((fromWhosePerspective == SIDE_WHITE)
                        && (MaxPassedPawns[file] < square)) {
                    score += 25;
                }
                if ((fromWhosePerspective == SIDE_BLACK)
                        && (MaxPassedPawns[file] > square)) {
                    score += 25;
                }

                rookboard ^= SquareBits[square];
                if (rookboard == 0) {
                    break;
                }
            }
        }
        return score;
    }

    private int EvalDevelopment(int fromWhosePerspective) {
        int score = 0;

        if (fromWhosePerspective == SIDE_WHITE) {
            // Has the machine advanced its center pawns?
            if (FindWhitePiece(51) == WHITE_PAWN) {
                score -= 15;
            }
            if (FindWhitePiece(52) == WHITE_PAWN) {
                score -= 15;
            }

            // Penalize bishops and knights on the back rank
            for (int square = 56; square < 64; square++) {
                if ((FindWhitePiece(square) == WHITE_KNIGHT)
                        || (FindWhitePiece(square) == WHITE_BISHOP)) {
                    score -= 10;
                }
            }

            // Penalize too-early queen movement
            long queenboard = GetBitBoard(WHITE_QUEEN);
            if ((queenboard != 0) && ((queenboard & SquareBits[59]) == 0)) {
                // First, count friendly pieces on their original squares
                int cnt = 0;
                if ((GetBitBoard(WHITE_BISHOP) & SquareBits[58]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(WHITE_BISHOP) & SquareBits[61]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(WHITE_KNIGHT) & SquareBits[57]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(WHITE_KNIGHT) & SquareBits[62]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(WHITE_ROOK) & SquareBits[56]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(WHITE_ROOK) & SquareBits[63]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(WHITE_KING) & SquareBits[60]) != 0) {
                    cnt++;
                }
                score -= (cnt << 3);
            }

            // And finally, incite castling when the enemy has a queen on the board
            // This is a slightly simpler version of a factor used by Cray Blitz
            if (GetBitBoard(BLACK_QUEEN) != 0) {
                // Being castled deserves a bonus
                if (GetHasCastled(SIDE_WHITE)) {
                    score += 10;
                } // small penalty if you can still castle on both sides
                else if (GetCastlingStatus(SIDE_WHITE + CASTLE_QUEENSIDE)
                        && GetCastlingStatus(SIDE_WHITE + CASTLE_QUEENSIDE)) {
                    score -= 24;
                } // bigger penalty if you can only castle kingside
                else if (GetCastlingStatus(SIDE_WHITE + CASTLE_KINGSIDE)) {
                    score -= 40;
                } // bigger penalty if you can only castle queenside
                else if (GetCastlingStatus(SIDE_WHITE + CASTLE_QUEENSIDE)) {
                    score -= 80;
                } // biggest penalty if you can't castle at all
                else {
                    score -= 120;
                }
            }
        } else // from black's perspective
        {
            // Has the machine advanced its center pawns?
            if (FindBlackPiece(11) == BLACK_PAWN) {
                score -= 15;
            }
            if (FindBlackPiece(12) == BLACK_PAWN) {
                score -= 15;
            }

            // Penalize bishops and knights on the back rank
            for (int square = 0; square < 8; square++) {
                if ((FindBlackPiece(square) == BLACK_KNIGHT)
                        || (FindBlackPiece(square) == BLACK_BISHOP)) {
                    score -= 10;
                }
            }

            // Penalize too-early queen movement
            long queenboard = GetBitBoard(BLACK_QUEEN);
            if ((queenboard != 0) && ((queenboard & SquareBits[3]) == 0)) {
                // First, count friendly pieces on their original squares
                int cnt = 0;
                if ((GetBitBoard(BLACK_BISHOP) & SquareBits[2]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(BLACK_BISHOP) & SquareBits[5]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(BLACK_KNIGHT) & SquareBits[1]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(BLACK_KNIGHT) & SquareBits[6]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(BLACK_ROOK) & SquareBits[0]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(BLACK_ROOK) & SquareBits[7]) != 0) {
                    cnt++;
                }
                if ((GetBitBoard(BLACK_KING) & SquareBits[4]) != 0) {
                    cnt++;
                }
                score -= (cnt << 3);
            }

            if (GetBitBoard(WHITE_QUEEN) != 0) {
                // Being castled deserves a bonus
                if (GetHasCastled(SIDE_BLACK)) {
                    score += 10;
                } // small penalty if you can still castle on both sides
                else if (GetCastlingStatus(SIDE_BLACK + CASTLE_QUEENSIDE)
                        && GetCastlingStatus(SIDE_BLACK + CASTLE_QUEENSIDE)) {
                    score -= 24;
                } // bigger penalty if you can only castle kingside
                else if (GetCastlingStatus(SIDE_BLACK + CASTLE_KINGSIDE)) {
                    score -= 40;
                } // bigger penalty if you can only castle queenside
                else if (GetCastlingStatus(SIDE_BLACK + CASTLE_QUEENSIDE)) {
                    score -= 80;
                } // biggest penalty if you can't castle at all
                else {
                    score -= 120;
                }
            }
        }
        return score;
    }

    private int EvalBadBishops(int fromWhosePerspective) {
        long where = GetBitBoard(BISHOP + fromWhosePerspective);
        if (where == 0) {
            return 0;
        }

        int score = 0;
        for (int square = 0; square < 64; square++) {
            // Find a bishop
            if ((where & SquareBits[square]) != 0) {
                // What is the bishop's square color?
                int rank = (square >> 3);
                int file = (square % 8);
                if ((rank % 2) == (file % 2)) {
                    score -= (MaxPawnColorBins[0] << 3);
                } else {
                    score -= (MaxPawnColorBins[1] << 3);
                }

                // Use the bitboard erasure trick to avoid looking for additional
                // bishops once they have all been seen
                where ^= SquareBits[square];
                if (where == 0) {
                    break;
                }
            }
        }
        return score;
    }

    private int EvalPawnStructure(int fromWhosePerspective) {
        int score = 0;

        // First, look for doubled pawns
        // In chess, two or more pawns on the same file usually hinder each other,
        // so we assign a minor penalty
        for (int bin = 0; bin < 8; bin++) {
            if (MaxPawnFileBins[bin] > 1) {
                score -= 8;
            }
        }

        // Now, look for an isolated pawn, i.e., one which has no neighbor pawns
        // capable of protecting it from attack at some point in the future
        if ((MaxPawnFileBins[0] > 0) && (MaxPawnFileBins[1] == 0)) {
            score -= 15;
        }
        if ((MaxPawnFileBins[7] > 0) && (MaxPawnFileBins[6] == 0)) {
            score -= 15;
        }
        for (int bin = 1; bin < 7; bin++) {
            if ((MaxPawnFileBins[bin] > 0) && (MaxPawnFileBins[bin - 1] == 0)
                    && (MaxPawnFileBins[bin + 1] == 0)) {
                score -= 15;
            }
        }

        // Assign a small penalty to positions in which Max still has all of his
        // pawns; this incites a single pawn trade (to open a file), but not by
        // much
        if (MaxTotalPawns == 8) {
            score -= 10;
        }

        // Penalize pawn rams, because they restrict movement
        score -= 8 * PawnRams;

        // Finally, look for a passed pawn; i.e., a pawn which can no longer be
        // blocked or attacked by a rival pawn
        if (fromWhosePerspective == SIDE_WHITE) {
            if (MaxMostAdvanced[0] < Math.min(MinMostBackward[0], MinMostBackward[1])) {
                score += (8 - (MaxMostAdvanced[0] >> 3))
                        * (8 - (MaxMostAdvanced[0] >> 3));
            }
            if (MaxMostAdvanced[7] < Math.min(MinMostBackward[7], MinMostBackward[6])) {
                score += (8 - (MaxMostAdvanced[7] >> 3))
                        * (8 - (MaxMostAdvanced[7] >> 3));
            }
            for (int i = 1; i < 7; i++) {
                if ((MaxMostAdvanced[i] < MinMostBackward[i])
                        && (MaxMostAdvanced[i] < MinMostBackward[i - 1])
                        && (MaxMostAdvanced[i] < MinMostBackward[i + 1])) {
                    score += (8 - (MaxMostAdvanced[i] >> 3))
                            * (8 - (MaxMostAdvanced[i] >> 3));
                }
            }
        } else // from Black's perspective
        {
            if (MaxMostAdvanced[0] > Math.max(MinMostBackward[0], MinMostBackward[1])) {
                score += (MaxMostAdvanced[0] >> 3)
                        * (MaxMostAdvanced[0] >> 3);
            }
            if (MaxMostAdvanced[7] > Math.max(MinMostBackward[7], MinMostBackward[6])) {
                score += (MaxMostAdvanced[7] >> 3)
                        * (MaxMostAdvanced[7] >> 3);
            }
            for (int i = 1; i < 7; i++) {
                if ((MaxMostAdvanced[i] > MinMostBackward[i])
                        && (MaxMostAdvanced[i] > MinMostBackward[i - 1])
                        && (MaxMostAdvanced[i] > MinMostBackward[i + 1])) {
                    score += (MaxMostAdvanced[i] >> 3)
                            * (MaxMostAdvanced[i] >> 3);
                }
            }
        }

        return score;
    }

    private boolean AnalyzePawnStructure(int fromWhosePerspective) {
        // Reset the counters
        for (int i = 0; i < 8; i++) {
            MaxPawnFileBins[i] = 0;
            MinPawnFileBins[i] = 0;
        }
        MaxPawnColorBins[0] = 0;
        MaxPawnColorBins[1] = 0;
        PawnRams = 0;
        MaxTotalPawns = 0;

        // Now, perform the analysis
        if (fromWhosePerspective == SIDE_WHITE) {
            for (int i = 0; i < 8; i++) {
                MaxMostAdvanced[i] = 63;
                MinMostBackward[i] = 63;
                MaxPassedPawns[i] = 63;
            }
            for (int square = 55; square >= 8; square--) {
                // Look for a white pawn first, and count its properties
                if (FindWhitePiece(square) == WHITE_PAWN) {
                    // What is the pawn's position, in rank-file terms?
                    int rank = square >> 3;
                    int file = square % 8;

                    // This pawn is now the most advanced of all white pawns on its file
                    MaxPawnFileBins[file]++;
                    MaxTotalPawns++;
                    MaxMostAdvanced[file] = square;

                    // Is this pawn on a white or a black square?
                    if ((rank % 2) == (file % 2)) {
                        MaxPawnColorBins[0]++;
                    } else {
                        MaxPawnColorBins[1]++;
                    }

                    // Look for a "pawn ram", i.e., a situation where a black pawn
                    // is located in the square immediately ahead of this one.
                    if (FindBlackPiece(square - 8) == BLACK_PAWN) {
                        PawnRams++;
                    }
                } // Now, look for a BLACK pawn
                else if (FindBlackPiece(square) == BLACK_PAWN) {
                    // If the black pawn exists, it is the most backward found so far
                    // on its file
                    int file = square % 8;
                    MinPawnFileBins[file]++;
                    MinMostBackward[file] = square;
                }
            }
        } else // Analyze from Black's perspective
        {
            for (int i = 0; i < 8; i++) {
                MaxMostAdvanced[i] = 0;
                MaxPassedPawns[i] = 0;
                MinMostBackward[i] = 0;
            }
            for (int square = 8; square < 56; square++) {
                if (FindBlackPiece(square) == BLACK_PAWN) {
                    // What is the pawn's position, in rank-file terms?
                    int rank = square >> 3;
                    int file = square % 8;

                    // This pawn is now the most advanced of all white pawns on its file
                    MaxPawnFileBins[file]++;
                    MaxTotalPawns++;
                    MaxMostAdvanced[file] = square;

                    if ((rank % 2) == (file % 2)) {
                        MaxPawnColorBins[0]++;
                    } else {
                        MaxPawnColorBins[1]++;
                    }

                    if (FindWhitePiece(square + 8) == WHITE_PAWN) {
                        PawnRams++;
                    }
                } else if (FindWhitePiece(square) == WHITE_PAWN) {
                    int file = square % 8;
                    MinPawnFileBins[file]++;
                    MinMostBackward[file] = square;
                }
            }
        }
        return true;
    }

    private int EvalMaterial(int side) {
        // If both sides are equal, no need to compute anything!
        if (MaterialValue[SIDE_BLACK] == MaterialValue[SIDE_WHITE]) {
            return 0;
        }

        int otherSide = (side + 1) % 2;
        int matTotal = MaterialValue[side] + MaterialValue[otherSide];

        // Who is leading the game, material-wise?
        if (MaterialValue[SIDE_BLACK] > MaterialValue[SIDE_WHITE]) {
            // Black leading
            int matDiff = MaterialValue[SIDE_BLACK] - MaterialValue[SIDE_WHITE];
            int val = Math.min(2400, matDiff)
                    + (matDiff * (12000 - matTotal) * NumPawns[SIDE_BLACK])
                    / (6400 * (NumPawns[SIDE_BLACK] + 1));
            if (side == SIDE_BLACK) {
                return val;
            } else {
                return -val;
            }
        } else {
            // White leading
            int matDiff = MaterialValue[SIDE_WHITE] - MaterialValue[SIDE_BLACK];
            int val = Math.min(2400, matDiff)
                    + (matDiff * (12000 - matTotal) * NumPawns[SIDE_WHITE])
                    / (6400 * (NumPawns[SIDE_WHITE] + 1));

            if (side == SIDE_WHITE) {
                return val;
            } else {
                return -val;
            }
        }
    }

    private int FindWhitePiece(int square) {
        if ((BitBoards[WHITE_KING] & SquareBits[square]) != 0) {
            return WHITE_KING;
        }
        if ((BitBoards[WHITE_QUEEN] & SquareBits[square]) != 0) {
            return WHITE_QUEEN;
        }
        if ((BitBoards[WHITE_ROOK] & SquareBits[square]) != 0) {
            return WHITE_ROOK;
        }
        if ((BitBoards[WHITE_KNIGHT] & SquareBits[square]) != 0) {
            return WHITE_KNIGHT;
        }
        if ((BitBoards[WHITE_BISHOP] & SquareBits[square]) != 0) {
            return WHITE_BISHOP;
        }
        if ((BitBoards[WHITE_PAWN] & SquareBits[square]) != 0) {
            return WHITE_PAWN;
        }
        return EMPTY_SQUARE;
    }

    private int FindBlackPiece(int square) {
        // Note: we look for kings first for two reasons: because it helps
        // detect check, and because there may be a phantom king (marking an
        // illegal castling move) and a rook on the same square!
        if ((BitBoards[BLACK_KING] & SquareBits[square]) != 0) {
            return BLACK_KING;
        }
        if ((BitBoards[BLACK_QUEEN] & SquareBits[square]) != 0) {
            return BLACK_QUEEN;
        }
        if ((BitBoards[BLACK_ROOK] & SquareBits[square]) != 0) {
            return BLACK_ROOK;
        }
        if ((BitBoards[BLACK_KNIGHT] & SquareBits[square]) != 0) {
            return BLACK_KNIGHT;
        }
        if ((BitBoards[BLACK_BISHOP] & SquareBits[square]) != 0) {
            return BLACK_BISHOP;
        }
        if ((BitBoards[BLACK_PAWN] & SquareBits[square]) != 0) {
            return BLACK_PAWN;
        }
        return EMPTY_SQUARE;
    }

    private long GetBitBoard(int which) {
        return BitBoards[which];
    }

    private boolean GetHasCastled(int which) {
        return HasCastled[which];
    }

    private boolean GetCastlingStatus(int which) {
        return CastlingStatus[which];
    }
}
