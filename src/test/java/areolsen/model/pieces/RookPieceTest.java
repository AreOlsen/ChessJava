package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for RookPiece. */
public class RookPieceTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board();
  }

  @Test
  public void testRookGetValue() {
    RookPiece rook = (RookPiece) board.getPiece(new Position(0, 0)).get();
    assertEquals(5.0d, rook.getValue());
  }

  @Test
  public void testRookGetType() {
    RookPiece rook = (RookPiece) board.getPiece(new Position(0, 0)).get();
    assertEquals("R", rook.getType());
  }

  @Test
  public void testRookBlockedByPieces() {
    List<Move> moves = board.getLegalMoves(new Position(0, 0));
    assertEquals(0, moves.size());
  }

  @Test
  public void testRookHorizontalMove() {
    Board emptyBoard = new Board().emptyBoard();
    new RookPiece(emptyBoard, new Position(0, 0), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(0, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(1, 0))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(2, 0))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 0))));
  }

  @Test
  public void testRookVerticalMove() {
    // Clear path by advancing the pawn at (0,1) out of the way
    board.movePiece(
        new Position(0, 1), new Position(0, 3), false); // White pawn moves (0,1) -> (0,3)
    board.movePiece(new Position(7, 6), new Position(7, 5), false); // Black dummy move

    // Now (0,1) is empty and reachable by the rook at (0,0)
    List<Move> moves = board.getLegalMoves(new Position(0, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 1))));
  }

  @Test
  public void testRookCaptureOpponentPiece() {
    // Clear path on an empty board to test targeting an opponent piece at (0,6)
    Board emptyBoard = new Board().emptyBoard();
    new RookPiece(emptyBoard, new Position(0, 0), Side.WHITE);
    new PawnPiece(emptyBoard, new Position(0, 6), Side.BLACK);

    List<Move> moves = emptyBoard.getLegalMoves(new Position(0, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 6))));
  }

  @Test
  public void testRookCannotCaptureOwnPiece() {
    List<Move> moves = board.getLegalMoves(new Position(0, 0));
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 0))));
  }

  @Test
  public void testRookDiagonalMovesNotAllowed() {
    Board emptyBoard = new Board().emptyBoard();
    new RookPiece(emptyBoard, new Position(0, 0), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(0, 0));
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(1, 1))));
  }
}
