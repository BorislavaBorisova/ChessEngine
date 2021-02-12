import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.HashMap;
import java.util.List;

public class DecisionMaker {
    private Move nextMove = null;
    private HashMap<Integer, Move> exploredPositions;
    public static final int MAX_AI_DEPTH = 8;
    public static final double[][] KING_POSITIONAL_COEFFICIENTS = { { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0 },
            { -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0 },
            { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0 },
            { 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0 },
            { 2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0 } };
    public static final double[][] QUEEN_POSITIONAL_COEFFICIENTS = { { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 },
            { -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0 },
            { -1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0 },
            { -0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5 },
            { 0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5 },
            { -1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0 },
            { -1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0 },
            { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0 } };
    public static final double[][] ROOK_POSITIONAL_COEFFICIENTS = { { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5 },
            { 0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0 } };
    public static final double[][] BISHOP_POSITIONAL_COEFFICIENTS = {
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 },
            { -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0 },
            { -1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0 },
            { -1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0 },
            { -1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0 },
            { -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0 },
            { -1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0 },
            { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0 } };
    public static final double[][] KNIGHT_POSITIONAL_COEFFICIENTS = {
            { -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 },
            { -4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0 },
            { -3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0 },
            { -3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0 },
            { -3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0 },
            { -3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0 },
            { -4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0 },
            { -5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0 } };
    public static final double[][] PAWN_POSITIONAL_COEFFICIENTS = { { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0 },
            { 1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 2.0, 1.0 },
            { 0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5 },
            { 0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0 },
            { 0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5 },
            { 0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5 },
            { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };

    public DecisionMaker() {
        exploredPositions = new HashMap<>();
    }

    private double getWeight(Piece piece) {
        if (piece.getPieceType() == PieceType.KNIGHT || piece.getPieceType() == PieceType.BISHOP)
            return 30.0;
        if (piece.getPieceType() == PieceType.ROOK)
            return 50.0;
        if (piece.getPieceType() == PieceType.QUEEN)
            return 90.0;
        if (piece.getPieceType() == PieceType.KING)
            return 900.0;
        else
            return 10.0;
    }

    private int weightComparator(Move move1, Move move2, Board board) {
        boolean isCaptureMove1 = Helpers.isCapture(move1, board), isCaptureMove2 = Helpers.isCapture(move2, board);
        if (isCaptureMove1 && isCaptureMove2) {
            double diff1 = getWeight(board.getPiece(move1.getTo())) - getWeight(board.getPiece(move1.getFrom()));
            double diff2 = getWeight(board.getPiece(move2.getTo())) - getWeight(board.getPiece(move2.getFrom()));
            if (diff1 > diff2)
                return -1;
            if (diff1 < diff2)
                return 1;
            return 0;
        }
        if (isCaptureMove1)
            return -1; // !isCaptureMove2 && isCaptureMove1
        if (isCaptureMove2)
            return 1; // isCaptureMove2 && !isCaptureMove1
        return 0; // !isCaptureMove2 && !isCaptureMove1
    }

    public double eval(Board board, int depth, Side AIColor) {
        Side turnSide = board.getSideToMove();
        if (board.isMated()) {
            return 200.0 * (turnSide == AIColor ? -1 : 1);
        } else if (board.isDraw()) {
            return 0.0;
        }

        double sum = 0.0;
        Piece currentPiece = null;
        for (Square square : Square.values()) {
            currentPiece = board.getPiece(square);
            if (currentPiece != null) {
                Side side = currentPiece.getPieceSide();
                int x = square.getFile().ordinal();
                int y = (side == Side.WHITE) ? square.getRank().ordinal() : Math.abs(7 - square.getRank().ordinal());
                if (currentPiece.getPieceType() == PieceType.PAWN)
                    sum += (10.0 + PAWN_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece.getPieceType() == PieceType.BISHOP)
                    sum += (30.0 + BISHOP_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece.getPieceType() == PieceType.KNIGHT)
                    sum += (30.0 + KNIGHT_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece.getPieceType() == PieceType.ROOK)
                    sum += (50.0 + ROOK_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece.getPieceType() == PieceType.QUEEN)
                    sum += (90.0 + QUEEN_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece.getPieceType() == PieceType.KING)
                    sum += (900.0 + KING_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
            }
        }
        return sum;
    }

    public boolean isTerminal(Board board) {
        return board.isDraw() || board.isMated();
    }

    private boolean isTerminal(Board board, int depth, int currentMaxDepth) {
        return isTerminal(board) || depth > currentMaxDepth;
    }

    private double maxValue(Board board, int depth, double alpha, double beta, Side AIColor, int currentMaxDepth) {
        if (isTerminal(board, depth, currentMaxDepth)) {
            return eval(board, depth, AIColor);
        }

        double value = -Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        List<Move> legalMoves = board.legalMoves();
        legalMoves.sort((move1, move2) -> weightComparator(move1, move2, board));
        if (exploredPositions.containsKey(board.hashCode())) {
            Move exploredMove = exploredPositions.get(board.hashCode());
            int idx = legalMoves.indexOf(exploredMove);
            if (idx != -1) {
                legalMoves.remove(idx);
                legalMoves.add(0, exploredMove);
            }
        }
//              old comparator
//        {
//            boolean isCaptureMove1 = Helpers.isCapture(move1, board), isCaptureMove2 = Helpers.isCapture(move2, board);
//            if(isCaptureMove1 && !isCaptureMove2) return -1;
//            if(isCaptureMove2 && !isCaptureMove1) return 1;
//            return 0;
//        }
        for (Move legalMove : legalMoves) {
            board.doMove(legalMove);
            currentValue = minValue(board, depth + 1, alpha, beta, AIColor, currentMaxDepth);
            if (currentValue > value) {
                nextMove = legalMove;
                value = currentValue;
            }
            board.undoMove();
            if (value >= beta) {
                break;
            }
            if (value > alpha) {
                alpha = value;
            }
        }
        exploredPositions.put(board.hashCode(), nextMove);

//        r.bqkbnr
//        ppp.pppp
//        ..n.....
//        ......N.
//        ....pP..
//        ........
//        PPPP..PP
//        RNBQKB.R
//        Side: BLACK

//        r.bqkbnr
//        .ppppppp
//        ........
//        p.......
//        .nP.P...
//        ........
//        PP.PBPPP
//        RNBQK.NR
//        Side: BLACK

//        -1094105495

        this.nextMove = nextMove;
        return value;
    }

    private double minValue(Board board, int depth, double alpha, double beta, Side AIColor, int currentMaxDepth) {
        if (isTerminal(board, depth, currentMaxDepth)) {
            return eval(board, depth, AIColor);
        }

        double value = Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        List<Move> legalMoves = board.legalMoves();
        legalMoves.sort((move1, move2) -> weightComparator(move1, move2, board));
        if (exploredPositions.containsKey(board.hashCode())) {
            Move exploredMove = exploredPositions.get(board.hashCode());
            int idx = legalMoves.indexOf(exploredMove);
            if (idx != -1) {
                legalMoves.remove(idx);
                legalMoves.add(0, exploredMove);
            }
        }
        for (Move legalMove : legalMoves) {
            board.doMove(legalMove);
            currentValue = maxValue(board, depth + 1, alpha, beta, AIColor, currentMaxDepth);
            if (currentValue < value) {
                nextMove = legalMove;
                value = currentValue;
            }
            board.undoMove();
            if (value <= alpha) {
                break;
            }
            if (value < beta) {
                beta = value;
            }
        }
        exploredPositions.put(board.hashCode(), nextMove);
        this.nextMove = nextMove;
        return value;
    }

    public boolean minimaxDecision(Board board, Side AIColor) {
        for (int i = 1; i < MAX_AI_DEPTH; i++) {
            maxValue(board, 0, Double.MIN_VALUE, Double.MAX_VALUE, AIColor, i);
        }
        return board.doMove(nextMove);
    }
}
