package position;

import java.util.Objects;

public class GCoups implements ICodage {

    private final int PAS_DE_PIECE = -1;
    private final int piece;
    private final int caseO;
    private final int caseX;
    private final int pieceprise;
    private final TYPE_DE_COUPS type_de_coups;
    private final int piecePromotion;
    private final int caseOTour;
    private final int caseXTour;


    public GCoups(int piece, int caseO, int caseX, int pieceprise, TYPE_DE_COUPS type_de_coups) {
        this.piece = piece;
        this.caseO = caseO;
        this.caseX = caseX;
        this.pieceprise = pieceprise;
        this.type_de_coups = type_de_coups;
        this.piecePromotion = PAS_DE_PIECE;
        this.caseOTour = PAS_DE_PIECE;
        this.caseXTour = PAS_DE_PIECE;
    }

    public GCoups(int piece, int caseO, int caseX, int pieceprise, TYPE_DE_COUPS type_de_coups, int piecePromotion) {
        this.piece = piece;
        this.caseO = caseO;
        this.caseX = caseX;
        this.pieceprise = pieceprise;
        this.type_de_coups = type_de_coups;
        this.piecePromotion = piecePromotion;
        this.caseOTour = PAS_DE_PIECE;
        this.caseXTour = PAS_DE_PIECE;
    }

    public GCoups(int piece, int caseO, int caseX, int caseOTour, int caseXTour, int pieceprise, TYPE_DE_COUPS type_de_coups) {
        this.piece = piece;
        this.caseO = caseO;
        this.caseX = caseX;
        this.caseOTour = caseOTour;
        this.caseXTour = caseXTour;
        this.pieceprise = pieceprise;
        this.type_de_coups = type_de_coups;
        this.piecePromotion = PAS_DE_PIECE;
    }

    public static String getString(GCoups coups) {
        if (((coups.piece == ROI) && (coups.caseO == e1) && (coups.caseX == g1))
                || ((coups.piece == ROI) && (coups.caseO == e8) && (coups.caseX == g8))) {
            return "O-O";
        } else if (((coups.piece == ROI) && (coups.caseO == e1) && (coups.caseX == c1))
                || ((coups.piece == ROI) && (coups.caseO == e8) && (coups.caseX == c8))) {
            return "O-O-O";
        } else if (coups.type_de_coups == TYPE_DE_COUPS.EnPassant) {
            return STRING_CASES[INDICECASES[coups.getCaseO()]] + "x" + STRING_CASES[INDICECASES[coups.getCaseX()]];
        } else if (coups.type_de_coups == TYPE_DE_COUPS.Promotion) {
            if (coups.pieceprise != 0) {
                return STRING_CASES[INDICECASES[coups.getCaseO()]] + "x" + STRING_CASES[INDICECASES[coups.getCaseX()]]
                        + STRING_PIECE[Math.abs(coups.piecePromotion)];
            } else {
                return STRING_CASES[INDICECASES[coups.getCaseO()]] + "-" + STRING_CASES[INDICECASES[coups.getCaseX()]]
                        + STRING_PIECE[Math.abs(coups.piecePromotion)];
            }
        } else if (coups.type_de_coups == TYPE_DE_COUPS.Prise) {
            return STRING_CASES[INDICECASES[coups.getCaseO()]] + "x" + STRING_CASES[INDICECASES[coups.getCaseX()]];
        } else {
            return STRING_CASES[INDICECASES[coups.getCaseO()]] + "-" + STRING_CASES[INDICECASES[coups.getCaseX()]];
        }
    }

    public int getCaseO() {
        return caseO;
    }

    public int getCaseX() {
        return caseX;
    }

    public int getCaseOTour() {
        return caseOTour;
    }

    public int getCaseXTour() {
        return caseXTour;
    }

    public int getPiece() {
        return piece;
    }

    public int getPiecePrise() {
        return pieceprise;
    }

    public TYPE_DE_COUPS getTypeDeCoups() {
        return type_de_coups;
    }

    public int getPiecePromotion() {
        return piecePromotion;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.piece;
        hash = 71 * hash + this.caseO;
        hash = 71 * hash + this.caseX;
        hash = 71 * hash + this.pieceprise;
        hash = 71 * hash + Objects.hashCode(this.type_de_coups);
        hash = 71 * hash + this.piecePromotion;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GCoups other = (GCoups) obj;
        if (this.piece != other.piece) {
            return false;
        }
        if (this.caseO != other.caseO) {
            return false;
        }
        if (this.caseX != other.caseX) {
            return false;
        }
        if (this.pieceprise != other.pieceprise) {
            return false;
        }
        if (this.piecePromotion != other.piecePromotion) {
            return false;
        }
        return this.type_de_coups == other.type_de_coups;
    }

}
