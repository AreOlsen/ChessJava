package areolsen.model.grid;

import java.util.Objects;

/**
 * Position record. Integer based positioning.
 *
 * @param x integer position
 * @param y integer position
 */
public record Position(int x, int y) {
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    Position otherPos = (Position) other;
    return (otherPos.x == this.x && otherPos.y == this.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(y, x);
  }

  /**
   * Checks if the position is on the same x or y values.
   *
   * @param position Position to compare.
   * @return true if on same x or y position values.
   */
  public boolean onSameLine(Position position) {
    return position.y() == y || position.x() == x;
  }

  /**
   * Checks if the position is on the same diagonal line.
   *
   * @param position Position to compare.
   * @return true if on same diagonal line.
   */
  public boolean onDiagonal(Position position) {
    return Math.abs(position.x() - x) == Math.abs(position.y() - y);
  }

  /**
   * Gets the difference in x value.
   *
   * @param position Position to compare difference in x against.
   * @return difference in x values.
   */
  public int xDifference(Position position) {
    return position.x() - x;
  }

  /**
   * Gets the difference in y value.
   *
   * @param position Position to compare difference in y against.
   * @return difference in y values.
   */
  public int yDifference(Position position) {
    return position.y() - y;
  }
}
