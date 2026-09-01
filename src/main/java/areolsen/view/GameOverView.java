package areolsen.view;

import java.awt.Graphics2D;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * Default Game Over View. Includes:
 *
 * <ul>
 *   <li>"Chess" title
 *   <li>Back to main menu button.
 * </ul>
 */
public class GameOverView extends View {

  /**
   * Construct a Chess View instance.
   *
   * @param handler ViewHandler to switch to menu.
   */
  public GameOverView(ViewHandler handler) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(Box.createVerticalGlue());
    addText("Chess", 50);
    add(Box.createVerticalStrut(30));
    addButton("Back to start", e -> handler.changeActiveView("menu"));
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
