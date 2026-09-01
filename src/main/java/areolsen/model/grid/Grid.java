package areolsen.model.grid;

import java.util.Optional;

/**
 * Basic grid interface. Defines grid related logic such as placing pieces-, removing-, getting
 * pieces on grid, checking bounds, getting grid size information...
 *
 * @param <T> piece type to be stored on grid.
 */
public interface Grid<T> extends Iterable<Position> {
  /**
   * Place piece on grid at position.|
   *
   * @param position to place piece at.
   * @param piece to place on grid at position.
   * @return true if piece was placed, false else.
   */
  public boolean placePiece(Position position, T piece);

  /**
   * Gets piece at position argument.
   *
   * @param position of piece to get.
   * @return Optional<T> of piece. Empty if no piece at position.
   */
  public Optional<T> getPiece(Position position);

  /**
   * Gets the position of a piece.
   *
   * @param piece to get position of.
   * @return position of piece.
   */
  public Optional<Position> getPosition(T piece);

  /**
   * Attempts removal of piece at position.
   *
   * @param position of piece to remove.
   * @return Optional of piece to be removed. Empty if no piece found to be removed.
   */
  public Optional<T> removePiece(Position position);

  /**
   * Attempts move of piece.
   *
   * @param start position of piece to move.
   * @param end position of piece to move.
   * @return true if piece was moved, false else.
   */
  public boolean movePiece(Position start, Position end);

  /**
   * Checks if position is inside grid bounds.
   *
   * @param position to check if inside bounds.
   * @return true if position is inside grid bounds, false else.
   */
  public boolean insideBounds(Position position);

  /**
   * Gets width of grid.
   *
   * @return width of grid.
   */
  public int getWidth();

  /**
   * Gets height of grid.
   *
   * @return height of grid.
   */
  public int getHeight();
}
