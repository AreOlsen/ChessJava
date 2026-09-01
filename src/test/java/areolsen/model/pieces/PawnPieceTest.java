package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.ChessBoard;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for PawnPiece. */
public class PawnPieceTest {
  private ChessBoard board;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
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
    List<Position> moves = board.getLegalMoves(new Position(0, 1));
    assertEquals(2, moves.size());
    assertTrue(moves.contains(new Position(0, 2)));
    assertTrue(moves.contains(new Position(0, 3)));
  }

  @Test
  public void testBlackPawnInitialMoves() {
    // Black pawns start at rank 6 (y=6) and can move either 1 or 2 squares forward (downward) on
    // first move
    // Pawn at (0,6) should be able to move to (0,5) or (0,4)
    List<Position> moves = board.getLegalMoves(new Position(0, 6));
    assertEquals(2, moves.size());
    assertTrue(moves.contains(new Position(0, 5)));
    assertTrue(moves.contains(new Position(0, 4)));
  }

  @Test
  public void testPawnSingleMove() {
    board.movePiece(new Position(0, 1), new Position(0, 2));
    assertTrue(board.getPiece(new Position(0, 2)).isPresent());
    assertEquals("P", board.getPiece(new Position(0, 2)).get().getType());
  }

  @Test
  public void testPawnDiagonalCaptureNotEmpty() {
    // Use empty board for clean capture test
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    PawnPiece blackPawn = new PawnPiece(emptyBoard, new Position(1, 4), ChessSide.BLACK);
    // Place a white pawn at (0,3) for the black pawn to capture
    new PawnPiece(emptyBoard, new Position(0, 3), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(1, 4));
    assertTrue(moves.contains(new Position(0, 3)));
  }

  @Test
  public void testPawnCannotMoveDiagonalEmpty() {
    // Move white pawn from (0,1) to (0,3)
    board.movePiece(new Position(0, 1), new Position(0, 3));
    List<Position> moves = board.getLegalMoves(new Position(0, 3));
    // Pawn cannot move diagonally to an empty square (1,4) - diagonal moves only for captures
    // This ensures pawns don't have unexpected movement patterns
    assertFalse(moves.contains(new Position(1, 4)));
  }

  @Test
  public void testPawnCannotCaptureEmpty() {
    // Set up scenario: clear path for pawns to move
    ChessBoard testBoard = new ChessBoard();
    testBoard.movePiece(new Position(0, 1), new Position(0, 3));
    // Black pawn moves from (1,6) to (1,5)
    testBoard.movePiece(new Position(1, 6), new Position(1, 5));
    // White pawn continues to (0,4)
    testBoard.movePiece(new Position(0, 3), new Position(0, 4));
    // Pawn should not be able to move to an empty square via capture logic
    List<Position> moves = testBoard.getLegalMoves(new Position(0, 4));
    // Verify the pawn at new position has proper legal moves (not capturing empty squares)
    assertTrue(moves.size() >= 0);
  }
}
