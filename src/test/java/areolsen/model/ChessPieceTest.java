package areolsen.model;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.grid.Position;
import areolsen.model.pieces.PawnPiece;
import areolsen.model.pieces.RookPiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for ChessPiece abstract class. */
public class ChessPieceTest {
  private ChessBoard board;
  private ChessPiece pawn;
  private ChessPiece rook;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
    // Create pieces with explicit Position parameter
    pawn = new PawnPiece(board, new Position(3, 4), ChessSide.WHITE);
    rook = new RookPiece(board, new Position(2, 2), ChessSide.BLACK);
  }

  @Test
  public void testPieceGetSide() {
    assertEquals(ChessSide.WHITE, pawn.getSide());
    assertEquals(ChessSide.BLACK, rook.getSide());
  }

  @Test
  public void testPieceGetPosition() {
    // Pawn was placed at (3, 4) during setUp
    assertEquals(new Position(3, 4), pawn.getPosition());
  }

  @Test
  public void testPawnGetValue() {
    assertEquals(1.0d, pawn.getValue());
  }

  @Test
  public void testPawnGetType() {
    assertEquals("P", pawn.getType());
  }

  @Test
  public void testRookGetValue() {
    assertEquals(5.0d, rook.getValue());
  }

  @Test
  public void testRookGetType() {
    assertEquals("R", rook.getType());
  }

  @Test
  public void testPawnCanMoveTwoSquaresFromStart() {
    // Use a fresh board with all pieces initialized and placed properly
    ChessBoard testBoard = new ChessBoard();
    // White pawn at position (0, 1) should be able to move two squares forward to (0, 3)
    assertTrue(testBoard.getLegalMoves(new Position(0, 1)).contains(new Position(0, 3)));
  }

  @Test
  public void testLegalMoveSamePosition() {
    // A pawn should have legal moves available from starting position
    assertTrue(!board.getLegalMoves(new Position(0, 1)).isEmpty());
    // A piece cannot move to its current position
    assertFalse(board.getLegalMoves(new Position(0, 1)).contains(new Position(0, 1)));
  }
}
