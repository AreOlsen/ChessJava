package areolsen.model.pieces;

import areolsen.model.ChessBoard;
import areolsen.model.ChessPiece;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;

/**
 * PawnPiece. The pawn piece typically can move 1 step forward. Attack diagonally forward. On first
 * step it can move 2 cells forward.
 */
public class PawnPiece extends ChessPiece {

  /**
   * Instantiate pawn piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the pawn piece belongs to.
   * @param side ChessSide the pawn piece belongs to.
   */
  public PawnPiece(ChessBoard board, Position position, ChessSide side) {
    super(board, position, side);
  }

  @Override
  public double getValue() {
    return 1d;
  }

  @Override
  public String getType() {
    return "P";
  }

  @Override
  protected boolean movementPattern(Position end) {
    int direction = getSide() == ChessSide.WHITE ? 1 : -1;

    boolean emptyEnd = board.getPiece(end).isEmpty();
    boolean sameColumn = end.x() == getPosition().x();
    boolean inFrontRow = end.y() == (getPosition().y() + direction);
    boolean inDoubleFrontRow = end.y() == (getPosition().y() + direction * 2);
    boolean inAnglePosition =
        inFrontRow && (end.x() == getPosition().x() - 1 || end.x() == getPosition().x() + 1);
    boolean onStartRow =
        getSide() == ChessSide.WHITE
            ? getPosition().y() == 1
            : getPosition().y() == board.getHeight() - 2;

    // Angle attack.
    if (!emptyEnd && inAnglePosition) {
      return true;
    }

    // Start double movement.
    boolean emptyFront =
        board.getPiece(new Position(getPosition().x(), getPosition().y() + direction)).isEmpty();
    if (onStartRow) {
      if (inDoubleFrontRow && sameColumn && emptyEnd && emptyFront) {
        return true;
      }
    }

    // Ordinary movement.
    if (emptyEnd && inFrontRow && sameColumn) {
      return true;
    }

    return false;
  }
}
