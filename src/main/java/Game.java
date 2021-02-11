import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.*;

public class Game {
    private Board currentBoard;
    private Scanner scanner;
    private Side playersColor;
    private DecisionMaker decisionMaker;

    public Game() {
        this.currentBoard = new Board();
        this.scanner = new Scanner(System.in);
        this.decisionMaker = new DecisionMaker();
    }

    public void play() {
        System.out.println("Who will be first player or computer?");
        String first = scanner.nextLine();
        playersColor = first.equals("player") ? Side.WHITE : Side.BLACK;
        System.out.println(currentBoard);
        System.out.println(" ");
        if (playersColor == Side.WHITE) {
            while (!move()) {
            }
            ;
            System.out.println(currentBoard);
            System.out.println(" ");
        }
        while (!decisionMaker.isTerminal(currentBoard)) {
            long startTime = System.nanoTime();
            decisionMaker.minimaxDecision(currentBoard, playersColor.flip());
            System.out.println(currentBoard);
            System.out.println(" ");
            long stopTime = System.nanoTime();
            System.out.println(stopTime - startTime);
            if (decisionMaker.isTerminal(currentBoard))
                break;
            while (!move()) {
            }
            ;
            System.out.println(currentBoard);
            System.out.println(" ");
        }
    }

    public boolean move() {
        String line = scanner.nextLine();
        String[] args = line.split(" ");
        Move move;
        Square sq1 = Square.fromValue(args[0].toUpperCase()), sq2 = Square.fromValue(args[1].toUpperCase());
        if(args.length == 3){
            Piece piece = Piece.fromValue(playersColor.value()+"_"+args[2].toUpperCase());
            move = new Move(sq1, sq2, piece);
        } else {
            move = new Move(sq1, sq2);
        }

        currentBoard.doMove(move);
        return true;
    }

    public static void main(String[] args) {
        new Game().play();
//        Board b = new Board();
//        b.doMove(new Move(Square.E2, Square.E4));
//        System.out.println(new DecisionMaker().eval(b, 0, Side.BLACK));
    }
}
//
//        // Creates a new chessboard in the standard initial position
//        Board board = new Board();
//
//        //Make a move from E2 to E4 squares
//        board.doMove(new Move(Square.E2, Square.E4));
//
//        //print the chessboard in a human-readable form
//        System.out.println(board.toString());

