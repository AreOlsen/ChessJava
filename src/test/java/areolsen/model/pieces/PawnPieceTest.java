package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for PawnPiece. */
public class PawnPieceTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board();
  }

  @Test
  public void testPawnGetValue() {
    // Get the actual pawn from the board at starting position
    PawnPiece pawn = (PawnPiece) board.getPiece(new Position(0, 1)).get();
    assertEquals(1.0d, pawn.getValue());
  }

  @Test
  public void testPawnGetType() {
    // Get the actual pawn from the board
    PawnPiece pawn = (PawnPiece) board.getPiece(new Position(0, 1)).get();
    assertEquals("P", pawn.getType());
  }

  @Test
  public void testWhitePawnInitialMoves() {
    // White pawns start at rank 1 (y=1) and can move either 1 or 2 squares forward on first move
    // Pawn at (0,1) should be able to move to (0,2) or (0,3)
    List<Move> moves = board.getLegalMoves(new Position(0, 1));
    assertEquals(2, moves.size());
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 2))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 3))));
  }

  @Test
  public void testBlackPawnInitialMoves() {
    // Switch turn to BLACK so getLegalMoves evaluates black pieces
    board.changeSide();

    // Black pawns start at rank 6 (y=6) and can move either 1 or 2 squares forward (downward) on
    // first move
    // Pawn at (0,6) should be able to move to (0,5) or (0,4)
    List<Move> moves = board.getLegalMoves(new Position(0, 6));
    assertEquals(2, moves.size());
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 5))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 4))));
  }

  @Test
  public void testPawnSingleMove() {
    board.movePiece(new Position(0, 1), new Position(0, 2), false);
    assertTrue(board.getPiece(new Position(0, 2)).isPresent());
    assertEquals("P", board.getPiece(new Position(0, 2)).get().getType());
  }

  @Test
  public void testPawnDiagonalCaptureNotEmpty() {
    // Use empty board for clean capture test
    Board emptyBoard = new Board().emptyBoard();
    new PawnPiece(emptyBoard, new Position(1, 4), Side.BLACK);
    // Place a white pawn at (0,3) for the black pawn to capture
    new PawnPiece(emptyBoard, new Position(0, 3), Side.WHITE);

    // Note: emptyBoard defaults turn to WHITE; switch turn to BLACK to test black pawn legal moves
    emptyBoard.changeSide();
    List<Move> moves = emptyBoard.getLegalMoves(new Position(1, 4));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 3))));
  }

  @Test
  public void testPawnCannotMoveDiagonalEmpty() {
    // Move white pawn from (0,1) to (0,3)
    board.movePiece(new Position(0, 1), new Position(0, 3), false); // White turn
    board.movePiece(new Position(7, 6), new Position(7, 5), false); // Black dummy turn

    List<Move> moves = board.getLegalMoves(new Position(0, 3));
    // Pawn cannot move diagonally to an empty square (1,4) - diagonal moves only for captures
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(1, 4))));
  }

  @Test
  public void testPawnCannotCaptureEmpty() {
    // Set up scenario: clear path for pawns to move
    Board testBoard = new Board();
    testBoard.movePiece(new Position(0, 1), new Position(0, 3), false);
    // Black pawn moves from (1,6) to (1,5)
    testBoard.movePiece(new Position(1, 6), new Position(1, 5), false);
    // White pawn continues to (0,4)
    testBoard.movePiece(new Position(0, 3), new Position(0, 4), false);

    List<Move> moves = testBoard.getLegalMoves(new Position(0, 4));
    // Verify the pawn at new position does not have diagonal move to empty square (1,4)
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(1, 4))));
  }
}
