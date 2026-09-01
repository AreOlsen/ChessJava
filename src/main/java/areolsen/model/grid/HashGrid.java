package areolsen.model.grid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * HashGrid. Grid implementation using a bidirectional hashmap structure. Fitting for sparse grids.
 *
 * @param <T> piece type.
 */
public class HashGrid<T> implements Grid<T> {
  private final Map<Position, T> values = new HashMap<>();
  private final Map<T, Position> valuesReversed = new HashMap<>();
  private final int width;
  private final int height;

  /**
   * Construct new hashgrid.
   *
   * @param width of grid.
   * @param height of grid.
   */
  public HashGrid(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public boolean placePiece(Position position, T piece) {
    if (values.containsKey(position) || valuesReversed.containsKey(piece)) {
      return false;
    }

    if (!insideBounds(position)) {
      return false;
    }

    values.put(position, piece);
    valuesReversed.put(piece, position);
    return true;
  }

  @Override
  public Optional<T> getPiece(Position position) {
    return Optional.ofNullable(values.get(position));
  }

  @Override
  public Optional<Position> getPosition(T piece) {
    return Optional.ofNullable(valuesReversed.get(piece));
  }

  @Override
  public boolean movePiece(Position start, Position end) {
    if (!insideBounds(end) || !insideBounds(start)) {
      return false;
    }

    if (!values.containsKey(start)) {
      return false;
    }

    T piece = values.get(start);
    if (piece == null) {
      return false;
    }

    removePiece(start);

    if (values.containsKey(end)) {
      removePiece(end);
    }

    placePiece(end, piece);
    return true;
  }

  @Override
  public Optional<T> removePiece(Position position) throws Error {
    T piece = values.remove(position);
    if (piece == null) {
      return Optional.empty();
    }

    Position pos = valuesReversed.remove(piece);
    if (pos == null) {
      throw new Error(
          "Error: Desync between reversed values hashmap  (V-K) and values hashmap (K-V).");
    }

    return Optional.ofNullable(piece);
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public boolean insideBounds(Position position) {
    int x = position.x();
    int y = position.y();

    return x >= 0 && x < width && y >= 0 && y < height;
  }

  @Override
  public Iterator<Position> iterator() {
    return values.keySet().iterator();
  }
}
