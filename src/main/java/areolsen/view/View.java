package areolsen.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Abstract view class which extends JPanel. Can be stored in ViewHandler. Can be drawn on a JFrame
 * instance. Includes undefined abstract functions: onStart, onStop, update, render.
 *
 * @see ViewHandler.
 */
public abstract class View extends JPanel {
  protected final Map<String, Image> imageMap = new HashMap<>();

  /**
   * Hook that gets run when ViewHandler sets View as active.
   *
   * @see ViewHandler
   */
  protected abstract void onStart();

  /** Hook for logic updating each time a frame is run. */
  protected abstract void update();

  /**
   * Hook that gets run when ViewHandler changes from this to another active view.
   *
   * @see ViewHandler
   */
  protected abstract void onStop();

  /**
   * Overridable render view function. Hooks into paintComponent by JPanel.
   *
   * @param g2 Graphics2D object that stuff gets rendered upon.
   * @see paintComponent(...)
   */
  protected abstract void render(Graphics2D g2);

  @Override
  public void paintComponent(Graphics g) {
    update();
    render((Graphics2D) g);
  }

  /**
   * Draws image stored at filepath relative to resources folder.
   *
   * @param g2 Graphics2D object to draw with. Draws from top-left corner of image.
   * @param filepath Filepath of image relative to resources folder.
   * @param x x position on screen from left.
   * @param y y position on screen from top.
   * @param width Width to draw image with.
   * @param height Height to draw image with.
   */
  protected void renderImage(Graphics2D g2, String filepath, int x, int y, int width, int height) {
    try {
      if (!imageMap.containsKey(filepath)) {
        imageMap.put(filepath, ImageIO.read(new File("src/main/resources/" + filepath)));
      }

      Image image = imageMap.get(filepath);
      g2.drawImage(image, x, y, width, height, null);
    } catch (Exception e) {
      System.out.println("Failure in drawing image. Error:" + e.toString());
    }
  }

  /**
   * Draws an background image stored relative to resources/backgrounds/ Background image fills
   * entire screen. Calls renderImage function.
   *
   * @see renderImage(...)
   * @param g2 Graphics2D object to draw upon.
   * @param backgroundPath Path of background image stored relative to resources/backgrounds.
   */
  protected void renderBackground(Graphics2D g2, String backgroundPath) {
    renderImage(g2, "backgrounds/" + backgroundPath, 0, 0, getWidth(), getHeight());
  }

  /**
   * Adds text JLabel.
   *
   * @param text Text to render.
   * @param fontSize Size of text.
   */
  protected void addText(String text, int fontSize) {
    JLabel label = new JLabel(text, JLabel.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, fontSize));
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setForeground(Color.WHITE);
    add(label);
  }

  /**
   * Adds button with text, actionListener, with predefined settings.
   *
   * @param text Text inside button.
   * @param actionListener Action to do upon button click.
   */
  protected void addButton(String text, ActionListener actionListener) {
    JButton button = new JButton(text);

    button.setFocusPainted(false);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setForeground(Color.WHITE);
    button.setBorderPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 20));
    button.addActionListener(actionListener);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);

    add(button);
  }
}
