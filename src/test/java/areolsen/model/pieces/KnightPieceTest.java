package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for KnightPiece. */
public class KnightPieceTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board();
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
    Board emptyBoard = new Board().emptyBoard();
    new KnightPiece(emptyBoard, new Position(1, 0), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(1, 0));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 2))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(2, 2))));
  }

  @Test
  public void testKnightCanJumpOverPieces() {
    // Knights can jump over other pieces, so even with blocked board
    // knight at (1,0) should still have valid moves
    List<Move> moves = board.getLegalMoves(new Position(1, 0));
    assertFalse(moves.isEmpty());
  }

  @Test
  public void testKnightCannotMoveDiagonally() {
    // Create an empty board to test knight move patterns
    Board emptyBoard = new Board().emptyBoard();
    new KnightPiece(emptyBoard, new Position(1, 1), Side.WHITE);
    List<Move> moves = emptyBoard.getLegalMoves(new Position(1, 1));
    // Knights cannot move diagonally - (2,2) is a diagonal move
    assertFalse(moves.stream().anyMatch(m -> m.end().equals(new Position(2, 2))));
  }
}
