package areolsen.controller;

import areolsen.model.Board;
import areolsen.model.Move;
import areolsen.model.Piece;
import areolsen.model.Side;
import areolsen.model.grid.Position;
import areolsen.view.ViewHandler;
import java.util.List;
import java.util.Optional;

/** MinMaxController. */
public class MinMaxController extends Controller {
  private final int searchDepth;
  private Board boardClone;
  private Position bestStart;
  private Position bestEnd;

  /**
   * Instantiate new player vs ai controller.
   *
   * @param viewHandler ViewHandler reference for escaping to menu.
   * @param board ChessBoard reference for game state logic controller handling.
   * @param int search depth denotes how deep in the move trees to evaluate.
   */
  public MinMaxController(Board board, ViewHandler viewHandler, int searchDepth) {
    super(viewHandler, board);
    this.searchDepth = searchDepth;
    this.boardClone = board.clone();
  }

  /**
   * Click hook for PvAI game mode that allows player moves, followed by the ai moves immediately
   * after.
   *
   * @see Controller#mouseClicked
   */
  @Override
  public void clickHook(Optional<Position> originalPosition, Position newPosition) {
    if (board.getSide() != Side.WHITE) {
      return;
    }

    // If no cell selected update actively chosen cell.
    if (originalPosition.isEmpty()) {
      super.clickHook(originalPosition, newPosition);
      return;
    }

    // If white -> move.
    Optional<Move> move = board.movePiece(originalPosition.get(), newPosition, true);
    if (move.isPresent()) {
      activeChosenPosition = Optional.empty();

      // If we moved perform AI's move.
      boardClone = board.clone();
      findBestMove(this.searchDepth, Side.BLACK);
      if (bestStart == null || bestEnd == null) {
        board.changeSide();
        return;
      }
      board.movePiece(bestStart, bestEnd, true);
      return;
    }

    // If couldnt move just update.
    super.clickHook(originalPosition, newPosition);
  }

  // Finds the best move to follow the player by.
  private void findBestMove(int depth, Side side) {
    List<Move> legalMoves = board.getAllLegalMoves(side);
    double bestEvaluation = Double.MIN_VALUE;
    for (Move move : legalMoves) {
      double evaluation =
          simulateMove(
              move.start(), move.end(), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, side);

      if (bestStart == null || evaluation >= bestEvaluation) {
        bestStart = move.start();
        bestEnd = move.end();
        bestEvaluation = evaluation;
      }
    }
  }

  // Simulates a move on the cloned board.
  private double simulateMove(
      Position start, Position end, int depth, double alpha, double beta, Side side) {
    Side opponentSide = side.equals(Side.WHITE) ? Side.BLACK : Side.WHITE;

    Optional<Piece> startPieceOpt = board.getPiece(start);
    if (startPieceOpt.isEmpty()) {
      return 0d;
    }

    Optional<Move> move = boardClone.movePiece(start, end, false);
    double evaluation = minmaxEvaluate(depth - 1, alpha, beta, opponentSide);

    if (move.isPresent()) {
      boardClone.undoMove(move.get(), false);
    }
    return evaluation;
  }

  // Evalute the board for each possible positioning up to depth, and return highest evaluation.
  private double minmaxEvaluate(int depth, double alpha, double beta, Side side) {
    if (depth <= 0 || this.boardClone.gameOver(false)) {
      return boardClone.getBoardValue(side);
    }

    List<Move> legalMoves = boardClone.getAllLegalMoves(side);

    if (side.equals(Side.BLACK)) {
      double maxEvaluation = Double.MIN_VALUE;

      for (Move move : legalMoves) {
        maxEvaluation =
            Math.max(
                simulateMove(move.start(), move.end(), depth, alpha, beta, side), maxEvaluation);
        if (maxEvaluation >= beta) {
          break;
        }

        alpha = Math.max(alpha, maxEvaluation);
      }
      return maxEvaluation;
    } else {
      double minEvaluation = Double.MAX_VALUE;

      for (Move move : legalMoves) {
        minEvaluation =
            Math.min(
                simulateMove(move.start(), move.end(), depth, alpha, beta, side), minEvaluation);
        if (minEvaluation <= alpha) {
          break;
        }

        beta = Math.min(beta, minEvaluation);
      }
      return minEvaluation;
    }
  }
}
