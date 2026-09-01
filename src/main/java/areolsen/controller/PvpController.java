package areolsen.controller;

import areolsen.model.ChessBoard;
import areolsen.model.grid.Position;
import areolsen.view.ViewHandler;
import java.util.Optional;

/** PvPController. */
public class PvpController extends Controller {

  /**
   * Instantiate new player vs player controller.
   *
   * @param handler ViewHandler reference for escaping to menu.
   * @param board ChessBoard reference for game state logic controller handling.
   */
  public PvpController(ViewHandler handler, ChessBoard board) {
    super(handler, board);
  }

  /**
   * Click hook for PvP game mode that only allows the current side to move a piece.
   *
   * @see Controller#mouseClicked
   */
  @Override
  protected void clickHook(Optional<Position> originalPosition, Position newPosition) {
    if (originalPosition.isEmpty()) {
      super.clickHook(originalPosition, newPosition);
      return;
    }

    boolean moved = board.movePiece(originalPosition.get(), newPosition);
    if (moved) {
      activeChosenPosition = Optional.empty();
      return;
    }

    super.clickHook(originalPosition, newPosition);
  }
}
