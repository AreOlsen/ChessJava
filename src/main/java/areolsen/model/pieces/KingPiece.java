package areolsen.model.pieces;

import areolsen.model.ChessBoard;
import areolsen.model.ChessPiece;
import areolsen.model.ChessSide;
import areolsen.model.grid.Position;

/**
 * KingPiece. Can move in any direction by 1 cell. Game is lost if king is in checkmate - a state
 * where the king is threatened and legally can't move anywhere.
 */
public class KingPiece extends ChessPiece {
  /**
   * Instantiate king piece by calling chesspiece constructor.
   *
   * @param board ChessBoard the king piece belongs to.
   * @param side ChessSide the king piece belongs to.
   */
  public KingPiece(ChessBoard board, Position position, ChessSide side) {
    super(board, position, side);
  }

  @Override
  public double getValue() {
    return 100000d;
  }

  @Override
  public String getType() {
    return "K";
  }

  @Override
  protected boolean movementPattern(Position end) {
    final int xOffset = Math.abs(end.x() - getPosition().x());
    final int yOffset = Math.abs(end.y() - getPosition().y());
    return xOffset <= 1 && yOffset <= 1;
  }
}
