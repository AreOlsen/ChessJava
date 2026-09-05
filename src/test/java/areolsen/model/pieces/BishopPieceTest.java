package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for BishopPiece. */
public class BishopPieceTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board();
  }

  @Test
  public void testBishopGetValue() {
    // Get the actual bishop from the board at starting position
    BishopPiece bishop = (BishopPiece) board.getPiece(new Position(2, 0)).get();
    assertEquals(3.2d, bishop.getValue());
  }

  @Test
  public void testBishopGetType() {
    // Get the actual bishop from the board
    BishopPiece bishop = (BishopPiece) board.getPiece(new Position(2, 0)).get();
    assertEquals("B", bishop.getType());
  }

  @Test
  public void testBishopBlockedByPieces() {
    // Bishop at (2,0) is blocked by pawns on both diagonal directions
    // Should have no legal moves
    List<Move> moves = board.getLegalMoves(new Position(2, 0));
    assertEquals(0, moves.size());
  }

  @Test
  public void testBishopDiagonalMove() {
    // Use empty board to test clean diagonal movements
    Board emptyBoard = new Board().emptyBoard();
    new BishopPiece(emptyBoard, new Position(2, 0), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(2, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 1))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(4, 2))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(5, 3))));
  }

  @Test
  public void testBishopHorizontalMovesNotAllowed() {
    // Verify bishop cannot move horizontally on a clean board
    Board emptyBoard = new Board().emptyBoard();
    new BishopPiece(emptyBoard, new Position(2, 0), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(2, 0));
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 0))));
  }

  @Test
  public void testBishopVerticalMovesNotAllowed() {
    // Verify bishop cannot move vertically on a clean board
    Board emptyBoard = new Board().emptyBoard();
    new BishopPiece(emptyBoard, new Position(2, 0), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(2, 0));
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(2, 1))));
  }

  @Test
  public void testBishopCaptureOpponent() {
    // Use empty board for clean capture scenario
    Board emptyBoard = new Board().emptyBoard();
    new BishopPiece(emptyBoard, new Position(2, 0), Side.WHITE);
    // Place black pawn at (4,2) on the diagonal
    new PawnPiece(emptyBoard, new Position(4, 2), Side.BLACK);
    // Bishop should be able to capture the pawn at (4,2)
    List<Move> moves = emptyBoard.getLegalMoves(new Position(2, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(4, 2))));
  }
}
