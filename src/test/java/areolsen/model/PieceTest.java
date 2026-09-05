package areolsen.model;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.grid.Position;
import areolsen.model.pieces.PawnPiece;
import areolsen.model.pieces.RookPiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for ChessPiece abstract class. */
public class PieceTest {
  private Board board;
  private Piece pawn;
  private Piece rook;

  @BeforeEach
  public void setUp() {
    board = new Board();
    // Create pieces with explicit Position parameter
    pawn = new PawnPiece(board, new Position(3, 4), Side.WHITE);
    rook = new RookPiece(board, new Position(2, 2), Side.BLACK);
  }

  @Test
  public void testPieceGetSide() {
    assertEquals(Side.WHITE, pawn.getSide());
    assertEquals(Side.BLACK, rook.getSide());
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
  public void testLegalMoveSamePosition() {
    // A pawn should have legal moves available from starting position
    assertTrue(!board.getLegalMoves(new Position(0, 1)).isEmpty());
    // A piece cannot move to its current position
    assertFalse(board.getLegalMoves(new Position(0, 1)).contains(new Position(0, 1)));
  }
}
