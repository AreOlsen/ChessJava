package areolsen.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 * ViewHandler. Stores and handles views. There is one active view shown at a time. Composes a
 * JFrame window upon which the active view is drawn.
 *
 * @see View
 * @see JFrame
 */
public class ViewHandler {
  private JFrame window = new JFrame("Chess");
  private Map<String, View> views = new HashMap<>();
  private String activeView = "";

  private static final Dimension MINIMUM_SCREEN_SIZE = new Dimension(300, 300);

  /**
   * Initializes new ViewHandler with a starter view with viewName argument.
   *
   * @param viewName Start active view name.
   * @param startView Start active view View.
   */
  public ViewHandler(String viewName, View startView) {
    this.activeView = viewName;
    initializeWindow();
    addView(viewName, startView);
  }

  /** Initialize empty ViewHandler. Has no active view and is essentially an empty JFrame. */
  public ViewHandler() {
    initializeWindow();
  }

  /** Initializes JFrame window with default window params. */
  private void initializeWindow() {
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLocationRelativeTo(null); // Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    window.setSize((int) screenSize.getHeight(), (int) screenSize.getHeight());
    window.setVisible(true);
    window.setMinimumSize(MINIMUM_SCREEN_SIZE);
  }

  /**
   * Adds a view with associated viewName.
   *
   * @param viewName The name of view, for example, "Menu"
   * @param view Actual view.
   * @throws IllegalArgumentException Existing view with viewName argument stored.
   */
  public void addView(String viewName, View view) throws IllegalArgumentException {
    if (views.containsKey(viewName)) {
      throw new IllegalArgumentException("Existing view with name");
    }
    views.put(viewName, view);
    view.setVisible(false);
  }

  /**
   * Gets view with associated viewName argument.
   *
   * @param viewName Name of view to retrieve.
   * @return View Instantiated view with associated viewName.
   * @throws IllegalArgumentException No view with viewName exists.
   */
  public View getView(String viewName) throws IllegalArgumentException {
    if (!views.containsKey(viewName)) {
      throw new IllegalArgumentException("View does not exist.");
    }
    return views.get(viewName);
  }

  /**
   * Gets the active view that is being displayed.
   *
   * @return Active view displayed.
   */
  public View getActiveView() {
    return getView(activeView);
  }

  /**
   * Removes stored view with viewName argument.
   *
   * @param viewName Name of view to remove.
   * @throws IllegalArgumentException No view stored with viewName argument.
   */
  public void removeView(String viewName) throws IllegalArgumentException {
    if (!views.containsKey(viewName)) {
      throw new IllegalArgumentException("Does not have view with viewName argument");
    }
    if (viewName.equals(activeView)) {
      throw new IllegalArgumentException("Can not remove active view");
    }

    views.remove(viewName);
  }

  /**
   * Changes the actively displayed view to stored view with viewName argument.
   *
   * @param viewName Name of view to change to.
   * @throws IllegalArgumentException No view with stored viewName argument.
   */
  public void changeActiveView(String viewName) throws IllegalArgumentException {
    if (!views.containsKey(viewName)) {
      throw new IllegalArgumentException("Not valid viewname");
    }

    if (viewName.equals(activeView)) {
      return;
    }

    // Get, stop,  remove active view.
    View currentView = views.get(activeView);
    if (currentView != null) {
      currentView.onStop();
      currentView.setVisible(false);
      window.remove(currentView);
    }

    // Update active view to new.
    activeView = viewName;
    View newView = views.get(viewName);

    // Add and start up new view.
    window.add(newView);
    newView.onStart();
    newView.setVisible(true);
    newView.setFocusable(true);
    newView.requestFocusInWindow();
    window.revalidate();
    window.repaint();
  }

  /**
   * Gets Dimension size of JFrame window measured in pixels.
   *
   * @return Dimension of Window.
   * @see Dimension
   */
  public Dimension getWindowDimension() {
    return window.getSize();
  }
}
