package areolsen.model.pieces;

import areolsen.model.Board;
import areolsen.model.Piece;
import areolsen.model.Side;
import areolsen.model.grid.Position;

/**
 * KnightPiece. The knight piece can move in an L shape - 2 cells in any direction and 1cell to the
 * side.
 */
public class KnightPiece extends Piece {
  /**
   * Instantiate knight piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the knight piece belongs to.
   * @param side ChessSide the knight piece belongs to.
   */
  public KnightPiece(Board board, Position position, Side side) {
    super(board, position, side);
  }

  @Override
  public double getValue() {
    return 3.25d;
  }

  @Override
  public String getType() {
    return "N";
  }

  @Override
  protected boolean movementPattern(Position end) {
    final int xOffset = Math.abs(end.x() - getPosition().x());
    final int yOffset = Math.abs(end.y() - getPosition().y());
    return (xOffset == 2 && yOffset == 1) || (xOffset == 1 & yOffset == 2);
  }
}
