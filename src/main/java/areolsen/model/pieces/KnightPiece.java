package areolsen.model.pieces;

import areolsen.model.ChessBoard;
import areolsen.model.ChessPiece;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;

/**
 * KnightPiece. The knight piece can move in an L shape - 2 cells in any direction and 1cell to the
 * side.
 */
public class KnightPiece extends ChessPiece {
  /**
   * Instantiate knight piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the knight piece belongs to.
   * @param side ChessSide the knight piece belongs to.
   */
  public KnightPiece(ChessBoard board, Position position, ChessSide side) {
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
