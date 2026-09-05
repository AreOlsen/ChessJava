package areolsen.controller;

import areolsen.model.Board;
import areolsen.model.Move;
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
  public PvpController(ViewHandler handler, Board board) {
    super(handler, board);
  }

  /**
   * Click hook for PvP game mode that only allows the current side to move a piece.
   *
   * @see Controller#mouseClicked
   */
  @Override
  protected void clickHook(Optional<Position> originalPosition, Position newPosition) {
    // If no cell selected update actively chosen cell.
    if (originalPosition.isEmpty()) {
      super.clickHook(originalPosition, newPosition);
      return;
    }

    // If cell was selected, and move was legal, perform it.
    Optional<Move> move = board.movePiece(originalPosition.get(), newPosition, true);
    if (move.isPresent()) {
      activeChosenPosition = Optional.empty();
      return;
    }

    // If couldn't move, just update actively chosen cell.
    super.clickHook(originalPosition, newPosition);
  }
}
