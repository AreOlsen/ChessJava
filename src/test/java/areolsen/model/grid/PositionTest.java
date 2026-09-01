package areolsen.model.grid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Unit tests for Position record. */
public class PositionTest {

  @Test
  public void testEquals() {
    Position pos1 = new Position(2, 4);
    Position pos2 = new Position(2, 4);
    Position pos3 = new Position(2, 5);

    assertEquals(pos1, pos2);
    assertNotEquals(pos1, pos3);
  }

  @Test
  public void testHashCode() {
    Position pos1 = new Position(2, 4);
    Position pos2 = new Position(2, 4);
    assertEquals(pos1.hashCode(), pos2.hashCode());
  }

  @Test
  public void testOnSameLine() {
    Position pos1 = new Position(3, 5);
    Position pos2 = new Position(7, 5); // same y
    Position pos3 = new Position(7, 8); // different x and y

    assertTrue(pos1.onSameLine(pos2));
    assertFalse(pos1.onSameLine(pos3));
  }

  @Test
  public void testOnDiagonal() {
    Position pos1 = new Position(0, 0);
    Position pos2 = new Position(3, 3); // on diagonal
    Position pos3 = new Position(2, 3); // not on diagonal

    assertTrue(pos1.onDiagonal(pos2));
    assertFalse(pos1.onDiagonal(pos3));
  }

  @Test
  public void testXDifference() {
    Position pos1 = new Position(2, 5);
    Position pos2 = new Position(8, 5);
    Position pos3 = new Position(2, 5);

    assertEquals(6, pos1.xDifference(pos2));
    assertEquals(0, pos1.xDifference(pos3));
  }

  @Test
  public void testYDifference() {
    Position pos1 = new Position(5, 2);
    Position pos2 = new Position(5, 8);
    Position pos3 = new Position(5, 2);

    assertEquals(6, pos1.yDifference(pos2));
    assertEquals(0, pos1.yDifference(pos3));
  }
}
