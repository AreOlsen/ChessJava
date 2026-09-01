package areolsen.model.pieces;

import areolsen.model.ChessBoard;
import areolsen.model.ChessPiece;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;

/**
 * QueenPiece. Can move in any direction by 1 cell like {@link KingPiece}. Can also move in diagonal
 * directions like {@link BishopPiece}, and in line directions like {@link RookPiece}.
 *
 * @see RookPiece
 * @see BishopPiece
 * @see KingPiece
 */
public class QueenPiece extends ChessPiece {
  /**
   * Instantiate queen piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the queen piece belongs to.
   * @param side ChessSide the queen piece belongs to.
   */
  public QueenPiece(ChessBoard board, Position position, ChessSide side) {
    super(board, position, side);
  }

  @Override
  public double getValue() {
    return 9.75d;
  }

  @Override
  public String getType() {
    return "Q";
  }

  @Override
  protected boolean movementPattern(Position end) {
    Position position = getPosition();

    // Square attacks.
    int xDifference = position.xDifference(end);
    int yDifference = position.yDifference(end);
    if (-1 <= xDifference && xDifference <= 1 && -1 <= yDifference && yDifference <= 1) {
      return true;
    }

    // Lines attack check:
    if (!position.onSameLine(end) || position.onDiagonal(end)) {
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
