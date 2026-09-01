package areolsen.model.pieces;

import areolsen.model.ChessBoard;
import areolsen.model.ChessPiece;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;

/** RookPiece. Can move in any side directions like a straight line. */
public class RookPiece extends ChessPiece {
  /**
   * Instantiate rook piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the rook piece belongs to.
   * @param side ChessSide the rook piece belongs to.
   */
  public RookPiece(ChessBoard board, Position position, ChessSide side) {
    super(board, position, side);
  }

  @Override
  public double getValue() {
    return 5d;
  }

  @Override
  public String getType() {
    return "R";
  }

  @Override
  protected boolean movementPattern(Position end) {
    Position position = getPosition();
    int xDifference = position.xDifference(end);
    int yDifference = position.yDifference(end);

    // On line check:
    if (!(position.onSameLine(end))) {
      return false;
    }

    // Raycast line check.
    int directionX = Integer.signum(xDifference);
    int directionY = Integer.signum(yDifference);

    // Loop in direction.
    Position checkPos = getPosition();
    for (int i = 0; i < Math.max(board.getWidth(), board.getHeight()); i++) {
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
