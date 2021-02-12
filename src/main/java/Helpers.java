import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.move.Move;

public class Helpers {
    public static boolean isCapture(Move move, Board board){
        return board.getPiece(move.getTo()) != Piece.NONE;
    }

    public static boolean isMoveCheck(Move move, Board board){
         board.doMove(move);
         boolean isCheck = board.isKingAttacked();
         board.undoMove();
         return isCheck;
    }
}
