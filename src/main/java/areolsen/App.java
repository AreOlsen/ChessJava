package areolsen;

import areolsen.controller.Controller;
import areolsen.controller.MinMaxController;
import areolsen.controller.PvpController;
import areolsen.model.Board;
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
    Board pvpBoard = new Board();
    Board aiBoard = new Board();
    Controller pvpController = new PvpController(viewHandler, pvpBoard);
    Controller aiController = new MinMaxController(aiBoard, viewHandler, 3);
    viewHandler.addView("pvp", new ChessView(viewHandler, pvpController, pvpBoard));
    viewHandler.addView("minmax", new ChessView(viewHandler, aiController, aiBoard));
    viewHandler.addView("gameover", new GameOverView(viewHandler));
    viewHandler.addView("menu", new MenuView(viewHandler));
    viewHandler.changeActiveView("menu");
  }
}
