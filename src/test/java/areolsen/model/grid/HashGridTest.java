package areolsen.model.grid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for HashGrid. */
public class HashGridTest {
  private HashGrid<String> grid;

  @BeforeEach
  public void setUp() {
    grid = new HashGrid<>(8, 8);
  }

  @Test
  public void testGridDimensions() {
    assertEquals(8, grid.getWidth());
    assertEquals(8, grid.getHeight());
  }

  @Test
  public void testPlacePiece() {
    Position pos = new Position(0, 0);
    assertTrue(grid.placePiece(pos, "piece1"));
    assertEquals("piece1", grid.getPiece(pos).get());
  }

  @Test
  public void testPlacePieceOutOfBounds() {
    Position pos = new Position(10, 10);
    assertFalse(grid.placePiece(pos, "piece1"));
  }

  @Test
  public void testPlacePieceAtSamePositionTwice() {
    Position pos = new Position(0, 0);
    grid.placePiece(pos, "piece1");
    assertFalse(grid.placePiece(pos, "piece2"));
  }

  @Test
  public void testPlaceSamePieceTwice() {
    Position pos1 = new Position(0, 0);
    Position pos2 = new Position(1, 1);
    grid.placePiece(pos1, "piece1");
    assertFalse(grid.placePiece(pos2, "piece1"));
  }

  @Test
  public void testGetPieceEmpty() {
    Position pos = new Position(3, 3);
    assertTrue(grid.getPiece(pos).isEmpty());
  }

  @Test
  public void testGetPositionOfPiece() {
    Position pos = new Position(2, 5);
    grid.placePiece(pos, "piece1");
    assertEquals(pos, grid.getPosition("piece1").get());
  }

  @Test
  public void testGetPositionOfNonexistentPiece() {
    assertTrue(grid.getPosition("nonexistent").isEmpty());
  }

  @Test
  public void testRemovePiece() {
    Position pos = new Position(1, 1);
    grid.placePiece(pos, "piece1");
    assertEquals("piece1", grid.removePiece(pos).get());
    assertTrue(grid.getPiece(pos).isEmpty());
  }

  @Test
  public void testRemovePieceEmpty() {
    Position pos = new Position(5, 5);
    assertTrue(grid.removePiece(pos).isEmpty());
  }

  @Test
  public void testMovePiece() {
    Position start = new Position(0, 0);
    Position end = new Position(1, 1);
    grid.placePiece(start, "piece1");
    assertTrue(grid.movePiece(start, end));
    assertTrue(grid.getPiece(end).isPresent());
    assertTrue(grid.getPiece(start).isEmpty());
  }

  @Test
  public void testMovePieceToOccupiedPosition() {
    Position start = new Position(0, 0);
    Position end = new Position(1, 1);
    grid.placePiece(start, "piece1");
    grid.placePiece(end, "piece2");
    assertTrue(grid.movePiece(start, end));
    assertEquals("piece1", grid.getPiece(end).get());
    assertTrue(grid.getPiece(start).isEmpty());
  }

  @Test
  public void testMovePieceOutOfBounds() {
    Position start = new Position(0, 0);
    Position end = new Position(10, 10);
    grid.placePiece(start, "piece1");
    assertFalse(grid.movePiece(start, end));
  }

  @Test
  public void testMovePieceFromEmpty() {
    Position start = new Position(5, 5);
    Position end = new Position(6, 6);
    assertFalse(grid.movePiece(start, end));
  }

  @Test
  public void testInsideBoundsValid() {
    assertTrue(grid.insideBounds(new Position(0, 0)));
    assertTrue(grid.insideBounds(new Position(7, 7)));
    assertTrue(grid.insideBounds(new Position(4, 4)));
  }

  @Test
  public void testInsideBoundsInvalid() {
    assertFalse(grid.insideBounds(new Position(-1, 0)));
    assertFalse(grid.insideBounds(new Position(8, 0)));
    assertFalse(grid.insideBounds(new Position(0, -1)));
    assertFalse(grid.insideBounds(new Position(0, 8)));
  }

  @Test
  public void testIterator() {
    grid.placePiece(new Position(0, 0), "piece1");
    grid.placePiece(new Position(1, 1), "piece2");
    grid.placePiece(new Position(2, 2), "piece3");
    int count = 0;
    for (Position pos : grid) {
      count++;
    }
    assertEquals(3, count);
  }
}
