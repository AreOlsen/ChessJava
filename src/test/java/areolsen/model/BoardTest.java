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
public class BoardTest {
  private Board board;

  @BeforeEach
  public void setUp() {
    board = new Board();
  }

  @Test
  public void testBoardInitialization() {
    assertEquals(8, board.getWidth());
    assertEquals(8, board.getHeight());
    assertEquals(Side.WHITE, board.getSide());
  }

  @Test
  public void testBoardReset() {
    board.movePiece(new Position(0, 1), new Position(0, 3), false);
    board.reset();
    assertEquals(Side.WHITE, board.getSide());
    assertEquals(32, countAllPieces());
  }

  @Test
  public void testGetPiece() {
    Optional<Piece> piece = board.getPiece(new Position(0, 0));
    assertTrue(piece.isPresent());
    assertEquals("R", piece.get().getType());
  }

  @Test
  public void testGetPieceEmpty() {
    Optional<Piece> piece = board.getPiece(new Position(4, 4));
    assertTrue(piece.isEmpty());
  }

  @Test
  public void testMovePieceWrongTurn() {
    // Attempting to move Black piece (y=6) on White's turn should return empty Optional
    Optional<Move> move = board.movePiece(new Position(0, 6), new Position(0, 5), false);
    assertTrue(move.isEmpty());
  }

  @Test
  public void testMovePieceChangeSide() {
    board.movePiece(new Position(0, 1), new Position(0, 2), false);
    assertEquals(Side.BLACK, board.getSide());
    board.movePiece(new Position(0, 6), new Position(0, 5), false);
    assertEquals(Side.WHITE, board.getSide());
  }

  @Test
  public void testGetLegalMovesForPawn() {
    List<Move> moves = board.getLegalMoves(new Position(0, 1));
    assertEquals(2, moves.size());
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 2))));
    assertTrue(moves.stream().anyMatch(m -> m.end().equals(new Position(0, 3))));
  }

  @Test
  public void testGetLegalMovesForEmptyPosition() {
    List<Move> moves = board.getLegalMoves(new Position(4, 4));
    assertEquals(0, moves.size());
  }

  @Test
  public void testKingInCheckFalseAtStart() {
    assertFalse(board.kingInCheck(Side.WHITE));
    assertFalse(board.kingInCheck(Side.BLACK));
  }

  @Test
  public void testGameOverFalseAtStart() {
    assertFalse(board.gameOver(false));
  }

  @Test
  public void testGetBoardValueInitial() {
    double whiteValue = board.getBoardValue(Side.WHITE);
    double blackValue = board.getBoardValue(Side.BLACK);
    assertEquals(whiteValue, -blackValue);
  }

  @Test
  public void testPromotePawnToQueenNotPawn() {
    assertFalse(board.promotePawnToQueen(new Position(0, 0)));
  }

  @Test
  public void testIteratorCountsAllPieces() {
    int count = 0;
    for (Piece piece : board) {
      count++;
    }
    assertEquals(32, count);
  }

  private List<Piece> getPiecesOfType(String type, Side side) {
    ArrayList<Piece> pieces = new ArrayList<>();
    for (Piece piece : board) {
      if (piece.getType().equals(type) && piece.getSide() == side) {
        pieces.add(piece);
      }
    }
    return pieces;
  }

  private int countAllPieces() {
    int count = 0;
    for (Piece piece : board) {
      count++;
    }
    return count;
  }

  @Test
  public void testInitialPiecePositionsWhitePawns() {
    for (int col = 0; col < 8; col++) {
      Optional<Piece> piece = board.getPiece(new Position(col, 1));
      assertTrue(piece.isPresent(), "White pawn missing at column " + col);
      assertEquals("P", piece.get().getType());
      assertEquals(Side.WHITE, piece.get().getSide());
    }
  }

  @Test
  public void testInitialPiecePositionsBlackPawns() {
    for (int col = 0; col < 8; col++) {
      Optional<Piece> piece = board.getPiece(new Position(col, 6));
      assertTrue(piece.isPresent(), "Black pawn missing at column " + col);
      assertEquals("P", piece.get().getType());
      assertEquals(Side.BLACK, piece.get().getSide());
    }
  }

  @Test
  public void testInitialPiecePositionsWhiteMajors() {
    assertEquals("R", board.getPiece(new Position(0, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(0, 0)).get().getSide());

    assertEquals("N", board.getPiece(new Position(1, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(1, 0)).get().getSide());

    assertEquals("B", board.getPiece(new Position(2, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(2, 0)).get().getSide());

    assertEquals("Q", board.getPiece(new Position(3, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(3, 0)).get().getSide());

    assertEquals("K", board.getPiece(new Position(4, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(4, 0)).get().getSide());

    assertEquals("B", board.getPiece(new Position(5, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(5, 0)).get().getSide());

    assertEquals("N", board.getPiece(new Position(6, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(6, 0)).get().getSide());

    assertEquals("R", board.getPiece(new Position(7, 0)).get().getType());
    assertEquals(Side.WHITE, board.getPiece(new Position(7, 0)).get().getSide());
  }

  @Test
  public void testInitialPiecePositionsBlackMajors() {
    assertEquals("R", board.getPiece(new Position(0, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(0, 7)).get().getSide());

    assertEquals("N", board.getPiece(new Position(1, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(1, 7)).get().getSide());

    assertEquals("B", board.getPiece(new Position(2, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(2, 7)).get().getSide());

    assertEquals("Q", board.getPiece(new Position(3, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(3, 7)).get().getSide());

    assertEquals("K", board.getPiece(new Position(4, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(4, 7)).get().getSide());

    assertEquals("B", board.getPiece(new Position(5, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(5, 7)).get().getSide());

    assertEquals("N", board.getPiece(new Position(6, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(6, 7)).get().getSide());

    assertEquals("R", board.getPiece(new Position(7, 7)).get().getType());
    assertEquals(Side.BLACK, board.getPiece(new Position(7, 7)).get().getSide());
  }

  @Test
  public void testInitialPiecePositionsEmptySquares() {
    for (int row = 2; row < 6; row++) {
      for (int col = 0; col < 8; col++) {
        Optional<Piece> piece = board.getPiece(new Position(col, row));
        assertTrue(piece.isEmpty(), "Square at (" + col + ", " + row + ") should be empty");
      }
    }
  }

  @Test
  public void testInitialBoardHasAllPieces() {
    assertEquals(32, countAllPieces());
    assertEquals(8, getPiecesOfType("P", Side.WHITE).size());
    assertEquals(8, getPiecesOfType("P", Side.BLACK).size());
    assertEquals(2, getPiecesOfType("R", Side.WHITE).size());
    assertEquals(2, getPiecesOfType("R", Side.BLACK).size());
    assertEquals(2, getPiecesOfType("N", Side.WHITE).size());
    assertEquals(2, getPiecesOfType("N", Side.BLACK).size());
    assertEquals(2, getPiecesOfType("B", Side.WHITE).size());
    assertEquals(2, getPiecesOfType("B", Side.BLACK).size());
    assertEquals(1, getPiecesOfType("Q", Side.WHITE).size());
    assertEquals(1, getPiecesOfType("Q", Side.BLACK).size());
    assertEquals(1, getPiecesOfType("K", Side.WHITE).size());
    assertEquals(1, getPiecesOfType("K", Side.BLACK).size());
  }
}
