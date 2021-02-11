import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.HashMap;
import java.util.List;

public class DecisionMaker {
    private Move nextMove = null;
    private HashMap<Integer, Double> exploredPositions;
    public static final int MAX_AI_DEPTH = 4;
    public static final double[][] KING_POSITIONAL_COEFFICIENTS= {{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0},
            {2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0}};
    public static final double[][] QUEEN_POSITIONAL_COEFFICIENTS= {{-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
            {-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
            {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
            {-1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}};
    public static final double[][] ROOK_POSITIONAL_COEFFICIENTS= {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}};
    public static final double[][] BISHOP_POSITIONAL_COEFFICIENTS= {{-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
            {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
            {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
            {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
            {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}};
    public static final double[][] KNIGHT_POSITIONAL_COEFFICIENTS= {{-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
            {-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0},
            {-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
            {-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
            {-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
            {-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
            {-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0},
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}};
    public static final double[][] PAWN_POSITIONAL_COEFFICIENTS= {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                                    {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
                                                                    {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 2.0, 1.0},
                                                                    {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
                                                                    {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
                                                                    {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
                                                                    {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
                                                                    {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

    public DecisionMaker(){
        exploredPositions = new HashMap<>();
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
        for(Square square : Square.values()){
            currentPiece = board.getPiece(square);
            if (currentPiece != null) {
                Side side = currentPiece.getPieceSide();
                int x = square.getFile().ordinal();
                int y = (side == Side.WHITE) ? square.getRank().ordinal() : Math.abs(7 - square.getRank().ordinal());
                if (currentPiece.getPieceType() == PieceType.PAWN)
                    sum += (10.0 + PAWN_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece == Piece.WHITE_BISHOP || currentPiece == Piece.BLACK_BISHOP)
                    sum += (30.0 + BISHOP_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece == Piece.WHITE_KNIGHT || currentPiece == Piece.BLACK_KNIGHT)
                    sum += (30.0 + KNIGHT_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece == Piece.WHITE_ROOK || currentPiece == Piece.BLACK_ROOK)
                    sum += (50.0 + ROOK_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece == Piece.WHITE_QUEEN || currentPiece == Piece.BLACK_QUEEN)
                    sum += (90.0 + QUEEN_POSITIONAL_COEFFICIENTS[7 - y][x]) * (side == AIColor ? 1 : -1);
                else if (currentPiece == Piece.WHITE_KING || currentPiece == Piece.BLACK_KING)
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
        if(exploredPositions.containsKey(board.hashCode())) return exploredPositions.get(board.hashCode());
        if (isTerminal(board, depth, currentMaxDepth)) {
            return eval(board, depth, AIColor);
        }

        double value = -Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        List<Move> legalMoves = board.legalMoves();
        legalMoves.sort((move1, move2)->{
            boolean isCaptureMove1 = Helpers.isCapture(move1, board), isCaptureMove2 = Helpers.isCapture(move2, board);
            if(isCaptureMove1 && !isCaptureMove2) return -1;
            if(isCaptureMove2 && !isCaptureMove1) return 1;
            return 0;
        });
        for (Move legalMove : legalMoves) {
            board.doMove(legalMove);
            currentValue = minValue(board, depth + 1, alpha, beta, AIColor, currentMaxDepth);
            if (currentValue > value) {
                nextMove = legalMove;
                value = currentValue;
            }
            board.undoMove();
            if (value >= beta) {
                this.nextMove = nextMove;
                return value;
            }
            if (value > alpha) {
                alpha = value;
            }
        }
        this.nextMove = nextMove;
        return value;
    }

    private double minValue(Board board, int depth, double alpha, double beta, Side AIColor, int currentMaxDepth) {
        if(exploredPositions.containsKey(board.hashCode())) return exploredPositions.get(board.hashCode());
        if (isTerminal(board, depth, currentMaxDepth)) {
            return eval(board, depth, AIColor);
        }

        double value = Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        List<Move> legalMoves = board.legalMoves();
        legalMoves.sort((move1, move2)->{
            boolean isCaptureMove1 = Helpers.isCapture(move1, board), isCaptureMove2 = Helpers.isCapture(move2, board);
            if(isCaptureMove1 && !isCaptureMove2) return -1;
            if(isCaptureMove2 && !isCaptureMove1) return 1;
            return 0;
        });
        for (Move legalMove : legalMoves) {
            board.doMove(legalMove);
            currentValue = maxValue(board, depth + 1, alpha, beta, AIColor, currentMaxDepth);
            if (currentValue < value) {
                nextMove = legalMove;
                value = currentValue;
            }
            board.undoMove();
            if (value <= alpha) {
                this.nextMove = nextMove;
                return value;
            }
            if (value < beta) {
                beta = value;
            }
        }
        this.nextMove = nextMove;
        return value;
    }

    public boolean minimaxDecision(Board board, Side AIColor) {
        for(int i = 1; i < MAX_AI_DEPTH; i++){
            maxValue(board, 0, Double.MIN_VALUE, Double.MAX_VALUE, AIColor, i);
        }
        return board.doMove(nextMove);
    }
}
