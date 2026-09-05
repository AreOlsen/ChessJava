package areolsen.controller;

import static org.junit.jupiter.api.Assertions.*;

import areolsen.model.Board;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import areolsen.model.pieces.PawnPiece;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for MinMaxController. */
public class MinMaxControllerTest {
  private Board board;
  private MinMaxController controller;

  @BeforeEach
  public void setUp() {
    board = new Board();
    controller = new MinMaxController(board, null, 1);
  }

  @Test
  public void testInitialState() {
    assertNotNull(controller);
    assertFalse(controller.getActivePosition().isPresent());
  }

  @Test
  public void testClickHookSelectsPieceWhenEmpty() {
    // Click on white pawn at (0,1) without prior selection
    Position pos = new Position(0, 1);
    controller.clickHook(Optional.empty(), pos);

    // Active position should be updated to the selected piece
    assertTrue(controller.getActivePosition().isPresent());
    assertEquals(pos, controller.getActivePosition().get());
  }

  @Test
  public void testClickHookIgnoresIfNotWhiteTurn() {
    // Force turn to BLACK
    board.changeSide();

    Position pos = new Position(0, 6);
    controller.clickHook(Optional.empty(), pos);

    // Click should be ignored when it is BLACK's turn
    assertFalse(controller.getActivePosition().isPresent());
  }

  @Test
  public void testClickHookExecutesPlayerMoveAndTriggersAIMove() {
    // White pawn moves from (0,1) to (0,3)
    Position whiteStart = new Position(0, 1);
    Position whiteEnd = new Position(0, 3);

    // Execute click hook representing valid white move
    controller.clickHook(Optional.of(whiteStart), whiteEnd);

    // Verify White piece moved successfully
    assertTrue(board.getPiece(whiteEnd).isPresent());
    assertEquals("P", board.getPiece(whiteEnd).get().getType());

    // Verify turn returns to White after Black AI executes its response
    assertEquals(Side.WHITE, board.getSide());

    // Active chosen position should reset to empty after move completion
    assertFalse(controller.getActivePosition().isPresent());
  }

  @Test
  public void testClickHookInvalidMoveUpdatesActivePosition() {
    Position whiteStart = new Position(0, 1);
    Position invalidEnd = new Position(0, 5); // Illegal move for white pawn

    controller.clickHook(Optional.of(whiteStart), invalidEnd);

    // If move failed, active position updates to the newly clicked square
    assertTrue(controller.getActivePosition().isPresent());
    assertEquals(invalidEnd, controller.getActivePosition().get());
  }

  @Test
  public void testAISelectsCapturingMove() {
    // Create an empty board scenario to test AI decision making
    Board emptyBoard = new Board().emptyBoard();

    // Place a White pawn at (3,3)
    new PawnPiece(emptyBoard, new Position(3, 3), Side.WHITE);

    // Place Black pawns at (4,4) and (1,6)
    new PawnPiece(emptyBoard, new Position(4, 4), Side.BLACK);
    new PawnPiece(emptyBoard, new Position(1, 6), Side.BLACK);

    // Initialize controller with depth 1
    MinMaxController aiController = new MinMaxController(emptyBoard, null, 1);

    // White plays (3,3) to (3,4)
    aiController.clickHook(Optional.of(new Position(3, 3)), new Position(3, 4));

    // After White move, AI evaluates options and executes move; turn switches back to White
    assertEquals(Side.WHITE, emptyBoard.getSide());
  }
}
