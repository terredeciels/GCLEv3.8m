package ia;

import position.GPosition;
import position.ICodage;
import static position.ICodage.CASES117;
import static position.ICodage.OUT;
import static position.ICodage.ROI;

public class GPositionEvaluator implements ICodage {

    int SIDE_BLACK = 1;
    int SIDE_WHITE = 0;

    int MaxPawnFileBins[];
    int MaxPawnColorBins[];
    int MaxMostAdvanced[];
    int MaxPassedPawns[];
    int MinPawnFileBins[];
    int MinMostBackward[];
    int MaterialValue[];
    int NumPawns[];
    int MaxTotalPawns;
    int PawnRams;

    int GRAIN = 3;

    private final GPosition gp;

    public GPositionEvaluator(GPosition gp) {
        this.gp = gp;
        MaxPawnFileBins = new int[8];
        MaxPawnColorBins = new int[2];
        MaxMostAdvanced = new int[8];
        MaxPassedPawns = new int[8];
        MinPawnFileBins = new int[8];
        MinMostBackward = new int[8];

        NumPawns = new int[2];
        MaterialValue = new int[2];
        NumPawns = new int[2];
        MaterialValue = new int[2];
        getMaterielValue();
    }

    public int evaluateQuick(int noirsOuBlancs) {
        noirsOuBlancs = noirsOuBlancs == BLANC ? SIDE_WHITE : SIDE_BLACK;
        return ((EvalMaterial(noirsOuBlancs) >> GRAIN) << GRAIN);
    }

    public int evaluateComplete(int noirsOuBlancs) {
        noirsOuBlancs = noirsOuBlancs == BLANC ? SIDE_WHITE : SIDE_BLACK;
        AnalyzePawnStructure(noirsOuBlancs);

        return (((EvalMaterial(noirsOuBlancs)
                //                + EvalPawnStructure(noirsOuBlancs)
                + EvalBadBishops(noirsOuBlancs)
                + EvalDevelopment(noirsOuBlancs)
                + EvalRookBonus(noirsOuBlancs)
                + EvalKingTropism(noirsOuBlancs)) >> GRAIN) << GRAIN);
    }

    private int EvalKingTropism(int noirsOuBlancs) {
        int score = 0;
        if (noirsOuBlancs == BLANC) {//SIDE_WHITE
            // Look for enemy king first!
            int _caseRoi = fCaseRoi(gp, BLANC);
            int kingRow = _caseRoi >> 8;
            int kingColumn = _caseRoi % 8;

            // Now, look for pieces which need to be evaluated
            for (int caseO : CASES117) {
                int _case = INDICECASES[caseO];//0 a 63
                //verifier
                int pieceRow = _case >> 8;
                int pieceColumn = _case % 8;

                if (couleurPiece(caseO) == BLANC) {

                    switch (typeDePiece(caseO)) {

                        case TOUR:
                            score -= (Math.min(Math.abs(kingRow - pieceRow),
                                    Math.abs(kingColumn - pieceColumn)) << 1);
                            break;
                        case CAVALIER:
                            score += 5 - Math.abs(kingRow - pieceRow)
                                    - Math.abs(kingColumn - pieceColumn);
                            break;
                        case DAME:
                            score -= Math.min(Math.abs(kingRow - pieceRow),
                                    Math.abs(kingColumn - pieceColumn));
                            break;
                        default:
                            break;
                    }
                }
            }

        } else {
            // Look for enemy king first
            int _caseRoi = fCaseRoi(gp, NOIR);
            int kingRow = _caseRoi >> 8;
            int kingColumn = _caseRoi % 8;

            // Now, look for pieces which need to be evaluated
            for (int caseO : CASES117) {
                int _case = INDICECASES[caseO];//0 a 63
                //verifier

                int pieceRow = _case >> 8;
                int pieceColumn = _case % 8;
                if (couleurPiece(caseO) == BLANC) {

                    switch (typeDePiece(caseO)) {
                        case TOUR:
                            score -= (Math.min(Math.abs(kingRow - pieceRow),
                                    Math.abs(kingColumn - pieceColumn)) << 1);
                            break;
                        case CAVALIER:
                            score += 5 - Math.abs(kingRow - pieceRow)
                                    - Math.abs(kingColumn - pieceColumn);
                            break;
                        case DAME:
                            score -= Math.min(Math.abs(kingRow - pieceRow),
                                    Math.abs(kingColumn - pieceColumn));
                            break;
                        default:
                            break;
                    }
                }
            }

        }
        return score;
    }

    private int EvalRookBonus(int noirsOuBlancs) {
        int score = 0;
        for (int caseO : CASES117) {
            int _case = INDICECASES[caseO];//0 a 63
            if (typeDePiece(caseO) == TOUR) {
                // Is this rook on the seventh rank?
                int file = (_case % 8);
                if (rang7(caseO, noirsOuBlancs)) {
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

                /*
                @TODO Is this rook behind a passed pawn?
                 */
                // Is this rook behind a passed pawn?
//                if ((noirsOuBlancs == SIDE_WHITE) && (MaxPassedPawns[file] < square)) {
//                    score += 25;
//                }
//                if ((noirsOuBlancs == SIDE_BLACK) && (MaxPassedPawns[file] > square)) {
//                    score += 25;
//                }
            }
        }
        return score;
    }

    private int EvalDevelopment(int noirsOuBlancs) {
        int score = 0;
//  noirsOuBlancs = noirsOuBlancs == BLANC ? SIDE_WHITE : SIDE_BLACK;
        if (noirsOuBlancs == SIDE_WHITE) {
            // Has the machine advanced its center pawns?
            if (typeDePiece(e2) == PION && couleurPiece(e2) == BLANC) {
                score -= 15;
            }
            if (typeDePiece(d2) == PION && couleurPiece(d2) == BLANC) {
                score -= 15;
            }

            // Penalize bishops and knights on the back rank
            for (int square = a8; square <= h8; square++) {//verif
                if (couleurPiece(square) == BLANC
                        && (typeDePiece(square) == CAVALIER || typeDePiece(square) == FOU)) {
                    score -= 10;
                }
            }

            /*
            @TODO Penalize too-early queen movement (white)
             */
//           long queenboard = GetBitBoard(WHITE_QUEEN);
//            if ((queenboard != 0) && ((queenboard & SquareBits[59]) == 0)) {
//                // First, count friendly pieces on their original squares
//                int cnt = 0;
//                if ((GetBitBoard(WHITE_BISHOP) & SquareBits[58]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(WHITE_BISHOP) & SquareBits[61]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(WHITE_KNIGHT) & SquareBits[57]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(WHITE_KNIGHT) & SquareBits[62]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(WHITE_ROOK) & SquareBits[56]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(WHITE_ROOK) & SquareBits[63]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(WHITE_KING) & SquareBits[60]) != 0) {
//                    cnt++;
//                }
//                score -= (cnt << 3);
//            }
            // And finally, incite castling when the enemy has a queen on the board
            // This is a slightly simpler version of a factor used by Cray Blitz
            if (isDame(NOIR)) {
                // Being castled deserves a bonus
                if (gp.hasRoques(BLANC)) {
                    score += 10;
                } else if (gp.isGrandRoque(BLANC) && gp.isPetitRoque(BLANC)) {
                    score -= 24;
                } // bigger penalty if you can only castle kingside
                else if (gp.isPetitRoque(BLANC)) {
                    score -= 40;
                } // bigger penalty if you can only castle queenside
                else if (gp.isGrandRoque(BLANC)) {
                    score -= 80;
                } // biggest penalty if you can't castle at all
                else {
                    score -= 120;
                }
            }
        } else // from black's perspective
        {
            // Has the machine advanced its center pawns?
            if (typeDePiece(e7) == PION && couleurPiece(e7) == NOIR) {
                score -= 15;
            }
            if (typeDePiece(d7) == PION && couleurPiece(d7) == NOIR) {
                score -= 15;
            }
            // Penalize bishops and knights on the back rank
            for (int square = a8; square <= h8; square++) {//verif
                if (couleurPiece(square) == NOIR
                        && (typeDePiece(square) == CAVALIER || typeDePiece(square) == FOU)) {
                    score -= 10;
                }
                /*
            @TODO Penalize too-early queen movement (black)
                 */
//            long queenboard = GetBitBoard(BLACK_QUEEN);
//            if ((queenboard != 0) && ((queenboard & SquareBits[3]) == 0)) {
//                // First, count friendly pieces on their original squares
//                int cnt = 0;
//                if ((GetBitBoard(BLACK_BISHOP) & SquareBits[2]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(BLACK_BISHOP) & SquareBits[5]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(BLACK_KNIGHT) & SquareBits[1]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(BLACK_KNIGHT) & SquareBits[6]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(BLACK_ROOK) & SquareBits[0]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(BLACK_ROOK) & SquareBits[7]) != 0) {
//                    cnt++;
//                }
//                if ((GetBitBoard(BLACK_KING) & SquareBits[4]) != 0) {
//                    cnt++;
//                }
//                score -= (cnt << 3);
//            }
                if (isDame(BLANC)) {
                    // Being castled deserves a bonus
                    if (gp.hasRoques(NOIR)) {
                        score += 10;
                    } else if (gp.isGrandRoque(NOIR) && gp.isPetitRoque(NOIR)) {
                        score -= 24;
                    } // bigger penalty if you can only castle kingside
                    else if (gp.isPetitRoque(NOIR)) {
                        score -= 40;
                    } // bigger penalty if you can only castle queenside
                    else if (gp.isGrandRoque(NOIR)) {
                        score -= 80;
                    } // biggest penalty if you can't castle at all
                    else {
                        score -= 120;
                    }
                }
            }

        }
        return score;
    }

    private int EvalBadBishops(int noirsOuBlancs) {

//        if (!isFOU(noirsOuBlancs)) {
//            return 0;
//        }
        int score = 0;
        for (int caseO : CASES117) {
            int square = INDICECASES[caseO];//0 a 63
            // Find a bishop
            if (typeDePiece(caseO) == FOU) {

                // What is the bishop's square color?
                /*
                @TODO square color
                 */
                int rank = (square >> 3);
                int file = (square % 8);
                if ((rank % 2) == (file % 2)) {
                    score -= (MaxPawnColorBins[0] << 3);
                } else {
                    score -= (MaxPawnColorBins[1] << 3);
                }

                /*
                @TODO ?
                 */
                // Use the bitboard erasure trick to avoid looking for additional
                // bishops once they have all been seen
//                where ^= SquareBits[square];
//                if (where == 0) {
//                    break;
//                }
            }
        }
        return score;
    }

    private int EvalMaterial(int side) {

        // If both sides are equal, no need to compute anything!
        if (MaterialValue[SIDE_BLACK] == MaterialValue[SIDE_WHITE]) {
            return 0;
        }

        int otherSide = (side + 1) % 2;
        // 0+1 % 2 = 1
        //1+1  %2  = 0
        int matTotal = MaterialValue[side] + MaterialValue[otherSide];

        // Who is leading the game, material-wise?
        if (MaterialValue[SIDE_BLACK] > MaterialValue[SIDE_WHITE]) {
            // Black leading/ mène
            int matDiff = MaterialValue[SIDE_BLACK] - MaterialValue[SIDE_WHITE];
            //equation ?
            int val = Math.min(2400, matDiff)
                    + (matDiff * (12000 - matTotal) * NumPawns[SIDE_BLACK])
                    / (6400 * (NumPawns[SIDE_BLACK] + 1));

            if (side == SIDE_BLACK) {
                return val;
            } else {
                return -val;
            }
        } else {
            // White leading / mène
            int matDiff = MaterialValue[SIDE_WHITE] - MaterialValue[SIDE_BLACK];
            // eq ?
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

    private void getMaterielValue() {

        for (int caseO : CASES117) {
            int typeDePiece = typeDePiece(caseO);
            int couleurPiece = couleurPiece(caseO) == BLANC ? SIDE_WHITE : SIDE_BLACK;
            MaterialValue[couleurPiece] += PIECE_VALUES[typeDePiece];

            if (typeDePiece == PION) {
                NumPawns[couleurPiece]++;
            }
        }

    }

    private boolean AnalyzePawnStructure(int noirsOuBlancs) {
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
        if (noirsOuBlancs == SIDE_WHITE) {
            for (int i = 0; i < 8; i++) {
                MaxMostAdvanced[i] = 63;
                MinMostBackward[i] = 63;
                MaxPassedPawns[i] = 63;
            }

            for (int square = 55; square >= 8; square--) {
                int caseO = CASES117[square];
                // Look for a white pawn first, and count its properties
                if (couleurPiece(caseO) == BLANC && typeDePiece(caseO) == PION) {

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
                    if (typeDePiece(caseO - 12) == PION && couleurPiece(caseO - 12) == NOIR) {
                        PawnRams++;
                    }
                } // Now, look for a BLACK pawn
                else if (typeDePiece(caseO) == PION && couleurPiece(caseO) == NOIR) {
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
                int caseO = CASES117[square];
                if (typeDePiece(caseO) == PION && couleurPiece(caseO) == NOIR) {

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
                    if (typeDePiece(caseO + 12) == PION && couleurPiece(caseO + 12) == BLANC) {
                        PawnRams++;
                    }
                } else if (typeDePiece(caseO + 12) == PION && couleurPiece(caseO + 12) == BLANC) {

                    int file = square % 8;
                    MinPawnFileBins[file]++;
                    MinMostBackward[file] = square;
                }
            }
        }
        return true;
    }

    private boolean isDame(int color) {
        for (int caseO : CASES117) {
            if (typeDePiece(caseO) == DAME && couleurPiece(caseO) == color) {
                return true;
            }
        }
        return false;
    }

    private int fCaseRoi(GPosition position, int couleur) {
        int[] pEtats = position.getEtats();
        int caseRoi = OUT;
        int etatO;
        int typeO;
        for (int caseO : CASES117) {
            etatO = pEtats[caseO];
            typeO = Math.abs(etatO);
            if ((typeO == ROI) && (etatO * couleur > 0)) {
                caseRoi = caseO;
                break;
            }
        }
        return caseRoi;
    }

    private int couleurPiece(int caseO) {
        return (gp.getEtats()[caseO] < 0) ? BLANC : NOIR;
    }

    private int typeDePiece(int caseO) {
        return (gp.getEtats()[caseO] < 0) ? -gp.getEtats()[caseO] : gp.getEtats()[caseO];
    }

    private boolean rangFinal(int caseX, int couleur) {
        if ((caseX >= a1) && (caseX <= h1) && (couleur == NOIR)) {
            return true;
        } else {
            return (caseX >= a8) && (caseX <= h8) && (couleur == BLANC);
        }
    }

    private boolean rang7(int caseX, int couleur) {
        if ((caseX >= a2) && (caseX <= h2) && (couleur == NOIR)) {
            return true;
        } else {
            return (caseX >= a7) && (caseX <= h7) && (couleur == BLANC);
        }
    }

}
