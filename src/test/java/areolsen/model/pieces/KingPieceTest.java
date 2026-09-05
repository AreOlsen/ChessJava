package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for KingPiece. */
public class KingPieceTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board().emptyBoard();
  }

  @Test
  public void testKingGetValue() {
    KingPiece king = new KingPiece(board, new Position(0, 0), Side.WHITE);
    assertEquals(100000d, king.getValue());
  }

  @Test
  public void testKingGetType() {
    KingPiece king = new KingPiece(board, new Position(0, 0), Side.WHITE);
    assertEquals("K", king.getType());
  }

  @Test
  public void testKingBlockedByPieces() {
    // Use standard board initialization where King at (4,0) is blocked by surrounding pieces
    Board standardBoard = new Board();
    List<Move> moves = standardBoard.getLegalMoves(new Position(4, 0));
    assertEquals(0, moves.size());
  }

  @Test
  public void testKingMoveOneSquareHorizontal() {
    // King at (4,0) can move one square horizontally
    new KingPiece(board, new Position(4, 0), Side.WHITE);
    List<Move> moves = board.getLegalMoves(new Position(4, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 0))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(5, 0))));
  }

  @Test
  public void testKingMoveOneSquareVertical() {
    // King at (4,0) should be able to move one square vertically to (4,1)
    new KingPiece(board, new Position(4, 0), Side.WHITE);
    List<Move> moves = board.getLegalMoves(new Position(4, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(4, 1))));
  }

  @Test
  public void testKingMoveOneSquareDiagonal() {
    // King at (4,1) should be able to move one square diagonally in multiple directions
    new KingPiece(board, new Position(4, 1), Side.WHITE);
    List<Move> moves = board.getLegalMoves(new Position(4, 1));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 0))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 1))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(3, 2))));
  }

  @Test
  public void testKingCannotMoveMoreThanOneSquare() {
    // Place king at center (4,4) on empty board
    new KingPiece(board, new Position(4, 4), Side.WHITE);
    List<Move> moves = board.getLegalMoves(new Position(4, 4));
    // King cannot move more than one square in any direction
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(6, 4))));
  }

  @Test
  public void testKingCaptureOpponent() {
    // King should be able to capture the adjacent piece at (5,5)
    new KingPiece(board, new Position(4, 4), Side.WHITE);
    // Place a black pawn diagonally adjacent to the king
    new PawnPiece(board, new Position(5, 5), Side.BLACK);
    // King should be able to capture the adjacent piece
    List<Move> moves = board.getLegalMoves(new Position(4, 4));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(5, 5))));
  }
}
