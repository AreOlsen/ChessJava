package areolsen.view;

import java.awt.Graphics2D;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * Default MenuView. Includes:
 *
 * <ul>
 *   <li>"Chess" title
 *   <li>Player vs. Player game mode.
 *   <li>//todo: Player vs AI game mode.
 * </ul>
 */
public class MenuView extends View {

  /**
   * Construct a Chess View instance.
   *
   * @param handler ViewHandler to switch to views with.
   */
  public MenuView(ViewHandler handler) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(Box.createVerticalGlue());
    addText("Chess", 50);
    add(Box.createVerticalStrut(30));
    addButton("Player vs Player", e -> handler.changeActiveView("pvp"));
    add(Box.createVerticalStrut(30));
    // addButton("Player vs AI", e -> handler.changeActiveView("minmax"));
    add(Box.createVerticalGlue());
  }

  @Override
  public void onStart() {}

  @Override
  public void onStop() {}

  @Override
  public void update() {}

  @Override
  public void render(Graphics2D g2) {
    renderBackground(g2, "background.png");
  }
}
