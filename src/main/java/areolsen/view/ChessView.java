package areolsen.view;

import areolsen.controller.Controller;
import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Piece;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Optional;

/** ChessView View that renders of game logic. */
public class ChessView extends View {
  private final Controller controller;
  private final Board board;
  private final ViewHandler handler;

  private static final Color CELL_COLOR_GRAY = new Color(125, 135, 150);
  private static final Color CELL_COLOR_WHITE = new Color(232, 235, 239);
  private static final Color CELL_COLOR_INVALID_MOVE = new Color(255, 51, 102, 100);
  private static final Color CELL_COLOR_VALID_MOVE = new Color(8, 178, 227, 100);

  /**
   * Construct a Chess View instance.
   *
   * @param handler ViewHandler to switch to gameover when game is finished.
   * @param controller Controller that handles game input logic.
   * @param board ChessBoard to play chess on.
   */
  public ChessView(ViewHandler handler, Controller controller, Board board) {
    this.board = board;
    this.controller = controller;
    this.addKeyListener(controller);
    this.addMouseListener(controller);
    this.handler = handler;
  }

  @Override
  public void onStart() {}

  @Override
  public void onStop() {}

  /** Checks if the game has finished. If so, reset the board for a replay, and change to menu. */
  @Override
  public void update() {
    if (board.gameOver(true)) {
      board.reset();
      handler.changeActiveView("gameover");
    }
  }

  @Override
  public void render(Graphics2D g2) {
    renderGrid(g2);
    renderMoves(g2);
    renderPieces(g2);
  }

  private void renderGrid(Graphics2D g2) {
    final int cellWidth = getWidth() / board.getWidth();
    final int cellHeight = getHeight() / board.getHeight();

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        g2.setColor((row + col) % 2 == 0 ? CELL_COLOR_GRAY : CELL_COLOR_WHITE);
        int xPosition = col * cellWidth;
        int yPosition = getHeight() - (row + 1) * cellHeight;
        g2.fillRect(xPosition, yPosition, cellWidth, cellHeight);
      }
    }
  }

  private void renderPieces(Graphics2D g2) {
    final int cellWidth = getWidth() / board.getWidth();
    final int cellHeight = getHeight() / board.getHeight();

    for (Piece piece : board) {
      String filename =
          "pieces/" + (piece.getSide() == Side.WHITE ? "w" : "b") + piece.getType() + ".png";
      int xPosition = piece.getPosition().x() * cellWidth;
      int yPosition = getHeight() - (piece.getPosition().y() + 1) * cellHeight;
      renderImage(g2, filename, xPosition, yPosition, cellWidth, cellHeight);
    }
  }

  private void renderMoves(Graphics2D g2) {
    final int cellWidth = getWidth() / board.getWidth();
    final int cellHeight = getHeight() / board.getHeight();

    // Check if position selected or not.
    Optional<Position> activePosition = controller.getActivePosition();
    if (activePosition.isEmpty()) {
      return;
    }

    // Get side move colour.
    List<Move> moves = board.getLegalMoves(activePosition.get());
    g2.setColor(CELL_COLOR_INVALID_MOVE);

    Optional<Piece> piece = board.getPiece(activePosition.get());
    if (piece.isPresent() && piece.get().getSide().equals(board.getSide())) {
      g2.setColor(CELL_COLOR_VALID_MOVE);
    }

    // Draw moves.
    for (Move move : moves) {
      int xPosition = move.end().x() * cellWidth;
      int yPosition = getHeight() - (move.end().y() + 1) * cellHeight;
      g2.fillRect(xPosition, yPosition, cellWidth, cellHeight);
    }
  }
}
