package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.ChessBoard;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for KnightPiece. */
public class KnightPieceTest {
  private ChessBoard board;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
  }

  @Test
  public void testKnightGetValue() {
    // Get the actual knight from the board at starting position
    KnightPiece knight = (KnightPiece) board.getPiece(new Position(1, 0)).get();
    assertEquals(3.25d, knight.getValue());
  }

  @Test
  public void testKnightGetType() {
    // Get the actual knight from the board
    KnightPiece knight = (KnightPiece) board.getPiece(new Position(1, 0)).get();
    assertEquals("N", knight.getType());
  }

  @Test
  public void testKnightLShapeMove() {
    // Use empty board for clean knight movement test
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    KnightPiece knight = new KnightPiece(emptyBoard, new Position(1, 0), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(1, 0));
    assertTrue(moves.contains(new Position(0, 2)));
    assertTrue(moves.contains(new Position(2, 2)));
  }

  @Test
  public void testKnightCanJumpOverPieces() {
    // Knights can jump over other pieces, so even with blocked board
    // knight at (1,0) should still have valid moves
    List<Position> moves = board.getLegalMoves(new Position(1, 0));
    assertFalse(moves.isEmpty());
  }

  @Test
  public void testKnightCannotMoveDiagonally() {
    // Create an empty board to test knight move patterns
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    KnightPiece knight = new KnightPiece(emptyBoard, new Position(1, 1), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(1, 1));
    // Knights cannot move diagonally - (2,2) is a diagonal move
    assertFalse(moves.contains(new Position(2, 2)));
  }
}
