package areolsen.model.pieces;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.ChessBoard;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for RookPiece. */
public class RookPieceTest {
  private ChessBoard board;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
  }

  @Test
  public void testRookGetValue() {
    // Get the actual rook from the board at starting position
    RookPiece rook = (RookPiece) board.getPiece(new Position(0, 0)).get();
    assertEquals(5.0d, rook.getValue());
  }

  @Test
  public void testRookGetType() {
    // Get the actual rook from the board
    RookPiece rook = (RookPiece) board.getPiece(new Position(0, 0)).get();
    assertEquals("R", rook.getType());
  }

  @Test
  public void testRookBlockedByPieces() {
    // A rook at (0,0) should have no legal moves as it's blocked by pawns
    List<Position> moves = board.getLegalMoves(new Position(0, 0));
    assertEquals(0, moves.size());
  }

  @Test
  public void testRookHorizontalMove() {
    // Use empty board for clean movement test
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    RookPiece rook = new RookPiece(emptyBoard, new Position(0, 0), ChessSide.WHITE);
    List<Position> moves = emptyBoard.getLegalMoves(new Position(0, 0));
    assertTrue(moves.contains(new Position(1, 0)));
    assertTrue(moves.contains(new Position(2, 0)));
    assertTrue(moves.contains(new Position(3, 0)));
  }

  @Test
  public void testRookVerticalMove() {
    // Clear one vertical space above the rook at (0,0)
    // Rook should be able to move to the cleared position (0,1)
    board.getGrid().removePiece(new Position(0, 1));
    List<Position> moves = board.getLegalMoves(new Position(0, 0));
    assertTrue(moves.contains(new Position(0, 1)));
  }

  @Test
  public void testRookCaptureOpponentPiece() {
    // Clear vertical path from (0,0) down to rank 6, where black pawn is located
    // Rook should be able to capture the black piece at (0,6)
    for (int i = 1; i < 7; i++) {
      board.getGrid().removePiece(new Position(0, i));
    }
    List<Position> moves = board.getLegalMoves(new Position(0, 0));
    assertTrue(moves.contains(new Position(0, 6)));
  }

  @Test
  public void testRookCannotCaptureOwnPiece() {
    // Rook should never be able to move to its own position
    List<Position> moves = board.getLegalMoves(new Position(0, 0));
    assertFalse(moves.contains(new Position(0, 0)));
  }

  @Test
  public void testRookDiagonalMovesNotAllowed() {
    // Use empty board to test movement patterns
    ChessBoard emptyBoard = new ChessBoard().emptyBoard();
    RookPiece rook = new RookPiece(emptyBoard, new Position(0, 0), ChessSide.WHITE);
    // Rook should NOT be able to move diagonally to (1,1)
    List<Position> moves = emptyBoard.getLegalMoves(new Position(0, 0));
    assertFalse(moves.contains(new Position(1, 1)));
  }
}
