package areolsen.model;

import areolsen.model.grid.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Abstract ChessPiece class. */
public abstract class ChessPiece {
  protected final ChessBoard board;
  protected final ChessSide side;

  /**
   * Initialize a chesspiece on board with side.
   *
   * @param board ChessBoard board the piece belongs to.
   * @param side The side the piece belongs to.
   */
  public ChessPiece(ChessBoard board, Position position, ChessSide side)
      throws IllegalArgumentException {
    if (board == null) {
      throw new IllegalArgumentException("Piece can't belong to null board.");
    }
    this.side = side;
    this.board = board;
    this.board.getGrid().placePiece(position, this);
  }

  /**
   * Gets side of chesspiece.
   *
   * @return ChessSide the piece belongs to.
   */
  public ChessSide getSide() {
    return this.side;
  }

  /**
   * Gets position of chesspiece.
   *
   * @return Position of piece.
   */
  public Position getPosition() {
    return board.getGrid().getPosition(this).get();
  }

  /**
   * Gets the value of the piece.
   *
   * @return value of piece.
   */
  public abstract double getValue();

  /**
   * Gets string representation character of piece. For example "P" for pawn, "N" for knight.
   *
   * @return character of piece.
   */
  public abstract String getType();

  /**
   * Gets list of end positions that are legal to be moved.
   *
   * @return List of positions piece can be moved to.
   */
  protected List<Position> getLegalMoves() {
    List<Position> positions = new ArrayList<>();

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        Position position = new Position(row, col);
        if (legalMove(position)) {
          positions.add(position);
        }
      }
    }

    return positions;
  }

  /**
   * Gets legality of end position to move to.
   *
   * @param end Position to check if legal to move to.
   * @return true if legal to move to end position, false else.
   */
  protected boolean legalMove(Position end) {
    boolean samePosition = end.equals(getPosition());
    boolean insideBounds = board.getGrid().insideBounds(end);
    if (samePosition || !insideBounds) {
      return false;
    }

    Position position = getPosition();

    // If to capture is an existing piece check if possible.
    Optional<ChessPiece> capturedPiece = board.getPiece(end);
    if (capturedPiece.isPresent() && capturedPiece.get().getSide() == getSide()) {
      return false;
    }

    // If adheres to movement patterns.
    if (!movementPattern(end)) {
      return false;
    }

    // Move piece.
    if (!board.getGrid().movePiece(position, end)) {
      return false;
    }

    // Check if promote this piece to queen.
    boolean promotedPawnToQueen = board.promotePawnToQueen(end);

    // Verify no king check
    final boolean kingCheck = board.kingInCheck(side);

    // If this piece got promoted to queen, restore this piece.
    if (promotedPawnToQueen) {
      board.getGrid().removePiece(end);
      board.getGrid().placePiece(end, this);
    }

    // Return back to start.
    if (!board.getGrid().movePiece(end, position)) {
      throw new IllegalStateException(
          "Error detected: Piece moved during legality simulation not reversed.");
    }

    // Replace captured piece.
    if (capturedPiece.isPresent()) {
      board.getGrid().placePiece(end, capturedPiece.get());
    }
    return !kingCheck;
  }

  /**
   * Checks if the end position is allowed by the movement pattern the piece has. Does not verify
   * legality of move regarding checks of king.
   *
   * @param end Position to check if follows movement pattern.
   * @return true if follows movement pattern, else false.
   */
  protected abstract boolean movementPattern(Position end);
}
