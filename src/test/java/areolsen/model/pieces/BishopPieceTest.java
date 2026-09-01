package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.ChessBoard;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for BishopPiece. */
public class BishopPieceTest {
  private ChessBoard board;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
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
    List<Position> moves = board.getLegalMoves(new Position(2, 0));
    assertEquals(0, moves.size());
  }

  @Test
  public void testBishopDiagonalMove() {
    // Clear diagonal path from (2,0) going down-right to enable bishop movement
    // Remove positions (3,1), (4,2), (5,3)
    // Bishop should be able to move along this diagonal
    board.getGrid().removePiece(new Position(3, 1));
    board.getGrid().removePiece(new Position(4, 2));
    board.getGrid().removePiece(new Position(5, 3));
    List<Position> moves = board.getLegalMoves(new Position(2, 0));
    assertTrue(moves.contains(new Position(3, 1)));
    assertTrue(moves.contains(new Position(4, 2)));
    assertTrue(moves.contains(new Position(5, 3)));
  }

  @Test
  public void testBishopHorizontalMovesNotAllowed() {
    // Clear horizontal rank and verify bishop cannot move horizontally
    // Bishops move only diagonally, not horizontally or vertically
    for (int i = 0; i < 8; i++) {
      board.getGrid().removePiece(new Position(i, 0));
    }
    List<Position> moves = board.getLegalMoves(new Position(2, 0));
    assertFalse(moves.contains(new Position(3, 0)));
  }

  @Test
  public void testBishopVerticalMovesNotAllowed() {
    // Clear vertical file and verify bishop cannot move vertically
    // Bishops move only diagonally, not horizontally or vertically
    for (int i = 0; i < 8; i++) {
      board.getGrid().removePiece(new Position(2, i));
    }
    List<Position> moves = board.getLegalMoves(new Position(2, 0));
    assertFalse(moves.contains(new Position(2, 1)));
  }

  @Test
  public void testBishopCaptureOpponent() {
    // Use empty board for clean capture scenario
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    BishopPiece bishop = new BishopPiece(emptyBoard, new Position(2, 0), ChessSide.WHITE);
    // Place black pawn at (4,2) on the diagonal
    new PawnPiece(emptyBoard, new Position(4, 2), ChessSide.BLACK);
    // Bishop should be able to capture the pawn at (4,2)
    List<Position> moves = emptyBoard.getLegalMoves(new Position(2, 0));
    assertTrue(moves.contains(new Position(4, 2)));
  }
}
