package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.ChessBoard;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for QueenPiece. */
public class QueenPieceTest {
  private ChessBoard board;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
  }

  @Test
  public void testQueenGetValue() {
    // Get the actual queen from the board at starting position
    QueenPiece queen = (QueenPiece) board.getPiece(new Position(3, 0)).get();
    assertEquals(9.75d, queen.getValue());
  }

  @Test
  public void testQueenGetType() {
    // Get the actual queen from the board
    QueenPiece queen = (QueenPiece) board.getPiece(new Position(3, 0)).get();
    assertEquals("Q", queen.getType());
  }

  @Test
  public void testQueenBlockedByPieces() {
    // Queen at (3,0) is blocked by pawns on all sides
    // Should have no legal moves
    List<Position> moves = board.getLegalMoves(new Position(3, 0));
    assertEquals(0, moves.size());
  }

  @Test
  public void testQueenHorizontalMove() {
    // Use empty board for clean horizontal movement test
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    QueenPiece queen = new QueenPiece(emptyBoard, new Position(3, 0), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(3, 0));
    assertTrue(moves.contains(new Position(4, 0)));
    assertTrue(moves.contains(new Position(5, 0)));
  }

  @Test
  public void testQueenVerticalMove() {
    // Use empty board for clean vertical test
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    QueenPiece queen = new QueenPiece(emptyBoard, new Position(3, 0), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(3, 0));
    assertTrue(moves.contains(new Position(3, 1)));
    assertTrue(moves.contains(new Position(3, 2)));
  }

  @Test
  public void testQueenDiagonalMove() {
    // Use empty board for clean diagonal test
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    QueenPiece queen = new QueenPiece(emptyBoard, new Position(3, 3), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(3, 3));
    // Queen can move diagonally in all four directions when board is clear
    assertTrue(moves.contains(new Position(5, 5))); // Down-right diagonal
    assertTrue(moves.contains(new Position(2, 2))); // Up-left diagonal
    assertTrue(moves.contains(new Position(3, 4))); // Vertical up
    assertTrue(moves.contains(new Position(4, 3))); // Horizontal right
  }

  @Test
  public void testQueenCannotJumpOverPieces() {
    // Queen at (3,0) is blocked by pawns on rank 1
    // Should not be able to move anywhere
    List<Position> moves = board.getLegalMoves(new Position(3, 0));
    assertTrue(moves.isEmpty());
  }
}
