package areolsen.controller;

import areolsen.model.ChessBoard;
import areolsen.model.grid.Position;
import areolsen.view.ViewHandler;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

/** Abstract chessboard controller class that implements KeyListener & MouseListener. */
public abstract class Controller implements KeyListener, MouseListener {
  protected Optional<Position> activeChosenPosition = Optional.empty();
  private ViewHandler handler;
  protected ChessBoard board;

  /**
   * Construct new controller.
   *
   * @param handler viewhandler reference.
   * @param board chessboard reference.
   */
  public Controller(ViewHandler handler, ChessBoard board) {
    this.handler = handler;
    this.board = board;
  }

  /**
   * Get actively chosen position on board.
   *
   * @return Actively chosen position on board.
   */
  public Optional<Position> getActivePosition() {
    return activeChosenPosition;
  }

  /**
   * Mouse click handler. Calculates grid positioning of click on board and passes to clickHook.
   *
   * @param event mouse event processed
   */
  @Override
  public void mouseClicked(MouseEvent event) {
    Dimension screen = handler.getWindowDimension();
    int gridX = (int) ((event.getX() / screen.getWidth()) * board.getWidth());
    int gridY = (int) (board.getHeight() - (event.getY() / screen.getHeight()) * board.getHeight());

    Position clickedPosition = new Position(gridX, gridY);

    clickHook(activeChosenPosition, clickedPosition);
    handler.getActiveView().repaint();
  }

  /**
   * Click hook for mouseClicked function. Overridable for specific controller implementations.
   *
   * @param originalPosition Optional of original grid position selected (empty if no position
   *     selected yet)
   * @param newPosition new grid position selected by click
   */
  protected void clickHook(Optional<Position> originalPosition, Position newPosition) {
    activeChosenPosition = Optional.of(newPosition);
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  /**
   * Key press handler. Simple default that makes R reset the chess game, Escape returns to main
   * menu.
   *
   * @param event Key event handled.
   */
  @Override
  public void keyPressed(KeyEvent event) {
    final int rKeyCode = 82;
    final int escKeyCode = 27;
    if (event.getKeyCode() == escKeyCode) {
      handler.changeActiveView("menu");
    } else if (event.getKeyCode() == rKeyCode) {
      board.reset();
      handler.getActiveView().repaint();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}
}
