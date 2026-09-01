package areolsen.model;

import areolsen.model.grid.Grid;
import areolsen.model.grid.HashGrid;
import areolsen.model.grid.Position;
import areolsen.model.pieces.BishopPiece;
import areolsen.model.pieces.KingPiece;
import areolsen.model.pieces.KnightPiece;
import areolsen.model.pieces.PawnPiece;
import areolsen.model.pieces.QueenPiece;
import areolsen.model.pieces.RookPiece;
import areolsen.sound.SoundPlayer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Basic ChessBoard implementation that contains chess board logic. Such as changing sides, getting
 * pieces, promoting pawns, moving pieces, game over handling, reseting...
 */
public class ChessBoard implements Iterable<ChessPiece> {
  private HashGrid<ChessPiece> board = new HashGrid<>(8, 8);
  private ChessSide side = ChessSide.WHITE;

  /** Construct new chessboard with all pieces initialized. */
  public ChessBoard() {
    initAllPieces();
  }

  /** Construct a new chessboard with no pieces. */
  public ChessBoard emptyBoard() {
    ChessBoard emptyBoard = new ChessBoard();
    emptyBoard.board = new HashGrid<>(8, 8);
    return emptyBoard;
  }

  private void initAllPieces() {
    initPawns(ChessSide.WHITE);
    initPawns(ChessSide.BLACK);
    initMajors(ChessSide.WHITE);
    initMajors(ChessSide.BLACK);
  }

  private void initPawns(ChessSide side) {
    int row = side == ChessSide.WHITE ? 1 : 6;
    for (int i = 0; i < 8; i++) {
      new PawnPiece(this, new Position(i, row), side);
    }
  }

  private void initMajors(ChessSide side) {
    int row = side == ChessSide.WHITE ? 0 : 7;
    new RookPiece(this, new Position(0, row), side);
    new RookPiece(this, new Position(7, row), side);
    new KnightPiece(this, new Position(1, row), side);
    new KnightPiece(this, new Position(6, row), side);
    new BishopPiece(this, new Position(2, row), side);
    new BishopPiece(this, new Position(5, row), side);
    new QueenPiece(this, new Position(3, row), side);
    new KingPiece(this, new Position(4, row), side);
  }

  /** Reset board state to start state. */
  public void reset() {
    board = new HashGrid<>(8, 8);
    side = ChessSide.WHITE;
    initAllPieces();
  }

  /**
   * Gets width (column count) of board.
   *
   * @return int of columns in board.
   */
  public int getWidth() {
    return board.getWidth();
  }

  /**
   * Gets height (row count) of board.
   *
   * @return int of rows in board.
   */
  public int getHeight() {
    return board.getHeight();
  }

  /**
   * Gets underlying grid datastructure of board.
   *
   * @return Grid datastructure pieces are stored on.
   */
  public Grid<ChessPiece> getGrid() {
    return this.board;
  }

  private void changeSide() {
    if (side == ChessSide.WHITE) {
      side = ChessSide.BLACK;
      return;
    }
    side = ChessSide.WHITE;
  }

  /**
   * Gets current chess side.
   *
   * @return ChessSide current playing chess side.
   */
  public ChessSide getSide() {
    return this.side;
  }

  /**
   * Get legal moves as a list of legal end positions.
   *
   * @param start Position of piece to get legal moves from.
   * @return List of legal moves' end positions.
   */
  public List<Position> getLegalMoves(Position start) {
    Optional<ChessPiece> pieceOptional = getPiece(start);
    if (pieceOptional.isEmpty()) {
      return new ArrayList<>();
    }
    return pieceOptional.get().getLegalMoves();
  }

  /**
   * Get piece stored at position.
   *
   * @param position of piece to retrieve.
   * @return Piece stored at position.
   */
  public Optional<ChessPiece> getPiece(Position position) {
    return board.getPiece(position);
  }

  /**
   * Get pieces with side.
   *
   * @param side of pieces to retrieve.
   * @return List of pieces with side on board.
   */
  protected List<ChessPiece> getPieces(ChessSide side) {
    List<ChessPiece> pieces = new ArrayList<>();
    for (Position position : board) {
      ChessPiece piece = getPiece(position).get();
      if (piece.getSide() == side) {
        pieces.add(piece);
      }
    }
    return pieces;
  }

  /**
   * Get pieces with side and piece type.
   *
   * @param side of pieces to retrieve.
   * @param type of pieces to retrieve.
   * @return List of pieces with side and piece type on board.
   */
  protected List<ChessPiece> getPieces(String type, ChessSide side) {
    List<ChessPiece> pieces = getPieces(side);
    return pieces.stream().filter(p -> p.getType().equals(type)).collect(Collectors.toList());
  }

  /**
   * Attempts a move; piece from position start to position end.
   *
   * @param start position of piece to move.
   * @param end position of piece to move.
   * @return true if piece was moved, false if not.
   */
  public boolean movePiece(Position start, Position end) {
    if (start.equals(end) || !board.insideBounds(end) || !board.insideBounds(start)) {
      return false;
    }

    Optional<ChessPiece> optionalPiece = board.getPiece(start);
    if (optionalPiece.isEmpty()) {
      return false;
    }

    ChessPiece piece = optionalPiece.get();
    boolean playingSide = piece.getSide().equals(side);
    if (!playingSide || !piece.legalMove(end)) {
      return false;
    }

    boolean moved = board.movePiece(start, end);
    if (moved) {
      promotePawnToQueen(end);
      changeSide();
      SoundPlayer.playSound("move.wav");
    }
    return moved;
  }

  /**
   * Gets if a side's king piece is in check.
   *
   * @param side to get if king is in check of.
   * @return if side's king is in check or not.
   */
  public boolean kingInCheck(ChessSide side) {
    List<ChessPiece> kings = getPieces("K", side);
    if (kings.isEmpty()) {
      return false;
    }

    ChessPiece king = kings.get(0);
    for (Position position : board) {
      if (board.getPiece(position).isEmpty()) {
        continue;
      }

      ChessPiece piece = board.getPiece(position).get();
      if (piece.getSide() == side) {
        continue;
      }
      ;

      if (piece.movementPattern(king.getPosition())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets if game is over. Game is over if a side has no available moves to perform.
   *
   * @return true if game is over, false else.
   */
  public boolean gameOver() {
    List<Position> moveEndingsWhite = new ArrayList<>();
    List<Position> moveEndingsBlack = new ArrayList<>();

    for (ChessPiece piece : this) {
      if (piece.getSide() == ChessSide.WHITE) {
        moveEndingsWhite.addAll(getLegalMoves(piece.getPosition()));
        continue;
      }
      moveEndingsBlack.addAll(getLegalMoves(piece.getPosition()));
    }

    if (moveEndingsBlack.isEmpty() || moveEndingsWhite.isEmpty()) {
      SoundPlayer.playSound("checkmate.wav");
      return true;
    }
    return false;
  }

  /**
   * Attempts a promotion of pawn to queen.
   *
   * @param position of pawn
   * @return true if piece got promoted to queen, false else.
   */
  protected boolean promotePawnToQueen(Position position) {
    if (!board.insideBounds(position)) {
      return false;
    }

    Optional<ChessPiece> pieceOpt = board.getPiece(position);
    if (pieceOpt.isEmpty()) {
      return false;
    }

    ChessPiece piece = pieceOpt.get();
    boolean pawnPieceMoved = piece.getType().equals("P");
    boolean onEndRow =
        (piece.getSide() == ChessSide.WHITE
            ? piece.getPosition().y() == board.getHeight() - 1
            : piece.getPosition().y() == 0);

    if (pawnPieceMoved && onEndRow) {
      board.removePiece(position);
      new QueenPiece(this, position, piece.getSide());
      return true;
    }
    return false;
  }

  /**
   * Gets value of board relative to side. Positively sums up all pieces of side, and negatively
   * sums up other side's pieces. Gives a rough metric of advantage of side over the other side.
   *
   * @param side of metric
   * @return sum of all piece values of side and negative sum of other side's piece values.
   * @see getValue() in ChessPiece
   */
  public double getBoardValue(ChessSide side) {
    double sum = 0d;
    for (ChessPiece piece : this) {
      sum += piece.getSide() == side ? piece.getValue() : -piece.getValue();
    }
    return sum;
  }

  @Override
  public Iterator<ChessPiece> iterator() {
    List<ChessPiece> pieces = new ArrayList<>();
    for (Position position : board) {
      Optional<ChessPiece> piece = board.getPiece(position);
      if (piece.isPresent()) {
        pieces.add(piece.get());
      }
    }
    return pieces.iterator();
  }
}
