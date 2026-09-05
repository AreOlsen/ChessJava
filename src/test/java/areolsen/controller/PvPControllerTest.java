package areolsen.controller;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for PvpController - testing core controller logic. */
public class PvPControllerTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board();
  }

  @Test
  public void testBoardInitialization() {
    // Verify board is properly initialized with 32 pieces
    assertEquals(8, board.getWidth());
    assertEquals(8, board.getHeight());
    assertEquals(Side.WHITE, board.getSide());
  }

  @Test
  public void testWhiteCanMoveFirst() {
    // White should always be the first player to move
    assertEquals(Side.WHITE, board.getSide());
  }

  @Test
  public void testValidMovesAvailable() {
    // White pawn at (0,1) should have 2 legal moves forward
    // Pawn can move 1 or 2 squares forward on first move
    assertTrue(board.getLegalMoves(new Position(0, 1)).size() > 0);
  }

  @Test
  public void testMovePiece() {
    // Move white pawn from (0,1) to (0,2)
    // After move, pawn should be at new position and old position should be empty
    Optional<Move> move = board.movePiece(new Position(0, 1), new Position(0, 2), false);
    assertTrue(move.isPresent());
    assertTrue(board.getPiece(new Position(0, 2)).isPresent());
    assertTrue(board.getPiece(new Position(0, 1)).isEmpty());
  }

  @Test
  public void testInvalidMoveNotAllowed() {
    // Try to move pawn from (0,1) to (0,5) - this should be invalid as pawn can only move 1-2
    // squares
    Optional<Move> move = board.movePiece(new Position(0, 1), new Position(0, 5), false);
    assertTrue(move.isEmpty());
    assertTrue(board.getPiece(new Position(0, 1)).isPresent());
  }

  @Test
  public void testSideSwitchesAfterMove() {
    // After white moves, it should be black's turn
    board.movePiece(new Position(0, 1), new Position(0, 2), false);
    assertEquals(Side.BLACK, board.getSide());
  }

  @Test
  public void testAlternatingMoves() {
    // Verify that sides alternate correctly through multiple moves
    // White move
    board.movePiece(new Position(1, 1), new Position(1, 3), false);
    assertEquals(Side.BLACK, board.getSide());
    // Black move
    board.movePiece(new Position(1, 6), new Position(1, 4), false);
    assertEquals(Side.WHITE, board.getSide());
  }

  @Test
  public void testCannotMoveOpponentPiece() {
    // White is playing first. Try to move black pawn from (0,6) - should not be allowed
    Optional<Move> move = board.movePiece(new Position(0, 6), new Position(0, 5), false);
    assertFalse(move.isPresent());
    // Black pawn should still be at original position
    assertTrue(board.getPiece(new Position(0, 6)).isPresent());
  }
}
