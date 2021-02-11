import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.move.Move;

public class Helpers {
    public static boolean isCapture(Move move, Board board){
        return board.getPiece(move.getTo()) != Piece.NONE;
    }
}
