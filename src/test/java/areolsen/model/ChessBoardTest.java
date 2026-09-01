package areolsen.model;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.grid.Position;
import areolsen.model.pieces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for ChessBoard. */
public class ChessBoardTest {
  private ChessBoard board;

  @BeforeEach
  public void setUp() {
    board = new ChessBoard();
  }

  @Test
  public void testBoardInitialization() {
    assertEquals(8, board.getWidth());
    assertEquals(8, board.getHeight());
    assertEquals(ChessSide.WHITE, board.getSide());
  }

  @Test
  public void testBoardReset() {
    board.movePiece(new Position(0, 1), new Position(0, 3));
    board.reset();
    assertEquals(ChessSide.WHITE, board.getSide());
    assertEquals(32, countAllPieces());
  }

  @Test
  public void testGetPiece() {
    Optional<ChessPiece> piece = board.getPiece(new Position(0, 0));
    assertTrue(piece.isPresent());
    assertEquals("R", piece.get().getType());
  }

  @Test
  public void testGetPieceEmpty() {
    Optional<ChessPiece> piece = board.getPiece(new Position(4, 4));
    assertTrue(piece.isEmpty());
  }

  @Test
  public void testMovePieceWrongTurn() {
    assertFalse(board.movePiece(new Position(0, 6), new Position(0, 5)));
  }

  @Test
  public void testMovePieceChangeSide() {
    board.movePiece(new Position(0, 1), new Position(0, 2));
    assertEquals(ChessSide.BLACK, board.getSide());
    board.movePiece(new Position(0, 6), new Position(0, 5));
    assertEquals(ChessSide.WHITE, board.getSide());
  }

  @Test
  public void testGetLegalMovesForPawn() {
    List<Position> moves = board.getLegalMoves(new Position(0, 1));
    assertEquals(2, moves.size());
    assertTrue(moves.contains(new Position(0, 2)));
    assertTrue(moves.contains(new Position(0, 3)));
  }

  @Test
  public void testGetLegalMovesForEmptyPosition() {
    List<Position> moves = board.getLegalMoves(new Position(4, 4));
    assertEquals(0, moves.size());
  }

  @Test
  public void testKingInCheckFalseAtStart() {
    assertFalse(board.kingInCheck(ChessSide.WHITE));
    assertFalse(board.kingInCheck(ChessSide.BLACK));
  }

  @Test
  public void testGameOverFalseAtStart() {
    assertFalse(board.gameOver());
  }

  @Test
  public void testGetBoardValueInitial() {
    double whiteValue = board.getBoardValue(ChessSide.WHITE);
    double blackValue = board.getBoardValue(ChessSide.BLACK);
    assertEquals(whiteValue, -blackValue);
  }

  @Test
  public void testPromotePawnToQueenNotPawn() {
    assertFalse(board.promotePawnToQueen(new Position(0, 0)));
  }

  @Test
  public void testIteratorCountsAllPieces() {
    int count = 0;
    for (ChessPiece piece : board) {
      count++;
    }
    assertEquals(32, count);
  }

  private List<ChessPiece> getPiecesOfType(String type, ChessSide side) {
    ArrayList<ChessPiece> pieces = new ArrayList<>();
    for (ChessPiece piece : board) {
      if (piece.getType().equals(type) && piece.getSide() == side) {
        pieces.add(piece);
      }
    }
    return pieces;
  }

  private int countAllPieces() {
    int count = 0;
    for (ChessPiece piece : board) {
      count++;
    }
    return count;
  }

  @Test
  public void testInitialPiecePositionsWhitePawns() {
    // Verify all white pawns are at row 1
    for (int col = 0; col < 8; col++) {
      Optional<ChessPiece> piece = board.getPiece(new Position(col, 1));
      assertTrue(piece.isPresent(), "White pawn missing at column " + col);
      assertEquals("P", piece.get().getType());
      assertEquals(ChessSide.WHITE, piece.get().getSide());
    }
  }

  @Test
  public void testInitialPiecePositionsBlackPawns() {
    // Verify all black pawns are at row 6
    for (int col = 0; col < 8; col++) {
      Optional<ChessPiece> piece = board.getPiece(new Position(col, 6));
      assertTrue(piece.isPresent(), "Black pawn missing at column " + col);
      assertEquals("P", piece.get().getType());
      assertEquals(ChessSide.BLACK, piece.get().getSide());
    }
  }

  @Test
  public void testInitialPiecePositionsWhiteMajors() {
    // Row 0: R N B Q K B N R
    assertEquals("R", board.getPiece(new Position(0, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(0, 0)).get().getSide());

    assertEquals("N", board.getPiece(new Position(1, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(1, 0)).get().getSide());

    assertEquals("B", board.getPiece(new Position(2, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(2, 0)).get().getSide());

    assertEquals("Q", board.getPiece(new Position(3, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(3, 0)).get().getSide());

    assertEquals("K", board.getPiece(new Position(4, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(4, 0)).get().getSide());

    assertEquals("B", board.getPiece(new Position(5, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(5, 0)).get().getSide());

    assertEquals("N", board.getPiece(new Position(6, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(6, 0)).get().getSide());

    assertEquals("R", board.getPiece(new Position(7, 0)).get().getType());
    assertEquals(ChessSide.WHITE, board.getPiece(new Position(7, 0)).get().getSide());
  }

  @Test
  public void testInitialPiecePositionsBlackMajors() {
    // Row 7: R N B Q K B N R
    assertEquals("R", board.getPiece(new Position(0, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(0, 7)).get().getSide());

    assertEquals("N", board.getPiece(new Position(1, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(1, 7)).get().getSide());

    assertEquals("B", board.getPiece(new Position(2, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(2, 7)).get().getSide());

    assertEquals("Q", board.getPiece(new Position(3, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(3, 7)).get().getSide());

    assertEquals("K", board.getPiece(new Position(4, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(4, 7)).get().getSide());

    assertEquals("B", board.getPiece(new Position(5, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(5, 7)).get().getSide());

    assertEquals("N", board.getPiece(new Position(6, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(6, 7)).get().getSide());

    assertEquals("R", board.getPiece(new Position(7, 7)).get().getType());
    assertEquals(ChessSide.BLACK, board.getPiece(new Position(7, 7)).get().getSide());
  }

  @Test
  public void testInitialPiecePositionsEmptySquares() {
    // Verify rows 2-5 are empty
    for (int row = 2; row < 6; row++) {
      for (int col = 0; col < 8; col++) {
        Optional<ChessPiece> piece = board.getPiece(new Position(col, row));
        assertTrue(piece.isEmpty(), "Square at (" + col + ", " + row + ") should be empty");
      }
    }
  }

  @Test
  public void testInitialBoardHasAllPieces() {
    // Verify total piece count and count by type
    assertEquals(32, countAllPieces());
    assertEquals(8, getPiecesOfType("P", ChessSide.WHITE).size());
    assertEquals(8, getPiecesOfType("P", ChessSide.BLACK).size());
    assertEquals(2, getPiecesOfType("R", ChessSide.WHITE).size());
    assertEquals(2, getPiecesOfType("R", ChessSide.BLACK).size());
    assertEquals(2, getPiecesOfType("N", ChessSide.WHITE).size());
    assertEquals(2, getPiecesOfType("N", ChessSide.BLACK).size());
    assertEquals(2, getPiecesOfType("B", ChessSide.WHITE).size());
    assertEquals(2, getPiecesOfType("B", ChessSide.BLACK).size());
    assertEquals(1, getPiecesOfType("Q", ChessSide.WHITE).size());
    assertEquals(1, getPiecesOfType("Q", ChessSide.BLACK).size());
    assertEquals(1, getPiecesOfType("K", ChessSide.WHITE).size());
    assertEquals(1, getPiecesOfType("K", ChessSide.BLACK).size());
  }
}
