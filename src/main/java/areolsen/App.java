package areolsen;

import areolsen.controller.Controller;
import areolsen.controller.PvpController;
import areolsen.model.ChessBoard;
import areolsen.view.ChessView;
import areolsen.view.GameOverView;
import areolsen.view.MenuView;
import areolsen.view.ViewHandler;

/**
 * Chess Application Main Start Point.
 *
 * @author Are Olsen.
 */
public class App {
  ViewHandler viewHandler = new ViewHandler();

  /**
   * Main function.
   *
   * @param args Main function arguments.
   */
  public void main(String[] args) {
    ChessBoard pvpBoard = new ChessBoard();
    Controller pvpController = new PvpController(viewHandler, pvpBoard);
    viewHandler.addView("pvp", new ChessView(viewHandler, pvpController, pvpBoard));
    viewHandler.addView("gameover", new GameOverView(viewHandler));
    viewHandler.addView("menu", new MenuView(viewHandler));
    viewHandler.changeActiveView("menu");
  }
}
