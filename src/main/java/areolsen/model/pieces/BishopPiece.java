package areolsen.model.pieces;

import areolsen.model.Board;
import areolsen.model.Piece;
import areolsen.model.Side;
import areolsen.model.grid.Position;

/** BishopPiece. Can move diagonally across board. */
public class BishopPiece extends Piece {
  /**
   * Instantiate bishop piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the bishop piece belongs to.
   * @param side ChessSide the bishop piece belongs to.
   */
  public BishopPiece(Board board, Position position, Side side) {
    super(board, position, side);
  }

  @Override
  public double getValue() {
    return 3.2d;
  }

  @Override
  public String getType() {
    return "B";
  }

  @Override
  protected boolean movementPattern(Position end) {
    // On line check:
    boolean onDiagonal =
        Math.abs(end.x() - getPosition().x()) == Math.abs(end.y() - getPosition().y());
    if (!onDiagonal) {
      return false;
    }

    // Raycast line check.
    int directionX = Integer.signum(end.x() - getPosition().x());
    int directionY = Integer.signum(end.y() - getPosition().y());

    // Loop in direction.
    Position checkPos = getPosition();
    for (int i = 0; i < Math.min(board.getWidth(), board.getHeight()); i++) {
      checkPos = new Position(checkPos.x() + directionX, checkPos.y() + directionY);

      // If reached end position.
      if (end.equals(checkPos)) {
        return true;
      } else if (board.getPiece(checkPos).isPresent()) {
        break; // Obstructed before reaching end position.
      }
    }
    return false;
  }
}
